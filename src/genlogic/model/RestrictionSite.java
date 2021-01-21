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
 * This class contains all necessary attributes and methods for restriction sites
 * objects.
 *
 * @author Luis Núñez Gómez
 */
public class RestrictionSite implements Serializable {

    /**
     * Stores the total number of restriction sites.
     */
    private static int totalRestrictionSites;
    
    /**
     * This attribute contains an internal id for every restriction site object.
     */
    private int matchID;
    
    /**
     * These attributes contain the number of the first and last bps (5'->3')
     * where the endonuclease matches in the DNA sequence.
     */
    private int firstBpMatch, lastBpMatch;
    
    /**
     * The endonuclease with target in the current fragment.
     */
    private Endonuclease endonuclease;
    
    /**
     * The sequence where the target is present
     */
    private DNASequence sequence;
    
    /**
     * These attributes contain the lengths before and after the cutting operation
     * in the restriction site.
     */
    private int upstreamLength, downstreamLength;
    
    /**
     * When a no palindromic endonuclease is applied to the analysis(anti sense 
     * strand), an extra calculated value needs to be obtained to detect the 
     * cleavage bp in the sense strand.
     */
    private int restrictionSitePosition;
    
    /**
     * This String attribute stores if the match happened in the sense or 
     * antisense string.
     */
    private String strand;
    
    /**
     * Stores the endonuclease object name.
     */
    private String endonucleaseName;
   

    //Constructors
    public RestrictionSite(Endonuclease endonuclease, int counter,String strand , DNASequence sequence) {
        this.matchID = idCounter();
        this.endonuclease = endonuclease;
        this.upstreamLength = (counter)+endonuclease.getEndonucleasesSenseStrandCuttingBp();
        this.downstreamLength = (int)(sequence.getLength()-this.upstreamLength);
        this.restrictionSitePosition = restrictionSitePositionCalculator(endonuclease, counter, strand, sequence);
        this.strand = strand;
        this.endonucleaseName = endonuclease.getEndonucleaseName();
    }


    /**
     * Returns the total amount of restriction sites found.
     * 
     * @return int Total restriction sites
     */
    public static int getTotalRestrictionSites() {
        return totalRestrictionSites;
    }

    /**
     * Sets the total amount of restriction sites. Useful to set value to 0
     * between analysis.
     * 
     * @param totalRestrictionSites int value for setting the total amount
     */
    public static void setTotalRestrictionSites(int totalRestrictionSites) {
        RestrictionSite.totalRestrictionSites = totalRestrictionSites;
    }

    /**
     * Returns the endonuclease for the restriction site.
     * 
     * @return Endonuclease object
     */
    public Endonuclease getEndonuclease() {
        return endonuclease;
    }
    
    /**
     * Returns the strand containing target sequence.
     * 
     * @return String strand
     */
    public String getStrand(){
        return strand;
    }
    
    /**
     * Returns the numeric position of the cleavage bp.
     * 
     * @return int cleavage numeric position
     */
    public int getRestrictionSitePosition(){
        return restrictionSitePosition;
    }
    
    /**
     * Returns the endonuclease name for the restriction site.
     * 
     * @return String endonuclease name
     */
    public String getEndonucleaseName(){
        return endonucleaseName;
    }
    
     /**
     * Increases the total amount counter of Restriction sites objects.
     * 
     * @return int with the Restriction site id value.
     */
    private int idCounter() {
        totalRestrictionSites = ++totalRestrictionSites;
        return totalRestrictionSites;
    }
    
    /**
     * Calculates the exact cleavage bp, if the analysis uses an non palindromic
     * endonuclease it calculates the position in the sense strand (even when the
     * target is in the antisense strand), if not it just returns the cleavage position.
     * 
     * @param strand String strand where the target sequence is placed, it can be sense or antisense
     * @param sequence DNA sequence where the restriction site is present
     * @param endonuclease endonuclease used in the analysis
     * @param counter Numeric position in the DNA strand where the restriciton site has been found
     * @return int extact cleavage bp in the anti sense strand, if not it just returns back the cleavage position
     */
    private int restrictionSitePositionCalculator(Endonuclease endonuclease, int counter,String strand, DNASequence sequence){
        if(strand.equals("antisense")){
            int upstreamNoPalindromic =(int) sequence.getLength()-(((counter)+endonuclease.getEndonucleasesAntiSenseStrandCuttingBp()));
            return upstreamNoPalindromic;
        }else{
            return this.upstreamLength;
        }
    }
    
    @Override
    public String toString(){
        return String.format("Restriction enzyme: %s; \n"
                +"Exact cleavage position in the sequence 5'->3': %d; \n"
                +"Sense or antisense strand: %s"
                ,this.endonuclease.getEndonucleaseName(), this.restrictionSitePosition, this.strand);
    }

}
