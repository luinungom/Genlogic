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

import genlogic.model.Endonuclease;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains all necessary methods to save and read endonuclease
 * objects from text, dat and memory.
 *
 * @author Luis Núñez Gómez
 */
public class EndonucleaseSerializator {

    /**
     * This method transforms a text file information to endonucleases objects
     * in memory. Text data must follow the following format to be inserted:
     * 1 endonuclease per line, 'name::origin::sequence (Only ACGTNRYWVBKMDHS 
     * chars). This method has been used for initial population during initial
     * development.
     * 
     * @param pathRead text file with information String
     * @return LinkedList with endonuclease objects
     */
    @Deprecated 
    public static List<Endonuclease> textToMemory(String pathRead) {

        Pattern correctLinePattern = Pattern.compile(".*::.*::([A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S]*)::([A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S,^]*)::([A,C,G,T,N,R,Y,W,V,B,K,M,D,H,S,^]*)::(true|false)::(true|false)");
        List<Endonuclease> endonucleasesList = new LinkedList<>();

        try {
            File endonucleaseFileIn = new File(pathRead);
            BufferedReader reader = new BufferedReader(new FileReader(endonucleaseFileIn));
            String text;
            String line = reader.readLine();

            while (line != null) {
                text = line;
                Matcher correctLine = correctLinePattern.matcher(text);
                line = reader.readLine();
                if (correctLine.matches()) {
                    String[] parts = text.split("::");
                    String name = parts[0];
                    String origin = parts[1];
                    String senseStrandSimpleTarget = parts[2];
                    String senseStrandStringTarget = parts[3];
                    String antiSenseStrandStringTarget = parts[4];
                    boolean palindromic = Boolean.parseBoolean(parts[5]);
                    boolean regex = Boolean.parseBoolean(parts[6]);

                    Endonuclease test = new Endonuclease(name, origin, senseStrandSimpleTarget ,senseStrandStringTarget, antiSenseStrandStringTarget, palindromic, regex);
                    endonucleasesList.add(test);
                }
            }
            reader.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Error, unable to find file: " + ex);
        } catch (IOException ex) {
            System.out.println("Error I/O, unable to read file: " + ex);
        }
        return endonucleasesList;

    }

    /**
     * Takes a list of endonuclease objects and saves it in an external dat file.
     * 
     * @param endonucleasesList In memory endonucleases list object
     * @param pathWrite String path to save the dat file
     */
    public static void memoryToDat(List endonucleasesList, String pathWrite) {

        try {
            FileOutputStream endonucleaseFileOut = new FileOutputStream(new File(pathWrite));
            ObjectOutputStream endonucleaseStreamOut = new ObjectOutputStream(endonucleaseFileOut);
            endonucleaseStreamOut.writeObject(endonucleasesList);
            // Close stream
            endonucleaseFileOut.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Error, unable to create file: " + ex);
        } catch (IOException ex) {
            System.out.println("Error I/O, unable to write in file: " + ex);
        }
    }

    /**
     * Reads a dat file with the endonucleases information and loads it in memory.
     * 
     * @param pathRead String path with the source dat file
     * @return Linked list object with endoucleases info
     */
    public static List<Endonuclease> datToMemory(String pathRead) {
        List<Endonuclease> endonucleasesList = new LinkedList();
        try {
            File endonucleaseDat = new File(pathRead);
            ObjectInputStream objectInStream = new ObjectInputStream(new FileInputStream(endonucleaseDat));
            endonucleasesList = (LinkedList) objectInStream.readObject();
            // Close stream
            objectInStream.close();
            
        } catch (ClassNotFoundException ex) {
            System.out.println("Unable to find the right class: " + ex);
        } catch (IOException ex) {
            System.out.println("Error I/O, unable to read the in file: " + ex);
        }
        return endonucleasesList;
    }
    
}
