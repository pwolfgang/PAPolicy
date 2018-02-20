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

import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class GeneralFundBalance extends BudgetTable {

    private boolean includeRainyDay;

    /**
     * Method to generate the HTML code for the title box.
     */
    @Override
    public String getTitleBox() {
        StringBuilder stb = new StringBuilder();
        stb.append("<input type=\"checkbox\" name=\"dataset\" value=\"");
        stb.append(getId());
        stb.append("\"/><span class=\"strong\">");
        stb.append(getTableTitle());
        stb.append("\r\n<br/>Include Rainy Day Fund ");
        stb.append("<input type=\"radio\" name=\"rainyDay\" value=\"0\" checked />");
        stb.append("No ");
        stb.append("<input type=\"radio\" name=\"rainyDay\" value=\"1\" />");
        stb.append("Yes");
        stb.append("</span></dd>");
        stb.append("</span>");
        return stb.toString();
    }

    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        includeRainyDay = "1".equals(request.getParameter("rainyDay"));
  }

    @Override
    public boolean isTopicSearchable() {
        return false;
    }

    @Override
    public String toString() {
        if (includeRainyDay)
            return "General Fund Balance Including Rainy Day Fund "
                    + getAxisTitle(getUnits(null));
        else
            return "General Fund Balance "+ getAxisTitle(getUnits(null));
    }

    @Override
    public String getDownloadTitle() {
        return "General Fund Balance Un-Adjusted Dollars (×1,000,000)";
    }

     /**
     * Method to convert the SQL query that gets the count to a
     * SQL query that gets all columns for download
     * @param query The query that gets the count.
     * @return Modified query that selects all columns.
     */
    @Override
    public QueryBuilder createDownloadQuery(QueryBuilder query) {
        QueryBuilder builder = query.clone();
        builder.clearColumns();
        builder.clearGroupBy();
        return builder;
    }

    @Override
    public QueryBuilder getTopicQuery(Topic topic) {
        QueryBuilder builder = new QueryBuilder();
        builder.addColumn(getYearColumn() + " AS TheYear");
        if (includeRainyDay) {
            builder.addColumn("SUM(Ending_Balance + Budget_Stabilization_Fund) AS TheValue");
        } else {
            builder.addColumn("SUM(Ending_Balance) AS TheValue");
        }
        builder.setTable(getTableName());
        return builder;
    }

    @Override
    public QueryBuilder getUnfilteredTotalQuery() {
        QueryBuilder builder = new QueryBuilder();
        builder.addColumn(getYearColumn() + " AS TheYear");
        builder.addColumn("Expenditures AS TheValue");
        builder.setTable(getTableName());
        return builder;
    }
    
    @Override
    public QueryBuilder getFilteredTotalQuery() {
        return getUnfilteredTotalQuery();
    }
    

    @Override
    public String getYearColumn() {
        return "Year";
    }

    @Override
    public String getTableName() {
        return "PennsylvaniaGeneralFundBalance";
    }

    @Override
    public String getTableTitle() {
        return "General Fund Balance";
    }

    @Override
    public String getTextColumn() {
        return null;
    }

    @Override
    public GeneralFundBalance clone() {
        return (GeneralFundBalance) super.clone();
    }
}
