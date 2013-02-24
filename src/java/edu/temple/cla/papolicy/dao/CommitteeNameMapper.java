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
public class CommitteeNameMapper implements ParameterizedRowMapper<CommitteeName>{

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    public CommitteeName mapRow(ResultSet rs, int colNum) throws SQLException {

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

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return (this.getClass() == o.getClass());
    }
}
