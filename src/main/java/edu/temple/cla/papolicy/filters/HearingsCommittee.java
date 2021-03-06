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

import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.RowMapper;

/**
 * Filter to select the committee(s) that held hearings in the Senate hearing dataset.
 * @author Paul Wolfgang
 */
public class HearingsCommittee extends Filter {

    /**
     * Construct a HouseHearingsCommittee object
     * @param id The unique id
     * @param tableId The table ID
     * @param description The description
     * @param columnName Null -- not used by this class
     * @param tableReference Reference to the CommitteeAliases table
     * @param additionalParam House or Senate
     */
    public HearingsCommittee(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
    }

    /**
     * The HearingsCommittee filter is a dropdown selection of all of the
     * chamber specific committee names.
     * @return HTML to generate the dropdown form input.
     */
    @Override
    public String getFilterFormInput() {
        RowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
        String chamberNumber;
        if (getAdditionalParam().equals("House")) {
            chamberNumber = "1";
        } else {
            chamberNumber = "2";
        }
        String query = "SELECT * FROM "+getTableReference()+" WHERE CtyCode LIKE('"+chamberNumber+"%%') ORDER BY Name";
        List<CommitteeAlias> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<label for=\"F").append(getId()).append("\">")
                .append(getAdditionalParam())
                .append(" Hearings</label>\n                <br /><select name=\"F")
                .append(getId()).append("\" id=\"F").append(getId())
                .append("\">\n                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n");
        items.forEach(item -> {
            stb.append("<option value=\"").append(item.getCtyCode())
                    .append("\">").append(item.getName()).append("</option>\n");
        });
        stb.append("</select><br/>");
        return stb.toString();
    }

    /**
     * This filter is currently not implemented
     * @param request HttpServletRequest from form submittal
     */
    public void setFilterParameterValues(HttpServletRequest request) {

    }

    /**
     * This filter is currently not implemented
     * @return the empty string.
     */
    public String getFilterQualifier() {
        return "";
    }
}
