/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class CheckBoxFilter extends Filter {

    private String parameterName;
    private String parameterValue;

    private String filterQualifier;

    public CheckBoxFilter(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    /**
     * Construct the filter form input as a set of two radio buttons: 
     * NotSelected (default) Selected
     * @return HTML to generate the form input.
     */
    @Override
    public String getFilterFormInput() {
        return "\n"+
"            <fieldset><legend>"+getDescription()+"</legend>\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"0\" value=\"0\" checked=\"checked\"/>"
                + "&nbsp; <label for=\""+parameterName+"0\">Not Selected</label>\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"1\" value=\"1\" />"
                + "&nbsp; <label for=\""+parameterName+"1\">Selected</label>\n" +
"            </fieldset>\n";
        }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    private void buildFilterStrings() {
        if ("1".equals(parameterValue)) {
            filterQuery = new Comparison(getColumnName(), "<>", "0");
            filterQualifier = getDescription();
        } else {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
