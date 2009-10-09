/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import edu.temple.cla.papolicy.dao.CommitteeAlias;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class HearingsCommittee extends Filter {

    public HearingsCommittee(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
    }

    @Override
    public String getFilterFormInput() {
        ParameterizedRowMapper<CommitteeAlias> itemMapper = new CommitteeAliasMapper();
        String chamberNumber = null;
        if (getAdditionalParam().equals("House")) {
            chamberNumber = "1";
        } else {
            chamberNumber = "2";
        }
        String query = {{SELECT * FROM {{getTableReference()}} WHERE CtyCode LIKE('{{chamberNumber}}%%') ORDER BY AlternateName}};
        List<CommitteeAlias> items = getJdbcTemplate().query(query, itemMapper);
        StringBuilder stb = new StringBuilder();
        stb.append({{{{getAdditionalParam()}} Hearings
                <br /><select name="F{{getId()}}>"
                <option value="ALL" selected="selected">ALL COMMITTEES</option>}});
        for (CommitteeAlias item : items) {
            stb.append({{<option value="{{item.getCtyCode()}}">{{item.getAlternateName()}}</option>}});
        }
        stb.append({{</select>}});
        return stb.toString();
    }

    public void setFilterParameterValues(HttpServletRequest request) {

    }

    public String getFilterQueryString() {
        return "";
    }

    public String getFilterQualifier() {
        return "";
    }
}
