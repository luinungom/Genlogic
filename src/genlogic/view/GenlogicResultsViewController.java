/*
 * Copyright (C) 2021 Luis Núñez Gómez
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package genlogic.view;

import genlogic.RestrictionSiteSerializator;
import genlogic.model.DNASequence;
import genlogic.model.Endonuclease;
import genlogic.model.RestrictionSite;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller class for the results view.
 *
 * @author Luis Núñez Gómez
 */
public class GenlogicResultsViewController {

    /**
     * Initializes the controller class.
     */
    private Stage viewStage;

    /**
     * Menu option.
     */
    @FXML
    private Menu fileBtn;

    /**
     * Button to select the FASAT file.
     */
    @FXML
    private MenuItem save;

    /**
     * Button for close the application.
     */
    @FXML
    private MenuItem closeApplicationBtn;

    /**
     * Visual elements for Restriction Sites table.
     */
    @FXML
    private TableView<RestrictionSite> restrictionSitesTable;

    /**
     * This column contains the match number.
     */
    @FXML
    private TableColumn<RestrictionSite, String> matchColumn;

    /**
     * This column contains the endonuclease name.
     */
    @FXML
    private TableColumn<RestrictionSite, String> nameColumn;

    /**
     * This column contains the numeric representation of the exact cleavage 
     * point in the sequence.
     */
    @FXML
    private TableColumn<RestrictionSite, Integer> cleavageSiteColumn;

    /**
     * This column contains the info for the matching strand.
     */
    @FXML
    private TableColumn<RestrictionSite, String> strandColumn;
    
    /**
     * This label shows the total amount of cleavage sites found per analysis.
     */
    @FXML
    private Label totalCleavageSites;

    /**
     * ObservableList that contains restriction sites.
     */
    private ObservableList<RestrictionSite> matchingRestrictionSites = FXCollections.observableArrayList();

    /**
     * It triggers the analysis and populates the results table. If it analysis
     * result equals 0, it shows an alert with the text "No matches found for 
     * selected endonuclease(s)".
     * 
     * @param sequence DNASequence object to be analyzed
     * @param endonucleasesList List with selected endonucleases
     * @param isCircular boolean that enables the circular analysis if necessary
     */
    public void showResults(DNASequence sequence, List<Endonuclease> endonucleasesList, boolean isCircular) {
        analysis(sequence, endonucleasesList, isCircular);
        // If there are no results
        if (matchingRestrictionSites.isEmpty()) {
            alert("No Results", "No matches found for selected endonuclease(s)");
            try {
                handleCloseApplication();
            } catch (NullPointerException e) {
                System.out.println("No matches found for selected endonuclease(s)");
            }
        } else {
            //If there are results
            // Sets columns for restrictionSitesTable
            nameColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseName"));
            cleavageSiteColumn.setCellValueFactory(new PropertyValueFactory("restrictionSitePosition"));
            strandColumn.setCellValueFactory(new PropertyValueFactory("strand"));
            // Populates the table
            restrictionSitesTable.setItems(matchingRestrictionSites);
            // Sorts the table based on the cleavage site position
            restrictionSitesTable.getSortOrder().add(cleavageSiteColumn);
            totalCleavageSites.setText(String.valueOf(RestrictionSite.getTotalRestrictionSites()));
        }
    }

