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
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.YearRange;
import edu.temple.cla.papolicy.dao.FilterMapper;
import edu.temple.cla.papolicy.dao.TableMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.policydb.queryBuilder.Conjunction;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * The Table interface defines the methods for each of the datasets.
 * For each of the datasets methods are required to generate the html used
 * to display the datasets selection, collect the selection information
 * from the form submittal, and generate the database query.
 * Table objects are created and initialized from the Tables table in the
 * database. All table objects are loaded to create the analysis form. Selected
 * table objects are loaded when the request is processed.
 * @author Paul Wolfgang
 */
public interface Table extends Cloneable {

    /**
     * The unique identifier of this table as defined in the Tables table
     * @return the id
     */
    int getId();

    /**
     * The table identifier is set when the class is loaded, either for
     * initial display of the analysis form, or when the request is processed.
     * @param id the id to set
     */
    void setId(int id);

    /**
     * Return the table name as defined in the Tables table
     * @return the tableName
     */
    String getTableName();

    /**
     * The table name is set when the class is loaded.
     * @param tableName the tableName to set
     */
    void setTableName(String tableName);

    /**
     * The table title as defined in the Tables table
     * @return the tableTitle
     */
    String getTableTitle();

    /**
     * The table title is set when the class is loaded.
     * @param tableTitle the tableTitle to set
     */
    void setTableTitle(String tableTitle);

    /**
     * This data set is only searchable by major topic.
     * @return the majorOnly
     */
    boolean isMajorOnly();

    /**
     * Set when the class is loaded.
     * @param majorOnly the majorOnly to set
     */
    void setMajorOnly(boolean majorOnly);

    /**
     * The minimum year for which data is available
     * @return the minYear
     */
    int getMinYear();

    /**
     * Set when the class is loaded
     * @param minYear the minYear to set
     */
    void setMinYear(int minYear);

    /**
     * The maximum year for which data is available
     * @return the maxYear
     */
    int getMaxYear();

    /**
     * Set when the class is loaded
     * @param maxYear the maxYear to set
     */
    void setMaxYear(int maxYear);

    /**
     * The list of filters is defined in the Filters table
     * @return the filterList
     */
    List<Filter> getFilterList();

    /**
     * Return the number of filters
     * @return filterList.size()
     */
    int getFilterListSize();

    /**
     * The filter list is set when the class is loaded.
     * @param filterList the filterList to set
     */
    void setFilterList(List<Filter> filterList);

    /**
     * Method to generate the HTML code for the title box.
     * @return HTML code for the title box
     */
    String getTitleBox();

    /**
     * The jdbcTemplate is set when the class is loaded.
     * @param jdbcTemplate the jdbcTemplate to set
     */
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    /**
     * The qualifier is used to distinguish subtables.
     * @return the qualifier
     */
    char getQualifier();

    /**
     * Set the qualifier. This is set from the HTML form when the
     * table object is created.
     * @param qualifier the qualifier to set
     */
    void setQualifier(char qualifier);

    /**
     * Return the text column.
     * @return the textColumn
     */
    String getTextColumn();

    /**
     * The textColumn is set when the table object is loaded or created
     * by the constructor.
     * @param textColumn the textColumn to set
     */
    void setTextColumn(String textColumn);

    /**
     * The filter qualifier string is appended to the table title to
     * identify which filters have been applied.
     * @return the filterQualifierString
     */
    StringBuilder getFilterQualifierString();

    /**
     * The filterQuery is the condition expression that selects the filtered items.
     * @return filterQuery
     */
    Conjunction getFilterQuery();
    
    /**
     * Indicate if this table represents topic searchable data.
     * @return true if this table is topic searchable.
     */
    boolean isTopicSearchable();

    /**
     * Method to capture additional parameters from the HTTP request.
     * @param request HTTP request.
     */
    void setAdditionalParameters(HttpServletRequest request);

    /**
     * The filtered total query selects all filtered items from the table in a given date range.
     * @return filteredTotalQuery
     */
    QueryBuilder getFilteredTotalQuery();
    
    /**
     * The unfiltered total query selects all items from the table in a given date range.
     * @return The unfiltered total query string. 
     */
    QueryBuilder getUnfilteredTotalQuery();

    /**
     * The topic query selects from the filtered items those which match the topic
     * @param topic The topic code to be searched
     * @return topicQuery
     */
    QueryBuilder getTopicQuery(Topic topic);
    
    /**
     * Get the yearColumn
     * @return yearColumn
     */
    String getYearColumn();

    /**
     * Get the units.
     * @param showResults The showResults parameter from the form
     * @return units based on the showResults parameter
     */
    Units getUnits(String showResults);

    /**
     * The yearColumn is set when the table is loaded.
     * @param yearColumn the yearColumn to set
     */
    void setYearColumn(String yearColumn);

    /**
     * Function to get the subtable of this table based on the qualifier
     * character.
     *
     * @param qualifier A character ('A', 'B', etc.) that follows the table ID
     * @return The subtable as indicated by the qualifier character.
     */
    Table getSubTable(char qualifier);

    /**
     * Compute the value for a range of years. Generally the sum.
     * @param valueMap Map of years to counts (for most tables)
     * @return the value for the range
     */
    Number getValueForRange(SortedMap<Integer, Number> valueMap);

    /**
     * Compute the percent value for a range of years.
     * @param valueMap Map of years to counts
     * @param totalMap Map of years to total
     * @return percent for the range.
     */
    Number getPercentForRange(SortedMap<Integer, Number> valueMap,
            SortedMap<Integer, Number> totalMap);

    /**
     * Generate the axis title
     * @param units of the axis
     * @return axis title
     */
    String getAxisTitle(Units units);

