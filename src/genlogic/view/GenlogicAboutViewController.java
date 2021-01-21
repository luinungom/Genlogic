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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

/**
 * FXML Controller class for the "About Genlogic" view.
 *
 * @author Luis Núñez Gómez
 */
public class GenlogicAboutViewController {

    /**
     * Clickable hyperlink.
     */
    @FXML
    private Hyperlink linkedIn;

    /**
     * Clickable hyperlink.
     */
    @FXML
    private Hyperlink gitHub;

    /**
     * Stage object.
     */
    @FXML
    private Stage aboutStage;

    /**
     * Opens the default browser and navigates to https://www.linkedin.com/in/luisnunezgomez
     */
    @FXML
    public void handleLinkedIn() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/luisnunezgomez"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

     /**
     * Opens the default browser and navigates to https://github.com/luinungom
     */
    @FXML
    public void handleGitHub() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/luinungom"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
}
