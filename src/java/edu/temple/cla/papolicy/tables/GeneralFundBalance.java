/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.dao.Topic;
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
        stb.append("<dl><dt><input type=\"checkbox\" name=\"dataset\" value=\"");
        stb.append(getId());
        stb.append("\"/><span class=\"strong\">");
        stb.append(getTableTitle());
        stb.append("\r\n<br/>Include Rainy Day Fund ");
        stb.append("<input type=\"radio\" name=\"rainyDay\" value=\"0\" checked />");
        stb.append("No ");
        stb.append("<input type=\"radio\" name=\"rainyDay\" value=\"1\" />");
        stb.append("Yes");
        stb.append("</span></dd>");
        stb.append("</span></dt></dl>");
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
    public String createDownloadQuery(String query) {
        int posFrom = query.indexOf("FROM");
        StringBuilder stb = new StringBuilder("SELECT *");
        stb.append(query.substring(posFrom));
        int posGroup = stb.indexOf("GROUP BY");
        int posOrder = stb.indexOf("ORDER BY");
        stb.delete(posGroup, posOrder);
        return stb.toString();
    }

    @Override
    public String getTopicQueryString(Topic topic) {
        StringBuilder stb = new StringBuilder();
        stb.append("SELECT ");
        stb.append(getYearColumn());
        stb.append(" AS TheYear, ");
        if (includeRainyDay) {
            stb.append("Ending_Balance + Budget_Stabilization_Fund ");
        } else {
            stb.append("Ending_Balance ");
        }
        stb.append(" AS TheValue ");
        stb.append("FROM ");
        stb.append(getTableName());
        stb.append(" WHERE ");
        return stb.toString();
    }

    @Override
    public String getUnfilteredTotalQueryString() {
                StringBuilder stb = new StringBuilder();
        stb.append("SELECT ");
        stb.append(getYearColumn());
        stb.append(" AS TheYear, ");
        stb.append("Expenditures AS TheValue FROM ");
        stb.append(getTableName());
        stb.append(" WHERE ");
        return stb.toString();
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
