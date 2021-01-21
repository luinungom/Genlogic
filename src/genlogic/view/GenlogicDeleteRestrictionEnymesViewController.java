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
import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Luis Núñez Gómez
 */
public class GenlogicDeleteRestrictionEnymesViewController {

    /**
     * Table to show all existing endonucleases.
     */
    @FXML
    private TableView<Endonuclease> endonucleaseTable;

    /**
     * Column of the endonucleaseTable TableView.
     */
    @FXML
    private TableColumn<Endonuclease, String> nameColumn;

    /**
     * Column of the endonucleaseTable TableView.
     */
    @FXML
    private TableColumn<Endonuclease, String> originColumn;

    /**
     * Column of the endonucleaseTable TableView.
     */
    @FXML
    private TableColumn<Endonuclease, String> targetColumn;

    /**
     * Column of the endonucleaseTable TableView.
     */
    @FXML
    private TableColumn<Endonuclease, Integer> lengthColumn;

    /**
     * Column of the endonucleaseTable TableView.
     */
    @FXML
    private TableColumn<Endonuclease, String> cleavage_siteColumn;

    /**
     * Column of the endonucleaseTable TableView.
     */
    @FXML
    private TableColumn<Endonuclease, String> overhangColumn;

    /**
     * Button for deleting the selected endonuclase
     */
    @FXML
    private Button deleteBtn;

    /**
     * Stage object for the delete view.
     */
    @FXML
    private Stage deleteStage;
    
    /**
     * ObservableList that contains endonuclease objects
     */
    private ObservableList<Endonuclease> endonucleases_table = FXCollections.observableArrayList();
    
    /**
     * List with all serialized endonucleases
     */
    private List<Endonuclease> serializedEndonucleases = new LinkedList();
    
    /**
     * Instance of the Main view controller.
     */
    private GenlogicMainViewController mainViewController = new GenlogicMainViewController();
    
    /**
     * Endonuclease selected in the endonuclease table.
     */
    private Endonuclease selectedEndonuclease;
    
    /**
     * Changes to false if the deletion operation has been completed.
     */
    private boolean isCancelled = true;


    /**
     * Loads the endonucleases table, disables and enables the delete button.
     * 
     * @param serializedEndonucleases
     */
    public void loadEndonucleasesTable(List<Endonuclease> serializedEndonucleases) {
        this.serializedEndonucleases = serializedEndonucleases;
        // Set columns for the endonucleases table
        nameColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseName"));
        originColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseOrigin"));
        targetColumn.setCellValueFactory(param ->{String result = String.join("\n", param.getValue().getVisualEndonucleaseTarget(),param.getValue().getVisualEndonucleaseAntiTarget()); return new SimpleStringProperty(result);});
        lengthColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseTargetLength"));
        cleavage_siteColumn.setCellValueFactory(param ->{String result = String.join("\n", param.getValue().getEndonucleaseCleavageRepresentation(),param.getValue().getEndonucleaseAntiCleavageRepresentation()); return new SimpleStringProperty(result);});
        overhangColumn.setCellValueFactory(new PropertyValueFactory("endonucleaseOverhang"));
        showEndonucleases(this.serializedEndonucleases);
        // Disable the delete button
        deleteBtn.setDisable(true);
        // Enable a selected table value listener
        endonucleaseTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
        selectedEndonuclease = newValue;
        // Enable the delete button once a endonuclease is selected
        if(selectedEndonuclease != null){
            // If a value is selected in the table enables the delete button
            deleteBtn.setDisable(false);
        }else{
            /** If no value is selected again (like just after deleting an element
             * of the table, it deisables the delete button)*/
            deleteBtn.setDisable(true);
        }
        });
    }
    
    /**
     * Removes the selected endonuclease from the load ones in the table.
     */
    @FXML
    public void handleDeleteButton(){
        if((confirmation("Confirmation","Are you sure? (This operation cannot be undone)"))== ButtonType.OK){
        this.serializedEndonucleases.remove(selectedEndonuclease);
        endonucleaseTable.getItems().clear();
        showEndonucleases(serializedEndonucleases);
        isCancelled = false;
        }
    } 

    /*     
    * Sets stage to this controller.
    
    * @param stage main stage
    */
    public void setStage(Stage stage) {
        this.deleteStage = stage;
    }
    
    /**
     * Loads endonucleases in the list.
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
     * Shows a confirmation dialog.
     * 
     * @param title String for the dialog's title
     * @param message String for the the dialog's message
     * @return result
     */
    public static ButtonType confirmation(String title, String message) {
    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "");
    a.setTitle(title);
    a.setHeaderText(null);
    a.setContentText(message);
    a.showAndWait();
    ButtonType result = a.getResult();
    return result;
    }
    
    /**
     * Returns if the endonuclease list has been modified.
     * 
     * @return boolean true if any endonuclease has been deleted
     */
    public boolean getIsCancelled(){
        return this.isCancelled;
    }
}
