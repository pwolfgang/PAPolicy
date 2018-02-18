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
 * Map queries to YearValue objects.
 * @author Paul Wolfgang
 */
public class YearValueMapper implements ParameterizedRowMapper<YearValue> {

    private static final Logger logger = Logger.getLogger(YearValueMapper.class);

    /**
     * Perform the mapping. The value is either an integer or a double.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    public YearValue mapRow(ResultSet rs, int rowNum) throws SQLException {
        int year = rs.getInt("TheYear");
        Number value = null;
        try {
            String valueString = rs.getString("TheValue");
            try {
                value = new Integer(valueString);
            } catch (NumberFormatException ex) {
                try {
                    value = new Double(valueString);
                } catch (NumberFormatException ex2) {
                    logger.error(valueString + " not valid ", ex2);
                }
            }
        } catch (SQLException sqlex) {
            logger.error(sqlex);
            throw sqlex;
         }
        return new YearValue(year, value);
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
