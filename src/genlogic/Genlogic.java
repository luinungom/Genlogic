/*
 * Copyright (C) 2021 Luis Núñez Gómez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package genlogic;

import com.sun.javafx.application.LauncherImpl;
import genlogic.model.DNASequence;
import genlogic.view.GenlogicMainViewController;
import genlogic.view.SplashScreenController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class to start the application.
 * @author Luis Núñez Gómez
 */
public class Genlogic extends Application {
    
    /**
     * Main stage object
     */
    private Stage primaryStage;
    /**
     * Observable list of DNA sequences.
     */
    public ObservableList<DNASequence> sequences_table = FXCollections.observableArrayList();
    
    @Override
    public void start(Stage stage) throws Exception {
        
        this.primaryStage = stage;
        this.primaryStage.setTitle("Genlogic");
              
        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("/resources/images/SimpleLogo.png"));
                
        FXMLLoader loader = new FXMLLoader (getClass().getResource("view/GenlogicMainView.fxml"));
        Parent root = (Parent) loader.load();
        GenlogicMainViewController mainViewController = loader.getController();
        // Pass the stage object to main controller
        mainViewController.setStage(stage);
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Returns the main stage.
     * 
     * @return primaryStage object
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }
    
    /**
     * Returns the data as an array list of DNASequences.
     * 
     * @return ObservableList of DNASequence objects
     */
    public ObservableList<DNASequence> getDNASequenceDataList(){
        return sequences_table;
    }
    
    /**
     * Allows closing the program using a MenuItem button.
     */
    public void closeApplication(){
        this.primaryStage.close();
    }

    /**
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(Genlogic.class,SplashScreenController.class, args);
        //launch(args);
    }
    
}
