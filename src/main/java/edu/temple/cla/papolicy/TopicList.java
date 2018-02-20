/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy;

import edu.temple.cla.papolicy.dao.TopicMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.AllTopics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * This class manages the list of topics and subtopics that have been selected
 * @author Paul Wolfgang
 */
public class TopicList {

    private Map<Integer, Topic> majorTopics = new TreeMap<>();

    private Map<Integer, Topic> selectedTopics = new TreeMap<>();

    /**
     * Construct a TopicsList from the response string. If the response is 
     * null, then a special AllTopics topic is selected.  If sub-topics are
     * selected, then the corresponding major topic is added to the 
     * majorTopics list.
     * @param topics The response string from the form submittal.
     * @param jdbcTemplate The jcbcTemplate used to access the database.
     */
    public TopicList(String[] topics, JdbcTemplate jdbcTemplate) {
        if (topics == null) {
            majorTopics.put(0, new AllTopics());
            selectedTopics.put(0, new AllTopics());
            return;
        }
        if (topics[0].contains(",")) {
            topics = topics[0].split(",\\s*");
        }
        for (String topic : topics) {
            int code = Integer.parseInt(topic);
            int majorCode = code > 100 ? code / 100 : code;
            majorTopics.put(majorCode, null);
            selectedTopics.put(code, null);
        }

        // Build comma separated list of major topics
        StringBuilder stb = new StringBuilder();
        for (Integer code : majorTopics.keySet()) {
            stb.append(code);
            stb.append(", ");
        }
        stb.delete(stb.length()-2, stb.length());
        String majorTopicsSelected = stb.toString();

        // Build comma separated list of major topics within selected topics
        stb = new StringBuilder();
        for (Integer code : selectedTopics.keySet()) {
            if (code < 100) {
                stb.append(code);
                stb.append(", ");
            }
        }
        if (stb.length() > 0) {
            stb.delete(stb.length() - 2, stb.length());
        }
        String majorTopicsInSelectedTopics = stb.toString();

        // Build comma separated list of subtopics only
        stb = new StringBuilder();
        for (Integer code : selectedTopics.keySet()) {
            if (code >= 100) {
                stb.append(code);
                stb.append(", ");
            }
        }
        if (stb.length() > 0) {
            stb.delete(stb.length() - 2, stb.length());
        }
        String subTopicsOnly = stb.toString();

        // Update the maps to include the Topic descriptions
        ParameterizedRowMapper<Topic> topicMapper = new TopicMapper();
        String query = "SELECT * FROM MajorCode WHERE Code IN ("
                + majorTopicsSelected + ")";
        List<Topic> topicList = jdbcTemplate.query(query, topicMapper);
        for (Topic aTopic : topicList) {
            majorTopics.put(aTopic.getCode(), aTopic);
        }

        if (majorTopicsInSelectedTopics.length() > 0) {
            query = "SELECT * FROM MajorCode WHERE Code IN ("
                    + majorTopicsInSelectedTopics + ")";
            topicList = jdbcTemplate.query(query, topicMapper);
            for (Topic aTopic : topicList) {
                selectedTopics.put(aTopic.getCode(), aTopic);
            }
        }

        if (subTopicsOnly.length() > 0) {
            query = "SELECT * FROM Code WHERE Code IN ("
                    + subTopicsOnly + ")";
            topicList = jdbcTemplate.query(query, topicMapper);
            for (Topic aTopic : topicList) {
                selectedTopics.put(aTopic.getCode(), aTopic);
            }
        }
    }

    /**
     * Get the selected major topics.
     * @return The selected major topics.
     */
    public Map<Integer, Topic> getMajorTopics() {return majorTopics;}

    /**
     * Get all selected topics.
     * @return All selected topics.
     */
    public Map<Integer, Topic> getSelectedTopics() {return selectedTopics;}

}
