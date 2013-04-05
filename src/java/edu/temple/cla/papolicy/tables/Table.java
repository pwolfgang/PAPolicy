/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.YearRange;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.queryBuilder.Conjunction;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import java.util.List;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public interface Table extends Cloneable {

    /**
     * @return the id
     */
    int getId();

    /**
     * @param id the id to set
     */
    void setId(int id);

    /**
     * @return the tableName
     */
    String getTableName();

    /**
     * @param tableName the tableName to set
     */
    void setTableName(String tableName);

    /**
     * @return the tableTitle
     */
    String getTableTitle();

    /**
     * @param tableTitle the tableTitle to set
     */
    void setTableTitle(String tableTitle);

    /**
     * @return the majorOnly
     */
    boolean isMajorOnly();

    /**
     * @param majorOnly the majorOnly to set
     */
    void setMajorOnly(boolean majorOnly);

    /**
     * @return the minYear
     */
    int getMinYear();

    /**
     * @param minYear the minYear to set
     */
    void setMinYear(int minYear);

    /**
     * @return the maxYear
     */
    int getMaxYear();

    /**
     * @param maxYear the maxYear to set
     */
    void setMaxYear(int maxYear);

    /**
     * @return the filterList
     */
    List<Filter> getFilterList();

    /**
     * @return filterList.size()
     */
    int getFilterListSize();

    /**
     * @param filterList the filterList to set
     */
    void setFilterList(List<Filter> filterList);

    /**
     * Method to generate the HTML code for the title box.
     */
    String getTitleBox();

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);

    /**
     * @return the qualifier
     */
    char getQualifier();

    /**
     * @param qualifier the qualifier to set
     */
    void setQualifier(char qualifier);

    /**
     * @return the textColumn
     */
    String getTextColumn();

    /**
     * @param textColumn the textColumn to set
     */
    void setTextColumn(String textColumn);

    StringBuilder getFilterQualifierString();

    Conjunction getFilterQuery();
    
    boolean isTopicSearchable();

    void setAdditionalParameters(HttpServletRequest request);

    QueryBuilder getFilteredTotalQuery();
    
    QueryBuilder getUnfilteredTotalQuery();

    QueryBuilder getTopicQuery(Topic topic);
    
    String getYearColumn();

    Units getUnits(String showResults);

    /**
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

    Number getValueForRange(SortedMap<Integer, Number> valueMap);

    Number getPercentForRange(SortedMap<Integer, Number> valueMap,
            SortedMap<Integer, Number> totalMap);

    String getAxisTitle(Units units);

    List<YearValue> getYearValueList(SimpleJdbcTemplate jdbcTemplate, String query);

    /**
     * @return the drill-down columns
     */
    String[] getDrillDownColumns();

    /**
     * @param drillDownColumns the array of columns to display in the drill-down
     * page
     */
    void setDrillDownColumns(String[] drillDownColumns);

    /**
     * @return the linkColumn
     */
    String getLinkColumn();

    /**
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
     * Method to set the Code column name
     */
    void setCodeColumn(String codeColumn);

    void setNoteColumn(String noteColumn);

    String getNoteColumn();

    public Table clone();

    String getDownloadTitle();

    String getDownloadURL(String downloadTitle, String downloadQueryString,
            YearRange yearRange);

    String getDisplayedValue(String key, Number value, Units units);
}