    /**
     * Analyzes the provided sequence and returns a list of cleavage sites. It
     * repeates an analysis for each selected endonuclease, first checks if it needs
     * a regex analysis, then if the sequence conformation is linear or circular
     * and finally it checks if the endonuclease is palindromic or not.
     * 
     * @param sequence DNASequence object to be analyzed
     * @param endonucleasesList List of selected endonucleases
     * @param isCircular boolean to check if a special extra circular analysis 
     * needs to be done
     * @return List of found cleavage sites
     */
    private ObservableList<RestrictionSite> analysis(DNASequence sequence, List<Endonuclease> endonucleasesList, boolean isCircular) {
        //matchingRestrictionSites.clear();
        RestrictionSite.setTotalRestrictionSites(0); // Resets the matches counter
        //GenlogicMainViewController genlogicMainViewController = getMainController();
        for (Endonuclease e : endonucleasesList) {
            if (!e.getIsRegex()) { // Checks if the endonuclease needs a REGEX analysis
                // Creates a map of digested DNA sequence build of fragments and positions
                Map<Integer, String> sequenceFragments = sequenceFragmentsConstructor(sequence, e);
                for (int i = 0; i < sequenceFragments.size(); i++) {
                    /* Performs an analisys of the sequence when the endonuclease is no regex, and no palindromic. 
                    It also checks if the analysis for circular sequences need to be done.*/
                    noRegexPalindromicAnalysis(sequenceFragments, i, e, isCircular, sequence);
                }
                if (!e.getIsPalindromic()) { // if it is a non palindromic endonuclease it performs an antisense analysis
                    Map<Integer, String> antiSequenceFragments = antiSequenceFragmentsConstructor(sequence, e);
                    for (int j = 0; j < antiSequenceFragments.size(); j++) {
                        // Performs an analysis of the antisequence when the endonuclease is no regex,
                        noRegexNoPalindromicAnalysis(antiSequenceFragments, j, e, isCircular, sequence);
                    }
                }
            } else {
                String regex = e.getEndonucleaseSimpleSenseTarget();
                Map<Integer, String> sequenceFragments = sequenceFragmentsConstructor(sequence, e);
                for (int k = 0; k < sequenceFragments.size(); k++) {
                    /* Performs an analisys of the sequence when the endonuclease is regex, and palindromic. 
                    It also checks if the analysis for circular sequences need to be done.*/
                    regexPalindromicAnalysis(sequenceFragments, k, regex, isCircular, e, sequence);
                }
                if (!e.getIsPalindromic()) {
                    String antiRegex = e.getEndonucleaseSimpleSenseTarget();
                    Map<Integer, String> antiSequenceFragments = antiSequenceFragmentsConstructor(sequence, e);
                    for (int m = 0; m < antiSequenceFragments.size(); m++) {
                        // Performs an analysis of the antisequence when the endonuclease is regex, and it is palindromic.
                        regexNoPalindromicAnalysis(antiSequenceFragments, m, antiRegex, isCircular, sequence, e);
                    }
                }
            }
        }
        return matchingRestrictionSites;
    }

