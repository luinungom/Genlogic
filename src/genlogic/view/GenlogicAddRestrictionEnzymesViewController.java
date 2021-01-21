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

import genlogic.model.Endonuclease;
import genlogic.model.RegexEndonuclease;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * FXML Controller class for the "Add Endonucleases" view.
 *
 * @author Luis Núñez Gómez
 */
public class GenlogicAddRestrictionEnzymesViewController {

    /**
     * Text field to insert the endonuclease name.
     */
    @FXML
    private TextField nameField;

    /**
     * Text field to insert the endonuclease origin (this is an optional field).
     */
    @FXML
    private TextField originField;

    /**
     * Text field to insert the endonuclease target sequence.
     */
    @FXML
    private TextField targetSequenceField;

    /**
     * Text field to insert the endonuclease sense strand sequence.
     */
    @FXML
    private TextField senseStrandSequence;

    /**
     * Text field to insert the endonuclease antisense strand sequence.
     */
    @FXML
    private TextField antiSenseStrandSequence;

    /**
     * Checkbox for the enduclease's palindromic property.
     */
    @FXML
    private CheckBox palindromic;

    /**
     * Accept button.
     */
    @FXML
    private Button accept;

    /**
     * Cancel button.
     */
    @FXML
    private Button cancel;

    /**
     * Add endonuclease window stage.
     */
    private Stage addStage;
    
    /**
     * Endonuclease object created using this view.
     */
    private Endonuclease newEndonuclease;
    
    /**
     * This boolean changes to false only if a new endonuclease object has been
     * created successfully, it works like a fuse.
     */
    private boolean cancelled = true;

    /**  
     * Sets stage to this controller.
     *
     * @param stage main stage
     */
    public void setStage(Stage stage) {
        this.addStage = stage;
    }

    /**
     * Creates a new endonuclease object once the accept button has been clicked.
     * First several checks are performed to ensure that all required information
     * has been filled.
     */
    @FXML
    private void accept() {
        String name = nameField.getText();
        String origin = originField.getText();
        String target = targetSequenceField.getText().toUpperCase().trim();
        String senseSequence = senseStrandSequence.getText().toUpperCase().trim();
        String antiSenseSequence = antiSenseStrandSequence.getText().toUpperCase().trim();
        //Checks that a name has been inserted
        if (name.length() == 0) {
            alert("Error", "Name field is mandatory");
            //Checks for the target field
        } else if (target.length() == 0) {
            alert("Error", "Target sequence field is mandatory");
            //Checks if the target sequence is correct
        } else if (!targetFieldValidator(target)) {
            alert("Error", "Target sequence is incorrect");
            //Checks for the sense strand sequence field
        } else if (senseSequence.length() == 0) {
            alert("Error", "Sense strand sequence field is mandatory");
            //Checks that the sense strand sequence is valid
        } else if (!sequenceFieldValidator(senseSequence)) {
            alert("Error", "Sense strand sequence is incorrect");
            //Checks for the antisense strand sequence field   
        } else if (antiSenseSequence.length() == 0) {
            alert("Error", "Antiense strand sequence field is mandatory");
            //Checks that the sense strand sequence is valid
        } else if (!sequenceFieldValidator(antiSenseSequence)) {
            alert("Error", "Antisense strand sequence is incorrect");
        }else{
            boolean isPalindromic = palindromic.isSelected();
            boolean isRegex = regexDetector(senseSequence);
            if(origin.length() == 0){
                // if no origin is provided
                if(!isRegex){
                    newEndonuclease = new Endonuclease(name, target, senseSequence, antiSenseSequence, isPalindromic, isRegex);
                }else{
                    newEndonuclease = new RegexEndonuclease(name, target, senseSequence, antiSenseSequence, isPalindromic, isRegex);
                }   
            }else{
                // if origin is provided
                if(!isRegex){
                    newEndonuclease = new Endonuclease(name, origin, target, senseSequence, antiSenseSequence, isPalindromic, isRegex);
                }else{
                newEndonuclease = new RegexEndonuclease(name, origin, target, senseSequence, antiSenseSequence, isPalindromic, isRegex);
            }
        }
        // Changes the cancelled attribute value
        cancelled = false;
        addStage.close();
    }
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
     * Method to verify that only "A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S" characters are
     * present in the textfield.
     * 
     * @param target String with the target sequence
     * @return boolean, true if only "A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S" are present
     */
    private boolean targetFieldValidator(String target) {
        boolean isValid = false;
        String regex = "[A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S]*";
        isValid = target.matches(regex);
        return isValid;
    }

    /**
     * Method to verify that only "A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S" characters are
     * present and at least one '^' is present in the textfield.
     * 
     * @param sequence String with the DNA sequence
     * @return boolean, true if both validations are ok
     */
    private boolean sequenceFieldValidator(String sequence) {
        boolean validSequence = false;
        String regex = "[A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S,^]*";
        if (sequence.matches(regex)) {
            for (int i = 0; i < sequence.length(); i++) {
                char temp = sequence.charAt(i);
                if (temp == '^') {
                    validSequence = true;
                }
            }
        }
        return validSequence;
    }

    /**
     * Method to detect if the endonuclease contains variable value bases in the
     * sequence and needs a regex analysis.
     * 
     * @param sequence endonuclease sense strand sequence
     * @return boolean true if the sequence contains N, R, Y, W, V, B, K, M, D, H, S
     */
    private boolean regexDetector(String sequence) {
        boolean isRegex = false;
        char[] regex = {'N', 'R', 'Y', 'W', 'V', 'B', 'K', 'M', 'D', 'H', 'S'};
        outerloop:
        for (int i = 0; i < sequence.length(); i++) {
            for (int j = 0; j < regex.length; j++) {
                if (sequence.charAt(i) == regex[j]) {
                    isRegex = true;
                    break outerloop;
                }
            }
        }
        return isRegex;
    }
    
    /**
     * Retruns if a new endonuclease object has been created successfully.
     * 
     * @return boolean cancelled
     */
    public boolean isCancelled(){
        return cancelled;
    }
    
    /**
     * Returns the new endonuclease object.
     * 
     * @return Endonuclease object
     */
    public Endonuclease getEndonuclease(){
        return this.newEndonuclease;
    }
    
    /**
     * Closes the add endonuclese window.
     */
    @FXML
    public void close(){
        addStage.close();
    }

}