    /**
     * Perform a query of the database and return the result as a list
     * @param query query to be executed
     * @return Result of query as a list of Year-Values.
     */
    List<YearValue> getYearValueList(String query);

    /**
     * Return the drill-down columns
     * @return the drill-down columns
     */
    String[] getDrillDownColumns();

    /**
     * Set the drill-down columns. Set when the table is loaded.
     * @param drillDownColumns the array of columns to display in the drill-down
     * page
     */
    void setDrillDownColumns(String[] drillDownColumns);

    /**
     * Get the linkColumn.
     * @return the linkColumn
     */
    String getLinkColumn();

    /**
     * Set the linkColumn. Set when the table is loaded.
     * @param linkColumn the linkColumn to set
     */
    void setLinkColumn(String linkColumn);

    /**
     * Method to create the drilldown url.
     *
     * @param query The query string that gets the count
     * @return the url that will invoke the DrillDownController
     */
    String createDrillDownURL(QueryBuilder query);

    /**
     * Method to convert the SQL query that gets the count to a SQL query that
     * gets all columns for download
     *
     * @param query The query that gets the count.
     * @return Modified query that selects all columns.
     */
    QueryBuilder createDownloadQuery(QueryBuilder query);

    /**
     * Method to get the Code column name
     *
     * @return the Code column name
     */
    String getCodeColumn();

    /**
     * Method to set the Code column name. Called when table is loaded.
     * @param codeColumn value to be set
     */
    void setCodeColumn(String codeColumn);

    /**
     * Method to set the note column. Called when table is loaded.
     * @param noteColumn The value to be set.
     */
    void setNoteColumn(String noteColumn);

    /**
     * Get the note column
     * @return noteColumn
     */
    String getNoteColumn();

    /**
     * Make a deep copy of this Table object
     * @return copy of this Table object
     */
    public Table clone();

    /**
     * Return the title for the download file
     * @return title for the download file
     */
    String getDownloadTitle();

    /**
     * Generate and return the URL to perform the download
     * @param downloadTitle title to appear in the URL
     * @param downloadQueryString query to perform the download less year range
     * @param yearRange Range of years to download.
     * @return The URL to perform the download.
     */
    String getDownloadURL(String downloadTitle, String downloadQueryString,
            YearRange yearRange);

    /**
     * Format the value for display in the analysis table
     * @param key Key (year) 
     * @param value Value to be displayed
     * @param units Units
     * @return A string to be included in the analysis table.
     */
    String getDisplayedValue(String key, Number value, Units units);
    
    /**
      * Load the selected table object and associated filters.
      * @param tableId The table id
      * @param qualifier The qualifier to select subtable
      * @param request HTTP request from the form
      * @param jdbcTemplate jdbcTemplate to access the database
      * @return The selected filter object encapsulated in an array.
      * @throws DataAccessException If there is a problem querying the database
      * @throws Error If the table does not exist in the database
      */
      static Table getTable(String tableId, char qualifier,
            HttpServletRequest request, JdbcTemplate jdbcTemplate)
            throws DataAccessException, Error {
        ParameterizedRowMapper<Table> tableMapper = new TableMapper();
        ParameterizedRowMapper<Filter> filterMapper = new FilterMapper();
        List<Table> aTableList = jdbcTemplate.query("SELECT * FROM Tables WHERE ID=" + tableId, tableMapper);
        if (aTableList.size() != 1) {
            throw new Error("TableID " + tableId + " not in database");
        }
        Table table = aTableList.get(0);
        table = table.getSubTable(qualifier);
        table.setAdditionalParameters(request);
        String query = "SELECT * from Filters WHERE TableID=" + table.getId() + " ORDER BY ID";
        List<Filter> filterList = jdbcTemplate.query(query, filterMapper);
        filterList.forEach(filter -> {
            filter.setJdbcTemplate(jdbcTemplate);
            filter.setFilterParameterValues(request);
        });
        table.setFilterList(filterList);
        table.setJdbcTemplate(jdbcTemplate);
        return table;
      }
        
//        This code was added to allow for comparison of filter choices, but is no longer
//        used.
//        //Scan filter list to see how many variations there may be.
//        int numVariations = 1;
//        for (Filter f : filterList) {
//            numVariations *= f.getNumberOfFilterChoices();
//        }
//        if (numVariations == 1) {
//            return Arrays.asList(table);
//        }
//        List<Table> tableList = new ArrayList<>();
//        tableList.add(table);
//        while (expandChoices(tableList)) { 
//            // work done in the method expandChoices method
//        }
//        return tableList;
//    }
//    
//     /**
//      * Method to expand the list of tables to account for multiple filter
//      * value choices. (This method was added to allow for comparison of
//      * filter choices, but is no longer used.)
//      * @param tableList The list of selected tables
//      * @return true if there are no more expansions to be performed.
//      */
//     static boolean expandChoices(List<Table> tableList) {
//        for (int i = 0; i < tableList.size(); i++) {
//            Table table = tableList.get(i);
//            List<Filter> filterList = table.getFilterList();
//            for (int j = 0; j < filterList.size(); j++) {
//                Filter filter = filterList.get(j);
//                if (filter.getNumberOfFilterChoices() != 1) {
//                    Table[] newTables = new Table[filter.getNumberOfFilterChoices()];
//                    Filter[] filters = filter.getFilterChoices();
//                    for (int k = 0; k < newTables.length; k++) {
//                        newTables[k] = table.clone();
//                        newTables[k].getFilterList().set(j, filters[k]);
//                    }
//                    tableList.remove(i);
//                    for (Table newTable : newTables) {
//                        tableList.add(i, newTable);
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;               
//    }

}
