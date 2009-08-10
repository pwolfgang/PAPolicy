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
public class CheckBoxFilter extends Filter {

    private String parameterName;
    private String parameterValue;

    private String filterQueryString;
    private String filterQualifier;

    public CheckBoxFilter(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    @Override
    public String getFilterFormInput() {
        return "<input type=\"checkbox\" name=\""+parameterName+"\" value=\"1\" /> "+getDescription()+"";
    }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    public String getFilterQueryString() {
        return filterQueryString;
    }

    private void buildFilterStrings() {
        if ("1".equals(parameterValue)) {
            filterQueryString = getColumnName() + "<>0";
            filterQualifier = getDescription();
        } else {
            filterQueryString = "";
            filterQualifier = "";
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
