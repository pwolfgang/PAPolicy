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
public class CommitteeAliasMapper implements ParameterizedRowMapper<CommitteeAlias> {

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    public CommitteeAlias mapRow(ResultSet rs, int colNum) throws SQLException {

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
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        return (o.getClass() == this.getClass());
    }
}
