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
public class CountMapper implements ParameterizedRowMapper<Integer> {

    public Integer mapRow(ResultSet rs, int colNum) throws SQLException {
        return new Integer(rs.getInt("Count"));
    }
}
