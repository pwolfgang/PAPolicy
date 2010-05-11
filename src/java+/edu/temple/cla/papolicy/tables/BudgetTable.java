/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.dao.Deflator;
import edu.temple.cla.papolicy.dao.DeflatorMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.BudgetFilters;
import edu.temple.cla.papolicy.filters.Filter;
import java.util.List;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class BudgetTable extends AbstractTable {

    /**
     * Method to get the HTML code for the title box
     */
    @Override
    public String getTitleBox() {
        return {{
<dl><input type="checkbox" name="dataset" value="{{getId()}}A"
        id="t{{getId()}}A" onclick="expandBudget({{getId()}});" />
    <span class="strong">Total Spending All Funds</strong></dl>
<dl><input type="checkbox" name="dataset" value="{{getId()}}B"
        id="t{{getId()}}B" onclick="expandBudget({{getId()}});" />
    <span class="strong">General Fund Balance<span>
    <div class="subtbl" id="subtbl{{getId()}}B">
       <dd>
       Include Rainy Day Fund
       <br /><input type="radio" name="rainyDay" value="1" /> Yes
          <input type="radio" name="rainyDay" value="0" checked="checked" />No
       </dd>
    </div>
</dl>
        }};
    }

    @Override
    public boolean isTopicSearchable() {
        return true;
    }

    @Override
    public String toString() {
        Units units = getUnits(null);
        if (units == Units.PERCENT) {
            return "Percent of Total Spending";
        } else {
            return "Total Spending " + getAxisTitle(getUnits(null));
        }
    }

    public String getAllocatedTotalQueryString() {
        return "SELECT " + getYearColumn() +
            ", Sum(BudgetTable.TheValue*Crosswalk.PercentMatch/100)/1000 AS " +
            "TheValue " +
            "FROM MajorCode INNER JOIN " +
            "(Crosswalk INNER JOIN " + getTableName() +" ON " +
            "(Crosswalk.FC=" + getTableName() + ".FC) AND " +
            "(Crosswalk.OC=" + getTableName() + ".OC)) " +
            "ON MajorCode.Code = Crosswalk.PolicyCode ";
    }

    @Override
    public String getTotalQueryString() {
//        return "SELECT " + getYearColumn() +
//            ", Sum(BudgetTable.TheValue/1000) AS " +
//            "TheValue FROM " + getTableName() + " WHERE ";
        return getAllocatedTotalQueryString();
    }

    @Override
    public String getTopicQueryString(Topic topic) {
        if (topic != null && topic.getCode() != 0) {
            return  getAllocatedTotalQueryString() + "WHERE MajorCode.Code=" +
                    topic.getCode();
        } else {
            return getAllocatedTotalQueryString();
        }
    }

    @Override
    public String getYearColumn() {
        return "TheYear";
    }

    @Override
    public AbstractTable getSubTable(char qualifier) {
        if (qualifier == 'A') {
            return this;
        } else {
            AbstractTable newTable = new GeneralFundBalance();
            newTable.setId(getId());
            newTable.setMinYear(getMinYear());
            newTable.setMaxYear(getMaxYear());
            return newTable;
        }
    }

    @Override
    public String getTableName() {
        return "BudgetTable";
    }

    @Override
    public String getTableTitle() {
        return "Total Spending";
    }

    @Override
    public String getTextColumn() {
        return null;
    }

    @Override
    public Units getUnits(String showResults) {
        List<Filter> filterList = getFilterList();
        BudgetFilters budgetFilters = (BudgetFilters)filterList.get(0);
        if ("0".equals(budgetFilters.getDisp()))
            return Units.DOLLARS;
        else if ("1".equals(budgetFilters.getDisp()))
                return Units.PERCENT;
        else
            return Units.PERCENT_CHANGE;
    }

    @Override
    public String getAxisTitle(Units units) {
        if (units == Units.DOLLARS) {
            List<Filter> filterList = getFilterList();
            BudgetFilters budgetFilters = (BudgetFilters)filterList.get(0);
            if ("0".equals(budgetFilters.getAdjust())){
                return "Un-Adjusted Dollars (×1,000,000)";
            } else {
                String baseYear = budgetFilters.getBaseYear();
                return baseYear + " Dollars (×1,000,000)";
            }
        } else {
            return Units.getTitle(units);
        }
    }

    @Override
    public List<YearValue> getYearValueList(SimpleJdbcTemplate jdbcTemplate, String query) {
        ParameterizedRowMapper<YearValue> mapper = new YearValueMapper();
        ParameterizedRowMapper<Deflator> deflatorMapper = new DeflatorMapper();
        List<YearValue> list = jdbcTemplate.query(query, mapper);
        List<Filter> filterList = getFilterList();
        BudgetFilters budgetFilters = (BudgetFilters)filterList.get(0);
        if ("0".equals(budgetFilters.getAdjust())) {
            return list;
        } else {
            String baseYearString = budgetFilters.getBaseYear();
            int baseYear = Integer.parseInt(baseYearString);
            List<Deflator> deflatorList =
                    jdbcTemplate.query("SELECT * FROM Deflator", deflatorMapper);
            int firstYear = deflatorList.get(0).getYear();
            double[] deflator = new double[deflatorList.size()];
            for (int i = 0; i < deflatorList.size(); i++) {
                deflator[i] = deflatorList.get(i).getPriceIndex();
            }
            double baseYearAdjust = deflator[baseYear - firstYear];
            for (int i = 0; i < deflator.length; i++) {
                deflator[i] /= baseYearAdjust;
            }
            for (int i = 0; i < list.size(); i++) {
                int year = list.get(i).getYear();
                double value = list.get(i).getValue().doubleValue();
                value /= deflator[year - firstYear];
                list.set(i, new YearValue(year, new Double(value)));
            }
            return list;
        }
    }
}
