/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import edu.temple.cla.papolicy.dao.DropDownItem;
import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.Composite;
import edu.temple.cla.papolicy.queryBuilder.Conjunction;
import edu.temple.cla.papolicy.queryBuilder.Disjunction;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class MultiValuedFilter extends Filter {

    private static final String BOTH = "587";
    private String selectParameterName;
    private String valuesParameterName;
    private String selectParameterValue;
    private String[] valuesParameterValues;

    private String filterQualifier;
    
    public MultiValuedFilter(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        selectParameterName = "F" + getId();
        valuesParameterName = "V" + getId();
    }

    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
        String query = "SELECT ID, Description FROM " + getTableReference() +
                " ORDER BY ID";
        List<DropDownItem> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<fieldset><legend>"+getDescription()+"</legend>\n"+
"              <label><input type=\"radio\" name=\""+selectParameterName+"\" value=\""+BOTH+"\" checked=\"checked\" />&nbsp;no filter</label>\n"+
"              <label><input type=\"radio\" name=\""+selectParameterName+"\" value=\"0\" />&nbsp;Exclude</label>\n"+
"              <label><input type=\"radio\" name=\""+selectParameterName+"\" value=\"1\" />&nbsp;Include</label>\n"+
"        <dl>");
        for (DropDownItem item : items) {
            if (item.getID() != 99) {
                stb.append("<dd><label><input type=\"checkbox\" name=\""+valuesParameterName+"\" value=\""+item.getID()+"\" checked=\"checked\" />\n"+
"                "+item.getDescription()+"</label></dd>");
            }
        }
        stb.append("</dl></fieldset>");
        return stb.toString();
    }

    public void setFilterParameterValues(HttpServletRequest request) {
        selectParameterValue = request.getParameter(selectParameterName);
        valuesParameterValues = request.getParameterValues(valuesParameterName);
        if (selectParameterValue != null) {
            buildFilterStrings();
        }
    }

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
            ParameterizedRowMapper<DropDownItem> itemMapper = new DropDownItemMapper();
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

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
