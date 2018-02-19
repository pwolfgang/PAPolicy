/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

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
        ParameterizedRowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
        String chamberNumber = null;
        if (getAdditionalParam().equals("House")) {
            chamberNumber = "1";
        } else {
            chamberNumber = "2";
        }
        String query = "SELECT * FROM "+getTableReference()+" WHERE CtyCode LIKE('"+chamberNumber+"%%') ORDER BY Name";
        List<CommitteeAlias> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append("<label for=\"F"+getId()+"\">"+getAdditionalParam()+" Hearings</label>\n"+
"                <br /><select name=\"F"+getId()+"\" id=\"F"+getId()+"\">\n"+
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n");
        for (CommitteeAlias item : items) {
            stb.append("<option value=\""+item.getCtyCode()+"\">"+item.getName()+"</option>\n");
        }
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
