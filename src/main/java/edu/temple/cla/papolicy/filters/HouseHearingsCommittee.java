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

import edu.temple.cla.papolicy.dao.CommitteeName;
import edu.temple.cla.papolicy.dao.CommitteeNameMapper;
import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Filter to select the committee(s) that held hearings in the House Hearings Dataset
 * @author Paul Wolfgang
 */
public class HouseHearingsCommittee extends Filter {

    private String ctyCode;
    private String filterQuallifierString;
    private boolean selected;

    /**
     * Construct a HouseHearingsCommittee object
     * @param id The unique id
     * @param tableId The table ID
     * @param description The description
     * @param columnName Null -- not used by this class
     * @param tableReference Reference to the CommitteeAliases table
     * @param additionalParam House or Senate
     */
    public HouseHearingsCommittee(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
    }

    /**
     * The HouseHearingsCommittee filter is a dropdown selection of all of the
     * chamber specific committee names.
     * @return HTML to generate the dropdown form input.
     */
    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<CommitteeName> itemMapper = new CommitteeNameMapper();
        String chamberNumber;
        if (getAdditionalParam().equals("House")) {
            chamberNumber = "1";
        } else {
            chamberNumber = "2";
        }
        String query = "SELECT * FROM "+getTableReference()+" WHERE CtyCode LIKE('"+chamberNumber+"%%') ORDER BY Name";
        List<CommitteeName> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<label for=\"F")
                .append(getId()).append("\">")
                .append(getAdditionalParam())
                .append(" Hearings</label>\n")
                .append("                <br/>\n<select name=\"F")
                .append(getId()).append("\" id=\"F")
                .append(getId())
                .append("\">\n")
                .append("                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n");
        for (CommitteeName item : items) {
            stb.append("<option value=\"")
                    .append(item.getCtyCode())
                    .append("\">")
                    .append(item.getName())
                    .append("</option>\n");
        }
        stb.append("</select><br/>\n");
        return stb.toString();
    }
    
    /**
     * Indicate that this filter is selected. This affects the select part of the
     * query of the HouseHearings data.
     * @return true, if this filter is selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set the filter parameter values from the HTTP request and set the
     * filter query and qualifier string.  The qualifier string is the
     * final committee name, even though an earlier name may have been
     * selected from the drop-down.
     * @param request The HTTP request object.
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        ctyCode = request.getParameter("F"+getId());
        if (ctyCode != null && !ctyCode.equals("ALL")) {
            filterQuery = new Comparison("CtyCode", "=", ctyCode);
            selected = true;
        } else {
            filterQuery = new EmptyExpression();
            selected = false;
        }
        setFilterQualifier();
    }

    /**
     * Set the filter qualifier string.  The qualifier string is the
     * final committee name, even though an earlier name may have been
     * selected from the drop-down.
     */
    public void setFilterQualifier() {
        if (ctyCode != null && !ctyCode.equals("ALL")) {
            ParameterizedRowMapper<CommitteeName> itemMapper = new CommitteeNameMapper();
            String query = "SELECT * FROM "+getTableReference()+" WHERE CtyCode=" + ctyCode +";";
            List<CommitteeName> items = getJdbcTemplate().query(query, itemMapper);
            if (!items.isEmpty()) {
                filterQuallifierString = ("Committee: " + items.get(0).getName());
            }
        } else {
            filterQuallifierString = "";
        }
    }
    
    /**
     * Return a string that describes the selected filter.
     * @return A string that describes the selected filter.
     */
    @Override
    public String getFilterQualifier() {
        return filterQuallifierString;
    }

}
