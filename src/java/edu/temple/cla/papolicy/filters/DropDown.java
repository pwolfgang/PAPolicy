/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import edu.temple.cla.papolicy.dao.DropDownItem;
import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Filter that consists of several options displayed in a dropdown form.
 * @author Paul Wolfgang
 */
public class DropDown extends Filter {

    private final String parameterName;
    private String parameterValue;

    private String filterQualifier;

    public DropDown(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }


    /**
     * Method to generate the HTML used to display the filter form
     * @return filter form HTML code
     */
    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
        String query = "SELECT ID, Description FROM " + getTableReference() +
                " ORDER BY ID";
        List<DropDownItem> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<label for=\""+parameterName+"\"><br/>"+getDescription()+"</label>");
        stb.append("<br /><select name=\""+parameterName+"\" id=\""+parameterName+"\">\n"+
"                <option value=\"-1\" selected=\"selected\">No Filter</option>\n");
        for (DropDownItem item : items) {
            stb.append("<option value=\""+item.getID()+"\">"+item.getDescription()+"</option>");
        }
        stb.append("</select>");
        return stb.toString();
    }
    
    /**
     * Method to get the parameter values and build the filter strings
     * @param request Request object.
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    /**
     * Get the filterQueryString
     * @return filterQueryString
     */
    public String getFilterQueryString() {
        return filterQuery.toString();
    }

    /**
     * Method to build the filter expression and qualifier string
     */
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
    
    /** Get the filterQualifier
     * 
     * @return filterQualifier 
     */
    @Override
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