    /**
     * Performs an analysis of the antisequence when the endonuclease is regex,
     * and it is palindromic.
     * 
     * @param antiSequenceFragments Digested DNA sequence in fragments
     * @param m Indicates the current fagment position in the sequence
     * @param antiRegex Target site sequence
     * @param isCircular Indicates if the DNA sequence is circular
     * @param sequence DNA sequence
     * @param e Endonuclease
     */
    private void regexNoPalindromicAnalysis(Map<Integer, String> antiSequenceFragments, int m, String antiRegex, boolean isCircular, DNASequence sequence, Endonuclease e) {
        String antiFragment = antiSequenceFragments.get(m);
        if (antiFragment.matches(antiRegex)) {
            if (!isCircular && ((sequence.getLength() - (m + e.getEndonucleasesAntiSenseStrandCuttingBp()) > 0))) {
                RestrictionSite targetMatch = new RestrictionSite(e, m, "antisense", sequence);
                matchingRestrictionSites.add(targetMatch);
            } else if (isCircular) {
                if ((m + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                    RestrictionSite match = new RestrictionSite(e, m, "antisense", sequence);
                    matchingRestrictionSites.add(match);
                } else { // If m is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                    RestrictionSite match = new RestrictionSite(e, m - (int) (sequence.getLength()), "antisense", sequence);
                    matchingRestrictionSites.add(match);
                }
            }
        }
    }

    /**
     * Performs an analisys of the sequence when the endonuclease is regex, and 
     * palindromic. It also checks if the analysis for circular sequences need
     * to be done.
     * 
     * @param sequenceFragments Digested DNA sequence in fragments
     * @param k Indicates the current fagment position in the sequence
     * @param regex Target site sequence
     * @param isCircular Indicates if the DNA sequence is circular
     * @param e Endonuclase
     * @param sequence DNA sequence
     */
    private void regexPalindromicAnalysis(Map<Integer, String> sequenceFragments, int k, String regex, boolean isCircular, Endonuclease e, DNASequence sequence) {
        String fragment = sequenceFragments.get(k);
        if (fragment.matches(regex)) {
            if (!isCircular && ((k + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength())) {
                RestrictionSite targetMatch = new RestrictionSite(e, k, "sense", sequence);
                matchingRestrictionSites.add(targetMatch);
            } else if (isCircular) {
                if ((k + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                    RestrictionSite match = new RestrictionSite(e, k, "sense", sequence);
                    matchingRestrictionSites.add(match);
                } else { // If k is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                    RestrictionSite match = new RestrictionSite(e, k - (int) (sequence.getLength()), "sense", sequence);
                    matchingRestrictionSites.add(match);
                }
            }
        }
        if (isCircular && k == sequenceFragments.size() - 1) {
            circularAnalysis(sequenceFragments, e, k, sequence, regex);
        }
    }

    /**
     * Performs an analysis of the antisequence when the endonuclease is no regex,
     * but it is palindromic.
     * 
     * @param antiSequenceFragments Digested DNA sequence in fragments
     * @param j Indicates the current fagment position in the sequence
     * @param e Endonuclease used in the current analysis
     * @param isCircular Indicates if the DNA sequence is circular
     * @param sequence DNA sequence
     */
    private void noRegexNoPalindromicAnalysis(Map<Integer, String> antiSequenceFragments, int j, Endonuclease e, boolean isCircular, DNASequence sequence) {
        String antiFragment = antiSequenceFragments.get(j);
        if (antiFragment.equalsIgnoreCase(e.getEndonucleaseSimpleSenseTarget())) {
            // If the sequence is not circular and the cutting bp is not out of the sequence's limit
            if (!isCircular && ((sequence.getLength() - (j + e.getEndonucleasesAntiSenseStrandCuttingBp()) > 0))) {
                RestrictionSite targetMatch = new RestrictionSite(e, j, "antisense", sequence);
                matchingRestrictionSites.add(targetMatch);
            } else if (isCircular) {
                if ((j + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                    RestrictionSite match = new RestrictionSite(e, j, "antisense", sequence);
                    matchingRestrictionSites.add(match);
                    System.out.println(j + e.getEndonucleasesSenseStrandCuttingBp());
                } else { // If i+j is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                    RestrictionSite match = new RestrictionSite(e, j - (int) (sequence.getLength()), "antisense", sequence);
                    matchingRestrictionSites.add(match);
                }
            }
        }
    }

    /**
     * Performs an analisys of the sequence when the endonuclease is no regex, and 
     * no palindromic. It also checks if the analysis for circular sequences need
     * to be done.
     * 
     * @param sequenceFragments Digested DNA sequence in fragments
     * @param i Indicates the current fagment position in the sequence
     * @param e Endonuclease used in the current analysis
     * @param isCircular Indicates if the DNA sequence is circular
     * @param sequence DNA sequence
     */
    private void noRegexPalindromicAnalysis(Map<Integer, String> sequenceFragments, int i, Endonuclease e, boolean isCircular, DNASequence sequence) {
        String fragment = sequenceFragments.get(i); // Extracts the DNA fragment of the current position
        if (fragment.equalsIgnoreCase(e.getEndonucleaseSimpleSenseTarget())) {
            // If the sequence is not circular and the cutting bp is not out of the sequence's limit
            if (!isCircular && ((i + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength())) {
                RestrictionSite targetMatch = new RestrictionSite(e, i, "sense", sequence);
                matchingRestrictionSites.add(targetMatch);
                // If the sequence is circular we don't need to check if the cutting bp is out of the sequence's limits
            } else if (isCircular) {
                // If i+j is smaller than sequence's length, the cutting bp is in the end of the sequence
                if ((i + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                    RestrictionSite match = new RestrictionSite(e, i, "sense", sequence);
                    matchingRestrictionSites.add(match);
                    System.out.println(i + e.getEndonucleasesSenseStrandCuttingBp());
                    // If i+j is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                } else {
                    RestrictionSite match = new RestrictionSite(e, (i - (int) sequence.getLength()), "sense", sequence);
                    matchingRestrictionSites.add(match);
                }
            }
        }
        // If the analysis reach the last fragment and the sequence is circular a last special analysis need to be done
        if (isCircular && i == sequenceFragments.size() - 1) {
            circularAnalysis(sequenceFragments, e, i, sequence);
        }
    }

    /**
     * Manages the dialog window to save results.
     */
    @FXML
    private void handleFileSaver() {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save results");
        fileChooser.getExtensionFilters().addAll(
                // Set extension filters
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("CSV (Comma-separated values)", "*.csv"));
        try {
            File file = fileChooser.showSaveDialog(viewStage);
            path = file.getPath();
            if (path != null) {
                if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().contains("txt")) {
                    // Calls the serialization method to generate the txt file
                    RestrictionSiteSerializator.saveFile(path, matchingRestrictionSites);
                } else {
                    // Calls the serialization method to generate the csv file
                    RestrictionSiteSerializator.writeToCSV(path, matchingRestrictionSites);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error saving the results file: " + ex);
        }
    }

    /**
     * Creates an special DNA fragment only for circular analysis joining the
     * end and the begining of the DNA fragment.
     * 
     * @param sequenceFragments Already digested DNA sequence fragments
     * @param e Endonuclease used on the analysis
     * @param i position in the DNA sequence
     * @param sequence DNA sequence
     */
    private void circularAnalysis(Map<Integer, String> sequenceFragments, Endonuclease e, int i, DNASequence sequence) {
        // Build the special fragment merging the last and the first sequence fragments
        StringBuilder temporalFragment = new StringBuilder(sequenceFragments.get(i) + sequenceFragments.get(0));
        // Delete first and last useless characters
        temporalFragment.deleteCharAt(0).deleteCharAt(temporalFragment.length() - 1);
        // Build a small DNA sequence based on last and first bps
        DNASequence jointFragment = new DNASequence(temporalFragment.toString());
        // Digest the sequence in fragments to search a match on it
        Map<Integer, String> sequenceJointFragments = sequenceFragmentsConstructor(jointFragment, e);
        // Process every fragment
        for (int j = 0; j < sequenceJointFragments.size(); j++) {
            String fragment = sequenceJointFragments.get(j);
            // If a match happens
            if (fragment.equalsIgnoreCase(e.getEndonucleaseSimpleSenseTarget())) {
                // If i+j is smaller than sequence's length, the cutting bp is in the end of the sequence
                if ((i + 1 + j + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                    RestrictionSite match = new RestrictionSite(e, ((i + 1) + j), "sense", sequence);
                    matchingRestrictionSites.add(match);
                    System.out.println(i + j + e.getEndonucleasesSenseStrandCuttingBp());
                    // If i+j is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                } else {
                    RestrictionSite match = new RestrictionSite(e, (((i + 1) + j) - ((int) (sequence.getLength()))), "sense", sequence);
                    matchingRestrictionSites.add(match);
                }
            }
        }
        // If the endonuclease is no palindromic, a new analysis in the antisense strand needs to be done
        if (!e.getIsPalindromic()) {
            // Creates the antistrand map
            Map<Integer, String> antiSequenceJointFragments = antiSequenceFragmentsConstructor(jointFragment, e);
            // Process every fragment
            for (int j = 0; j < antiSequenceJointFragments.size(); j++) {
                String antiFragment = antiSequenceJointFragments.get(j);
                // If a match happens
                if (antiFragment.equalsIgnoreCase(e.getEndonucleaseSimpleSenseTarget())) {
                    if ((i + 1 + j + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                        RestrictionSite match = new RestrictionSite(e, ((i + 1) + j), "antisense", sequence);
                        matchingRestrictionSites.add(match);
                        System.out.println(i + j + e.getEndonucleasesSenseStrandCuttingBp());
                    } else { // If i+j is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                        RestrictionSite match = new RestrictionSite(e, (((i + 1) + j) - ((int) (sequence.getLength()))), "antisense", sequence);
                        matchingRestrictionSites.add(match);
                    }
                }
            }
        }
    }

    /**
     * Overloaded version, creates an special DNA fragment only for circular
     * analysis joining the end and the beggining of the DNA fragment. This
     * version is for endonucleases that need a regex analysis.
     *
     * @param sequenceFragments Already digested DNA sequence fragments
     * @param e Endonuclease used on the analysis
     * @param k position in the DNA sequence
     * @param sequence DNA sequence
     */
    private void circularAnalysis(Map<Integer, String> sequenceFragments, Endonuclease e, int k, DNASequence sequence, String regex) {
        // Build the special fragment merging the last and the first sequence fragments
        StringBuilder temporalFragment = new StringBuilder(sequenceFragments.get(k) + sequenceFragments.get(0));
        // Delete first and last useless characters
        temporalFragment.deleteCharAt(0).deleteCharAt(temporalFragment.length() - 1);
        // Build a small DNA sequence based on last and first bps
        DNASequence jointFragment = new DNASequence(temporalFragment.toString());
        // Digest the sequence in fragments to search a match on it
        Map<Integer, String> sequenceJointFragments = sequenceFragmentsConstructor(jointFragment, e);
        // Process every fragment
        for (int j = 0; j < sequenceJointFragments.size(); j++) {
            String fragment = sequenceJointFragments.get(j);
            // If a match happens
            if (fragment.matches(regex)) {
                // If k+j is smaller than sequence's length, the cutting bp is in the end of the sequence
                if ((k + 1 + j + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                    RestrictionSite match = new RestrictionSite(e, ((k + 1) + j), "sense", sequence);
                    matchingRestrictionSites.add(match);
                } else { // If k+j is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                    RestrictionSite match = new RestrictionSite(e, (((k + 1) + j) - ((int) (sequence.getLength()))), "sense", sequence);
                    matchingRestrictionSites.add(match);
                }
            }
        }
        // If the endonuclease is no palindromic, a new analysis in the antisense strand needs to be done
        if (!e.getIsPalindromic()) {
            // Creates the antistrand map
            Map<Integer, String> antiSequenceJointFragments = antiSequenceFragmentsConstructor(jointFragment, e);
            // Process every fragment
            for (int j = 0; j < antiSequenceJointFragments.size(); j++) {
                String antiFragment = antiSequenceJointFragments.get(j);
                // If a match happens
                if (antiFragment.matches(regex)) {
                    if ((k + 1 + j + e.getEndonucleasesSenseStrandCuttingBp()) < sequence.getLength()) {
                        RestrictionSite match = new RestrictionSite(e, ((k + 1) + j), "antisense", sequence);
                        matchingRestrictionSites.add(match);
                    } else { // If k+j is bigger or equal than sequence's lenght, the cleavage point is in the begining of the sequence
                        RestrictionSite match = new RestrictionSite(e, (((k + 1) + j) - ((int) (sequence.getLength()))), "antisense", sequence);
                        matchingRestrictionSites.add(match);
                    }
                }
            }
        }
    }

    /**
     * Sets stage to this controller
     *
     * @param stage main stage
     */
    public void setStage(Stage stage) {
        this.viewStage = stage;
    }

    /**
     * 'Close' functionality using the close option in the menu.
     */
    @FXML
    public void handleCloseApplication() {
        viewStage.close();
    }

    /**
     * It fragments the DNA sequence into fragments depending on inidicated
     * endonuclase size.
     *
     * @param sequence DNA sequence to be fragmented
     * @param endonuclease endonuclase to get size
     * @return Map <Integer, String> DNA sequence fragmented in fragments of the
     * endonuclease size
     */
    public Map<Integer, String> sequenceFragmentsConstructor(DNASequence sequence, Endonuclease endonuclease) {
        String DNAsequence = sequence.getSequence();
        int cleavageSize = endonuclease.getEndonucleaseTargetLength();
        Map<Integer, String> fragmentedSequence = new HashMap();
        for (int i = 0; i <= (DNAsequence.length() - cleavageSize); i++) {
            int DNAfragmentNumber = i;
            String DNAfragment = DNAsequence.substring(i, (cleavageSize + i));
            fragmentedSequence.put(DNAfragmentNumber, DNAfragment);
        }
        return fragmentedSequence;
    }

    /**
     * It fragments the DNA sequence into fragements depending on inidicated
     * endonuclase size but generating the antisense strand fragments.
     *
     * @param sequence
     * @param endonuclease
     * @return <Integer, String> DNA anti sequence fragmented in fragments of the
     * endonuclease size
     */
    public Map<Integer, String> antiSequenceFragmentsConstructor(DNASequence sequence, Endonuclease endonuclease) {
        String DNAsequence = sequence.getSequence();
        int cleavageSize = endonuclease.getEndonucleaseTargetLength();
        Map<Integer, String> fragmentedAntiSequence = new HashMap();
        for (int i = 0; i <= (DNAsequence.length() - cleavageSize); i++) {
            int antiDNAfragmentNumber = (DNAsequence.length() - i) - cleavageSize;
            String DNAfragment = DNAsequence.substring(i, (cleavageSize + i));
            StringBuilder antiDNAfragment = new StringBuilder();
            for (int j = 0; j < DNAfragment.length(); j++) {
                char base = DNAfragment.charAt(j);
                switch (base) {
                    case ('A'):
                        antiDNAfragment.append('T');
                        break;
                    case ('T'):
                        antiDNAfragment.append('A');
                        break;
                    case ('C'):
                        antiDNAfragment.append('G');
                        break;
                    case ('G'):
                        antiDNAfragment.append('C');
                        break;
                    default:
                        antiDNAfragment.append(base);
                }
            }
            antiDNAfragment.reverse();
            fragmentedAntiSequence.put(antiDNAfragmentNumber, antiDNAfragment.toString());
        }
        return fragmentedAntiSequence;
    }

    /**
     * Generates alert messages.
     *
     * @param title Alert window title
     * @param message Alert window content text
     */
    private static void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Returns found matches.
     *
     * @return ObservableList with all matches in the sequence
     */
    public ObservableList getMatchingRestrictionSites() {
        return this.matchingRestrictionSites;
    }
}
