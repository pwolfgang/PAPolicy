/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Paul Wolfgang
 */
public class AllTopics extends Topic {

     /**
     * @return the code
     */
    @Override
    public int getCode() {
        return 0;
    }

    /**
     * @return the description
     */
    @Override
    public String getDescription() {
        return "All topics";
    }

    /**
     * @return the subTopics
     */
    @Override
    public List<Topic> getSubTopics() {
        return new ArrayList<Topic>();
    }

}
