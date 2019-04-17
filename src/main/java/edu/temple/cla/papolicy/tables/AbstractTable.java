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
import edu.temple.cla.papolicy.Utility;
import edu.temple.cla.papolicy.YearRange;
import edu.temple.cla.papolicy.dao.FilterMapper;
import edu.temple.cla.papolicy.dao.TableMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.Conjunction;
import edu.temple.cla.policydb.queryBuilder.Like;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * The AbstractTable provides default implementation of the Table interface.
 * The only method not implemented is getTitleBox.
 * @author Paul Wolfgang
 */
public abstract class AbstractTable implements Table {
    private static final Logger LOGGER = Logger.getLogger(AbstractTable.class);

    private int id;
    private String tableName;
    private String tableTitle;
    private boolean majorOnly;
    private String codeColumn;
    private String textColumn;
    private String yearColumn;
    protected String[] drillDownColumns;
    private String linkColumn;
    private int minYear;
    private int maxYear;
    private char qualifier;
    private String noteColumn;
    private List<Filter> filterList;
    protected JdbcTemplate jdbcTemplate;
    private Conjunction filterQuery;
    private QueryBuilder totalUnfilteredQuery;
    private QueryBuilder totalFilteredQuery;

    /**
     * Get the ID
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the ID
     * @param id the id to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return the table name
     * @return the tableName
     */
    @Override
    public String getTableName() {
        return tableName;
    }

    /**
     * Set the table name
     * @param tableName the tableName to set
     */
    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Get the table title
     * @return the tableTitle
     */
    @Override
    public String getTableTitle() {
        return tableTitle;
    }

    /**
     * Set the table title
     * @param tableTitle the tableTitle to set
     */
    @Override
    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    /**
     * Get majorOnly
     * @return majorOnly
     */
    @Override
    public boolean isMajorOnly() {
        return majorOnly;
    }

    /**
     * Set majorOnly
     * @param majorOnly the value majorOnly to set
     */
    @Override
    public void setMajorOnly(boolean majorOnly) {
        this.majorOnly = majorOnly;
    }

    /**
     * Get the minYear
     * @return the minYear
     */
    @Override
    public int getMinYear() {
        return minYear;
    }

    /**
     * Set the minYear
     * @param minYear the minYear to set
     */
    @Override
    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    /**
     * Get the maxYear
     * @return the maxYear
     */
    @Override
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * Set the maxYear
     * @param maxYear the maxYear to set
     */
    @Override
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    /**
     * Get the filterList
     * @return the filterList
     */
    @Override
    public List<Filter> getFilterList() {
        return filterList;
    }

    /**
     * Get the filterList size
     * @return filterList.size()
     */
    @Override
    public int getFilterListSize() {
        return filterList.size();
    }

