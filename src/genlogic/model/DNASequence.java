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
package genlogic.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all necessary atributes and methods for DNA sequence
 * objects.
 *
 * @author Luis Núñez Gómez
 */
public class DNASequence implements Serializable {

    /**
     * This atribute contains the total number of existing sequences in the
     * running program.
     */
    private static int totalSequences;
    /**
     * Atribute with the internal ID of the object.
     */
    private int internalID;
    /**
     * Atribute with the provided information about the sequence. If this information
     * is not provided, the default value will be "No information provided".
     */
    private String information;
    /**
     * Atribute with the nucleotid sequence as String.
     */
    private String sequence;
    /**
     * Atribute with the total sequence length.
     */
    private float length;
    /**
     * Atribute with nucleotids percentages in the sequence.
     */
    private Map<String, Float> percentages = new HashMap<>();
    /**
     * Serialization atribute.
     */
    private static final long serialVersionUID = 4245486943L;

    // Constructors
    /**
     * Only sequence given constructor.
     *
     * @param sequence DNA sequence
     */
    public DNASequence(String sequence) {
        this.sequence = sequence;
        this.length = sequence.length();
        this.information = "No information provided";
        this.percentages = nucleotidePercentageAnalyzer(sequence);
        this.internalID = idCounter();
    }

    /**
     * Two params contructor. It calls the one param constructor and fills the
     * optional information sequence.
     *
     * @param information Technical information provided in the FASTA sequence
     * header
     * @param sequence DNA sequence
     */
    public DNASequence(String information, String sequence) {
        this(sequence);
        this.information = information;
    }
        
    /**
     * Method to access the DNA sequence object internal ID.
     * 
     * @return int with the DNA sequence internal ID
     */
    public int getInternalID() {
        return internalID;
    }

    /**
     * Method to access the DNA sequence object information.
     * 
     * @return String with information present in the FASTA sequence
     */
    public String getInformation() {
        return information;
    }

    /**
     * Method to access the DNA sequence object sequence.
     * 
     * @return String with the DNA sequence
     */
    public String getSequence() {
        return sequence;
    }
    
    /**
     * Method to get the DNA sequence length.
     * 
     * @return DNA sequence length float 
     */
    public float getLength(){
        return length;
    }
    
    /**
     * Method to get the DNA sequence length parsed as int for visual purposes.
     * 
     * @return DNA sequence length integer
     */
    public int getIntLength(){
        return (int)length;
    }
    
    /**
     * Method to get the total amount of DNA sequences loaded.
     * 
     * @return int number of total sequences
     */
    public int getTotalSequences(){
        return totalSequences;
    }
    
    /**
     * Returns percentages for each nucleotide type.
     * 
     * @return Map<String nucleotide name, Float percentage>
     */
    public Map<String, Float> getPercentage(){
        return percentages;
    }
    
    /**
     * This method increases the total amount counter of DNA sequece objects.
     * 
     * @return int with the DNA sequence id value.
     */
    private int idCounter() {
        totalSequences = ++totalSequences;
        return totalSequences;
    }

    /**
     * Analyzes the DNA sequence nucleotides percentages.
     * 
     * @param sequence String DNA sequence
     * @return Map with sequence nucleotides percentages
     */
    private Map<String, Float> nucleotidePercentageAnalyzer(String sequence) {
        int totalA = 0, totalC = 0, totalG = 0, totalT = 0, totalOthers = 0;
        
        for (char c : sequence.toCharArray()) {
            switch (c) {
                case 'A':
                    ++totalA;
                    break;
                case 'C':
                    ++totalC;
                    break;
                case 'G':
                    ++totalG;
                    break;
                case 'T':
                    ++totalT;
                    break;
                default:
                    ++totalOthers;
            }
        }
        percentages.put("Adenine", (totalA / length) * 100);
        percentages.put("Cytosine", (totalC / length) * 100);
        percentages.put("Guanine", (totalG / length) * 100);
        percentages.put("Thymine", (totalT / length) * 100);
        percentages.put("Others", (totalOthers / length) * 100);
        
        return percentages;
    }
    
    @Override
    public String toString() {
        String toStringSequenceFormatter;
        if(sequence.length()>90){
            toStringSequenceFormatter = (String.format("5' %.90s... 3'", this.sequence));
        }else{
            toStringSequenceFormatter = (String.format("5' %s 3'", this.sequence));
        }

        return String.format("FASTA SEQUENCE\n"
                +"--------------\n"
                +"Internal program id: %s \n"
                + "Information: %s \n"
                + "Sequence length: %.0f bp\n"
                + "DNA sequence (showing 90 bp max): \n"
                + toStringSequenceFormatter+"\n"
                + "Adenine: %.2f%% \n"
                + "Cytosine: %.2f%% \n"
                + "Guanine: %.2f%% \n"
                + "Thymine: %.2f%% \n"
                + "Others*: %.2f%% "
                + "*(several FASTA formats could include variable nucleotid "
                + "combinations as characters) \n",
                 this.internalID, this.information,this.length ,
                 percentages.get("Adenine"), percentages.get("Cytosine"), 
                 percentages.get("Guanine"), percentages.get("Thymine"), 
                 percentages.get("Others"));
    }
}
