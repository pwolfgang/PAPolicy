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
 * The Table interface defines the methods for each of the datasets.
 * For each of the datasets methods are required to generate the html used
 * to display the datasets selection, collect the selection information
 * from the form submittal, and generate the database query.
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
     */
    String getTitleBox();

    /**
     * The jdbcTemplate is set when the class is loaded.
     * @param jdbcTemplate the jdbcTemplate to set
     */
    void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);

    /**
     * 
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
