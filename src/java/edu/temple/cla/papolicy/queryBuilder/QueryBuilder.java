package edu.temple.cla.papolicy.queryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to build SQL queries
 * @author Paul Wolfgang
 */
public class QueryBuilder implements Cloneable {
    
    private String table;
    private List<String> columnNames;
    private Conjunction selectCriteria;
    private Conjunction filters;
    private Expression topic;
    private Expression freeText;
    private Between between;
    private String groupBy;
    private String orderBy;
    
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
        sb.append(table);
        Conjunction selectAndFilters = new Conjunction();
        selectAndFilters.addTerm(selectCriteria);
        selectAndFilters.addTerm(filters);
        selectAndFilters.addTerm(topic);
        selectAndFilters.addTerm(freeText);
        selectAndFilters.addTerm(between);
        if (!selectAndFilters.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectAndFilters.toStringNoParen());
        }
        if (groupBy != null) {
            sb.append(" GROUP BY ");
            sb.append(groupBy);
        }
        if (orderBy != null) {
            sb.append(" ORDER BY ");
            sb.append(orderBy);
        }
        return sb.toString();
    }
    
    /**
     * Set the database name
     */
    public void setTable(String table) {
        this.table = table;
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
    
    /**
     * Clear the list of columns
     */
    public void clearColumns() {
        columnNames = null;
    }
    
    /**
     * Add a condition to the select criteria
     */
    public void addToSelectCriteria(Expression e) {
        if (selectCriteria == null) {
            selectCriteria = new Conjunction();
        }
        selectCriteria.addTerm(e);
    }
    
    /**
     * Add a filter
     */
    public void addFilter(Expression e) {
        if (filters == null) {
            filters = new Conjunction();
        }
        filters.addTerm(e);
    }
    
    /**
     * Clear filters
     */
    public void clearFilters() {
        filters = null;
    }
    
    /**
     * Set the topic
     * @param e The topic selection expression
     */
    public void setTopic(Expression e) {
        topic = e;
    };
    
    /**
     * Set Free Text
     */
    public void setFreeText(Expression e) {
        freeText = e;
    }
    
    /**
     * Add a between criteria
     */
    public void setBetween(Between between) {
        this.between = between;
    }
    
    /**
     * Clear between
     */
    public void clearBetween() {
        between = null;
    }
    
    /**
     * Set the group by column
     */
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
    
    public void clearGroupBy() {
        this.groupBy = null;
    }
    
    /**
     * Set the order by column
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public void clearOrderBy() {
        this.orderBy = null;
    }
    
    @Override
    public QueryBuilder clone() {
        try {
            QueryBuilder theClone = (QueryBuilder)super.clone();
            return theClone;
        } catch (CloneNotSupportedException ex) {
            throw new Error("This is an impossible situation", ex);
        }
    }
}
