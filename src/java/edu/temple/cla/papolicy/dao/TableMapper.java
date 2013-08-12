/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.dao;

import edu.temple.cla.papolicy.tables.Table;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Map a query of the Tables table to a Table object.
 * @author Paul Wolfgang
 */
public class TableMapper implements ParameterizedRowMapper<Table> {

    private static final Logger logger = Logger.getLogger(TableMapper.class);

    public TableMapper() {
    }

    /**
     * Create a new Table object based on the contents of the current row.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
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
            item.setNoteColumn(rs.getString("Note"));
            return item;
        } catch (SQLException sqlex) {
            logger.error(sqlex);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }
    
    /**
     * Determine of two ParameterizedRowmapper objects are equal.  Since
     * the row mapper is stateless, equality of class is sufficient.
     * @param o
     * @return 
     */
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
