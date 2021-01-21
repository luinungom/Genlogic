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

import genlogic.EndonucleaseSerializator;
import genlogic.FASTASequenceReader;
import genlogic.Genlogic;
import genlogic.model.DNASequence;
import genlogic.model.Endonuclease;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for the main window.
 * 
 * @author Luis Núñez Gómez
 */
public class GenlogicMainViewController implements Initializable {
    
    /**
     * Main view stage.
     */
    private Stage primaryStage;
    
    /**
     * DNA Sequence table.
     */
    @FXML
    private TableView<DNASequence> DNASequenceTable;

    /**
     * DNA Sequence table column.
     */
    @FXML
    private TableColumn<DNASequence, String> informationColumn;

    /**
     * Label element for the DNASequence indentifier.
     */
    @FXML
    private Label sequenceID;

    /**
     * Label element for the DNASequence length.
     */
    @FXML
    private Label sequenceLength;
    
    /**
     * Chart element for DNASequence composition.
     */
    @FXML
    private BarChart<?,?> PercentagesChart;
    
    /**
     * X axis for the chart.
     */
    @FXML
    private CategoryAxis x;
    
    /**
     * Y axis for the chart.
     */
    @FXML
    private NumberAxis y;

    /**
     * Button to perform the analysis using selected endonucleases.
     */
    @FXML
    private Button analize;

    /**
     * Menu option.
     */
    @FXML
    private Menu fileBtn;

    /**
     * Button to select the FASTA file.
     */
    @FXML
    private MenuItem fileChooserBtn;

    /**
     * Button for close the application.
     */
    @FXML
    private MenuItem closeApplicationBtn;
    
    /**
     * Button for adding new restriction endonucleases.
     */
    @FXML
    private MenuItem addRestrictionEnzymesButton;
    
    /**
     * Button for deleting an existing endonuclease.
     */
    @FXML
    private MenuItem deleteRestrictionEnzymesButton;
    
    /**
     * Button for the about window
     */
    @FXML
    private MenuItem aboutGenlogicButton;
    
    /**
     * Radio button for selecting the DNA conformation.
     */
    @FXML
    private RadioButton lineal;
    
    /**
     * Radio button for selecting the DNA conformation.
     */
    @FXML
    private RadioButton circular;

    /**
     * Reference to the main application.
     */
    private Genlogic genlogic;

    /**
     * Restriction endonucleases table.
     */
    @FXML
    private TableView<Endonuclease> endonucleaseTable;

    /**
     * Name column for the endonucleaseTable.
     */
    @FXML
    private TableColumn<Endonuclease, String> nameColumn;

    /**
     * Origin column for the endonucleaseTable.
     */
    @FXML
    private TableColumn<Endonuclease, String> originColumn;

    /**
     * Target column for the endonucleaseTable.
     */
    @FXML
    private TableColumn<Endonuclease, String> targetColumn;

    /**
     * Length column for the endonucleaseTable.
     */
    @FXML
    private TableColumn<Endonuclease, Integer> lengthColumn;

    /**
     * Cleavage site column for the endonucleaseTable.
     */
    @FXML
    private TableColumn<Endonuclease, String> cleavage_siteColumn;
    
    /**
     * Overhang column for the endonucleaseTable.
     */
    @FXML
    private TableColumn<Endonuclease, String> overhangColumn;

    /**
     * ObservableList that contains DNA sequence objects.
     */
    private ObservableList<DNASequence> sequences_table = FXCollections.observableArrayList();

    /**
     * ObservableList that contains endonuclease objects
     */
    private ObservableList<Endonuclease> endonucleases_table = FXCollections.observableArrayList();

    // These 2 lines are necessary to generate a new ENDONUCLEASES.dat file
     //////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Path with endonucleases dat file.
     */
    private String datEndonucleaseFilePath = "." + File.separator + "ENDONUCLEASES.dat";
    
    /**
     * Path with the txt file.
     */
    String txtPath = "C:" + File.separator + "Users" + File.separator + "bkr_man" + File.separator + "Downloads" + File.separator + "ENDONUCLEASES.txt";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * List with all serialized endonucleases.
     */
    private List<Endonuclease> serializedEndonucleases = new LinkedList();

    /**
     * Selected DNA Sequence.
     */
    private DNASequence selectedDNASequence;

    /**
     * Selected Endonuclases to preform the analysis.
     */
    private ObservableList<Endonuclease> selectedEndonucleases;
    
    /**
     * Defines if the DNA sequence conformation is circular.
     */
    private boolean isCircular;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the column for the DNA sequences table
        informationColumn.setCellValueFactory(new PropertyValueFactory("information"));
        // Show nothing until a FASTA file is loaded
        showDNASequenceDetails(null);

        //////////////////////////////////////////////////////////////////////////////
        // These 2 lines generate a new fresh ENDONUCLEASES.dat file DISABLE from a txt
        //List<Endonuclease> txtmemory = EndonucleaseSerializator.textToMemory(txtPath);
        //EndonucleaseSerializator.memoryToDat(txtmemory, datEndonucleaseFilePath);
        /////////////////////////////////////////////////////////////////////////////
        
