/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.tables.AbstractTable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Paul Wolfgang
 */
public class Dataset {

    private AbstractTable table;
    private List<Topic> topics = new ArrayList<Topic>();
    private String freeText;

    public Dataset(AbstractTable table) {
        this.table = table;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    @Override
    public String toString() {
        if (freeText == null && topics.size() != 0) {
            return getTopicsDescriptions() + " " + table.toString();
        } else if (freeText != null && topics.size() != 0) {
            return table.toString() + " about " + freeText + " in " + getTopicsDescriptions();
        } else if (freeText != null && topics.size() == 0) {
            return table.toString() + " about " + freeText;
        } else {
            return table.toString();
        }
    }

    private String getTopicsDescriptions() {
            StringBuilder stb = new StringBuilder();
            stb.append(topics.get(0).getDescription());
            for (int i = 1; i < topics.size() - 1; i++) {
                stb.append(", ");
                stb.append(topics.get(i).getDescription());
            }
            if (topics.size()-1 > 0) {
                stb.append(", and ");
                stb.append(topics.get(topics.size()-1).getDescription());
            }
            return stb.toString();
    }
}
