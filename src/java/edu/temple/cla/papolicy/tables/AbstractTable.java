/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public abstract class AbstractTable implements Table {
    private int id;
    private String tableName;
    private String tableTitle;
    private boolean majorOnly;
    private String textColumn;
    private String yearColumn;
    private String[] drillDownColumns;
    private String linkColumn;
    private int minYear;
    private int maxYear;
    private char qualifier;
    private List<Filter> filterList;
    private SimpleJdbcTemplate jdbcTemplate;
    private String filterQueryString = null;
    private String totalQueryString = null;

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

    public StringBuilder getFilterQualifierString() {
        StringBuilder stb = new StringBuilder();
        ArrayList<String> filterQualifiers = new ArrayList<String>();
        for (Filter filter:filterList) {
            if (!"".equals(filter.getFilterQualifier())) {
                filterQualifiers.add(filter.getFilterQualifier());
            }
        }
        if (filterQualifiers.size() != 0) {
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

    public String getTotalQueryString() {
        if (totalQueryString == null) {
            StringBuilder stb = new StringBuilder("SELECT ");
            stb.append(yearColumn);
            stb.append(" AS TheYear, count(ID) AS TheValue FROM ");
            stb.append(tableName);
            stb.append(" WHERE ");
            stb.append(getFilterQueryString());
            totalQueryString = stb.toString();
        }
        return totalQueryString;
    }

    public String getTopicQueryString(Topic topic) {
        StringBuilder stb = new StringBuilder(getTotalQueryString());
        if (topic != null) {
            if (!getTotalQueryString().endsWith("WHERE ")) {
                stb.append(" AND ");
            }
            if (isMajorOnly() || topic.getCode() >= 100) {
                stb.append("Code=");
                stb.append(topic.getCode());
            } else {
                stb.append("Code LIKE('");
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

    /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets the DrillDown columns
     * @param query The query that gets the count.
     * @return Modified query that selects the DrillDown columns and
     * link column if defined.
     */
    public String createDrillDownQuery(String query) {
        int posFrom = query.indexOf("FROM");
        StringBuilder stb = new StringBuilder("SELECT ");
        stb.append(drillDownColumns[0]);
        for (int i = 1; i < drillDownColumns.length; i++) {
            stb.append(", ");
            stb.append(drillDownColumns[i]);
        }
        if (linkColumn != null) {
            stb.append(", ");
            stb.append(linkColumn);
        }
        stb.append(" ");
        stb.append(query.substring(posFrom));
        int posGroup = stb.indexOf("GROUP BY");
        int posOrder = stb.indexOf("ORDER BY");
        stb.delete(posGroup, posOrder);
        return stb.toString();
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
}
