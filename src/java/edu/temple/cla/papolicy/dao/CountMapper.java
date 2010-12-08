/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import edu.temple.cla.papolicy.Utility;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class CountMapper implements ParameterizedRowMapper<Integer> {

    private static final Logger logger = Logger.getLogger(CountMapper.class);

    public Integer mapRow(ResultSet rs, int colNum) throws SQLException {
        try {
            return new Integer(rs.getInt("Count"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
    }
}
