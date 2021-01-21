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

/**
 * This class contains all necessary attributes and methods for endonuclease
 * objects.
 *
 * @author Luis Núñez Gómez
 */
public class Endonuclease implements Serializable {

    /**
     * Static class attribute. It contains total number of endonuclease objects.
     */
    protected static int totalEndonucleases;

    /**
     * Attribute for the internal object id.
     */
    protected int internalID;

    /**
     * Attribute for the endonuclease name.
     */
    protected String endonucleaseName;

    /**
     * Attribute for the endonuclease origin information.
     */
    protected String endonucleaseOrigin;

    /**
     * Attribute that contains the endonuclease simplified target (no cutting
     * point).
     */
    protected String endonucleaseSimpleSenseTarget;

    /**
     * Attribute that contains the endonuclease simplified anti sense
     * strandtarget (no cutting point).
     */
    protected String endonucleaseSimpleAntiSenseTarget;

    /**
     * Attribute that contains the endonuclease target sequence and the cutting
     * position.
     */
    protected String senseStrandStringTarget;

    /**
     * Attribute that contains the endonuclease anti sense target sequence.
     */
    protected String antiSenseStrandStringTarget;

    /**
     * Attribute for the endonuclease target size (string size)
     */
    protected int endonucleaseTargetLength;

    /**
     * This attribute contains the number of the exact bp inside the target
     * sequence where the DNA is going to be splited. Example if the terget
     * sequence is "GAATTC" and the result after the cut is "G AATTC", this
     * value equals 1.
     */
    protected int endonucleasesSenseStrandCuttingBp;

    /**
     * This attribute contains the number of the exact bp inside the anti target
     * sequence where the DNA is going to be splited. Example if the terget
     * sequence is "GAATTC" and the result after the cut is "G AATTC", this
     * value equals 1.
     */
    protected int endonucleasesAntiSenseStrandCuttingBp;

    /**
     * Boolean attribute that stores if a endonuclease is palindromic or not. If
     * it is a non palindromic endonuclease, an antisense string analysis need
     * to be performed.
     */
    protected boolean isPalindromic;

    /**
     * Boolean attribute that stores if a endonuclease contains ambiguous bp in
     * the target sequence. This requires a regex analysis.
     */
    protected boolean isRegex;

    /**
     * Visual simplified representation for the sense strand target.
     */
    protected String visualEndonucleaseTarget;

    /**
     * Visual simplified representation for the antisense strand target.
     */
    protected String visualEndonucleaseAntiTarget;

    /**
     * Visual representation for the sense strand target with the cutting bp.
     */
    protected String endonucleaseCleavageRepresentation;

    /**
     * Visual representation for the antisense strand target with the cutting bp.
     */
    protected String endonucleaseAntiCleavageRepresentation;
    
    /**
     * Visual representation for the resulting overhang for the endonuclase.
     */
    protected String endonucleaseOverhang;

    /**
     * Attribute for serialization.
     */
    protected static final long serialVersionUID = 56354267L;

    // Constructor
    
    /**
     * 8 params constructor
     *
     * @param name endonuclease name string
     * @param endonucleaseSimpleSenseTarget String endonuclease target sequence without cleavage bp
     * @param senseStrandStringTarget String endonuclease target sequence with cleavage bp
     * @param antiSenseStrandStringTarget String endonuclease antitarget sequence with cleavage bp
     * @param palindromic Boolean attribute to indicate if the endonuclease is palindromic
     * @param regex Boolean attribute to indicate if the endonuclease has degenerated bps (if true regex anaysis is necessary)
     */
    public Endonuclease(String name, String endonucleaseSimpleSenseTarget, String senseStrandStringTarget, String antiSenseStrandStringTarget, boolean palindromic, boolean regex) {
        this.endonucleaseName = name;
        this.endonucleaseOrigin = "No origin info provided";
        this.endonucleaseSimpleSenseTarget = endonucleaseSimpleSenseTarget;
        this.endonucleaseSimpleAntiSenseTarget = antiTargetSequence(endonucleaseSimpleSenseTarget);
        this.senseStrandStringTarget = senseStrandStringTarget;
        this.antiSenseStrandStringTarget = antiSenseStrandStringTarget;
        this.endonucleasesSenseStrandCuttingBp = cuttingPositionFinder(senseStrandStringTarget);
        this.endonucleasesAntiSenseStrandCuttingBp = cuttingPositionFinder(antiSenseStrandStringTarget);
        this.internalID = idCounter();
        this.endonucleaseTargetLength = endonucleaseSimpleSenseTarget.length();
        this.isPalindromic = palindromic;
        this.isRegex = regex;
        this.visualEndonucleaseTarget = visual53Generator(endonucleaseSimpleSenseTarget, false);
        this.visualEndonucleaseAntiTarget = visual53Generator(antiTargetSequence(endonucleaseSimpleSenseTarget), true);
        this.endonucleaseCleavageRepresentation = visual53Generator(senseStrandStringTarget, false);
        this.endonucleaseAntiCleavageRepresentation = visual53Generator(antiSenseStrandStringTarget, true);
        this.endonucleaseOverhang = overhangGenerator(senseStrandStringTarget, antiSenseStrandStringTarget, this.endonucleasesSenseStrandCuttingBp, this.endonucleasesAntiSenseStrandCuttingBp);
    }

