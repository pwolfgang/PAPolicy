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
public class DropDownItemMapper implements ParameterizedRowMapper<DropDownItem> {

    private static final Logger logger = Logger.getLogger(DropDownItemMapper.class);

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
}
