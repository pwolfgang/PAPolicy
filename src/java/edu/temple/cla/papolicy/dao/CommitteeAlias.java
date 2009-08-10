/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 *
 * @author Paul Wolfgang
 */
public class CommitteeAlias {

    private int ID;
    private int ctyCode;
    private String name;
    private String alternateName;
    private int startYear;
    private int endYear;

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the ctyCode
     */
    public int getCtyCode() {
        return ctyCode;
    }

    /**
     * @param ctyCode the ctyCode to set
     */
    public void setCtyCode(int ctyCode) {
        this.ctyCode = ctyCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the alternateName
     */
    public String getAlternateName() {
        return alternateName;
    }

    /**
     * @param alternateName the alternateName to set
     */
    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    /**
     * @return the startYear
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * @param startYear the startYear to set
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     * @return the endYear
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * @param endYear the endYear to set
     */
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

}
