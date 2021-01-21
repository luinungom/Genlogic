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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Luis Núñez Gómez
 */
public class SplashScreenController extends Preloader {

    private Stage preloaderStage;
    private Scene scene;

    /**
     * Initializes the controller class.
     * 
     * @throws java.io.IOException
     */
    @Override
    public void init() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
        scene = new Scene(root);
    }

    @Override
    public void start(Stage primaryStage) {
        this.preloaderStage = primaryStage;
        primaryStage.getIcons().add(new Image("resources/images/SimpleLogo.png"));

        //Set preloader scene and show stage
        preloaderStage.setScene(scene);
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.alwaysOnTopProperty();
        preloaderStage.centerOnScreen();
        preloaderStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Type.BEFORE_START) {
            try {
               Thread.sleep(1500);
                preloaderStage.hide();
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
