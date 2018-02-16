/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import javax.servlet.http.HttpServletRequest;

/**
 * The WhereHeld filter is a special case of the BinaryFilter. It displays a set
 * of radio buttons BOTH, &lt;city&gt;, Outside &lt;city&gt. The &lt;city&gt; is
 * set to "Harrisburg" by the additionalParameter from the Filters database row.
 * This allows this to be used in other states.
 *
 * @author Paul Wolfgang
 */
public class WhereHeld extends Filter {

    private static final String BOTH = "587";
    private String parameterName;
    private String parameterValue;
    private String filterQualifier;

    /**
     * Construct a Filter
     *
     * @param id The unique ID of this filter from the database
     * @param tableId The unique ID of the referencing table
     * @param description The description displayed on the form
     * @param columnName The column of the database which this filter is based
     * on.
     * @param tableReference not used.
     * @param additionalParam Specifies the capital city.
     */
    public WhereHeld(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    /**
     * Generate the HTML to display the filter form input. It displays a set of
     * radio buttons BOTH, &lt;city&gt;, Outside &lt;city&gt.
     *
     * @return HTML to display the filter form input.
     */
    @Override
    public String getFilterFormInput() {
        return "       <fieldset><legend>" + getDescription() + "</legend>\n"
                + "              <input type=\"radio\" name=\"" + parameterName + "\" value=\"" + BOTH + "\" id=\"" + parameterName + "B\" checked=\"checked\" />"
                + "<label for=\"" + parameterName + "B\">Both</label>\n"
                + "              <input type=\"radio\" name=\"" + parameterName + "\" value=\"0\" id=\"" + parameterName + "0\" />"
                + "<label for=\"" + parameterName + "0\">" + getAdditionalParam() + "</label>\n"
                + "              <input type=\"radio\" name=\"" + parameterName + "\" value=\"1\" id=\"" + parameterName + "1\" />"
                + "<label for=\"" + parameterName + "1\">Outside " + getAdditionalParam() + "</label>"
                + "</fieldset>";
    }

    /**
     * Method to capture the form input from the HTTP request.
     * @param request
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    /**
     * Method to build the filterQuery and filterQualifier based on the
     * filter form input.
     */
    private void buildFilterStrings() {
        if (BOTH.equals(parameterValue)) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else if ("0".equals(parameterValue)) {
            filterQuery = new Comparison(getColumnName(), "=", "\'" 
                    + getAdditionalParam() + "\'");
            filterQualifier = "Held in " + getAdditionalParam();
        } else {
            filterQuery = new Comparison(getColumnName(), "<>", "\'" 
                    + getAdditionalParam() + "\'");
            filterQualifier = "Held outside " + getAdditionalParam();
        }
    }

    /**
     * Return the string that describes the filtered data
     * @return The string that describes the filtered data
     */
    @Override
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
