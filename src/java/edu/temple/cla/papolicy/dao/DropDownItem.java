/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 * A DropDownItem is an item to be displayed in a drop down menu from a filter
 * or as multiple check boxes in a filter.
 * @author Paul Wolfgang
 */
public class DropDownItem {

    private int ID;
    private String description;
    
    public DropDownItem() {};
    
    public DropDownItem(int ID, String description) {
        this.ID = ID;
        this.description = description;
    }

    /**
     * The unique id for this item
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * The unique id for this item
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * The text to be displayed.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The text to be displayed.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
