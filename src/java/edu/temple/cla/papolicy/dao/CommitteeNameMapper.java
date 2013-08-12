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
 * Map queries of the CommitteeNames table to a CommitteeName object.
 * @author Paul Wolfgang
 */
public class CommitteeNameMapper implements ParameterizedRowMapper<CommitteeName>{

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    public CommitteeName mapRow(ResultSet rs, int rowNum) throws SQLException {

        CommitteeName item = new CommitteeName();
        try {
        item.setCtyCode(rs.getInt("CtyCode"));
        item.setName(rs.getString("Name"));
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
        if (o == null) return false;
        return (this.getClass() == o.getClass());
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
