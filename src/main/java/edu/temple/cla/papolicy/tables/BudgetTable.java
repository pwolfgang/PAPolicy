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
import edu.temple.cla.papolicy.dao.Deflator;
import edu.temple.cla.papolicy.dao.DeflatorMapper;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.BudgetFilters;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import java.util.List;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * The BudgetTable contains the spending data from the US Census allocated
 * to the policy areas via the crosswalk.
 * @author Paul Wolfgang
 */
public class BudgetTable extends AbstractTable {

    /**
     * Method to get the HTML code for the title box. The title box allows
     * the user to select Total Spending or the General Fund Balance or both.
     * @return HTML code for the title box.
     */
    @Override
    public String getTitleBox() {
        return "\n"+
"<input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"A\"\n"+
"        id=\"t"+getId()+"A\" onclick=\"expandBudget("+getId()+");\" />\n"+
"    <label for=\"t"+getId()+"A\"><span class=\"strong\">Total Spending All Funds</span></label>\n"+
"<br/><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"B\"\n"+
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
"\n";
    }

    /**
     * The BudgetTable is topic searchable.
     * @return true.
     */
    @Override
    public boolean isTopicSearchable() {
        return true;
    }

    /**
     * A string representation of this table
     * @return Title modified by selected units.
     */
    @Override
    public String toString() {
        Units units = getUnits(null);
        if (units == Units.PERCENT) {
            return "Percent of Total Spending";
        } else {
            return "Total Spending " + getAxisTitle(units);
        }
    }

    /**
     * The download is always in un-adjusted dollars.
     * @return "Total Spending Un-Adjusted Dollars (×1,000,000)"
     */
    @Override
    public String getDownloadTitle() {
        return "Total Spending Un-Adjusted Dollars (×1,000,000)";
    }

    /**
     * Method to generate query to obtain the total allocated each policy code.
     * @return query to obtain the total allocated to each policy code
     */
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

    /**
     * The unfiltered total query is the allocated total query
     * @return allocatedTotalQuery
     */
    @Override
    public QueryBuilder getUnfilteredTotalQuery() {
        return getAllocatedTotalQuery();
    }

    /**
     * Since there are no filters, the filtered total query is the same
     * @return allocatedTotalQuery
     */
    @Override
    public QueryBuilder getFilteredTotalQuery() {
        return getAllocatedTotalQuery();
    }

    /**
     * The year column is hard-coded for this table.
     * @return "TheYear"
     */
    @Override
    public String getYearColumn() {
        return "TheYear";
    }

    /**
     * Construct a GeneralFundBalance table object if the qualifier (specified
     * by the form request) is not an 'A'. Otherwise, return this.
     * @param qualifier The qualified from the form request.
     * @return either this or a GeneralFundBalance object.
     */
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

    /**
     * The table name is hard-coded
     * @return "BudgetTable"
     */
    @Override
    public String getTableName() {
        return "BudgetTable";
    }

    /**
     * The table title is hard-coded
     * @return "Total Spending"
     */
    @Override
    public String getTableTitle() {
        return "Total Spending";
    }

    /**
     * There is no text column
     * @return null
     */
    @Override
    public String getTextColumn() {
        return null;
    }

    /**
     * Units are based on the filter selection.
     * @param showResults ignored.
     * @return units based on filter selection.
     */
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

    /**
     * Construct the axis title based on the units and the deflator year
     * selected from the filter form input.
     * @param units Units to display
     * @return axis title that included units and deflator year
     */
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


    /**
     * Query the database and return the year-value list.
     * @param query query to select the data
     * @return list of values by year
     */
    @Override
    public List<YearValue> getYearValueList(String query) {
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
    
    /**
     * Make a shallow copy of this object
     * @return a copy of this object.
     */
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
