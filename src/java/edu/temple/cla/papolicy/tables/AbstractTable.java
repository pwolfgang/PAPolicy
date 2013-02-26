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
    private String filterQueryString = null;
    private String totalUnfilteredQueryString = null;
    private String totalFilteredQueryString = null;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableTitle
     */
    public String getTableTitle() {
        return tableTitle;
    }

    /**
     * @param tableTitle the tableTitle to set
     */
    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    /**
     * @return the majorOnly
     */
    public boolean isMajorOnly() {
        return majorOnly;
    }

    /**
     * @param majorOnly the majorOnly to set
     */
    public void setMajorOnly(boolean majorOnly) {
        this.majorOnly = majorOnly;
    }

    /**
     * @return the minYear
     */
    public int getMinYear() {
        return minYear;
    }

    /**
     * @param minYear the minYear to set
     */
    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    /**
     * @return the maxYear
     */
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * @param maxYear the maxYear to set
     */
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    /**
     * @return the filterList
     */
    public List<Filter> getFilterList() {
        return filterList;
    }

    /**
     * @return filterList.size()
     */
    public int getFilterListSize() {
        return filterList.size();
    }

    /**
     * @param filterList the filterList to set
     */
    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }


    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @return the qualifier
     */
    public char getQualifier() {
        return qualifier;
    }

    /**
     * @param qualifier the qualifier to set
     */
    public void setQualifier(char qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * @return the textColumn
     */
    public String getTextColumn() {
        return textColumn;
    }

    /**
     * @param textColumn the textColumn to set
     */
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

    public StringBuilder getFilterQualifierString() {
        StringBuilder stb = new StringBuilder();
        ArrayList<String> filterQualifiers = new ArrayList<String>();
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

    public String getFilterQueryString() {
        if (filterQueryString == null) {
            StringBuilder stb = new StringBuilder();
            ArrayList<String> filterQueryStrings = new ArrayList<String>();
            for (Filter filter : filterList) {
                if (!"".equals(filter.getFilterQueryString())) {
                    filterQueryStrings.add(filter.getFilterQueryString());
                }
            }
            if (filterQueryStrings.size() != 0) {
                stb.append(filterQueryStrings.get(0));
                for (int i = 1; i < filterQueryStrings.size(); i++) {
                    stb.append(" AND ");
                    stb.append(filterQueryStrings.get(i));
                }
            }
            filterQueryString = stb.toString();
        }
        return filterQueryString;
    }

    public boolean isTopicSearchable() {return true;}

    public void setAdditionalParameters(HttpServletRequest request) {
        // do nothing in base class
    }

    public String getUnfilteredTotalQueryString() {
        if (totalUnfilteredQueryString == null) {
            StringBuilder stb = new StringBuilder("SELECT ");
            stb.append(yearColumn);
            stb.append(" AS TheYear, count(ID) AS TheValue FROM ");
            stb.append(tableName);
            stb.append(" WHERE ");
            totalUnfilteredQueryString = stb.toString();
        }
        return totalUnfilteredQueryString;
    }

    public String getFilteredTotalQueryString() {
        if (totalFilteredQueryString == null) {
            StringBuilder stb = new StringBuilder(getUnfilteredTotalQueryString());
            String filterQueryString = getFilterQueryString();
            if (filterQueryString != null && !filterQueryString.isEmpty()) {
                if (!getUnfilteredTotalQueryString().endsWith("WHERE ")) {
                    stb.append(" AND ");
                }
                stb.append(getFilterQueryString());
            }
            totalFilteredQueryString = stb.toString();
        }
        return totalFilteredQueryString;
    }

    public String getTopicQueryString(Topic topic) {
        StringBuilder stb = new StringBuilder(getFilteredTotalQueryString());
        if (topic != null && topic.getCode() != 0) {
            if (!getFilteredTotalQueryString().endsWith("WHERE ")) {
                stb.append(" AND ");
            }
            if (isMajorOnly() || topic.getCode() >= 100) {
                stb.append(getCodeColumn());
                stb.append("=");
                stb.append(topic.getCode());
            } else {
                stb.append(getCodeColumn());
                stb.append(" LIKE('");
                stb.append(topic.getCode());
                stb.append("__')");
            }
        }
        return stb.toString();
    }

    public String getYearColumn() {return yearColumn;}

    public Units getUnits(String showResults) {
        if ("percent".equals(showResults)) {
            return Units.PERCENT;
        } else if ("percent_of_total".equals(showResults)){
            return Units.PERCENT_OF_TOTAL;
        } else if ("percent_of_filtered".equals(showResults)) {
            return Units.PERCENT_OF_FILTERED;
        } else {
            return Units.COUNT;
        }
    }

    /**
     * @param yearColumn the yearColumn to set
     */
    public void setYearColumn(String yearColumn) {
        this.yearColumn = yearColumn;
    }

    /**
     * Function to get the subtable of this table based on the
     * qualifier character.
     * @param qualifier A character ('A', 'B', etc.) that follows the table ID
     * @return The subtable as indicated by the qualifier character.
     */
    public AbstractTable getSubTable(char qualifier) {
        // The AbstractTable class has no subtables
        return this;
    }

    public Number getValueForRange(SortedMap<Integer, Number> valueMap) {
        Number sum = null;
        for (Map.Entry<Integer, Number> entry : valueMap.entrySet()) {
            sum = add(sum, entry.getValue());
        }
        return sum;
    }

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

    public String getAxisTitle(Units units) {
        return Units.getTitle(units);
    }

    public List<YearValue> getYearValueList(SimpleJdbcTemplate jdbcTemplate, String query) {
        ParameterizedRowMapper<YearValue> mapper = new YearValueMapper();
        List<YearValue> list = jdbcTemplate.query(query, mapper);
        return list;
    }

    /**
     * @return the drill-down columns
     */
    public String[] getDrillDownColumns() {
        return drillDownColumns;
    }

    /**
     * @param drillDownColumns the array of columns to display in the
     * drill-down page
     */
    public void setDrillDownColumns(String[] drillDownColumns) {
        this.drillDownColumns = drillDownColumns.clone();
    }

    /**
     * @return the linkColumn
     */
    public String getLinkColumn() {
        return linkColumn;
    }

    /**
     * @param linkColumn the linkColumn to set
     */
    public void setLinkColumn(String linkColumn) {
        this.linkColumn = linkColumn;
    }

    public void setNoteColumn(String noteColumn) {
        this.noteColumn = noteColumn;
    }

    public String getNoteColumn() {return noteColumn != null ? noteColumn : "";}

    /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets the DrillDown columns
     * @param query The query that gets the count.
     * @return Modified query that selects the DrillDown columns and
     * link column if defined.
     */
    protected String createDrillDownQuery(String query) {
        int posFrom = query.indexOf("FROM");
        StringBuilder stb = new StringBuilder("SELECT ");
        stb.append(getDrillDownColumns()[0]);
        for (int i = 1; i < getDrillDownColumns().length; i++) {
            stb.append(", ");
            stb.append(getDrillDownColumns()[i]);
        }
        if (linkColumn != null) {
            stb.append(", ");
            stb.append(linkColumn);
            stb.append(" as Link");
        }
        stb.append(" ");
        stb.append(query.substring(posFrom));
        int posGroup = stb.indexOf("GROUP BY");
        int posOrder = stb.indexOf("ORDER BY");
        if (posGroup != -1 && posOrder != -1) {
            stb.delete(posGroup, posOrder);
        }
        return stb.toString();
    }

    /**
     * Method to create the drilldown url.
     * This method returns the string &quot;drilldown.spg?query=<i>queyr</i>&quot;
     * Tables that need a different DrillDownController can override this method.
     * @param query The query string that gets the count
     * @return the url that will invoke the DrillDownController
     */
    public String createDrillDownURL(String query) {
        String drillDownQuery = createDrillDownQuery(query);
        return "drilldown.spg?query=" + Utility.compressAndEncode(drillDownQuery);
    }

    /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets all columns for download
     * @param query The query that gets the count.
     * @return Modified query that selects all columns.
     */
    public String createDownloadQuery(String query) {
        int posFrom = query.indexOf("FROM");
        StringBuilder stb = new StringBuilder("SELECT *");
        stb.append(query.substring(posFrom));
        int posGroup = stb.indexOf("GROUP BY");
        int posOrder = stb.indexOf("ORDER BY");
        stb.delete(posGroup, posOrder);
        return stb.toString();
    }

    /**
     * Method to get the Code column name
     * @return the Code column name
     */
     public String getCodeColumn() {return codeColumn;}

    /**
     * Method to set the Code column name
     */
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
        List<Table> tableList = new ArrayList<Table>();
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
                theClone.filterList = new ArrayList(filterList);
            }
            return theClone;
        } catch (CloneNotSupportedException ex) {
            throw new Error("CloneNotSupportedException should never be thrown");
        }
    }

    public String getDownloadURL(String downloadTitle, String downloadQuery, YearRange yearRange ) {
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
        stb.append(downloadQuery);
        stb.append("\">");
        stb.append(downloadTitle);
        stb.append("</a><br/>");
        return stb.toString();
    }

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
