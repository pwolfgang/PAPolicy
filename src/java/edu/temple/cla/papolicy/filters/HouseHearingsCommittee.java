/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeName;
import edu.temple.cla.papolicy.dao.CommitteeNameMapper;
import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.EmptyExpression;
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
