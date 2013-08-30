/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import edu.temple.cla.papolicy.queryBuilder.Expression;
import javax.servlet.http.HttpServletRequest;

/**
 * The mention filter is similar to the binary filter.  Originally three options
 * were available: no mention, mention, and significant mention, but the
 * significant mention option has been eliminated. However, some data is still
 * coded for significant mention. Therefore, the separate filter class is
 * still needed.
 * @author Paul Wolfgang
 */
public class Mention extends Filter {

    private static final String BOTH = "587";
    private String parameterName;
    private String parameterValue;

    private String filterQualifier;

    /**
     * Construct a BinaryFilter object
     * @param id The unique id
     * @param tableId The id of the referencing table
     * @param description The description of this filter
     * @param columnName The column name to base the filter on.
     * @param tableReference Not used, NULL
     * @param additionalParam Not used, NULL
     */
    public Mention(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    /**
     * Construct the filter form input as a set of three radio buttons: 
     * No Filter, No Mention, Mention.
     * @return HTML to generate the form input.
     */
    @Override
    public String getFilterFormInput() {
        return 
"            <fieldset><legend>"+getDescription()+"</legend>\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\""+BOTH+"\" id=\"F"+getId()+"B\" checked=\"checked\" />"
                + "<label for=\"F"+getId()+"B\">No Filter</label>\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"0\" id=\"F"+getId()+"0\" />"
                + "<label for=\"F"+getId()+"0\">No Mention</label>\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"2\" id=\"F"+getId()+"2\" />"
                + "<label for=\"F"+getId()+"2\">Mention</label>\n"
                + "</fieldset>\n";
        
    }

    /**
     * Method to capture the form input
     * @param request HTTP request object
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    /**
     * Method to build the filter expression and the filter qualifier string.
     */
    private void buildFilterStrings() {
        if (BOTH.equals(parameterValue)) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else if ("0".equals(parameterValue)) { // No Mention
            filterQuery = new Comparison(getColumnName(), "=", "0");
            filterQualifier = "No Mention of " + getDescription();
        } else { // Mention includes signficant mention
            filterQuery = new Comparison(getColumnName(), "<>", "0");
            filterQualifier = "Mention of " + getDescription();
        }
    }

    /**
     * Method to return the description of this filter
     * @return Description of this filter
     */
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
