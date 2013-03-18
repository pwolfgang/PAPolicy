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
 *
 * @author Paul Wolfgang
 */
public class Mention extends Filter {

    private static final String BOTH = "587";
    private String parameterName;
    private String parameterValue;

    private String filterQualifier;

    public Mention(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    @Override
    public String getFilterFormInput() {
        return ""+getDescription()+"\n"+
"        <br /><input type=\"radio\" name=\"F"+getId()+"\" value=\""+BOTH+"\" checked=\"checked\" />&nbsp;no filter\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"0\" />&nbsp;No Mention\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"2\" />&nbsp;Mention\n";
        
    }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

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

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
