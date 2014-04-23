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
 * Map a count query to an Integer value.
 * @author Paul Wolfgang
 */
public class CountMapper implements ParameterizedRowMapper<Integer> {

    private static final Logger logger = Logger.getLogger(CountMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new Integer(rs.getInt("Count"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
    }
    
    /**
     * Determine of two ParameterizedRowmapper objects are equal.  Since
     * the row mapper is stateless, equality of class is sufficient.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        return (o.getClass() == this.getClass());
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
