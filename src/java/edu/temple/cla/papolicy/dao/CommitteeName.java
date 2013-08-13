/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 * CommitteeNames maps the committee codes to the current committee names.
 * @author Paul Wolfgang
 */
public class CommitteeName {
    
    private int ctyCode;
    private String name;

    /**
     * The ctyCode is the unique code assigned to this committee
     * @return the ctyCode
     */
    public int getCtyCode() {
        return ctyCode;
    }

    /**
     * The ctyCode is the unique code assigned to this committee
     * @param ctyCode the ctyCode to set
     */
    public void setCtyCode(int ctyCode) {
        this.ctyCode = ctyCode;
    }

    /**
     * The name is the current name of this committee.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The name is the current name of this committee.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }



}
