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
package genlogic.model;

/**
 * This class contains all necessary attributes and methods for regex endonuclease
 * objects. This is a subclass for endonuclease class. Regex endonucleases are a
 * specialized endonuclease objects that contains degenerate genetic sequences, 
 * due to this these objects need to strore a special regex target string attribute.
 * @author Luis Núñez Gómez
 */
public class RegexEndonuclease extends Endonuclease {
    
    /**
     * Stores the regex expression based on the endonuclease target sequence.
     */
    public String regex;
    
    // Constructor
    
    /**
     * 8 params constructor.
     * 
     * @param name endonuclease name string
     * @param endonucleaseSimpleSenseTarget String endonuclease target sequence without cleavage bp
     * @param senseStrandStringTarget String endonuclease target sequence with cleavage bp
     * @param antiSenseStrandStringTarget String endonuclease antitarget sequence with cleavage bp
     * @param palindromic Boolean attribute to indicate if the endonuclease is palindromic
     * @param regex Boolean attribute to indicate if the endonuclease has degenerated bps (if true regex anaysis is necessary)
     */
    public RegexEndonuclease(String name, String endonucleaseSimpleSenseTarget, String senseStrandStringTarget, String antiSenseStrandStringTarget, boolean palindromic, boolean regex){
        super(name, endonucleaseSimpleSenseTarget, senseStrandStringTarget, antiSenseStrandStringTarget, palindromic, regex);
        this.regex = regexEnsambler(endonucleaseSimpleSenseTarget);
    };
    
    /**
     * 9 params constructor.
     * 
     * @param name endonuclease name string
     * @param origin String with the biological origin info
     * @param endonucleaseSimpleSenseTarget String endonuclease target sequence without cleavage bp
     * @param senseStrandStringTarget String endonuclease target sequence with cleavage bp
     * @param antiSenseStrandStringTarget String endonuclease antitarget sequence with cleavage bp
     * @param palindromic Boolean attribute to indicate if the endonuclease is palindromic
     * @param regex Boolean attribute to indicate if the endonuclease has degenerated bps (if true regex anaysis is necessary)
     */
    public RegexEndonuclease(String name, String origin, String endonucleaseSimpleSenseTarget, String senseStrandStringTarget, String antiSenseStrandStringTarget, boolean palindromic, boolean regex){
        super(name, origin, endonucleaseSimpleSenseTarget, senseStrandStringTarget, antiSenseStrandStringTarget, palindromic, regex);
        this.regex = regexEnsambler(endonucleaseSimpleSenseTarget);
    };
    
    /**
     * Generares a String with a regex expression based on the endonuclease
     * target sequence.
     * 
     * @param target endonuclease target sequence
     * @return Regular expresion
     */
    private String regexEnsambler(String target) {
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
            switch (c) {
                case ('A'):
                    regex.append("[A]");
                    break;
                case ('T'):
                    regex.append("[T]");
                    break;
                case ('C'):
                    regex.append("[C]");
                    break;
                case ('G'):
                    regex.append("[G]");
                    break;
                case ('N'):
                    regex.append("[A|T|C|G]");
                    break;
                case ('M'):
                    regex.append("[A|C]");
                    break;
                case ('R'):
                    regex.append("[A|G]");
                    break;
                case ('W'):
                    regex.append("[A|T]");
                    break;
                case ('Y'):
                    regex.append("[C|T]");
                    break;
                case ('S'):
                    regex.append("[C|G]");
                    break;
                case ('K'):
                    regex.append("[G|T]");
                    break;
                case ('H'):
                    regex.append("[A|C|T]");
                    break;
                case ('B'):
                    regex.append("[C|G|T]");
                    break;
                case ('V'):
                    regex.append("[A|C|G]");
                    break;
                case ('D'):
                    regex.append("[A|G|T]");
                    break;
            }
        }
        return regex.toString();
    }
    
    /**
     * Returns the regex string build based on the target sequence.
     * @return String regex
     */
    @Override
    public String getEndonucleaseSimpleSenseTarget(){
        return regex;
    };
}
