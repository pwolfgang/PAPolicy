/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy;

import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.tables.Table;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class Column {

    private Table table;
    private Topic topic;
    private String freeText;
    private String topicCountQueryString = null;
    private String totalQueryString = null;
    private Units units;
    private SortedMap<Integer, Number> valueMap;
    private SortedMap<Integer, Number> totalMap;
    private SortedMap<String, Number> displayedValueMap = new TreeMap<String, Number>();
    private SortedMap<String, String> drillDownMap = new TreeMap<String, String>();
    private Number prevValue = null;
    private String downloadQuery = null;
    private Number minValue = null;
    private Number maxValue = null;

    public Column(Table table, Topic topic, String freeText, String showResults) {
        this.table = table;
        this.topic = topic;
        this.freeText = freeText;
        units = table.getUnits(showResults);
    }

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

    public String getTotalQueryString() {
        if (totalQueryString == null) {
            totalQueryString = table.getTotalQueryString();
        }
        return totalQueryString;
    }

    public String getTotalQueryString(int startYear, int endYear) {
        String result = getTotalQueryString();
        if (!result.endsWith("WHERE ")) {
            result = result + " AND ";
        }
        StringBuilder stb = new StringBuilder(result);
        stb.append(table.getYearColumn());
        stb.append(" BETWEEN ");
        stb.append(startYear);
        stb.append(" AND ");
        stb.append(endYear);
        stb.append(" GROUP BY ");
        stb.append(table.getYearColumn());
        stb.append(" ORDER BY ");
        stb.append(table.getYearColumn());
        return stb.toString();
    }

    public String getTopicCountQueryString() {
        if (topicCountQueryString == null) {
            topicCountQueryString = table.getTopicQueryString(topic);
            if (freeText != null) {
                if (!topicCountQueryString.endsWith(" WHERE ")) {
                    topicCountQueryString = topicCountQueryString + " AND ";
                }
                topicCountQueryString = topicCountQueryString +
                        table.getTextColumn() + " LIKE('%" + freeText + "%')";
            }
        }
        return topicCountQueryString;
    }

    public String getTopicCountQueryString(int startYear, int endYear) {
        String result = getTopicCountQueryString();
        if (!result.endsWith("WHERE ")) {
            result = result + " AND ";
        }
        StringBuilder stb = new StringBuilder(result);
        stb.append(table.getYearColumn());
        stb.append(" BETWEEN ");
        stb.append(startYear);
        stb.append(" AND ");
        stb.append(endYear);
        stb.append(" GROUP BY ");
        stb.append(table.getYearColumn());
        stb.append(" ORDER BY ");
        stb.append(table.getYearColumn());
        return stb.toString();
    }

    /**
     * @return the units
     */
    public Units getUnits() {
        return units;
    }

    public Number getValue(int minYear, int maxYear) {
        Number returnVal = table.getValueForRange(valueMap.subMap(minYear, maxYear+1));
        return returnVal;
    }

    public Number getPercent(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear+1),
                totalMap.subMap(minYear, maxYear+1));
        return returnVal;
    }
    
    public void setInitialPrevValue(int minYear, int maxYear) {
        prevValue = table.getValueForRange(valueMap.subMap(minYear, maxYear));
    }

    public Number getPercentChange(int minYear, int maxYear) {
        Number currentValue = table.getValueForRange(valueMap.subMap(minYear, maxYear+1));
        if (currentValue == null || prevValue == null) {
            prevValue = null;
            return null;
        }
        Double pv = prevValue.doubleValue();
        Double cv = currentValue.doubleValue();
        Number result = new Double((cv - pv) / pv * 100.0);
        prevValue = currentValue;
        return result;
    }

    public void setValueMap(SimpleJdbcTemplate jdbcTemplate, int minYear, int maxYear) {
        String query = getTopicCountQueryString(minYear, maxYear);
        List<YearValue> list = table.getYearValueList(jdbcTemplate, query);
        valueMap = new TreeMap<Integer, Number>();
        for (YearValue yv : list) {
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
            valueMap.put(new Integer(yv.getYear()), yv.getValue());
        }
    }

    public void setTotalMap(SimpleJdbcTemplate jdbcTemplate, int minYear, int maxYear) {
        String query = getTotalQueryString(minYear, maxYear);
        List<YearValue> list = table.getYearValueList(jdbcTemplate, query);
        totalMap = new TreeMap<Integer, Number>();
        for (YearValue yv : list) {
            totalMap.put(new Integer(yv.getYear()), yv.getValue());
        }
    }

    public void setDisplayedValue(String key, Number value) {
        displayedValueMap.put(key, value);
    }

    public void setDrillDown(String key, String value) {
        drillDownMap.put(key, value);
    }

    public Number getDisplayedValue(String key) {
        return displayedValueMap.get(key);
        
    }

    public String getDisplayedValueString(String key) {
        Number retValue = getDisplayedValue(key);
        switch (getUnits()) {
            case COUNT : return retValue != null ? retValue.toString() : "null";
            case DOLLARS : return retValue != null ?
                String.format("$%,.0f", retValue.doubleValue()) : "null";
            case PERCENT : case PERCENT_CHANGE :
                return retValue != null ?
                    String.format("%.1f%%", retValue.doubleValue()) : "null";
            case RANK:
                return retValue != null ?
                    String.format("%.1f", retValue.doubleValue()) : "null";
            default: return retValue != null ? retValue.toString() : "null";
        }
    }

    public String getDrillDown(String key) {
        return drillDownMap.get(key);
    }

    public Set<String> getRowKeys() {
        return displayedValueMap.keySet();
    }

    public String getAxisTitle() {
        return table.getAxisTitle(getUnits());
    }

    public Table getTable() {return table;}

    /**
     * @return the downloadQuery
     */
    public String getDownloadQuery() {
        return downloadQuery;
    }

    /**
     * @param downloadQuery the downloadQuery to set
     */
    public void setDownloadQuery(String downloadQuery) {
        this.downloadQuery = downloadQuery;
    }

    public Number getMinValue() {return minValue;}

    public Number getMaxValue() {return maxValue;}
}
