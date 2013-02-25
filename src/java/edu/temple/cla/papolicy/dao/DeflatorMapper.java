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
public class DeflatorMapper implements ParameterizedRowMapper<Deflator> {

    private static final Logger logger = Logger.getLogger(DeflatorMapper.class);

    public Deflator mapRow(ResultSet rs, int colNum) throws SQLException {

        Deflator item = new Deflator();
        try {
        item.setYear(rs.getInt("Year"));
        item.setGDP(rs.getFloat("GDP"));
        item.setPriceIndex(rs.getFloat("Price_Index"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return (this.getClass() == o.getClass());
    }
    
}
