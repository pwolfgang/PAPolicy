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
 * Filter to select the committee(s) that held hearings on a bill.
 * @author Paul Wolfgang
 */
public class BillsCommittee extends Filter {

    private String parameterName;
    private String primaryName;
    private String parameterValue;
    private String primaryValue;
    private String filterQualifier;
    private String chamberNumber;

    /**
     * Construct a BillsCommittee object
     * @param id The unique id
     * @param tableId The table ID
     * @param description The description
     * @param columnName Null -- not used by this class
     * @param tableReference Reference to the CommitteeAliases table
     * @param additionalParam House or Senate
     */
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

    /**
     * The BillsCommittee filter is a dropdown selection of all of the
     * chamber specific committee names.
     * @return HTML to generate the dropdown form input.
     */
    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
        String query = "SELECT * FROM " + getTableReference() + " WHERE CtyCode LIKE(\'" +
                chamberNumber + "%%\') ORDER BY AlternateName";
        List<CommitteeAlias> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<fieldset><legend>Referred to ").append(getAdditionalParam()).append(" Committee</legend>\n")
                .append("<label><input name=\"").append(primaryName).append("\" value=\"1\" type=\"checkbox\"/> Primary Only</label>\n")
                .append("<br/><label><select name=\"").append(parameterName).append("\">Name\n")
                .append("<option value=\"ALL\">ALL COMMITTEES</option>\n");
        for (CommitteeAlias item : items) {
            if (item.getCtyCode() % 100 != 99) { // Exclude special committees
                String committeeName = item.getAlternateName();
                if (!committeeName.startsWith(getAdditionalParam())) {
                    stb.append("<option value=\"")
                            .append(item.getCtyCode())
                            .append("\">")
                            .append(item.getAlternateName())
                            .append("</option>\n");
                }
            }
        }
        stb.append("</select></label>");
        stb.append("</fieldset>");
        return stb.toString();
    }

    /**
     * Method to build the filter query. If ALL is selected, then there is not
     * filter. If the primary checkbox is selected, then only bills to which
     * the selected committee is the first committee the bill is referred to is
     * selected, otherwise first (primary) and other are selected.
     */
    private void buildFilterQuery() {
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

    /**
     * Return a string that describes the selected filter.
     * @return A string that describes the selected filter.
     */
    @Override
    public String getFilterQualifier() {return filterQualifier;}

    /**
     * Set the filter parameter values from the HTTP request
     * @param request The HTTP request object.
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        primaryValue = request.getParameter(primaryName);
        if (parameterValue != null) {
            buildFilterQuery();
        }
    }

}
