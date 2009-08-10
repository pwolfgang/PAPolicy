/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import edu.temple.cla.papolicy.dao.Topic;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class TopicMapper implements ParameterizedRowMapper<Topic> {

    public Topic mapRow(ResultSet rs, int colNum) throws SQLException {

        Topic item = new Topic();
        item.setCode(rs.getInt("Code"));
        item.setDescription(rs.getString("Description"));
        return item;
    }

}
