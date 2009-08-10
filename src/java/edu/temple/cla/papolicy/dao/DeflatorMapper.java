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
public class DeflatorMapper implements ParameterizedRowMapper<Deflator> {

    public Deflator mapRow(ResultSet rs, int colNum) throws SQLException {

        Deflator item = new Deflator();
        item.setYear(rs.getInt("Year"));
        item.setGDP(rs.getFloat("GDP"));
        item.setPriceIndex(rs.getFloat("Price_Index"));
        return item;
    }
    
}
