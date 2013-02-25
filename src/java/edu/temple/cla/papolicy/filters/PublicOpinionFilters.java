/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import edu.temple.cla.papolicy.queryBuilder.Expression;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class PublicOpinionFilters extends Filter {

    private String mipdisp;
    private Expression filterQuery;

    public PublicOpinionFilters(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        filterQuery = new EmptyExpression();
    }

    @Override
    public String getFilterFormInput() {
        return "Display as <input type=\"radio\" name=\"mipdisp\" value=\"0\" checked=\"checked\" />Percent\n"+
"                <input type=\"radio\" name=\"mipdisp\" value=\"1\" />Rank";
    }

    @Override
    public void setFilterParameterValues(HttpServletRequest request){
        mipdisp = request.getParameter("mipdisp");
    }

    @Override
    public String getFilterQueryString() {
        return filterQuery.toString();
    }

    @Override
    public String getFilterQualifier() {
        return "";
    }

    /**
     * @return the mipdisp
     */
    public String getMipdisp() {
        return mipdisp;
    }


}
