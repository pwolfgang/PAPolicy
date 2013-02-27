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
 *
 * @author Paul Wolfgang
 */
public class FilterMapper implements ParameterizedRowMapper<Filter> {

    private static final Logger logger = Logger.getLogger(FilterMapper.class);

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
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return this.getClass() == o.getClass();
    }

}
