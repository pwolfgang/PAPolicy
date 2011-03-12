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
 * @author Paul
 */
public class StringMapper implements ParameterizedRowMapper<String> {

    private static final Logger logger = Logger.getLogger(YearValueMapper.class);

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return rs.getString(1);
        } catch (SQLException sqlex) {
            logger.error(sqlex);
            throw sqlex;
        }
    }
}
