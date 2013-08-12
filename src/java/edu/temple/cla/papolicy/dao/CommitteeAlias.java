/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 * CommitteeAlias represents the table that contains the different names
 * that a committee had over its lifetime.
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
     * Unique ID used as primary key.
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Unique ID used as primary key.
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * The committee code. Unique ID of the committee.
     * @return the ctyCode
     */
    public int getCtyCode() {
        return ctyCode;
    }

    /**
     * The committee code.
     * @param ctyCode the ctyCode to set
     */
    public void setCtyCode(int ctyCode) {
        this.ctyCode = ctyCode;
    }

    /**
     * The committee current name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The committee current name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * An alternate name for this committee.
     * @return the alternateName
     */
    public String getAlternateName() {
        return alternateName;
    }

    /**
     * An alternate name for this committee.
     * @param alternateName the alternateName to set
     */
    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    /**
     * The year that the committee started using this alternate name.
     * @return the startYear
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * The year that the committee started using this alternate name.
     * @param startYear the startYear to set
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     * The last year that the committee used this alternate name.
     * @return the endYear
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * The last year that the committee used this alternate name.
     * @param endYear the endYear to set
     */
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

}
