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
public class YearValueMapper implements ParameterizedRowMapper<YearValue> {

    private static final Logger logger = Logger.getLogger(YearValueMapper.class);

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
}
