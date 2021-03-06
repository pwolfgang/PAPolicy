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
package edu.temple.cla.papolicy;

import edu.temple.cla.policydb.queryBuilder.FreeTextParser;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.policydb.queryBuilder.Between;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import edu.temple.cla.papolicy.tables.Table;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 * The Column class encapsulates the data to be displayed in each column
 * of the results page. A column is created for each dataset-topic pair.
 * @author Paul Wolfgang
 */
public class Column {

    private static Logger LOGGER = Logger.getLogger(Column.class);
    private Table table;
    private Topic topic;
    private String freeText;
    private QueryBuilder topicCountQuery = null;
    private QueryBuilder unfilteredTotalQuery = null;
    private QueryBuilder filteredTotalQuery = null;
    private Units units;
    private SortedMap<Integer, Number> valueMap;
    private SortedMap<Integer, Number> unfilteredTotalMap;
    private SortedMap<Integer, Number> filteredTotalMap;
    private final SortedMap<String, Number> displayedValueMap = new TreeMap<>();
    private final SortedMap<String, String> drillDownMap = new TreeMap<>();
    private Number prevValue = null;
    private String downloadQueryString = null;
    private Number minValue = null;
    private Number maxValue = null;
    private YearRange yearRange = null;

    protected Column() {
    } // used for unit test purposes only

    /**
     * Construct a Column object
     * @param table The table (dataset) that contains the data
     * @param topic The selected topic
     * @param freeText The free text (if any) that refined or is in addition to the topic
     * @param showResults The indicator as to how the results are to be displayed
     * @param yearRange The range of years (or legislative sessions)
     */
    public Column(Table table, Topic topic, String freeText, String showResults, YearRange yearRange) {
        this.table = table;
        this.topic = topic;
        this.freeText = freeText;
        units = table.getUnits(showResults);
        this.yearRange = yearRange;
    }

    /**
     * Generate a string representation of this column.  This string is used
     * in the display. It combines the description of the table with either
     * the topic, free text, or both.
     * @return String to display in the column heading and graph label.
     */
    @Override
    public String toString() {
        if (freeText == null && topic != null) {
            return topic.getDescription() + " " + table.toString();
        } else if (freeText != null && topic != null) {
            return table.toString() + " about " + freeText + " in " + topic.getDescription();
        } else if (freeText != null && topic == null) {
            return table.toString() + " about " + freeText;
        } else {
            return table.toString();
        }
    }

    /**
     * Generate a string that is used in the download link.  It is similar to
     * the result of toString except that the text used for the table is
     * specific for download.
     * @return String to use as the download link.
     * 
     */
    public String getDownloadTitle() {
        if (freeText == null && topic != null) {
            return topic.getDescription() + " " + table.getDownloadTitle();
        } else if (freeText != null && topic != null) {
            return table.getDownloadTitle() + " about " + freeText + " in " + topic.getDescription();
        } else if (freeText != null && topic == null) {
            return table.getDownloadTitle() + " about " + freeText;
        } else {
            return table.getDownloadTitle();
        }
    }

    /**
     * Construct and return the URL to download the data.  This is delegated
     * to the table with the yearRange.  The returned string contains the
     * an compressed encoded query string that will select the data to be
     * downloaded.
     * @return URL to download the data.
     */
    public String getDownloadURL() {
        return table.getDownloadURL(getDownloadTitle(), getDownloadQueryString(), yearRange);
    }

    /**
     * The filteredTotalQuery is the query that selects the filtered data
     * for all topics. Creation is delegated to the table and the result is
     * cached.
     * @return QueryBuilder object that will select the filtered data for all topics.
     */
    public QueryBuilder getFilteredTotalQuery() {
        if (filteredTotalQuery == null) {
            filteredTotalQuery = table.getFilteredTotalQuery().clone();
        }
        return filteredTotalQuery;
    }

    /**
     * Construct the query string that selects the filtered data for all topics.
     * @return The query string that selects the filtered  data for all topics.
     */
    public String getFilteredTotalQueryString() {
        return getFilteredTotalQuery().build();
    }

    /**
     * The unfilteredTotalQuery is the query that selects the data for all topics
     * with no filters applied.
     * @return The QueryBuilder object that will select the un-filtered data for all topics.
     */
    public QueryBuilder getUnfilteredTotalQuery() {
        if (unfilteredTotalQuery == null) {
            unfilteredTotalQuery = table.getUnfilteredTotalQuery().clone();
        }
        return unfilteredTotalQuery;
    }

    /**
     * Construct the query string that selects all data for all topics.
     * @return A query string that selects all data for all topics
     */
    public String getUnfilteredTotalQueryString() {
        return getUnfilteredTotalQuery().build() + " WHERE ";
    }

