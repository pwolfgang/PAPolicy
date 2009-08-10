/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import edu.temple.cla.papolicy.dao.DropDownItem;
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

    private String filterQueryString;
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
        stb.append({{{{getDescription()}}
        <br /><input type="radio" name="{{selectParameterName}}" value="{{BOTH}}" checked="checked" />&nbsp;no filter
              <input type="radio" name="{{selectParameterName}}" value="0" />&nbsp;Exclude
              <input type="radio" name="{{selectParameterName}}" value="1" />&nbsp;Include
        <dl>}});
        for (DropDownItem item : items) {
            if (item.getID() != 99) {
                stb.append({{<dd><input type="checkbox" name="{{valuesParameterName}}" value="{{item.getID()}}" checked="checked" />
                {{item.getDescription()}}</dd>}});
            }
        }
        stb.append({{</dl>}});
        return stb.toString();
    }

    public void setFilterParameterValues(HttpServletRequest request) {
        selectParameterValue = request.getParameter(selectParameterName);
        valuesParameterValues = request.getParameterValues(valuesParameterName);
        buildFilterStrings();
    }

    public String getFilterQueryString() {
        return filterQueryString;
    }

    private void buildFilterStrings() {
        if (BOTH.equals(selectParameterValue)) {
            filterQueryString = "";
            filterQualifier = "";
        } else {
            String comparisonOperator;
            String joinOperator;
            String qualifier;
            if ("0".equals(selectParameterValue)) {
                comparisonOperator = "<>";
                joinOperator = " AND ";
                qualifier = "Exclude ";
            } else {
                comparisonOperator = "=";
                joinOperator = " OR ";
                qualifier = "Include ";
            }
            StringBuilder qstb = new StringBuilder();
            StringBuilder vstb = new StringBuilder();
            qstb.append("(");
            vstb.append("(");
            for (String value : valuesParameterValues) {
                qstb.append(getColumnName());
                qstb.append(comparisonOperator);
                qstb.append(value);
                vstb.append(value);
                qstb.append(joinOperator);
                vstb.append(", ");
            }
            qstb.delete(qstb.length()-joinOperator.length(), qstb.length());
            vstb.delete(vstb.length()-2, vstb.length());
            qstb.append(")");
            vstb.append(")");
            filterQueryString = qstb.toString();
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
                qualifierBuilder.delete(qualifierBuilder.length()-4, qualifierBuilder.length());
                filterQualifier = qualifierBuilder.toString();
            }
        }
    }

    public String getFilterQualifier() {
        return filterQualifier;
    }
}