        //This line reads the dat file and loads endonuclease objects in memory
        serializedEndonucleases = EndonucleaseSerializator.datToMemory(datEndonucleaseFilePath);

        // Set columns for the endonucleases table
        nameColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseName"));
        originColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseOrigin"));
        targetColumn.setCellValueFactory(param ->{String result = String.join("\n", param.getValue().getVisualEndonucleaseTarget(),param.getValue().getVisualEndonucleaseAntiTarget()); return new SimpleStringProperty(result);});
        lengthColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseTargetLength"));
        cleavage_siteColumn.setCellValueFactory(param ->{String result = String.join("\n", param.getValue().getEndonucleaseCleavageRepresentation(),param.getValue().getEndonucleaseAntiCleavageRepresentation()); return new SimpleStringProperty(result);});
        overhangColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseOverhang"));
        
        
        // Initialize selected DNA Sequence table listener to modify values dynamically
        DNASequenceTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
        PercentagesChart.getData().clear();
        showDNASequenceDetails(newValue);
        endonucleaseTable.getItems().clear();
            //preAnalysis disabled//
            //preAnalysis(newValue, serializedEndonucleases);
        showEndonucleases(serializedEndonucleases);
        selectedDNASequence = newValue;
        });

        // Initialize selected Endonucleases table to allow multiple selection
        endonucleaseTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Shows selected DNASequence info in real time, if no DNASequence is
     * selected it clears all fields and disable the analize button.
     * 
     * @param newValue DNASequence object
     */
    public void showDNASequenceDetails(DNASequence newValue) {
        XYChart.Series set1 = new XYChart.Series<>();
        if (newValue != null) {
            sequenceID.setText(newValue.getInformation());
            sequenceLength.setText(String.valueOf(newValue.getIntLength()));
            //DNASequence.setText(newValue.getSequence());
            set1.getData().add(new XYChart.Data("Adenine", Float.valueOf(newValue.getPercentage().get("Adenine"))));
            set1.getData().add(new XYChart.Data("Cytosine", Float.valueOf(newValue.getPercentage().get("Cytosine"))));
            set1.getData().add(new XYChart.Data("Guanine", Float.valueOf(newValue.getPercentage().get("Guanine"))));
            set1.getData().add(new XYChart.Data("Thymine", Float.valueOf(newValue.getPercentage().get("Thymine"))));
            PercentagesChart.getData().add(set1);
            analize.setDisable(false);
        } else {
            sequenceID.setText("");
            sequenceLength.setText("");
            analize.setDisable(true);
        }
    }
    
    /**
     * Populates the endonucleases table with all serializated endonucleases and
     * sorts it based on the endonucleases' names.
     * 
     * @param endonucleasesList 
     */
    public void showEndonucleases(List<Endonuclease> endonucleasesList){
        for(Endonuclease e : endonucleasesList){
            endonucleases_table.add(e);
            endonucleaseTable.setItems(endonucleases_table);
            // Sorts the table based on the endonucleases' names
            endonucleaseTable.getSortOrder().add(nameColumn);
        }
    }
    
    /**
     * Method to show alert dialogs.
     * 
     * @param title alert window title
     * @param message alert window message
     */
    private static void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Opens a file chooser dialog to allow user to select a txt file. It the
     * file contains FASTA DNA sequences, it loads it in the application.
     */
    @FXML
    private void handleFileChooser() {

        FASTASequenceReader reader = new FASTASequenceReader();
        String path = null;
        FileChooser fileChooser = new FileChooser();
        
        try {
            fileChooser.setTitle("Select FASTA file");
            fileChooser.getExtensionFilters().addAll(
                // Set extension filters
                new FileChooser.ExtensionFilter("Text files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            path = selectedFile.getPath();
        } catch (Exception e) {
            System.err.println("No file has been selected. " + e);
        }
        if (path != null) {
            sequences_table = reader.fileReader(path);
            if (!sequences_table.isEmpty()) {
                DNASequenceTable.setItems(sequences_table);
            } else {
                alert("No DNA sequences found","Unable to find DNA sequences in the "
                        + "provided file");
            }
        }
    }

    /**
     * This method triggers the DNA sequence analysis. First it checks if at least
     * one restriction endonuclease has been selected, then it calls the results view
     * controller and calls the analysis method. Once the analysis is performed
     * results are shown in the results view.
     */
    @FXML
    private void handleAnalyzeButton() {
        selectedEndonucleases = endonucleaseTable.getSelectionModel().getSelectedItems();
        if (selectedEndonucleases.isEmpty()) {
            alert("Restriction endonucleases needed", "Please, select one restriction "
                    + "endonuclease at least to perform the analysis (you can choose "
                    + "several using the CTRL + click combo)");
        } else {
            // Loading results view and get instance of the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GenlogicResultsView.fxml"));
            try {
                Parent root = loader.load();
                // Get controller of results view
                GenlogicResultsViewController resultsViewController = loader.getController();
                // Check the selected radio button for the DNA conformation
                sequenceConformationChecker();
                // Show information in the results view
                resultsViewController.showResults(selectedDNASequence,selectedEndonucleases, isCircular);
                if(!resultsViewController.getMatchingRestrictionSites().isEmpty()){
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Analysis results for sequence - "+selectedDNASequence.getInformation());
                stage.getIcons().add(new Image("/resources/images/SimpleLogo.png"));
                // Both lines make this stage active and block primary stage
                stage.initOwner(primaryStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                // Make results window size not resizable
                stage.setResizable(false);
                // Sets the new stage object as the one for the results view
                resultsViewController.setStage(stage);
                stage.showAndWait();
                }
            } catch (IOException ex) {
                Logger.getLogger(GenlogicMainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 'Close' functionality using the close option in the menu.
     */
    @FXML
    private void handleCloseApplication() {
        primaryStage.close();
    }
    
    /**
     * Opens the add restriction endonucleases window.
     */
    @FXML
    private void handleAddRestictionEnzime(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GenlogicAddRestrictionEnzymesView.fxml"));
        try {
            Parent root = loader.load();
            // Get controller of add restriction endonuclease view
            GenlogicAddRestrictionEnzymesViewController addRestrictionEnzymesViewController = loader.getController();
            Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Add Restriction Enzime");
                stage.getIcons().add(new Image("/resources/images/SimpleLogo.png"));
                // Both lines make this stage active and block primary stage
                stage.initOwner(primaryStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                // Make results window size not resizable
                stage.setResizable(false);
                // Sets the new stage object as the one for the results view
                addRestrictionEnzymesViewController.setStage(stage);
                stage.showAndWait();
                if(!addRestrictionEnzymesViewController.isCancelled()){
                    // Adds the new endonuclease to the dat file
                    serializedEndonucleases.add(addRestrictionEnzymesViewController.getEndonuclease());
                    // Updates the dat file
                    EndonucleaseSerializator.memoryToDat(serializedEndonucleases, datEndonucleaseFilePath);
                    if(selectedDNASequence != null){
                        endonucleaseTable.getItems().clear();
                        showEndonucleases(serializedEndonucleases);
                    }
                }
        } catch (IOException ex) {
            Logger.getLogger(GenlogicMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Opens the delete endonuclease window.
     */
    @FXML
    private void handleDeleteEndonucleases(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GenlogicDeleteRestrictionEnymesView.fxml"));
        try{
            Parent root = loader.load();
            // Get controller of add restriction endonuclease view
            GenlogicDeleteRestrictionEnymesViewController deleteRestrictionEnzymesViewController = loader.getController();
            deleteRestrictionEnzymesViewController.loadEndonucleasesTable(serializedEndonucleases);
            Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Delete Restriction Enzime");
                stage.getIcons().add(new Image("/resources/images/SimpleLogo.png"));
                // Both lines make this stage active and block primary stage
                stage.initOwner(primaryStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                // Make results window size not resizable
                stage.setResizable(false);
                // Sets the new stage object as the one for the results view
                deleteRestrictionEnzymesViewController.setStage(stage);
                stage.showAndWait();
                if(!deleteRestrictionEnzymesViewController.getIsCancelled()){
                    // Updates the dat file
                    EndonucleaseSerializator.memoryToDat(serializedEndonucleases, datEndonucleaseFilePath);
                if(selectedDNASequence != null){
                    endonucleaseTable.getItems().clear();
                    showEndonucleases(serializedEndonucleases);
                    }
                }
                
        } catch (IOException ex) {
            Logger.getLogger(GenlogicMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Opens the About Genlogic window.
     */
    @FXML
    private void handleAboutGenlogic(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GenlogicAboutView.fxml"));
        Stage stage = new Stage();
        try{
        Parent root = loader.load();
        stage.setScene(new Scene(root));
                stage.setTitle("About Genlogic");
                stage.getIcons().add(new Image("/resources/images/SimpleLogo.png"));
                // Both lines make this stage active and block primary stage
                stage.initOwner(primaryStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                // Make results window size not resizable
                stage.setResizable(false);
                // Sets the new stage object as the one for the results view
                stage.showAndWait();
        }catch (IOException ex) {
            Logger.getLogger(GenlogicMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Checks the selected radio button for the DNA sequence conformation,
     * changing the isSelected boolean attribute.
     */
    public void sequenceConformationChecker(){
        if(circular.isSelected()){
            isCircular = true;
        }else{
            isCircular = false;
        }
    }
    
    /**
     * Sets stage to this controller.
     * @param stage main stage
     */
    public void setStage(Stage stage){
        this.primaryStage = stage;
    }
    
    /**
     * Returns the serialized endonucleases in memory.
     * @return List of endonucleases
     */
    public List<Endonuclease> getSerializedEndonucleases(){
        return this.serializedEndonucleases;
    }
    
}
