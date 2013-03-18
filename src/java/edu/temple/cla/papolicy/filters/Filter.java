/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.Expression;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public abstract class Filter {

    private int id;
    private int tableId;
    private String description;
    private String columnName;
    private String tableReference;
    private String additionalParam;
    private SimpleJdbcTemplate jdbcTemplate;
    protected Expression filterQuery;

    public Filter(int id, int tableId, String description, String columnName,
            String tableReference, String additionalParam) {
        this.id = id;
        this.tableId = tableId;
        this.description = description;
        this.columnName = columnName;
        this.tableReference = tableReference;
        this.additionalParam = additionalParam;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the tableId
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @return the tableReference
     */
    public String getTableReference() {
        return tableReference;
    }

    /**
     * @return the additionalParam
     */
    public String getAdditionalParam() {
        return additionalParam;
    }

    public abstract String getFilterFormInput();

    public abstract void setFilterParameterValues(HttpServletRequest request);

    public String getFilterQueryString() {
        return filterQuery != null ? filterQuery.toString() : "";
    }
    
    public Expression getFilterQuery() {
        return filterQuery;
    }

    public abstract String getFilterQualifier();

    /**
     * @return the jdbcTemplate
     */
    public SimpleJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Return an array of Filter instances when multiple filter choices
     * have been selected.
     * By default this method returns this enclosed in an array
     */
    public Filter[] getFilterChoices() {
        return new Filter[] {this};
    }
    
    public int getNumberOfFilterChoices() {
        return 1;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() == o.getClass()) {
            Filter other = (Filter) o;
            return (getId() == other.getId() &&
                    getTableId() == other.getTableId() &&
                    getDescription().equals(other.getDescription()) &&
                    getTableReference() == null ? other.getTableReference() == null : getTableReference().equals(other.getTableReference()) &&
                    getAdditionalParam() == null ? other.getAdditionalParam() == null : getAdditionalParam().equals(other.getAdditionalParam()));
        } else {
            return false;
        }
    }

}
