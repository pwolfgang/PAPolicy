/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.dao.Deflator;
import edu.temple.cla.papolicy.dao.DeflatorMapper;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.BudgetFilters;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
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
        return "\n"+
"<dl><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"A\"\n"+
"        id=\"t"+getId()+"A\" onclick=\"expandBudget("+getId()+");\" />\n"+
"    <label for=\"t"+getId()+"A\"><span class=\"strong\">Total Spending All Funds</span></label></dl>\n"+
"<dl><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"B\"\n"+
"        id=\"t"+getId()+"B\" onclick=\"expandBudget("+getId()+");\" />\n"+
"    <label for=\"t"+getId()+"B\"><span class=\"strong\">General Fund Balance</span></label>\n"+
"    <div class=\"subtbl\" id=\"subtbl"+getId()+"B\">\n"+
"       <fieldset>" +
"       <legend>Include Rainy Day Fund</legend>\n"+
"          <input type=\"radio\" name=\"rainyDay\" value=\"1\" id=\"rainyDay1\"/>"
                + "<label for=\"rainyDay1\">Yes</label>\n"+
"          <input type=\"radio\" name=\"rainyDay\" value=\"0\" id=\"rainyDay0\" checked=\"checked\" />"
                + "<label for=\"rainyDay0\">No</label>\n"+
"       </fieldset>\n"+
"    </div>\n"+
"</dl>\n";
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
            return "Total Spending " + getAxisTitle(units);
        }
    }

    @Override
    public String getDownloadTitle() {
        return "Total Spending Un-Adjusted Dollars (×1,000,000)";
    }

    private QueryBuilder getAllocatedTotalQuery() {
        QueryBuilder builder = new QueryBuilder();
        builder.addColumn(getYearColumn());
        builder.addColumn("Sum(BudgetTable.TheValue*Crosswalk.PercentMatch/100)/1000 AS TheValue");
        builder.setTable("MajorCode INNER JOIN " +
            "(Crosswalk INNER JOIN " + getTableName() +" ON " +
            "(Crosswalk.FC=" + getTableName() + ".FC) AND " +
            "(Crosswalk.OC=" + getTableName() + ".OC)) " +
            "ON MajorCode.Code = Crosswalk.PolicyCode");
        return builder;
    }

    @Override
    public QueryBuilder getUnfilteredTotalQuery() {
        return getAllocatedTotalQuery();
    }

    @Override
    public QueryBuilder getFilteredTotalQuery() {
        return getAllocatedTotalQuery();
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
    
    @Override
    public BudgetTable clone() {
        return (BudgetTable)super.clone();
    }

    /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets all columns for download.
     * For the BudgetTable only selected columns are extracted from
     * the join that creates the allocated budget data.
     * @param query The query that gets the count.
     * @return Modified query that selects all columns.
     */
    @Override
    public QueryBuilder createDownloadQuery(QueryBuilder query) {
        QueryBuilder builder = query.clone();
        builder.clearColumns();
        builder.addColumn(getYearColumn());
        builder.addColumn("MajorCode.Code");
        builder.addColumn("Crosswalk.FC");
        builder.addColumn("Crosswalk.OC");
        builder.addColumn("Crosswalk.PercentMatch");
        builder.addColumn("BudgetTable.TheValue/1000 as TotalAmount");
        builder.addColumn("BudgetTable.TheValue*Crosswalk.PercentMatch/100000 as AllocatedAmount");
        builder.clearGroupBy();
        builder.clearOrderBy();
        builder.setOrderBy(getYearColumn() + ", MajorCode.Code, Crosswalk.FC, Crosswalk.OC");
        return builder;
    }
}
