package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to build SQL queries
 * @author Paul Wolfgang
 */
public class QueryBuilder {
    
    private String dbName;
    private List<String> columnNames;
    
    /**
     * Build query
     * @return The SQL query string
     */
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if (columnNames == null || columnNames.isEmpty()) {
            sb.append("* ");
        } else {
            for (int i = 0; i < columnNames.size(); i++) {
                sb.append(columnNames.get(i));
                if (i == columnNames.size()-1) {
                    sb.append(" ");
                } else {
                    sb.append(", ");
                }
            }
        }
        sb.append("FROM ");
        sb.append(dbName);
        return sb.toString();
    }
    
    /**
     * Set the database name
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    
    /**
     * Add a column name
     */
    public void addColumn(String columnName) {
        if (columnNames == null) {
            columnNames = new ArrayList<>();
        }
        columnNames.add(columnName);
    }

}
