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
 * Map queries for DropDown items associated with a filter to DropDown objects.
 * @author Paul Wolfgang
 */
public class DropDownItemMapper implements ParameterizedRowMapper<DropDownItem> {

    private static final Logger logger = Logger.getLogger(DropDownItemMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    public DropDownItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        DropDownItem item = new DropDownItem();
        try {
            item.setID(rs.getInt("ID"));
            item.setDescription(rs.getString("Description"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
    }
    
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
