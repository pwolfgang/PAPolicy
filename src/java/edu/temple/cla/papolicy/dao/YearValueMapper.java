/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class YearValueMapper implements ParameterizedRowMapper<YearValue> {

    @Override
    public YearValue mapRow(ResultSet rs, int rowNum) throws SQLException {
        int year = rs.getInt("TheYear");
        String valueString = rs.getString("TheValue");
        Number value = null;
        try {
            value = new Integer(valueString);
        } catch (NumberFormatException ex) {
            try {
                value = new Double(valueString);
            } catch (NumberFormatException ex2) {
                // do nothing in this case
            }
        }
        return new YearValue(year, value);
    }
    
}
