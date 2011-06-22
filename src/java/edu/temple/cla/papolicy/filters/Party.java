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
public class Party extends Filter implements Cloneable {

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
        return ""+getDescription()+"\n"+
"        <br /><input type=\"radio\" name=\"F"+getId()+"\" value=\"NOFILTER\" checked=\"checked\" />&nbsp;no filter\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"0\" />&nbsp;Republican\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"1\" />&nbsp;Democrat\n"+
"              <input type=\"radio\" name=\"F"+getId()+"\" value=\"ALL\" />&nbsp;Both\n";                
    }

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    public String getFilterQueryString() {
        return filterQueryString;
    }

    private void buildFilterStrings() {
        if ("NOFILTER".equals(parameterValue)) {
            filterQueryString = "";
            filterQualifier = "";
        } else if ("0".equals(parameterValue)) { // Republican
            filterQueryString = getColumnName() + "=0";
            filterQualifier = "Sponsored by a Republican";
        } else if ("1".equals(parameterValue)) { // Democrat
            filterQueryString = getColumnName() + "=1";
            filterQualifier = "Sponsored by a Democrat";
        }
    }
    
    @Override
    public Party[] getFilterChoices() {
        if ("ALL".equals(parameterValue)) {
            Party[] result = new Party[2];
            result[0] = clone();
            result[1] = clone();
            result[0].parameterValue = "0";
            result[0].buildFilterStrings();
            result[1].parameterValue = "1";
            result[1].buildFilterStrings();
            return result;
        } else {
            return new Party[]{this};
        }
    }
    
    public int getNumberOfFilterChoices() {
        if ("ALL".equals(parameterValue)) {
            return 2;
        } else {
            return 1;
        }
    }
    
    @Override
    public Party clone() {
        try {
            return (Party) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("CloneNotSupportedException should never be thrown");
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
