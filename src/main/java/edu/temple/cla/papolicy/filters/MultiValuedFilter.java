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
import edu.temple.cla.policydb.queryBuilder.Composite;
import edu.temple.cla.policydb.queryBuilder.Conjunction;
import edu.temple.cla.policydb.queryBuilder.Disjunction;
import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.RowMapper;

/**
 * A multivalued filter consists of a set of radio buttons
 * labeled No Filter, Include, Exclude and a series of check boxes giving
 * the choices to either include or exclude. The choices are given in a
 * table specified by the tableReference attribute.
 * @author Paul Wolfgang
 */
public class MultiValuedFilter extends Filter {

    private static final String BOTH = "587";
    private final String selectParameterName;
    private final String valuesParameterName;
    private String selectParameterValue;
    private String[] valuesParameterValues;

    private String filterQualifier;
    
    /**
     * Construct a MultiValuedFilter object
     * @param id unique ID
     * @param tableId Table containing the dataset
     * @param description Description of the filter
     * @param columnName Column containing the data to be filtered
     * @param tableReference Table containing the filter choices
     * @param additionalParam not used.
     */
    public MultiValuedFilter(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        selectParameterName = "F" + getId();
        valuesParameterName = "V" + getId();
    }

    /**
     * Construct the html to display the filter form. Filter consists of
     * radio buttons: No Filter, Exclude, Include, and a set of check boxes.
     * @return HTML to display the filter form.
     */
    @Override
    public String getFilterFormInput() {
        RowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
        String query = "SELECT ID, Description FROM " + getTableReference() +
                " ORDER BY ID";
        List<DropDownItem> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<fieldset><legend>"+getDescription()+"</legend>\n"+
"              <input type=\"radio\" id=\""+selectParameterName+"BOTH\" "
                + "name=\""+selectParameterName+"\" value=\""+BOTH+"\" checked=\"checked\" />"
                + "&nbsp;<label for=\""+selectParameterName+"BOTH\">No Filter</label>\n"+
"              <input type=\"radio\" id=\""+selectParameterName+"0\" "
                + "name=\""+selectParameterName+"\" value=\"0\" />"
                + "&nbsp;<label for=\""+selectParameterName+"0\">Exclude</label>\n"+
"              <input type=\"radio\" id=\""+selectParameterName+"1\" "
                + "name=\""+selectParameterName+"\" value=\"1\" />"
                + "&nbsp;<label for=\""+selectParameterName+"1\">Include</label>\n"+
"        ");
        for (DropDownItem item : items) {
            if (item.getID() != 99) {
                stb.append("<br/><input type=\"checkbox\" "
                        + "id=\""+valuesParameterName+item.getID()+"\" name=\""+valuesParameterName+"\" "
                        + "value=\""+item.getID()+"\" checked=\"checked\" />\n"
                        + "<label for=\""+valuesParameterName+item.getID()+"\">"
                        + item.getDescription()+"</label>");
            }
        }
        stb.append("</fieldset>");
        return stb.toString();
    }

    /**
     * Capture the values from the filter form and build the filter.
     * @param request HTTP request object with form input data.
     */
    public void setFilterParameterValues(HttpServletRequest request) {
        selectParameterValue = request.getParameter(selectParameterName);
        valuesParameterValues = request.getParameterValues(valuesParameterName);
        if (selectParameterValue != null) {
            buildFilterStrings();
        }
    }

    /**
     * Build the filter query and filter qualifier string.
     */
    private void buildFilterStrings() {
        if (BOTH.equals(selectParameterValue)) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else {
            Composite composite;
            String comparisonOperator;
            String joinOperator;
            String qualifier;
            if ("0".equals(selectParameterValue)) {
                comparisonOperator = "<>";
                joinOperator = " AND ";
                qualifier = "Exclude ";
                composite = new Conjunction();
            } else {
                comparisonOperator = "=";
                joinOperator = " OR ";
                qualifier = "Include ";
                composite = new Disjunction();
            }
            StringBuilder vstb = new StringBuilder();
            vstb.append("(");
            for (String value : valuesParameterValues) {
                composite.addTerm(new Comparison(getColumnName(), comparisonOperator, value));
                vstb.append(value);
                vstb.append(", ");
            }
            vstb.delete(vstb.length()-2, vstb.length());
            vstb.append(")");
            filterQuery = composite;
            String query = "SELECT ID, Description FROM " + getTableReference()
                    + " WHERE ID IN " + vstb;
            String queryForAll = "SELECT ID, Description FROM " + getTableReference();
            RowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
            List<DropDownItem> items = getJdbcTemplate().query(query, itemMapper);
            List<DropDownItem> allItems = getJdbcTemplate().query(queryForAll, itemMapper);
            if (items.size() == allItems.size()) {
                filterQualifier = qualifier + getDescription();
            } else {
                StringBuilder qualifierBuilder = new StringBuilder(qualifier);
                for (DropDownItem item : items) {
                    qualifierBuilder.append(item.getDescription());
                    qualifierBuilder.append(joinOperator);
                }
                qualifierBuilder.delete(qualifierBuilder.length()-joinOperator.length(), qualifierBuilder.length());
                filterQualifier = qualifierBuilder.toString();
            }
        }
    }

    /**
     * Method to return the description of this filter
     * @return Description of this filter
     */
    @Override
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
