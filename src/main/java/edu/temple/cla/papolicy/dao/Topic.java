/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import java.util.List;

/**
 * A topic consists of a major topic and a list of subtopics for each
 * major topic.
 * @author Paul Wolfgang
 */
public class Topic {

    private int code;
    private String description;
    private List<Topic> subTopics;

    /**
     * The unique topic code
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * The unique topic code
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * The text to display for this topic
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The text to display for this topic
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The list of subtopics.
     * @return the subTopics
     */
    public List<Topic> getSubTopics() {
        return subTopics;
    }

    /**
     * The list of subtopics.
     * @param subTopics the subTopics to set
     */
    public void setSubTopics(List<Topic> subTopics) {
        this.subTopics = subTopics;
    }

}
