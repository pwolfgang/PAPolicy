/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class TopicMapper implements ParameterizedRowMapper<Topic> {

    private static final Logger logger = Logger.getLogger(TopicMapper.class);

    public Topic mapRow(ResultSet rs, int colNum) throws SQLException {

        Topic item = new Topic();
        try {
            item.setCode(rs.getInt("Code"));
            item.setDescription(rs.getString("Description"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
    }

}
