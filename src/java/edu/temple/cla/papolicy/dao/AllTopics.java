/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import java.util.Collections;
import java.util.List;

/**
 * This is a special Topic class that represents the selection of
 * all topics.  If no topic is selected, then all filtered records
 * for the selected tables are selected.
 * @author Paul Wolfgang
 */
public class AllTopics extends Topic {

     /**
      * This topic has the topic code of zero.
     * @return 0
     */
    @Override
    public int getCode() {
        return 0;
    }

    /**
     * This topic has the description "All topics"
     * @return "All topics"
     */
    @Override
    public String getDescription() {
        return "All topics";
    }

    /**
     * This topic has no subtopics.
     * @return an empty list.
     */
    @Override
    public List<Topic> getSubTopics() {
        return Collections.emptyList();
    }

}