    /**
     * Create an unfilteredTotalQuery that selects data for a given year range
     * @param startYear The start year
     * @param endYear The end year
     * @return QueryBuilder object that will select data for a given year.
     */
    public QueryBuilder getUnfilteredTotalQuery(int startYear, int endYear) {
        QueryBuilder builder = getUnfilteredTotalQuery().clone();
        builder.setBetween(new Between(table.getYearColumn(), startYear, endYear));
        builder.setGroupBy(table.getYearColumn());
        builder.setOrderBy(table.getYearColumn());
        return builder;
    }

    /**
     * Create a query string that will select the data for a given year range
     * @param startYear The start year
     * @param endYear The end year
     * @return A query string that selects all data for a given year range
     */
    public String getUnfilteredTotalQueryString(int startYear, int endYear) {
        return getUnfilteredTotalQuery(startYear, endYear).build();
    }

    /**
     * Create a filteredtotalQuery for a given year range
     * @param startYear The start year
     * @param endYear The end year
     * @return QueryBuilder object that will select the filtered data for a given year range.
     */
    public QueryBuilder getFilteredTotalQuery(int startYear, int endYear) {
        QueryBuilder builder = getFilteredTotalQuery().clone();
        builder.setBetween(new Between(table.getYearColumn(), startYear, endYear));
        builder.setGroupBy(table.getYearColumn());
        builder.setOrderBy(table.getYearColumn());
        return builder;
    }

    /**
     * Generate the query string to select the filtered data for a given year range
     * @param startYear The start year
     * @param endYear The end year
     * @return A query string to select the filtered data for a given year range
     */
    public String getFilteredTotalQueryString(int startYear, int endYear) {
        return getFilteredTotalQuery(startYear, endYear).build();
    }

    /**
     * The topic count query selects the count of the items with a particular
     * policy code or major policy code. It also selects items that match
     * the free text.  Computation is delegated to the table, and the results
     * are cached.
     * @return QueryBuilder object that selects the items for a topic and/or free text.
     */
    public QueryBuilder getTopicCountQuery() {
        if (topicCountQuery == null) {
            topicCountQuery = table.getTopicQuery(topic).clone();
            if (freeText != null) {
                topicCountQuery.setFreeText(FreeTextParser.parse(table.getTextColumn(), freeText));
            }
        }
        return topicCountQuery;
    }

    /**
     * Generate the query string that selects the items for a topic and/or free text.
     * @return Query string that selects the items for a topic and/or free text.
     */
    public String getTopicCountQueryString() {
        return getTopicCountQuery().build();
    }

    /**
     * Create a topic count query for a particular range of years
     * @param startYear The start year
     * @param endYear The end year
     * @return QueryBuilder to select topic items for a given year range
     */
    public QueryBuilder getTopicCountQuery(int startYear, int endYear) {
        QueryBuilder builder = getTopicCountQuery().clone();
        builder.setBetween(new Between(table.getYearColumn(), startYear, endYear));
        builder.setGroupBy(table.getYearColumn());
        builder.setOrderBy(table.getYearColumn());
        return builder;
    }

    /**
     * Generate the query string to select items for a particular topic for a given year range.
     * @param startYear The start year
     * @param endYear The end year
     * @return The query string to select items for a particular topic for a given year range.
     */
    public String getTopicCountQueryString(int startYear, int endYear) {
        return getTopicCountQuery(startYear, endYear).build();
    }

    /**
     * The units that this column is to be displayed in.
     * @return the units
     */
    public Units getUnits() {
        return units;
    }

    /**
     * Compute the value (sum of count, or average) for a given year range.
     * Calculation is delegated to the table
     * @param minYear The start year
     * @param maxYear The end year
     * @return The value for a given year range.
     */
    public Number getValue(int minYear, int maxYear) {
        Number returnVal = table.getValueForRange(valueMap.subMap(minYear, maxYear + 1));
        return returnVal;
    }

