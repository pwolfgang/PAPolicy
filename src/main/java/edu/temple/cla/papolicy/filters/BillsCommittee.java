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

import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.Disjunction;
import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import edu.temple.cla.policydb.queryBuilder.Expression;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.RowMapper;

/**
 * Filter to select the committee(s) that held hearings on a bill.
 * @author Paul Wolfgang
 */
public class BillsCommittee extends Filter {

    private final String parameterName;
    private final String primaryName;
    private String parameterValue;
    private String primaryValue;
    private String filterQualifier;
    private final String chamberNumber;

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
        RowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
        String query = "SELECT * FROM " + getTableReference() + " WHERE CtyCode LIKE(\'" +
                chamberNumber + "%%\') ORDER BY AlternateName";
        List<CommitteeAlias> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<fieldset><legend>Referred to ").append(getAdditionalParam()).append(" Committee</legend>\n")
                .append("<input name=\"").append(primaryName).append("\" value=\"1\" ")
                .append("id=\"").append(primaryName).append("\" ")
                .append("type=\"checkbox\"/><label for=\"").append(primaryName).append("\"> Primary Only</label>\n")
                .append("<br/><label for=\"").append(parameterName).append("\">Name</label>")
                .append("<select name=\"").append(parameterName).append("\" ")
                .append("id=\"").append(parameterName).append("\">")
                .append("<option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n");
        items.forEach(item -> {
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
        });
        stb.append("</select>");
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
            StringBuilder stb;
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
            RowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
            String query = "SELECT * FROM " + getTableReference()
                    + " WHERE CtyCode=\'" + ctyCode + "\'";
            List<CommitteeAlias> selectedCommitteeList =
                    getJdbcTemplate().query(query, itemMapper);
            Map<Integer, CommitteeAlias> uniqueCommittees = new TreeMap<>();
            selectedCommitteeList.forEach(committee -> {
                uniqueCommittees.put(committee.getCtyCode(), committee);
            });
            stb = new StringBuilder();
            stb.append("Referred to ");
            uniqueCommittees.values().forEach(committee -> {
                stb.append(getAdditionalParam());
                stb.append(" ");
                stb.append(committee.getName());
                stb.append(" committee");
            });
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
