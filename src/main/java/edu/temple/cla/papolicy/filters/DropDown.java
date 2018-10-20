/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import edu.temple.cla.papolicy.dao.DropDownItem;
import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.RowMapper;

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
        RowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
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
            RowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
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
