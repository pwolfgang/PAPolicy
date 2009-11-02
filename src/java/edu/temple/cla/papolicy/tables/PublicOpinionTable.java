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
import edu.temple.cla.papolicy.filters.PublicOpinionFilters;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class PublicOpinionTable extends StandardTable {

    Units units = null;

    @Override
    public String getTotalQueryString() {
        if (getUnits(null) == Units.PERCENT) {
            return "SELECT Year AS TheYear, AVG(Percentage) as TheValue FROM " +
                    getTableName()
                    + " WHERE ";
        } else {
            return "SELECT Year AS TheYear, AVG(25 - Rank_With_25) AS TheValue FROM " +
                    getTableName()
                    + " WHERE ";
        }
    }

    @Override
    public String getTopicQueryString(Topic topic) {
        if (topic != null && topic.getCode() != 0) {
            return getTotalQueryString()
                + "Code="
                + topic.getCode();
        } else {
            return getTotalQueryString();
        }
    }

    @Override
    public Units getUnits(String showResults) {
        if (units == null) {
            List<Filter> filterList = getFilterList();
            PublicOpinionFilters pof = (PublicOpinionFilters) filterList.get(0);
            if (pof.getMipdisp().equals("0")) {
                units = Units.PERCENT;
            } else {
                units = Units.RANK;
            }
        }
        return units;
    }

    @Override
    public Number getValueForRange(SortedMap<Integer, Number> valueMap) {
        double total = 0.0;
        for (Map.Entry<Integer, Number> entry : valueMap.entrySet()) {
            total += (25.0 - entry.getValue().doubleValue());
        }
        double value = 25.0 - total/valueMap.size();
        return new Double(value);
    }

    @Override
    public Number getPercentForRange(SortedMap<Integer, Number> valueMap,
            SortedMap<Integer, Number> totalMap) {
        double total = 0.0;
        for (Map.Entry<Integer, Number> entry : valueMap.entrySet()) {
            total += entry.getValue().doubleValue();
        }
        return new Double(total/valueMap.size() * 100);
    }

    @Override
    public List<YearValue> getYearValueList(SimpleJdbcTemplate jdbcTemplate, String query) {
        ParameterizedRowMapper<YearValue> mapper = new YearValueMapper();
        List<YearValue> list = jdbcTemplate.query(query, mapper);
        if (getUnits(null) == Units.RANK) {
            for (YearValue yv : list) {
                yv.setValue(new Double(25 - yv.getValue().doubleValue()));
            }
        }
        return list;
    }

}