    /**
     * 9 params constructor
     *
     * @param name endonuclease name string
     * @param origin String with the biological origin info
     * @param endonucleaseSimpleSenseTarget String endonuclease target sequence without cleavage bp
     * @param senseStrandStringTarget String endonuclease target sequence with cleavage bp
     * @param antiSenseStrandStringTarget String endonuclease antitarget sequence with cleavage bp
     * @param palindromic Boolean attribute to indicate if the endonuclease is palindromic
     * @param regex Boolean attribute to indicate if the endonuclease has degenerated bps (if true regex anaysis is necessary)
     */
    public Endonuclease(String name, String origin, String endonucleaseSimpleSenseTarget, String senseStrandStringTarget, String antiSenseStrandStringTarget, boolean palindromic, boolean regex) {
        this(name, endonucleaseSimpleSenseTarget, senseStrandStringTarget, antiSenseStrandStringTarget, palindromic, regex);
        this.endonucleaseOrigin = origin;
    }

    /**
     * This method increases the total amount counter of endonuclease objects.
     * 
     * @return int with the endonuclease id value.
     */
    private int idCounter() {
        totalEndonucleases = ++totalEndonucleases;
        return totalEndonucleases;
    }

    /**
     * Returns the endonuclease name.
     * 
     * @return String endonuclease name
     */
    public String getEndonucleaseName() {
        return endonucleaseName;
    }
    
    /**
     * Returns the endonuclease origin.
     * 
     * @return String endonuclease origin
     */
    public String getEndonucleaseOrigin(){
        return endonucleaseOrigin;
    }

    /**
     * Returns the target sequence without cleavage point.
     * 
     * @return String endonuclease target sequence
     */
    public String getEndonucleaseSimpleSenseTarget() {
        return endonucleaseSimpleSenseTarget;
    }

    /**
     * Returns the target's sequence length.
     * 
     * @return int target sequence length
     */
    public int getEndonucleaseTargetLength() {
        return endonucleaseTargetLength;
    }

    /**
     * Returns the cleavage bp position in the sense strand.
     * 
     * @return int endonuclease sense strand cleavage position
     */
    public int getEndonucleasesSenseStrandCuttingBp() {
        return endonucleasesSenseStrandCuttingBp;
    }

    /**
     * Returns the cleavage bp position in the anti sense strand.
     * 
     * @return int endonuclease anti sense cleavage position
     */
    public int getEndonucleasesAntiSenseStrandCuttingBp() {
        return endonucleasesAntiSenseStrandCuttingBp;
    }

    /**
     * Returns if the endonuclease is palindromic.
     * 
     * @return Boolean palindromic
     */
    public boolean getIsPalindromic() {
        return isPalindromic;
    }

    /**
     * Returns if the endonuclease has a degenerated sequence (regex analysis needed).
     * 
     * @return Boolean regex
     */
    public boolean getIsRegex() {
        return isRegex;
    }

    /**
     * Returns a visual representation of the endonuclease's sense strand.
     * 
     * @return String visual target representation
     */
    public String getVisualEndonucleaseTarget() {
        return visualEndonucleaseTarget;
    }

    /**
     * Returns a visual representation of the endonuclease's anti sense strand.
     * 
     * @return String visual antitarget representation
     */
    public String getVisualEndonucleaseAntiTarget() {
        return visualEndonucleaseAntiTarget;
    }

    /**
     * Returns a visual representation of the endonuclease's sense strand showing
     * the cleavage site.
     * 
     * @return String visual cleavage site representation
     */
    public String getEndonucleaseCleavageRepresentation() {
        return endonucleaseCleavageRepresentation;
    }

    /**
     * Returns a visual representation of the endonuclease's anti sense strand showing
     * the cleavage site.
     * 
     * @return String visual anti cleavage site representation
     */
    public String getEndonucleaseAntiCleavageRepresentation() {
        return endonucleaseAntiCleavageRepresentation;
    }
    
