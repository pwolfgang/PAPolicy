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
public class CommitteeAliasMapper implements ParameterizedRowMapper<CommitteeAlias> {

    public CommitteeAlias mapRow(ResultSet rs, int colNum) throws SQLException {

        CommitteeAlias item = new CommitteeAlias();
        item.setID(rs.getInt("ID"));
        item.setCtyCode(rs.getInt("CtyCode"));
        item.setName(rs.getString("Name"));
        item.setAlternateName(rs.getString("AlternateName"));
        item.setStartYear(rs.getInt("StartYear"));
        item.setEndYear(rs.getInt("EndYear"));
        return item;
    }

}
