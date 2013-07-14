/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DeflatorMapper;
import edu.temple.cla.papolicy.dao.Deflator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class BudgetFilters extends Filter {

    private String dispParameterName = "disp";
    private String adjustParameterName = "adjust";
    private String baseYearParameterName = "baseYear";
    private String dispParameterValue;
    private String adjustParameterValue;
    private String baseYearParameterValue;

    public BudgetFilters(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
    }
    
    public String getFilterFormInput() {
        String query = "SELECT * FROM " + getAdditionalParam() + " ORDER BY Year";
        ParameterizedRowMapper<Deflator> itemMapper = new DeflatorMapper();
        List<Deflator> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<fieldset><legend>Display</legend>"
                + "&nbsp;&nbsp;<label><input type=\"radio\" name=\"disp\" value=\"0\" checked=\"checked\" />\n"+
"                Dollar Values</label>\n"+
"                <br/>&nbsp;&nbsp;<label><input type=\"radio\" name=\"disp\" value=\"1\" />\n"+
"                Percent of Total Spending</label>\n"+
"                <br />&nbsp;&nbsp;<label><input type=\"radio\" name=\"disp\" value=\"2\" />\n"+
"                Display Percent Change</label>\n"+
"                </fieldset><br />\n"+
"                <fieldset><legend>Inflation Adjustment</legend>" +                
"                &nbsp;&nbsp;<label><input type=\"radio\" name=\"adjust\" value=\"0\" />\n"+
"                Un-adjusted Dollars</label>\n"+
"                <br/>&nbsp;&nbsp;<label><input type=\"radio\" name=\"adjust\" value=\"1\" checked=\"checked\" />\n"+
"                Inflation-adjusted</label><label for=\"baseYear\"> Dollars Base Year</label>\n"+
"                <select id=\"baseYear\" name=\"baseYear\">\n"+
"        ");
        for (Deflator item : items) {
            stb.append("<option value=\""+item.getYear()+"\"");
            if (item.getYear() != 2000) {
                stb.append(">");
            } else {
                stb.append(" selected=\"selected\">");
            }
            stb.append(item.getYear());
            stb.append("</option>");
        }
        stb.append("</select></fieldset>");
        return stb.toString();
    }


    public void setFilterParameterValues(HttpServletRequest request) {
        dispParameterValue = request.getParameter(dispParameterName);
        adjustParameterValue = request.getParameter(adjustParameterName);
        baseYearParameterValue = request.getParameter(baseYearParameterName);
    }

    public String getFilterQualifier() {
        return "";
    }

    public String getDisp() { return dispParameterValue;}
    public String getAdjust() {return adjustParameterValue;}
    public String getBaseYear() {return baseYearParameterValue;}

}
