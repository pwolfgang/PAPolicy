/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import javax.servlet.http.HttpServletRequest;

/**
 * The PublicOpinionFilters are not actual filters.
 * They display choices for the display of the public opinion data.
 * @author Paul Wolfgang
 */
public class PublicOpinionFilters extends Filter {

    private String mipdisp;

    /**
     * Construct a Filter
     * @param id The unique ID of this filter from the database
     * @param tableId The unique ID of the referencing table
     * @param description not used
     * @param columnName not used
     * @param tableReference not used
     * @param additionalParam not used
     */
    public PublicOpinionFilters(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        filterQuery = new EmptyExpression();
    }

    /**
     * Generate the HTML to display the filter form input.
     * Displays a set of radio buttons to select either
     * Percent or Rank
     * @return HTML to display the filter form input.
     */

    @Override
    public String getFilterFormInput() {
        return "<fieldset><legend>Display as</legend>"
                + "<input type=\"radio\" id=\"mipdisp0\" name=\"mipdisp\" value=\"0\" "
                + "checked=\"checked\" /><label for=\"mipdisp0\">Percent</label>\n"
                + "<input type=\"radio\" id=\"mipdisp1\" name=\"mipdisp\" value=\"1\" />"
                + "<label for=\"mipdisp1\">Rank</label></fieldset>";
    }

    /**
     * Method to capture the form input from the HTTP request.
     * @param request 
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request){
        mipdisp = request.getParameter("mipdisp");
    }

    /**
     * FilterQualifier is not applicable to this filter.
     * @return The empty string
     */
    @Override
    public String getFilterQualifier() {
        return "";
    }

    /**
     * Return the selected choice.
     * @return the mipdisp
     */
    public String getMipdisp() {
        return mipdisp;
    }


}
