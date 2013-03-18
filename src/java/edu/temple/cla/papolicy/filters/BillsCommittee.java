/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.Disjunction;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
import edu.temple.cla.papolicy.queryBuilder.Expression;
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

    private String parameterName;
    private String primaryName;
    private String parameterValue;
    private String primaryValue;
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
        stb.append(" Referred to ").append(getAdditionalParam()).append(" Committee\n")
                .append("<input name=\"").append(primaryName).append("\" value=\"1\" type=\"checkbox\"/> Primary Only\n")
                .append("<br /><select name=\"").append(parameterName).append("\">\n")
                .append("<option value=\"ALL\">ALL COMMITTEES</option>\n");
        for (CommitteeAlias item : items) {
            if (item.getCtyCode() % 100 != 99) { // Exclude special committees
                String committeeName = item.getAlternateName();
                if (!committeeName.startsWith(getAdditionalParam())) {
                    stb.append("<option value=\"")
                            .append(item.getCtyCode())
                            .append("\">")
                            .append(item.getAlternateName())
                            .append("</option>");
                }
            }
        }
        stb.append("</select>");
        return stb.toString();
    }

    void buildFilterStrings() {
        if (parameterValue.equals("ALL")) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else {
            StringBuilder stb = new StringBuilder();
            String ctyCode = parameterValue;
            Expression primary = new Comparison("_" + ctyCode + "P", "<>", "0");
            if ("1".equals(primaryValue)) {
                filterQuery = primary;
            } else {
                Disjunction disjunction = new Disjunction();
                disjunction.addTerm(primary);
                disjunction.addTerm(new Comparison("_" + ctyCode + "O", "<>", "0"));
                filterQuery = disjunction;
            }
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
        if (parameterValue != null) {
            buildFilterStrings();
        }
    }

}
