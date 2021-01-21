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

import genlogic.model.DNASequence;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is a reader class. It takes the FASTA DNA containing sequence document
 * and create DNASequence objects.
 *
 * @author Luis Núñez Gómez
 */
public class FASTASequenceReader {

    /**
     * Method to read standard FASTA or multiFASTA files and create DNAsequence
     * objects.
     * 
     * @param path String with the path where the FASTA file has been placed
     * @return DNASequence objets list
     */
    public ObservableList<DNASequence> fileReader(String path) {
        ObservableList<DNASequence> sequences = FXCollections.observableArrayList();
        try {
            /**
             * This block reads the file and save it as a buffered Reader
             */
            File fastaFile = new File(path);
            BufferedReader buffer = new BufferedReader(new FileReader(fastaFile));
            String text;
            StringBuilder temporal = new StringBuilder();
            String line = buffer.readLine();
            /**
             * The program merges all lines in a StringBuilder object (saving
             * memory), adding "-&" characters set between merged lines
             */
            while (line != null) {
                text = line;
                line = buffer.readLine();
                temporal = temporal.append(text).append("-&");
            }
            buffer.close();
            /**
             * The program turns the StringBuilder to string and saves every
             * fasta sequence in a String array using the symbol ">"
             */
            String temporalString = temporal.toString();
            String[] fastaSequences = temporalString.split(">");
            /**
             * The program splits every element of the first String array in a
             * new String array, using the previously "-&" inserted symbols to
             * split lines again
             */
            for (int i = 1; i < fastaSequences.length; i++) {
                String information;
                String sequence = "";
                String[] parts2 = fastaSequences[i].split("-&");
                /**
                 * The first string is always the information atribute for a
                 * DNASequence object, the program saves it in a string called
                 * "information"
                 */
                information = parts2[0];
                /**
                 * The rest of the strings (j =1) are the DNA sequence, the
                 * program concate all of them in a single string called
                 * "sequence"
                 */
                for (int j = 1; j < parts2.length; j++) {
                    sequence = sequence.concat(parts2[j].toUpperCase());
                }
                /**
                 * For every element of the first array called "fastaSequence"
                 * the program creates a new DNASequence object and saves it in
                 * a list object.
                 */
                DNASequence test = new DNASequence(information, sequence);
                sequences.add(test);

            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error, unable to find FASTA file: " + ex);
        } catch (IOException ex) {
            System.out.println("Error I/O, unable to read file: " + ex);
        }
        return sequences;
    }
}
