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
public class WhereHeld extends Filter {

    private static final String BOTH = "587";
    private String parameterName;
    private String parameterValue;

    private String filterQualifier;

    public WhereHeld(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    @Override
    public String getFilterFormInput() {
        return ""+getDescription()+"\n"+
"        <br /><input type=\"radio\" name=\""+parameterName+"\" value=\""+BOTH+"\" checked=\"checked\" />&nbsp;Both\n"+
"              <input type=\"radio\" name=\""+parameterName+"\" value=\"0\" />&nbsp;"+getAdditionalParam()+"\n"+
"              <input type=\"radio\" name=\""+parameterName+"\" value=\"1\" />&nbsp;Outside "+getAdditionalParam()+"";
    }

public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    private void buildFilterStrings() {
        if (BOTH.equals(parameterValue)) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else if ("0".equals(parameterValue)) {
            filterQuery = new Comparison(getColumnName(), "=", "\'" + getAdditionalParam() + "\'");
            filterQualifier = "Held in " + getAdditionalParam();
        } else {
            filterQuery = new Comparison(getColumnName(), "<>", "\'" + getAdditionalParam() + "\'");
            filterQualifier = "Held outside " + getAdditionalParam();
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
