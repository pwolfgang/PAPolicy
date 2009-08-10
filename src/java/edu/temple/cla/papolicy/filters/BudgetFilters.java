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
        stb.append("<input type=\"radio\" name=\"disp\" value=\"0\" checked=\"checked\" />\n"+
"                Display Dolar Values\n"+
"                <br />&nbsp;&nbsp;<input type=\"radio\" name=\"disp\" value=\"1\" />\n"+
"                Display Percent of Total Spending\n"+
"                <br />&nbsp;&nbsp;<input type=\"radio\" name=\"disp\" value=\"2\" />\n"+
"                Display Percent Change\n"+
"                <br />\n"+
"                <br />&nbsp;&nbsp;<input type=\"radio\" name=\"adjust\" value=\"0\" />\n"+
"                Un-adjusted Dollars\n"+
"                <br/>&nbsp;&nbsp;<input type=\"radio\" name=\"adjust\" value=\"1\" checked=\"checked\" />\n"+
"                Inflation-adjusted Dollars Base Year\n"+
"                <select name=\"baseYear\">\n"+
"        ");
        for (Deflator item : items) {
            stb.append("<option value=\""+item.getYear()+"\"");
            if (item.getYear() != 2000) {
                stb.append(">");
            } else {
                stb.append("selected=\"selected\">");
            }
            stb.append(item.getYear());
            stb.append("</option>");
        }
        stb.append("</select>");
        return stb.toString();
    }


    public void setFilterParameterValues(HttpServletRequest request) {
        dispParameterValue = request.getParameter(dispParameterName);
        adjustParameterValue = request.getParameter(adjustParameterName);
        baseYearParameterValue = request.getParameter(baseYearParameterName);
    }

    public String getFilterQueryString() {
        return null;
    }

    public String getFilterQualifier() {
        return null;
    }

    public String getDisp() { return dispParameterValue;}
    public String getAdjust() {return adjustParameterValue;}
    public String getBaseYear() {return baseYearParameterValue;}

}