    /**
     * Returns a visual representation of the endonuclease overhang fragment.
     * 
     * @return String visual overhang representation
     */
    public String getEndonucleaseOverhang(){
     return endonucleaseOverhang; 
    }
    
    /**
     * Adds 5'- -3' at the end of the provided sequence, just for visual
     * representation.
     * 
     * @param sequence DNA sequence string
     * @param anti indicates if it is a sense or antisense senquence
     * @return String sequence with 5'- -3' in both ends
     */
    private String visual53Generator(String sequence, boolean anti) {
        StringBuilder sbSequence = new StringBuilder(sequence);
        if (!anti) {
            sbSequence.append("-3'").insert(0, "5'-");
        } else {
            sbSequence.append("-5'").insert(0, "3'-");
        }
        return sbSequence.toString();
    }

    /**
     * This method generates the antisense secuence for the endonuclease target
     * sequence.
     * 
     * @param target String with the sense strand sequence
     * @return String with the antisense strand sequence
     */
    private String antiTargetSequence(String target) {
        StringBuilder antiTarget = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char base = target.charAt(i);
            switch (base) {
                case ('A'):
                    antiTarget.append('T');
                    break;
                case ('T'):
                    antiTarget.append('A');
                    break;
                case ('C'):
                    antiTarget.append('G');
                    break;
                case ('G'):
                    antiTarget.append('C');
                    break;
                case ('N'):
                    antiTarget.append('N');
                    break;
                case ('R'):
                    antiTarget.append('Y');
                    break;
                case ('Y'):
                    antiTarget.append('R');
                    break;
                case ('W'):
                    antiTarget.append('W');
                    break;
                case ('V'):
                    antiTarget.append('B');
                    break;
                case ('B'):
                    antiTarget.append('V');
                    break;
                case ('K'):
                    antiTarget.append('M');
                    break;
                case ('M'):
                    antiTarget.append('K');
                    break;
                case ('D'):
                    antiTarget.append('H');
                    break;
                case ('H'):
                    antiTarget.append('D');
                    break;
                case ('S'):
                    antiTarget.append('S');
                    break;
            }
        }
        return antiTarget.toString();
    }
    
    /**
     * Generates the overhang representation, it calculates it based on sense and
     * antisense strand cutting bp positions.
     * 
     * @param antiSenseStrandStringTarget
     * @param senseStrandCutPosition
     * @param antiSenseStrandCutPostion
     * @return String with the overhang respresentation
     */
    private String overhangGenerator(String senseStrandStringTarget, String antiSenseStrandStringTarget , int senseStrandCutPosition, int antiSenseStrandCutPostion){
        StringBuilder overhang;
        if(senseStrandCutPosition == antiSenseStrandCutPostion){
            overhang = new StringBuilder("Blunt");
        }else if(senseStrandCutPosition > antiSenseStrandCutPostion){
           overhang = new StringBuilder(senseStrandStringTarget.replaceAll("^", "").substring(antiSenseStrandCutPostion ,senseStrandCutPosition)+"-3'");
        }else{
            overhang = new StringBuilder((antiSenseStrandStringTarget.replaceAll("^", "").substring(senseStrandCutPosition, antiSenseStrandCutPostion))).reverse();
            overhang.insert(0, "5'-");
        }
        return overhang.toString();
    }

    @Override
    public String toString() {

        return String.format("Name: %s\n"
                + "Origin: %s\n"
                + "Target sequence: 5'--%s--3'\n"
                + "Target sequence length: %d\n"
                + "Antitarget sequence: %s\n"
                + "Cleavage site: %s'\n"
                + "Cutting bp position : %d\n" /*,this.internalID*/
                +"Overhang: %s",
                 this.endonucleaseName, this.endonucleaseOrigin,
                this.endonucleaseSimpleSenseTarget, this.endonucleaseTargetLength, 
                this.endonucleaseSimpleAntiSenseTarget, this.endonucleaseCleavageRepresentation, 
                this.endonucleasesSenseStrandCuttingBp, this.endonucleaseOverhang);
    }
    
    /**
     * Detects the numeric position in the endonuclease sequence where the cut 
     * operation happens, it must be indicated with the '^' character.
     * 
     * @param target String with the endonuclease targer sequence
     * @return int with the numeric value of the cutting position
     */
    private static int cuttingPositionFinder(String target){
        int cutPosition = 0;
        for(int i = 0; i<target.length(); i++){
            char character = target.charAt(i);
            if(character == '^'){
                cutPosition = i;
            }
        }
        return cutPosition;
    }
}