    /**
     * Compute the percentage of the total for a given year range. Computation
     * is delegated to the table.
     * @param minYear Start year
     * @param maxYear End year
     * @return The percentage of the total for a given year range.
     */
    public Number getPercentOfTotal(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear + 1),
                unfilteredTotalMap.subMap(minYear, maxYear + 1));
        return returnVal;
    }

    /**
     * Compute the percent of filtered for a given year range. Computation is
     * delegated to the table.
     * @param minYear The start year
     * @param maxYear The end year
     * @return The percent of filtered for a given year range.
     */
    public Number getPercent(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear + 1),
                filteredTotalMap.subMap(minYear, maxYear + 1));
        return returnVal;
    }

    /**
     * Sets the initial value to be used when calculating percent changed. This
     * may be the sum of values for a given year range or the year range may
     * only be one year. Calculation is delegated to the tabel.
     * @param minYear Start year
     * @param maxYear End year
     */
    public void setInitialPrevValue(int minYear, int maxYear) {
        prevValue = table.getValueForRange(valueMap.subMap(minYear, maxYear));
    }

    /**
     * Compute the percentage change from the previous value to the current
     * value. The value may be the sum of a year range. 
     * @param minYear Start year
     * @param maxYear End year
     * @return The percentage change from the previous value to the current value.
     */
    public Number getPercentChange(int minYear, int maxYear) {
        Number currentValue = table.getValueForRange(valueMap.subMap(minYear, maxYear + 1));
        if (currentValue == null) {
            prevValue = null;
            return null;
        }
        if (prevValue == null) {
            prevValue = currentValue;
            return null;
        }
        Double pv = prevValue.doubleValue();
        Double cv = currentValue.doubleValue();
        Number result = (cv - pv) / pv * 100.0;
        prevValue = currentValue;
        return result;
    }

    /**
     * Load the data from the filtered topic selection query into a SortedMap indexed by year.
     * @param minYear The start year
     * @param maxYear The end year
     */
    public void setValueMap(int minYear, int maxYear) {
        String query = getTopicCountQueryString(minYear, maxYear);
        List<YearValue> yearValueList = table.getYearValueList(query);
        valueMap = new TreeMap<>();
        yearValueList.forEach((yv) -> {
            Number value = yv.getValue();
            if (value != null) {
                if (minValue == null) {
                    minValue = value;
                } else {
                    if (minValue.doubleValue() > value.doubleValue()) {
                        minValue = value;
                    }
                }
                if (maxValue == null) {
                    maxValue = value;
                } else {
                    if (maxValue.doubleValue() < value.doubleValue()) {
                        maxValue = value;
                    }
                }
            }
            valueMap.put(yv.getYear(), yv.getValue());
        });
    }

    /**
     * Load the data from the un-filtered total selection query into a SortedMap indexed by year.
     * @param minYear The start year
     * @param maxYear The end year
     */
    public void setUnfilteredTotalMap(int minYear, int maxYear) {
        String query = getUnfilteredTotalQueryString(minYear, maxYear);
        List<YearValue> yearValueList = table.getYearValueList(query);
        unfilteredTotalMap = new TreeMap<>();
        yearValueList.forEach((yv) -> {
            unfilteredTotalMap.put(yv.getYear(), yv.getValue());
        });
    }

    /**
     * Load the data from the filtered total selection query into a SortedMap indexed by year.
     * @param minYear The start year
     * @param maxYear The end year
     */
    public void setFilteredTotalMap(int minYear, int maxYear) {
        String query = getFilteredTotalQueryString(minYear, maxYear);
        List<YearValue> yearValueList = table.getYearValueList(query);
        filteredTotalMap = new TreeMap<>();
        yearValueList.forEach((yv) -> {
            filteredTotalMap.put(yv.getYear(), yv.getValue());
        });
    }

    /**
     * Set the value to be displayed or a given row in the column.
     * @param key The row key (year or session)
     * @param value The value to be displayed
     */
    public void setDisplayedValue(String key, Number value) {
        displayedValueMap.put(key, value);
    }

    /**
     * Set the drilldown URL for a given row.
     * @param key The row key (year or session)
     * @param value The drilldown URL
     */
    public void setDrillDown(String key, String value) {
        drillDownMap.put(key, value);
    }

    /**
     * Get the displayed value for a given row
     * @param key The key for the row (year or session)
     * @return The displayed value for a given row
     */
    public Number getDisplayedValue(String key) {
        return displayedValueMap.get(key);

    }

    /**
     * Convert the displayed value into a string. Delegated to the table
     * that applies the correct units format.
     * @param key The key for the row (year or session)
     * @return The string to place in the results table.
     */
    public String getDisplayedValueString(String key) {
        Number retValue = getDisplayedValue(key);
        return getTable().getDisplayedValue(key, retValue, getUnits());
    }

    /**
     * Get the drilldown URL for a given row.
     * @param key The key for the row (year or session)
     * @return The drilldown URL for a given row.
     */
    public String getDrillDown(String key) {
        return drillDownMap.get(key);
    }

    /**
     * Get the set of keys that index the rows. This is a sorted set.
     * @return The set of keys that index the rows.
     */
    public Set<String> getRowKeys() {
        return displayedValueMap.keySet();
    }

    /**
     * Get the axis title. Delegated to the table and is a function of the units.
     * @return The axis title.
     */
    public String getAxisTitle() {
        return table.getAxisTitle(getUnits());
    }

    /**
     * Get the corresponding table object.
     * @return The corresponding table object.
     */
    public Table getTable() {
        return table;
    }

    /**
     * Get the download query string. The value has been cached.
     * @return the downloadQuery
     */
    public String getDownloadQueryString() {
        return downloadQueryString;
    }

    /**
     * Set the download query string.
     * @param downloadQueryString the downloadQueryString to set
     */
    public void setDownloadQueryString(String downloadQueryString) {
        this.downloadQueryString = downloadQueryString;
    }

    /**
     * The minimum value of the data in a given column. This is used to scale
     * the graph.
     * @return The minimum value of the data in a given column
     */
    public Number getMinValue() {
        return minValue;
    }

    /**
     * The maximum value of the data in a given column. This is used to scale
     * the graph.
     * @return The maximum value of the data in a given column.
     */
    public Number getMaxValue() {
        return maxValue;
    }

}
