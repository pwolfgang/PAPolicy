/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

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

    public abstract String getFilterQueryString();

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

}
