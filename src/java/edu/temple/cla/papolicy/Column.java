/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy;

import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.tables.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class Column {
    private static Logger logger = Logger.getLogger(Column.class);

    private Table table;
    private Topic topic;
    private String freeText;
    private String topicCountQueryString = null;
    private String unfilteredTotalQueryString = null;
    private String filteredTotalQueryString = null;
    private Units units;
    private SortedMap<Integer, Number> valueMap;
    private SortedMap<Integer, Number> unfilteredTotalMap;
    private SortedMap<Integer, Number> filteredTotalMap;
    private SortedMap<String, Number> displayedValueMap = new TreeMap<String, Number>();
    private SortedMap<String, String> drillDownMap = new TreeMap<String, String>();
    private Number prevValue = null;
    private String downloadQuery = null;
    private Number minValue = null;
    private Number maxValue = null;
    private YearRange yearRange = null;

    protected Column() {} // used for unit test purposes only

    public Column(Table table, Topic topic, String freeText, String showResults, YearRange yearRange) {
        this.table = table;
        this.topic = topic;
        this.freeText = freeText;
        units = table.getUnits(showResults);
        this.yearRange = yearRange;
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

    public String getDownloadURL() {
        return table.getDownloadURL(getDownloadTitle(), getDownloadQuery(), yearRange);
    }

    public String getFilteredTotalQueryString() {
        if (filteredTotalQueryString == null) {
            filteredTotalQueryString = table.getFilteredTotalQueryString();
        }
        return filteredTotalQueryString;
    }

    public String getUnfilteredTotalQueryString() {
        if (unfilteredTotalQueryString == null) {
            unfilteredTotalQueryString = table.getUnfilteredTotalQueryString();
        }
        return unfilteredTotalQueryString;
    }

    public String getUnfilteredTotalQueryString(int startYear, int endYear) {
        String result = getUnfilteredTotalQueryString();
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

    public String getFilteredTotalQueryString(int startYear, int endYear) {
        String result = getFilteredTotalQueryString();
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
                        parseFreeText(table.getTextColumn(), freeText);
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

    public Number getPercentOfTotal(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear+1),
                unfilteredTotalMap.subMap(minYear, maxYear+1));
        return returnVal;
    }


    public Number getPercent(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear+1),
                filteredTotalMap.subMap(minYear, maxYear+1));
        return returnVal;
    }
    
    public void setInitialPrevValue(int minYear, int maxYear) {
        prevValue = table.getValueForRange(valueMap.subMap(minYear, maxYear));
    }

    public Number getPercentChange(int minYear, int maxYear) {
        Number currentValue = table.getValueForRange(valueMap.subMap(minYear, maxYear+1));
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

    public void setUnfilteredTotalMap(SimpleJdbcTemplate jdbcTemplate, int minYear, int maxYear) {
        String query = getUnfilteredTotalQueryString(minYear, maxYear);
        List<YearValue> list = table.getYearValueList(jdbcTemplate, query);
        unfilteredTotalMap = new TreeMap<Integer, Number>();
        for (YearValue yv : list) {
            unfilteredTotalMap.put(new Integer(yv.getYear()), yv.getValue());
        }
    }

    public void setFilteredTotalMap(SimpleJdbcTemplate jdbcTemplate, int minYear, int maxYear) {
        String query = getFilteredTotalQueryString(minYear, maxYear);
        List<YearValue> list = table.getYearValueList(jdbcTemplate, query);
        filteredTotalMap = new TreeMap<Integer, Number>();
        for (YearValue yv : list) {
            filteredTotalMap.put(new Integer(yv.getYear()), yv.getValue());
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
        return getTable().getDisplayedValue(key, retValue, getUnits());
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

    /**
     * Method to parse the free text and generate the query condition
     * @param textColumn The name of the text column
     * @param freeText the free text string
     */
    public String parseFreeText(String textColumn, String freeText) {
        Pattern p = Pattern.compile("(\\\"[^\\\"]+\\\")|([^\\p{javaWhitespace}]+)");
        Scanner scan = new Scanner(freeText);
        List<String> list = new ArrayList<String>();
        String t = null;
        while ((t = scan.findInLine(p)) != null) {
            list.add(t);
        }
        boolean operatorNeeded = false;
        StringBuilder stb = new StringBuilder("(");
        for (String token : list) {
            if (token.toLowerCase().equals("and")) {
                stb.append(" AND ");
                operatorNeeded = false;
            } else if (token.toLowerCase().equals("or")) {
                stb.append(" OR ");
                operatorNeeded = false;
            } else {
                if (operatorNeeded) {
                    stb.append(" AND ");
                    operatorNeeded = false;
                }
                if (token.startsWith("\"")){
                    token = token.substring(1, token.length()-1);
                }
                stb.append(textColumn);
                stb.append(" LIKE(\"%");
                stb.append(token);
                stb.append("%\")");
                operatorNeeded = true;
           }
        }
        stb.append(")");
        return stb.toString();
    }
}
