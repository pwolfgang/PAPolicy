/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import edu.temple.cla.papolicy.dao.CommitteeAlias;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class BillsCommittee extends Filter {

    String parameterName;
    String primaryName;
    String parameterValue;
    String primaryValue;

    private String filterQueryString;
    private String filterQualifier;
    private String chamberNumber;

    public BillsCommittee(int id, int tableId, String description, 
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
        if (getAdditionalParam().equals("House")) {
            chamberNumber = "1";
            primaryName = "hprimary";
        } else {
            chamberNumber = "2";
            primaryName = "sprimary";
        }
    }

    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
        String query = "SELECT * FROM " + getTableReference() + " WHERE CtyCode LIKE(\'" +
                chamberNumber + "%%\') ORDER BY AlternateName";
        List<CommitteeAlias> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append({{ Referred to {{getAdditionalParam()}} Committee
                <input name="{{primaryName}}" value="1" type="checkbox"/> Primary Only
                <br /><select name="{{parameterName}}">
                    <option value="ALL">ALL COMMITTEES</option>
        }});
        for (CommitteeAlias item : items) {
            stb.append({{<option value="{{item.getCtyCode()}}">{{item.getAlternateName()}}</option>}});
        }
        stb.append({{</select>}});
        return stb.toString();
    }

    public String getFilterQueryString() {
        return filterQueryString;
    }

    void buildFilterStrings() {
        if (parameterValue.equals("ALL")) {
            filterQueryString = "";
            filterQualifier = "";
        } else {
            StringBuilder stb = new StringBuilder();
            stb.append("(");
            String ctyCode = parameterValue;
            stb.append("_");
            stb.append(ctyCode);
            stb.append("P<>0");
            if (!"1".equals(primaryValue)) {
                stb.append(" OR _");
                stb.append(ctyCode);
                stb.append("O<>0");
            }
            stb.append(")");
            filterQueryString = stb.toString();
            ParameterizedRowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
            String query = "SELECT * FROM " + getTableReference()
                    + " WHERE CtyCode=\'" + ctyCode + "\'";
            List<CommitteeAlias> selectedCommitteeList =
                    getJdbcTemplate().query(query, itemMapper);
            Map<Integer, CommitteeAlias> uniqueCommittees =
                    new TreeMap<Integer, CommitteeAlias>();
            for (CommitteeAlias committee : selectedCommitteeList) {
                uniqueCommittees.put(committee.getCtyCode(), committee);
            }
            stb = new StringBuilder();
            stb.append("Referred to ");
            for (CommitteeAlias committee : uniqueCommittees.values()) {
                stb.append(getAdditionalParam());
                stb.append(" ");
                stb.append(committee.getName());
                stb.append(" committee");
            }
            if ("1".equals(primaryValue)) {
                stb.append(" as primary committee");
            }
            filterQualifier = stb.toString();
        }
    }

    public String getFilterQualifier() {return filterQualifier;}

    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        primaryValue = request.getParameter(primaryName);
        buildFilterStrings();
    }

}
