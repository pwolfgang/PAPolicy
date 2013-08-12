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
 * Map a query of the Code or MajorCode tables to a Topic object.
 * @author Paul Wolfgang
 */
public class TopicMapper implements ParameterizedRowMapper<Topic> {

    private static final Logger logger = Logger.getLogger(TopicMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
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

    /**
     * Determine of two ParameterizedRowmapper objects are equal.  Since
     * the row mapper is stateless, equality of class is sufficient.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.getClass() == o.getClass();
    }

    /**
     * Since all RowMapper objects of the same class are equal,
     * the hashCode is the hashCode of the class object.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
