/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class PublicOpinionFilters extends Filter {

    private String mipdisp;

    public PublicOpinionFilters(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        filterQuery = new EmptyExpression();
    }

    @Override
    public String getFilterFormInput() {
        return "<fieldset><legend>Display as</legend>"
                + "<label><input type=\"radio\" name=\"mipdisp\" value=\"0\" checked=\"checked\" />Percent</label>\n"+
"                <label><input type=\"radio\" name=\"mipdisp\" value=\"1\" />Rank</label></fieldset>";
    }

    @Override
    public void setFilterParameterValues(HttpServletRequest request){
        mipdisp = request.getParameter("mipdisp");
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
