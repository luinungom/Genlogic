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
package genlogic;

import genlogic.model.RestrictionSite;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the logic necessary to save results in txt and csv files.
 *
 * @author Luis Núñez Gómez
 */
public class RestrictionSiteSerializator {

    /**
     * Creates a txt file in the specified path and populates it using the provided
     * results in the Observable list object.
     * 
     * @param path String that contains the file destination
     * @param matchingRestrictionSites Observable list that contains results
     */
    public static void saveFile(String path, List<RestrictionSite> matchingRestrictionSites) {
        try {
            // Creates the file
            FileWriter fileWriter = new FileWriter(path);
            // Populate the file with RestrictionSite result objects
            for (RestrictionSite site : matchingRestrictionSites){
            fileWriter.write(site.toString());
            fileWriter.write(System.getProperty("line.separator")); // new line
        }
            fileWriter.write("Total Cleavage Sites: "+matchingRestrictionSites.size());
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(RestrictionSiteSerializator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Comma symbol used as separator.
     */
    private static final String CSV_SEPARATOR = ",";
    
    /**
     * Creates a csv file in the specified path and populates it using the provided
     * results in the Observable list object.
     * 
     * @param path String that contains the file destination
     * @param matchingRestrictionSites Observable list that contains results
     */
    public static void writeToCSV (String path, List<RestrictionSite> matchingRestrictionSites)
    {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            for (RestrictionSite site : matchingRestrictionSites){
                // Populate the file with RestrictionSite result objects
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(site.getEndonucleaseName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(site.getRestrictionSitePosition());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(site.getStrand());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.write("Total Cleavage Sites: "+matchingRestrictionSites.size());
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {}
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }
}
