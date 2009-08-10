/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import edu.temple.cla.papolicy.dao.TopicMapper;
import edu.temple.cla.papolicy.dao.Topic;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * This class manages the list of topics and subtopics that have been selected
 * @author Paul Wolfgang
 */
public class TopicList {

    private Map<Integer, Topic> majorTopics = new TreeMap<Integer, Topic>();

    private Map<Integer, Topic> subTopics = new TreeMap<Integer, Topic>();

    public TopicList(String[] topics, SimpleJdbcTemplate jdbcTemplate) {
        if (topics == null) return;
        if (topics[0].contains(",")) {
            topics = topics[0].split(",\\s*");
        }
        for (String topic : topics) {
            int code = Integer.parseInt(topic);
            int majorCode = code > 100 ? code / 100 : code;
            majorTopics.put(majorCode, null);
            subTopics.put(code, null);
        }

        // Build comma separated list of major topics
        StringBuilder stb = new StringBuilder();
        for (Integer code : majorTopics.keySet()) {
            stb.append(code);
            stb.append(", ");
        }
        stb.delete(stb.length()-2, stb.length());
        String majorTopicsSelected = stb.toString();

        // Build comma separated list of major topics within subtopics
        stb = new StringBuilder();
        for (Integer code : subTopics.keySet()) {
            if (code < 100) {
                stb.append(code);
                stb.append(", ");
            }
        }
        if (stb.length() > 0) {
            stb.delete(stb.length() - 2, stb.length());
        }
        String majorTopicsInSubtopics = stb.toString();

        // Build comma separated list of subtopics only
        stb = new StringBuilder();
        for (Integer code : subTopics.keySet()) {
            if (code >= 100) {
                stb.append(code);
                stb.append(", ");
            }
        }
        if (stb.length() > 0) {
            stb.delete(stb.length() - 2, stb.length());
        }
        String subTopicsOnly = stb.toString();

        // Update the maps to includ the Topics
        ParameterizedRowMapper<Topic> topicMapper = new TopicMapper();
        String query = "SELECT * FROM MajorCode WHERE Code IN ("
                + majorTopicsSelected + ")";
        List<Topic> topicList = jdbcTemplate.query(query, topicMapper);
        for (Topic aTopic : topicList) {
            majorTopics.put(aTopic.getCode(), aTopic);
        }

        if (majorTopicsInSubtopics.length() > 0) {
            query = "SELECT * FROM MajorCode WHERE Code IN ("
                    + majorTopicsInSubtopics + ")";
            topicList = jdbcTemplate.query(query, topicMapper);
            for (Topic aTopic : topicList) {
                subTopics.put(aTopic.getCode(), aTopic);
            }
        }

        if (subTopicsOnly.length() > 0) {
            query = "SELECT * FROM Code WHERE Code IN ("
                    + subTopicsOnly + ")";
            topicList = jdbcTemplate.query(query, topicMapper);
            for (Topic aTopic : topicList) {
                subTopics.put(aTopic.getCode(), aTopic);
            }
        }
    }

    public Map<Integer, Topic> getMajorTopics() {return majorTopics;}

    public Map<Integer, Topic> getSubTopics() {return subTopics;}

}
