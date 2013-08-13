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
 * Class to generate the filter form input for the budget related data.
 * @author Paul Wolfgang
 */
public class BudgetFilters extends Filter {

    private final String dispParameterName = "disp";
    private final String adjustParameterName = "adjust";
    private final String baseYearParameterName = "baseYear";
    private String dispParameterValue;
    private String adjustParameterValue;
    private String baseYearParameterValue;

    /**
     * Construct a BudgetFilter object
     * @param id Unique id of this filter
     * @param tableId ID of the referencing table
     * @param description NULL, hard coded in the class
     * @param columnName NULL, hard coded in the class
     * @param tableReference NULL, not used
     * @param additionalParam Reference to the Deflator table.
     */
    public BudgetFilters(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
    }
    
    /**
     * Construct the HTML to display the filter form input. The form consists
     * of radio buttons to select the display units, and a drop-down to select
     * the base year for inflation adjustment.
     * @return HTML to generate filter form input.
     */
    @Override
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


    /**
     * Method to capture the form input parameters
     * @param request HTML request object
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        dispParameterValue = request.getParameter(dispParameterName);
        adjustParameterValue = request.getParameter(adjustParameterName);
        baseYearParameterValue = request.getParameter(baseYearParameterName);
    }

    /**
     * This filter is not really a filter, thus a blank filter qualifier is returned.
     * @return An empty string.
     */
    public String getFilterQualifier() {
        return "";
    }

    /**
     * Return the display parameter that indicates the display units
     * @return The display parameter
     */
    public String getDisp() { return dispParameterValue;}
    
    /**
     * Return the adjust parameter that indicates where inflation adjustment is to be applied
     * @return The adjust parameter
     */
    public String getAdjust() {return adjustParameterValue;}
    
    /**
     * Return the base year for inflation adjustment.
     * @return The base year for inflation adjustment.
     */
    public String getBaseYear() {return baseYearParameterValue;}

}
