/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import edu.temple.cla.papolicy.filters.Filter;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Map a query of the Filters table to a Filter object.
 * @author Paul Wolfgang
 */
public class FilterMapper implements ParameterizedRowMapper<Filter> {

    private static final Logger logger = Logger.getLogger(FilterMapper.class);

    /**
     * Create a Filter object from the contents of the current row.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    @Override
    @SuppressWarnings("unchecked")
    public Filter mapRow(ResultSet rs, int rowNum) throws SQLException {
        String packageName = "edu.temple.cla.papolicy.filters";
        String className = rs.getString("FilterClass");
        try {
            Class<?> itemClass = Class.forName(packageName + "." + className);
            Constructor<Filter> constructor =
                    (Constructor<Filter>)itemClass.getDeclaredConstructor(
                    int.class, int.class, String.class, String.class, 
                    String.class, String.class);
            Integer id = new Integer(rs.getInt("ID"));
            Integer tableId = new Integer(rs.getInt("TableID"));
            String columnName = rs.getString("ColumnName");
            String description = rs.getString("Description");
            String tableReference = rs.getString("TableReference");
            String additionalParam = rs.getString("AdditionalParam");
            Filter item = constructor.newInstance(id, tableId, description,
                    columnName, tableReference, additionalParam);
            return item;
        } catch (SQLException sqlex) {
            logger.error(sqlex);
            throw sqlex;
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