    /**
     * Set the filterList
     * @param filterList the filterList to set
     */
    @Override
    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }


    /**
     * Set the jdbcTemplate
     * @param jdbcTemplate the jdbcTemplate to set
     */
    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get the qualifier
     * @return the qualifier
     */
    @Override
    public char getQualifier() {
        return qualifier;
    }

    /**
     * Set the qualifier
     * @param qualifier the qualifier to set
     */
    @Override
    public void setQualifier(char qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * Get the textColumn
     * @return the textColumn
     */
    @Override
    public String getTextColumn() {
        return textColumn;
    }

    /**
     * Set the textColumn
     * @param textColumn the textColumn to set
     */
    @Override
    public void setTextColumn(String textColumn) {
        this.textColumn = textColumn;
    }

    /**
     * Return a String representation of the table
     * @return The tableTitle followed by the filterQualifierString
     */
    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder(tableTitle);
        stb.append(getFilterQualifierString());
        return stb.toString();
    }

    /**
     * Get the title to be used in the download
     * @return same as toString
     */
    @Override
    public String getDownloadTitle() {
        return toString();
    }

    /**
     * The filterQualifierString is the list of all filters that were applied.
     * @return filterQualifierString
     */
    @Override
    public StringBuilder getFilterQualifierString() {
        StringBuilder stb = new StringBuilder();
        ArrayList<String> filterQualifiers = new ArrayList<>();
        filterList.stream()
                .filter((filter) -> (!"".equals(filter.getFilterQualifier())))
                .forEachOrdered((filter) -> {
                    filterQualifiers.add(filter.getFilterQualifier());
        });
        if (!filterQualifiers.isEmpty()) {
            stb.append(" ");
            stb.append(filterQualifiers.get(0));
            for (int i = 1; i < filterQualifiers.size(); i++) {
                stb.append(" and ");
                stb.append(filterQualifiers.get(i));
            }
        }
        return stb;
    }

    /**
     * Construct the query expression that applies the filters
     * @return conjunction of the selected filters
     */
    @Override
    public Conjunction getFilterQuery() {
        if (filterQuery == null) {
            filterQuery = new Conjunction();
            filterList.forEach((filter) -> {
                filterQuery.addTerm(filter.getFilterQuery());
            });
        }
        return filterQuery;
    }
    
    /**
     * Indicate if this table can be searched by topic.
     * @return true is the default
     */
    @Override
    public boolean isTopicSearchable() {return true;}

    /**
     * Capture additional parameters for this table. By default this method
     * does nothing
     * @param request The HTTP request object. 
     */
    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        // do nothing in base class
    }

    /**
     * Construct a query for this table that does not apply any filters.
     * @return unfilteredQuery 
     */
    @Override
    public QueryBuilder getUnfilteredTotalQuery() {
        if (totalUnfilteredQuery == null) {
            totalUnfilteredQuery = new QueryBuilder();
            totalUnfilteredQuery.addColumn(yearColumn + " AS TheYear");
            totalUnfilteredQuery.addColumn("count(ID) AS TheValue");
            totalUnfilteredQuery.setTable(tableName);
        }
        return totalUnfilteredQuery;
    }

    /**
     * Construct a query for this table that does applies the filters.
     * @return filteredQuery
     */
    @Override
    public QueryBuilder getFilteredTotalQuery() {
        if (totalFilteredQuery == null) {
            totalFilteredQuery = getUnfilteredTotalQuery().clone();
            totalFilteredQuery.addFilter(getFilterQuery());
        }
        return totalFilteredQuery;
    }
    
    /**
     * Construct a query for this table that selects by topic code.
     * @param topic The topic code to be selected
     * @return typicQuery
     */
    @Override
    public QueryBuilder getTopicQuery(Topic topic) {
        QueryBuilder topicQuery = getFilteredTotalQuery().clone();
        if (topic != null && topic.getCode() != 0) {
            if (isMajorOnly() || topic.getCode() >= 100) {
                topicQuery.setTopic(new Comparison(getCodeColumn(), "=", Integer.toString(topic.getCode())));
            } else {
                topicQuery.setTopic(new Like(getCodeColumn(), topic.getCode()));
            }
        }
        return topicQuery;
    }
    
    /**
     * Get the column that contains the year
     * @return year column
     */
    @Override
    public String getYearColumn() {return yearColumn;}

    /**
     * Get the units used to display the results.
     * @param showResults the showResults parameter from the analysis form
     * @return The units used to display the results.
     */
    @Override
    public Units getUnits(String showResults) {
        switch (showResults) {
            case "percent": return Units.PERCENT;
            case "percent_of_total": return Units.PERCENT_OF_TOTAL;
            case "percent_of_filtered": return Units.PERCENT_OF_FILTERED;
            default: return Units.COUNT;
        }
    }

    /**
     * Set the yearColumn
     * @param yearColumn the yearColumn to set
     */
    @Override
    public void setYearColumn(String yearColumn) {
        this.yearColumn = yearColumn;
    }

    /**
     * Function to get the subtable of this table based on the
     * qualifier character.
     * @param qualifier A character ('A', 'B', etc.) that follows the table ID
     * @return The subtable as indicated by the qualifier character.
     */
    @Override
    public Table getSubTable(char qualifier) {
        // The AbstractTable class has no subtables
        return this;
    }

    /**
     * Compute the sum of values within a range of years.
     * @param valueMap The map of values by year
     * @return The sum of values within the range
     */
    @Override
    public Number getValueForRange(SortedMap<Integer, Number> valueMap) {
        Number sum = null;
        for (Map.Entry<Integer, Number> entry : valueMap.entrySet()) {
            sum = add(sum, entry.getValue());
        }
        return sum;
    }

    /**
     * Compute the percent (selected vs total)  of values within a range of years.
     * @param valueMap The map of values by year
     * @param totalMap The map of totals by year
     * @return The percent within the range
     */
    @Override
    public Number getPercentForRange(SortedMap<Integer, Number> valueMap,
            SortedMap<Integer, Number> totalMap) {
        Number sum = getValueForRange(valueMap);
        Number total = getValueForRange(totalMap);
        if (total != null && total.doubleValue() != 0) {
            return sum != null ? sum.doubleValue() / total.doubleValue() * 100 : new Double(0);
        }
        return null;
    }

    /**
     * Utility method to add two Numbers. Null values are treated as zero.
     * @param n1 First number.
     * @param n2 Second number.
     * @return n1 + n2 or null if both are null.
     */
    static protected Number add(Number n1, Number n2) {
        if (n1 == null && n2 == null) return null;
        if (n1 == null) return n2;
        if (n2 == null) return n1;
        if (n1 instanceof Integer && n2 instanceof Integer) {
            return n1.intValue() + n2.intValue();
        }
        return n1.doubleValue() + n2.doubleValue();
    }

    /**
     * Get the axis title. Delegated to the units object.
     * @param units Display units
     * @return The axis title.
     */
    @Override
    public String getAxisTitle(Units units) {
        return Units.getTitle(units);
    }

    /**
     * Apply the query and get the values for each year.
     * @param query the query to be applied
     * @return A list of YearValue objects from the query
     */
    @Override
    public List<YearValue> getYearValueList(String query) {
        RowMapper<YearValue> mapper = new YearValueMapper();
        List<YearValue> list = jdbcTemplate.query(query, mapper);
        return list;
    }

    /**
     * Get the drillDown columns
     * @return the drill-down columns
     */
    @Override
    public String[] getDrillDownColumns() {
        return drillDownColumns;
    }

    /**
     * Set the drillDown columns
     * @param drillDownColumns the array of columns to display in the
     * drill-down page
     */
    @Override
    public void setDrillDownColumns(String[] drillDownColumns) {
        if (drillDownColumns != null) {
            this.drillDownColumns = drillDownColumns.clone();
        } else {
            this.drillDownColumns = null;
        }
    }

    /**
     * Get the linkColumn.
     * @return the linkColumn
     */
    @Override
    public String getLinkColumn() {
        return linkColumn;
    }

    /**
     * Set the linkColumn.
     * @param linkColumn the linkColumn to set
     */
    @Override
    public void setLinkColumn(String linkColumn) {
        this.linkColumn = linkColumn;
    }

    /**
     * Set the noteColumn.
     * @param noteColumn the noteColumn value to be set.
     */
    @Override
    public void setNoteColumn(String noteColumn) {
        this.noteColumn = noteColumn;
    }

    /**
     * Get the noteColumn
     * @return the noteColumn
     */
    @Override
    public String getNoteColumn() {return noteColumn != null ? noteColumn : "";}

    /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets the DrillDown columns
     * @param query The query that gets the count.
     * @return Modified query that selects the DrillDown columns and
     * link column if defined.
     */
    protected QueryBuilder createDrillDownQuery(QueryBuilder query) {
        QueryBuilder builder = query.clone();
        builder.clearColumns();
        for (String column:getDrillDownColumns()) {
            builder.addColumn(column);
        }
        if (linkColumn != null) {
            builder.addColumn(linkColumn + " as Link");
        }
        builder.clearGroupBy();
        return builder;
    }

    /**
     * Method to create the drilldown url.
     * This method returns the string &quot;drilldown.spg?query=<i>queyr</i>&quot;
     * Tables that need a different DrillDownController can override this method.
     * @param query The query string that gets the count
     * @return the url that will invoke the DrillDownController
     */
    @Override
    public String createDrillDownURL(QueryBuilder query) {
        QueryBuilder drillDownQuery = createDrillDownQuery(query);
        return "drilldown.spg?query=" + Utility.compressAndEncode(drillDownQuery.build());
    }

    /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets all columns for download
     * @param query The query that gets the count.
     * @return Modified query that selects all columns.
     */
    @Override
    public QueryBuilder createDownloadQuery(QueryBuilder query) {
        QueryBuilder builder = query.clone();
        builder.clearColumns();
        builder.clearGroupBy();
        return builder;
    }

    /**
     * Method to get the Code column name
     * @return the Code column name
     */
     @Override
     public String getCodeColumn() {return codeColumn;}

    /**
     * Method to set the Code column name
     * @param codeColumn the codeColumn to be set
     */
     @Override
     public void setCodeColumn(String codeColumn) {
         this.codeColumn = codeColumn;
     }

    public static List<Table> getTable(String tableId, char qualifier,
            HttpServletRequest request, JdbcTemplate jdbcTemplate)
            throws DataAccessException, Error {
        RowMapper<Table> tableMapper = new TableMapper();
        RowMapper<Filter> filterMapper = new FilterMapper();
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
        //Scan filter list to see how many variations there may be.
        int numVariations = 1;
        for (Filter f : filterList) {
            numVariations *= f.getNumberOfFilterChoices();
        }
        List<Table> tableList = new ArrayList<>();
        tableList.add(table);
        if (numVariations > 1) {
            while (expandChoices(tableList)) { 
               // work done in the method expandChoices method
            }
        }
        return tableList;
    }
    
    private static boolean expandChoices(List<Table> tableList) {
        for (int i = 0; i < tableList.size(); i++) {
            Table table = tableList.get(i);
            List<Filter> filterList = table.getFilterList();
            for (int j = 0; j < filterList.size(); j++) {
                Filter filter = filterList.get(j);
                if (filter.getNumberOfFilterChoices() != 1) {
                    Table[] newTables = new Table[filter.getNumberOfFilterChoices()];
                    Filter[] filters = filter.getFilterChoices();
                    for (int k = 0; k < newTables.length; k++) {
                        newTables[k] = table.clone();
                        newTables[k].getFilterList().set(j, filters[k]);
                    }
                    tableList.remove(i);
                    for (Table newTable : newTables) {
                        tableList.add(i, newTable);
                    }
                    return true;
                }
            }
        }
        return false;               
    }

    /**
     * Make a deep copy of this table object
     * This method catches CloneNotSupportedException
     * @return a copy of this table object
     */
    @Override
    public AbstractTable clone() {
        try {
            AbstractTable theClone = (AbstractTable)super.clone();
            if (drillDownColumns != null) {
                theClone.drillDownColumns = drillDownColumns.clone();
            }
            if (filterList != null) {
                theClone.filterList = new ArrayList<>(filterList);
            }
            return theClone;
        } catch (CloneNotSupportedException ex) {
            throw new Error("CloneNotSupportedException should never be thrown");
        }
    }

    /**
     * Construct the URL to perform the download
     * @param downloadTitle The title
     * @param downloadQueryString The query
     * @param yearRange The range of years
     * @return URL to perform the download
     */
    @Override
    public String getDownloadURL(String downloadTitle, String downloadQueryString, YearRange yearRange ) {
        StringBuilder stb = new StringBuilder("<a href=\"");
        StringBuilder stb2 = new StringBuilder(downloadTitle);
        stb2.append(" ");
        stb2.append(yearRange.getMinYear());
        stb2.append("_");
        stb2.append(yearRange.getMaxYear());
        try {
            stb.append(URLEncoder.encode(stb2.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex);
        }
        stb.append(".xlsx?table=");
        stb.append(getTableName());
        stb.append("&query=");
        stb.append(downloadQueryString);
        stb.append("\">");
        stb.append(downloadTitle);
        stb.append("</a><br/>");
        return stb.toString();
    }

    /**
     * Construct the displayed value string.
     * @param key Not used 
     * @param retValue The value returned from the query
     * @param units Unit to display
     * @return Formatted value based on units.
     */
    @Override
    public String getDisplayedValue(String key, Number retValue, Units units) {
        String result;
        switch (units) {
            case COUNT : 
                result = (retValue != null ? retValue.toString() : "null");
                break;
            case DOLLARS :
                result = (retValue != null ?
                    String.format("$%,.0f", retValue.doubleValue()) : "null");
                break;
            case PERCENT:
            case PERCENT_CHANGE:
            case PERCENT_OF_TOTAL:
            case PERCENT_OF_FILTERED:
                result =  (retValue != null ?
                    String.format("%.1f%%", retValue.doubleValue()) : "null");
                break;
            case RANK:
                result = (retValue != null ?
                    String.format("%.1f", retValue.doubleValue()) : "null");
                break;
            default: result = (retValue != null ? retValue.toString() : "null");
        }
        return result;
    }

}
