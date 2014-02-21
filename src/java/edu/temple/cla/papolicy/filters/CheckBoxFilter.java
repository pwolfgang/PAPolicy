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

    @Override
    public String getFilterFormInput() {
        return "<input type=\"checkbox\" id=\""+parameterName+"\" name=\""+parameterName+"\" value=\"1\" /><lable>"+getDescription()+"</label>\n";
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
