/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import edu.temple.cla.papolicy.dao.DropDownItem;
import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import edu.temple.cla.papolicy.queryBuilder.Expression;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class DropDown extends Filter {

    private String parameterName;
    private String parameterValue;

    private Expression filterQuery;
    private String filterQualifier;

    public DropDown(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }


    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
        String query = "SELECT ID, Description FROM " + getTableReference() +
                " ORDER BY ID";
        List<DropDownItem> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append(getDescription());
        stb.append("<br /><select name=\""+parameterName+"\">\n"+
"                <option value=\"-1\" selected=\"selected\">no filter</option>");
        for (DropDownItem item : items) {
            stb.append("<option value=\""+item.getID()+"\">"+item.getDescription()+"</option>");
        }
        stb.append("</select>");
        return stb.toString();
    }
    
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    public String getFilterQueryString() {
        return filterQuery.toString();
    }

    private void buildFilterStrings() {
        if ("-1".equals(parameterValue)){
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else {
            filterQuery = new Comparison(getColumnName(), "=", "\'" + parameterValue + "\'");
            String query = "SELECT ID, Description FROM " + getTableReference()
                + " WHERE ID=" + parameterValue;
            ParameterizedRowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
            List<DropDownItem> itemList = getJdbcTemplate().query(query, itemMapper);
            if (itemList.size() == 1) {
                filterQualifier = getDescription() + " is " + itemList.get(0).getDescription();
            } else {
                filterQualifier = "Undefined";
            }
        }
    }
    
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
