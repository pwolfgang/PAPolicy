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
 * Class to map query of the CommitteeAliases table to the CommitteeAlias object.
 * @author Paul Wolfgang
 */
public class CommitteeAliasMapper implements ParameterizedRowMapper<CommitteeAlias> {

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    public CommitteeAlias mapRow(ResultSet rs, int rowNum) throws SQLException {

        CommitteeAlias item = new CommitteeAlias();
        try {
            item.setID(rs.getInt("ID"));
            item.setCtyCode(rs.getInt("CtyCode"));
            item.setName(rs.getString("Name"));
            item.setAlternateName(rs.getString("AlternateName"));
            item.setStartYear(rs.getInt("StartYear"));
            item.setEndYear(rs.getInt("EndYear"));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
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
