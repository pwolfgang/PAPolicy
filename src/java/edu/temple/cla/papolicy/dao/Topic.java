/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import java.util.List;

/**
 *
 * @author Paul Wolfgang
 */
public class Topic {

    private int code;
    private String description;
    private List<Topic> subTopics;

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the subTopics
     */
    public List<Topic> getSubTopics() {
        return subTopics;
    }

    /**
     * @param subTopics the subTopics to set
     */
    public void setSubTopics(List<Topic> subTopics) {
        this.subTopics = subTopics;
    }

}
