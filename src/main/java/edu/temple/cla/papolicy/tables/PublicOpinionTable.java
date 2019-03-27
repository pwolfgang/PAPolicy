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
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.filters.PublicOpinionFilters;
import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class PublicOpinionTable extends StandardTable {

    Units units = null;

    @Override
    public QueryBuilder getUnfilteredTotalQuery() {
        QueryBuilder builder = new QueryBuilder();
        builder.setTable(getTableName());
        builder.addColumn("Year AS TheYear");
        if (getUnits(null) == Units.PERCENT) {
            builder.addColumn("AVG(Percentage) AS TheValue");
        } else {
            builder.addColumn("AVG(25 - Rank_With_25) AS TheValue");
        }
        return builder;
    }

    @Override
    public QueryBuilder getTopicQuery(Topic topic) {
        QueryBuilder builder = getUnfilteredTotalQuery().clone();
        if (topic != null && topic.getCode() != 0) {
            builder.setTopic(new Comparison("Code", "=", Integer.toString(topic.getCode())));
        }
        return builder;
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
        if (Double.isNaN(value)) return null;
        else return value;
    }

    @Override
    public Number getPercentForRange(SortedMap<Integer, Number> valueMap,
            SortedMap<Integer, Number> totalMap) {
        double total = 0.0;
        for (Map.Entry<Integer, Number> entry : valueMap.entrySet()) {
            total += entry.getValue().doubleValue();
        }
        double value = total/valueMap.size() * 100;
        if (Double.isNaN(value)) return null;
        else return value;

    }

    @Override
    public List<YearValue> getYearValueList(String query) {
        RowMapper<YearValue> mapper = new YearValueMapper();
        List<YearValue> list = jdbcTemplate.query(query, mapper);
        if (getUnits(null) == Units.RANK) {
            list.forEach((yv) -> {
                yv.setValue(25 - yv.getValue().doubleValue());
            });
        }
        return list;
    }
    
    @Override
    public PublicOpinionTable clone() {
        
        return (PublicOpinionTable) super.clone();
    }


    /**
     * Method to return the value to be displayed in the results table.
     * This is a special case for the PublicOpinion table to append an
     * asterisk after the value for the years 2008 and 2009 and to add
     * a title attribute.
     * @param key The string that contains the year(s)
     * @param retValue The value for the year as a Number
     * @param units The units
     * @return The string to be displayed.
     */
    @Override
    public String getDisplayedValue(String key, Number retValue, Units units) {
        String result = super.getDisplayedValue(key, retValue, units);
        if (key.contains("2008") || key.contains("2009")) {
            result = "<span title=\"Most Important Problem data from 2008 and "
                    + "2009 were constructed from national Gallup polls using "
                    + "only the responses of PA residents. Please see the User "
                    + "Guide for more information.\">"+ result + "*</span>";
        }
        return result;
    }

}
