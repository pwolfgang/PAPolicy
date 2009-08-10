/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import edu.temple.cla.papolicy.dao.DropDownItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class DropDownItemMapper implements ParameterizedRowMapper<DropDownItem> {

    @Override
    public DropDownItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        DropDownItem item = new DropDownItem();
        item.setID(rs.getInt("ID"));
        item.setDescription(rs.getString("Description"));
        return item;
    }
}
