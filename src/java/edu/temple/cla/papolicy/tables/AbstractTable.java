/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.Conjunction;
import edu.temple.cla.papolicy.queryBuilder.Like;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public abstract class AbstractTable implements Table {
    private static Logger logger = Logger.getLogger(AbstractTable.class);

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
    private SimpleJdbcTemplate jdbcTemplate;
    private Conjunction filterQuery;
    private QueryBuilder totalUnfilteredQuery;
    private QueryBuilder totalFilteredQuery;

    /**
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the tableName
     */
    @Override
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableTitle
     */
    @Override
    public String getTableTitle() {
        return tableTitle;
    }

    /**
     * @param tableTitle the tableTitle to set
     */
    @Override
    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    /**
     * @return the majorOnly
     */
    @Override
    public boolean isMajorOnly() {
        return majorOnly;
    }

    /**
     * @param majorOnly the majorOnly to set
     */
    @Override
    public void setMajorOnly(boolean majorOnly) {
        this.majorOnly = majorOnly;
    }

    /**
     * @return the minYear
     */
    @Override
    public int getMinYear() {
        return minYear;
    }

    /**
     * @param minYear the minYear to set
     */
    @Override
    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    /**
     * @return the maxYear
     */
    @Override
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * @param maxYear the maxYear to set
     */
    @Override
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    /**
     * @return the filterList
     */
    @Override
    public List<Filter> getFilterList() {
        return filterList;
    }

    /**
     * @return filterList.size()
     */
    @Override
    public int getFilterListSize() {
        return filterList.size();
    }

    /**
     * @param filterList the filterList to set
     */
    @Override
    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }


    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    @Override
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @return the qualifier
     */
    @Override
    public char getQualifier() {
        return qualifier;
    }

    /**
     * @param qualifier the qualifier to set
     */
    @Override
    public void setQualifier(char qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * @return the textColumn
     */
    @Override
    public String getTextColumn() {
        return textColumn;
    }

    /**
     * @param textColumn the textColumn to set
     */
    @Override
    public void setTextColumn(String textColumn) {
        this.textColumn = textColumn;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder(tableTitle);
        stb.append(getFilterQualifierString());
        return stb.toString();
    }

    @Override
    public String getDownloadTitle() {
        return toString();
    }

    @Override
    public StringBuilder getFilterQualifierString() {
        StringBuilder stb = new StringBuilder();
        ArrayList<String> filterQualifiers = new ArrayList<>();
        for (Filter filter:filterList) {
            if (!"".equals(filter.getFilterQualifier())) {
                filterQualifiers.add(filter.getFilterQualifier());
            }
        }
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

    @Override
    public Conjunction getFilterQuery() {
        if (filterQuery == null) {
            filterQuery = new Conjunction();
            for (Filter filter : filterList) {
                filterQuery.addTerm(filter.getFilterQuery());
            }
        }
        return filterQuery;
    }
    
    @Override
    public boolean isTopicSearchable() {return true;}

    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        // do nothing in base class
    }

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

    @Override
    public QueryBuilder getFilteredTotalQuery() {
        if (totalFilteredQuery == null) {
            totalFilteredQuery = getUnfilteredTotalQuery().clone();
            totalFilteredQuery.addFilter(getFilterQuery());
        }
        return totalFilteredQuery;
    }
    
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
    
    @Override
    public String getYearColumn() {return yearColumn;}

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

    @Override
    public Number getValueForRange(SortedMap<Integer, Number> valueMap) {
        Number sum = null;
        for (Map.Entry<Integer, Number> entry : valueMap.entrySet()) {
            sum = add(sum, entry.getValue());
        }
        return sum;
    }

    @Override
    public Number getPercentForRange(SortedMap<Integer, Number> valueMap,
            SortedMap<Integer, Number> totalMap) {
        Number sum = getValueForRange(valueMap);
        Number total = getValueForRange(totalMap);
        if (total != null && total.doubleValue() != 0) {
            return new Double(sum != null ? sum.doubleValue() / total.doubleValue() * 100 : new Double(0));
        }
        return null;
    }

    static protected Number add(Number n1, Number n2) {
        if (n1 == null && n2 == null) return null;
        if (n1 == null) return n2;
        if (n2 == null) return n1;
        if (n1 instanceof Integer && n2 instanceof Integer) {
            return new Integer(n1.intValue() + n2.intValue());
        }
        return new Double(n1.doubleValue() + n2.doubleValue());
    }

    @Override
    public String getAxisTitle(Units units) {
        return Units.getTitle(units);
    }

    @Override
    public List<YearValue> getYearValueList(SimpleJdbcTemplate jdbcTemplate, String query) {
        ParameterizedRowMapper<YearValue> mapper = new YearValueMapper();
        List<YearValue> list = jdbcTemplate.query(query, mapper);
        return list;
    }

    /**
     * @return the drill-down columns
     */
    @Override
    public String[] getDrillDownColumns() {
        return drillDownColumns;
    }

    /**
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
     * @return the linkColumn
     */
    @Override
    public String getLinkColumn() {
        return linkColumn;
    }

    /**
     * @param linkColumn the linkColumn to set
     */
    @Override
    public void setLinkColumn(String linkColumn) {
        this.linkColumn = linkColumn;
    }

    @Override
    public void setNoteColumn(String noteColumn) {
        this.noteColumn = noteColumn;
    }

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
     */
     @Override
     public void setCodeColumn(String codeColumn) {
         this.codeColumn = codeColumn;
     }

    public static Table[] getTable(String tableId, char qualifier,
            HttpServletRequest request, SimpleJdbcTemplate jdbcTemplate)
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
        for (Filter filter : filterList) {
            filter.setJdbcTemplate(jdbcTemplate);
            filter.setFilterParameterValues(request);
        }
        table.setFilterList(filterList);
        table.setJdbcTemplate(jdbcTemplate);
        //Scan filter list to see how many variations there may be.
        int numVariations = 1;
        for (Filter f : filterList) {
            numVariations *= f.getNumberOfFilterChoices();
        }
        if (numVariations == 1) {
            return new Table[] {table};
        }
        List<Table> tableList = new ArrayList<>();
        tableList.add(table);
        while (expandChoices(tableList)) { 
            // work done in the method expandChoices method
        }
        return tableList.toArray(new Table[tableList.size()]);
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
            logger.error(ex);
        }
        stb.append(".xlsx?query=");
        stb.append(downloadQueryString);
        stb.append("\">");
        stb.append(downloadTitle);
        stb.append("</a><br/>");
        return stb.toString();
    }

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
