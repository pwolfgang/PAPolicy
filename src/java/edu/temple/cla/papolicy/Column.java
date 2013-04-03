/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy;

import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.queryBuilder.Between;
import edu.temple.cla.papolicy.queryBuilder.Composite;
import edu.temple.cla.papolicy.queryBuilder.Conjunction;
import edu.temple.cla.papolicy.queryBuilder.Disjunction;
import edu.temple.cla.papolicy.queryBuilder.Expression;
import edu.temple.cla.papolicy.queryBuilder.Like;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
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
    private QueryBuilder topicCountQuery = null;
    private QueryBuilder unfilteredTotalQuery = null;
    private QueryBuilder filteredTotalQuery = null;
    private Units units;
    private SortedMap<Integer, Number> valueMap;
    private SortedMap<Integer, Number> unfilteredTotalMap;
    private SortedMap<Integer, Number> filteredTotalMap;
    private SortedMap<String, Number> displayedValueMap = new TreeMap<String, Number>();
    private SortedMap<String, String> drillDownMap = new TreeMap<String, String>();
    private Number prevValue = null;
    private String downloadQueryString = null;
    private Number minValue = null;
    private Number maxValue = null;
    private YearRange yearRange = null;

    protected Column() {
    } // used for unit test purposes only

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
        return table.getDownloadURL(getDownloadTitle(), getDownloadQueryString(), yearRange);
    }

    public QueryBuilder getFilteredTotalQuery() {
        if (filteredTotalQuery == null) {
            filteredTotalQuery = table.getFilteredTotalQuery().clone();
        }
        return filteredTotalQuery;
    }

    public String getFilteredTotalQueryString() {
        return getFilteredTotalQuery().build();
    }

    public QueryBuilder getUnfilteredTotalQuery() {
        if (unfilteredTotalQuery == null) {
            unfilteredTotalQuery = table.getUnfilteredTotalQuery().clone();
        }
        return unfilteredTotalQuery;
    }

    public String getUnfilteredTotalQueryString() {
        return getUnfilteredTotalQuery().build() + " WHERE ";
    }

    public QueryBuilder getUnfilteredTotalQuery(int startYear, int endYear) {
        QueryBuilder builder = getUnfilteredTotalQuery().clone();
        builder.setBetween(new Between(table.getYearColumn(), startYear, endYear));
        builder.setGroupBy("TheYear");
        builder.setOrderBy("TheYear");
        return builder;
    }

    public String getUnfilteredTotalQueryString(int startYear, int endYear) {
        return getUnfilteredTotalQuery(startYear, endYear).build();
    }

    public QueryBuilder getFilteredTotalQuery(int startYear, int endYear) {
        QueryBuilder builder = getFilteredTotalQuery().clone();
        builder.setBetween(new Between(table.getYearColumn(), startYear, endYear));
        builder.setGroupBy("TheYear");
        builder.setOrderBy("TheYear");
        return builder;
    }

    public String getFilteredTotalQueryString(int startYear, int endYear) {
        return getFilteredTotalQuery(startYear, endYear).build();
    }

    public QueryBuilder getTopicCountQuery() {
        if (topicCountQuery == null) {
            topicCountQuery = table.getTopicQuery(topic).clone();
            if (freeText != null) {
                topicCountQuery.setFreeText(parseFreeText(table.getTextColumn(), freeText));
            }
        }
        return topicCountQuery;
    }

    public String getTopicCountQueryString() {
        return getTopicCountQuery().build();
    }

    public QueryBuilder getTopicCountQuery(int startYear, int endYear) {
        QueryBuilder builder = getTopicCountQuery().clone();
        builder.setBetween(new Between(table.getYearColumn(), startYear, endYear));
        builder.setGroupBy("TheYear");
        builder.setOrderBy("TheYear");
        return builder;
    }

    public String getTopicCountQueryString(int startYear, int endYear) {
        return getTopicCountQuery(startYear, endYear).build();
    }

    /**
     * @return the units
     */
    public Units getUnits() {
        return units;
    }

    public Number getValue(int minYear, int maxYear) {
        Number returnVal = table.getValueForRange(valueMap.subMap(minYear, maxYear + 1));
        return returnVal;
    }

    public Number getPercentOfTotal(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear + 1),
                unfilteredTotalMap.subMap(minYear, maxYear + 1));
        return returnVal;
    }

    public Number getPercent(int minYear, int maxYear) {
        Number returnVal = table.getPercentForRange(valueMap.subMap(minYear, maxYear + 1),
                filteredTotalMap.subMap(minYear, maxYear + 1));
        return returnVal;
    }

    public void setInitialPrevValue(int minYear, int maxYear) {
        prevValue = table.getValueForRange(valueMap.subMap(minYear, maxYear));
    }

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
        Number result = new Double((cv - pv) / pv * 100.0);
        prevValue = currentValue;
        return result;
    }

    public void setValueMap(SimpleJdbcTemplate jdbcTemplate, int minYear, int maxYear) {
        String query = getTopicCountQueryString(minYear, maxYear);
        List<YearValue> list = table.getYearValueList(jdbcTemplate, query);
        valueMap = new TreeMap<>();
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

    public Table getTable() {
        return table;
    }

    /**
     * @return the downloadQuery
     */
    public String getDownloadQueryString() {
        return downloadQueryString;
    }

    /**
     * @param downloadQuery the downloadQuery to set
     */
    public void setDownloadQueryString(String downloadQueryString) {
        this.downloadQueryString = downloadQueryString;
    }

    public Number getMinValue() {
        return minValue;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    /**
     * Method to parse the free text and generate the query condition
     *
     * @param textColumn The name of the text column
     * @param freeText the free text string
     */
    public Expression parseFreeText(String textColumn, String freeText) {
        Pattern p = Pattern.compile("(\\\"[^\\\"]+\\\")|([^\\p{javaWhitespace}]+)");
        Scanner scan = new Scanner(freeText);
        List<String> list = new ArrayList<String>();
        String t;
        while ((t = scan.findInLine(p)) != null) {
            list.add(t);
        }
        Expression result;
        if (list.size() == 1) {
            result = createLike(textColumn, list.get(0));
        } else {
            result = new Conjunction();
            for (String token : list) {
                switch (token.toLowerCase()) {
                    case "and":
                        if (result.getClass() != Conjunction.class) {
                            Expression lhs = result;
                            Conjunction c = new Conjunction();
                            c.addTerm(lhs);
                            result = c;
                        }
                        break;
                    case "or":
                        if (result.getClass() != Disjunction.class) {
                            Expression lhs = result;
                            Disjunction d = new Disjunction();
                            d.addTerm(lhs);
                            result = d;
                        }
                        break;
                    default:
                        Composite c = (Composite) result;
                        c.addTerm(createLike(textColumn, token));
                }
            }
        }
        return result;
    }

    private Like createLike(String textColumn, String token) {
        if (token.startsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        return new Like(textColumn, "%" + token + "%");
    }
}
