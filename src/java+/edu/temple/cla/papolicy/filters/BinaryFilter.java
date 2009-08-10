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
public class BinaryFilter extends Filter {

    private String parameterName;
    private String parameterValue;
    private static final String BOTH = "587";

    private String filterQueryString;
    private String filterQualifier;

    public BinaryFilter(int id, int tableId, String description, 
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    @Override
    public String getFilterFormInput() {
        return {{
            {{getDescription()}}
            <br /><input type="radio" name="{{parameterName}}" value="{{BOTH}}" checked="checked" />&nbsp no filter
                  <input type="radio" name="{{parameterName}}" value="0" />&nbsp Exclude
                  <input type="radio" name="{{parameterName}}" value="1" />&nbsp Include}};
        }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    public String getFilterQueryString() {
        return filterQueryString;
    }

    private void buildFilterStrings() {
        if (parameterValue.equals(BOTH)) {
            filterQueryString = "";
            filterQualifier = "";
        } else if (parameterValue.equals("1")) {
            filterQueryString = getColumnName() + "<>0";
            filterQualifier = "Include " + getDescription();
        } else {
            filterQueryString = getColumnName() + "=0";
            filterQualifier = "Exclude " + getDescription();
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
