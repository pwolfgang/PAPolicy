/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import edu.temple.cla.policydb.queryBuilder.Expression;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A Filter is responsible for displaying the html fragment that is included
 * in the form, and for generating the appropriate query fragment based
 * on the form input.
 * @author Paul Wolfgang
 */
public abstract class Filter {

    private final int id;
    private final int tableId;
    private final String description;
    private final String columnName;
    private final String tableReference;
    private final String additionalParam;
    private JdbcTemplate jdbcTemplate;
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
        this.filterQuery = new EmptyExpression();
    }

    /**
     * Return the id
     * @return the id
     */
    public final int getId() {
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
    public final String getColumnName() {
        return columnName;
    }

    /**
     * Return the table reference
     * @return the tableReference
     */
    public final String getTableReference() {
        return tableReference;
    }

    /**
     * Return the additional parameter
     * @return the additionalParam
     */
    public final String getAdditionalParam() {
        return additionalParam;
    }

    /**
     * Generate the HTML to display the filter form input.
     * @return HTML to display the filter form input.
     */
    public abstract String getFilterFormInput();

    /**
     * Method to capture the form input from the HTTP request.
     * @param request The HTTP request.
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
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * Set the jdbcTemplate. Set when the filter object is constructed.
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
//    /**
//     * Return an array of Filter instances when multiple filter choices
//     * have been selected. This feature allows for a filter to be used to
//     * select all variations which will be displayed in separate columns
//     * in the result table. 
//     * By default this method returns this enclosed in an array
//     * @return An array of Filter instances.
//     */
//    public Filter[] getFilterChoices() {
//        return new Filter[] {this};
//    }
//    
//    /**
//     * Return the number of filter choices.
//     * @return Default return value of 1
//     */
//    public int getNumberOfFilterChoices() {
//        return 1;
//    }
    
    /**
     * Determine if two filter objects are equal.
     * @param o The other object
     * @return True if this and o are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() == o.getClass()) {
            Filter other = (Filter) o;
            return (getId() == other.getId() &&
                    getTableId() == other.getTableId() &&
                    getDescription().equals(other.getDescription()) &&
                    Objects.equals(getTableReference(), other.getTableReference())) &&
                    Objects.equals(getAdditionalParam(), other.getAdditionalParam());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.id;
        hash = 67 * hash + this.tableId;
        hash = 67 * hash + Objects.hashCode(this.description);
        hash = 67 * hash + Objects.hashCode(this.tableReference);
        hash = 67 * hash + Objects.hashCode(this.additionalParam);
        return hash;
    }
    
}
