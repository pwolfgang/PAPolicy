/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.dao;

import edu.temple.cla.papolicy.tables.Table;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class TableMapper implements ParameterizedRowMapper<Table> {

    public TableMapper() {
    }

    @Override
    public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
        String packageName = "edu.temple.cla.papolicy.tables";
        String className = rs.getString("Class");
        try {
            Table item =
                    (Table) Class.forName(packageName+ "."
                    + className).newInstance();
            item.setId(rs.getInt("ID"));
            item.setTableName(rs.getString("TableName"));
            item.setTableTitle(rs.getString("TableTitle"));
            item.setMajorOnly(rs.getBoolean("MajorOnly"));
            item.setCodeColumn(rs.getString("CodeColumn"));
            item.setTextColumn(rs.getString("TextColumn"));
            item.setYearColumn(rs.getString("YearColumn"));
            item.setMinYear(rs.getInt("MinYear"));
            item.setMaxYear(rs.getInt("MaxYear"));
            item.setLinkColumn(rs.getString("LinkColumn"));
            String drillDownColumnList = rs.getString("DrillDownFields");
            if (drillDownColumnList != null) {
                String[] drillDownColumnListArray = drillDownColumnList.split(",\\s*");
                item.setDrillDownColumns(drillDownColumnListArray);
            }
            return item;
        } catch (Exception ex) {
            // do nothing
        }
        return null;
    }
}
