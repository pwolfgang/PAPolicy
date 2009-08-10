/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class Mention extends Filter {

    private static final String BOTH = "587";
    private String parameterName;
    private String parameterValue;

    private String filterQueryString;
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
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"2\" />&nbsp;Mention\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"1\" />&nbsp;Significant Mention";
    }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    public String getFilterQueryString() {
        return filterQueryString;
    }

    private void buildFilterStrings() {
        if (BOTH.equals(parameterValue)) {
            filterQueryString = "";
            filterQualifier = "";
        } else if ("0".equals(parameterValue)) { // No Mention
            filterQueryString = getColumnName() + "=0";
            filterQualifier = "No Mention of " + getDescription();
        } else if ("1".equals(parameterValue)) { // Significant Mention
            filterQueryString = getColumnName() + "=1";
            filterQualifier = "Significant Mention of " + getDescription();
        } else { // Mention includes signficant mention
            filterQueryString = getColumnName() + "<>0";
            filterQualifier = "Mention of " + getDescription();
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
