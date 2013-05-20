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
 *
 * @author Paul Wolfgang
 */
public class HouseHearingsCommittee extends Filter {

    private String ctyCode;
    private String filterQuallifierString;
    private boolean selected;

    public HouseHearingsCommittee(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
    }

    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<CommitteeName> itemMapper = new CommitteeNameMapper();
        String chamberNumber = null;
        if (getAdditionalParam().equals("House")) {
            chamberNumber = "1";
        } else {
            chamberNumber = "2";
        }
        String query = "SELECT * FROM "+getTableReference()+" WHERE CtyCode LIKE('"+chamberNumber+"%%') ORDER BY Name";
        List<CommitteeName> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append(""+getAdditionalParam()+" Hearings\n"+
"                <br />\n<select name=\"F"+getId()+"\">\"\n"+
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n");
        for (CommitteeName item : items) {
            stb.append("<option value=\""+item.getCtyCode()+"\">"+item.getName()+"</option>\n");
        }
        stb.append("</select>\n");
        return stb.toString();
    }
    
    public boolean isSelected() {
        return selected;
    }

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
    
    public String getFilterQualifier() {
        return filterQuallifierString;
    }

}
