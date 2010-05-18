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
public class Party extends Filter {

    private static final String BOTH = "587";
    private String parameterName;
    private String parameterValue;

    private String filterQueryString;
    private String filterQualifier;

    public Party(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    @Override
    public String getFilterFormInput() {
        return {{{{getDescription()}}
        <br /><input type="radio" name="F{{getId()}}" value="{{BOTH}}" checked="checked" />&nbsp;no filter
              <input type="radio" name="F{{getId()}}" value="0" />&nbsp;Republican
              <input type="radio" name="F{{getId()}}" value="1" />&nbsp;Democrat
              <input type="radio" name="F{{getId()}}" value="2" />&nbsp;Other}};
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
        } else if ("0".equals(parameterValue)) { // Republican
            filterQueryString = getColumnName() + "=0";
            filterQualifier = "Sponsored by a Republican";
        } else if ("1".equals(parameterValue)) { // Democrat
            filterQueryString = getColumnName() + "=1";
            filterQualifier = "Sponsored by a Democrat";
        } else if ("2".equals(parameterValue)) { // Third Party
            filterQueryString = getColumnName() + "=2";
            filterQualifier = "Sponsored by a Member of a Third Party";
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
