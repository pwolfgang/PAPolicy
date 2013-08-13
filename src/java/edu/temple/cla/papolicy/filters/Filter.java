/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.Expression;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * A Filter is responsible for displaying the html fragment that is included
 * in the form, and for generating the appropriate query fragment based
 * on the form input.
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

    /**
     * Construct a Filter
     * @param id The unique ID of this filter from the database
     * @param tableId The unique ID of the referencing table
     * @param description The description displayed on the form
     * @param columnName The column of the database which this filter is based on.
     * @param tableReference Table that provides additional information for the filter
     * (for example, drop down items).
     * @param additionalParam An additional paramter for this filter.
     */
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
     * Return the id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Return the tableId
     * @return the tableId
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * Return the description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the columnName
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Return the table reference
     * @return the tableReference
     */
    public String getTableReference() {
        return tableReference;
    }

    /**
     * Return the additional parameter
     * @return the additionalParam
     */
    public String getAdditionalParam() {
        return additionalParam;
    }

    /**
     * Generate the HTML to display the filter form input.
     * @return HTML to display the filter form input.
     */
    public abstract String getFilterFormInput();

    /**
     * Method to capture the form input from the HTTP request.
     * @param request 
     */
    public abstract void setFilterParameterValues(HttpServletRequest request);

    /**
     * Return the query to select the filtered items
     * @return The query to select the filtered items
     */
    public Expression getFilterQuery() {
        return filterQuery;
    }

    /**
     * Return the string that describes the filtered data
     * @return The string that describes the filtered data
     */
    public abstract String getFilterQualifier();

    /**
     * Return the jdbcTemplate
     * @return the jdbcTemplate
     */
    public SimpleJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * Set the jdbcTemplate. Set when the filter object is constructed.
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
    
    /**
     * Return the number of filter choices.
     * @return Default return value of 1
     */
    public int getNumberOfFilterChoices() {
        return 1;
    }
    
    /**
     * Determine if two filter objects are equal.
     * @param o The other object
     * @return True if this and o are equal.
     */
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
