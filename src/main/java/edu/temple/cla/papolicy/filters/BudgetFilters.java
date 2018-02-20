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
                + "&nbsp;&nbsp;<input type=\"radio\" id=\"disp0\" name=\"disp\" value=\"0\" checked=\"checked\" />\n"+
"                <label for=\"disp0\">Dollar Values</label>\n"+
"                <br/>&nbsp;&nbsp;<input type=\"radio\" id=\"disp1\" name=\"disp\" value=\"1\" />\n"+
"                <label for=\"disp1\">Percent of Total Spending</label>\n"+
"                <br />&nbsp;&nbsp;<input type=\"radio\" id=\"disp2\" name=\"disp\" value=\"2\" />\n"+
"                <label for=\"disp2\">Display Percent Change</label>\n"+
"                </fieldset><br />\n"+
"                <fieldset><legend>Inflation Adjustment</legend>" +                
"                &nbsp;&nbsp;<input type=\"radio\" id=\"adjust0\" name=\"adjust\" value=\"0\" />\n"+
"                <label for=\"adjust0\">Un-adjusted Dollars</label>\n"+
"                <br/>&nbsp;&nbsp;<input type=\"radio\" id=\"adjust1\" name=\"adjust\" value=\"1\" checked=\"checked\" />\n"+
"                <label for=\"adjust1\">Inflation-adjusted</label><label for=\"baseYear\"> Dollars Base Year</label>\n"+
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
            stb.append("</option>\n");
        }
        stb.append("</select>\n</fieldset>\n");
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
