/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItem;
import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul
 */
public class MultiValuedFilterTest {
    
    private MultiValuedFilter filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;
    
    private DropDownItem[] conAmmendTable = new DropDownItem[] {
        new DropDownItem(0, "Not Passed by Legislature"),
        new DropDownItem(1, "Passed Once by Legislature"),
        new DropDownItem(2, "Passed Twice in Consecutive Sessions"),
        new DropDownItem(3, "Approved by the Voters"),
        new DropDownItem(4, "Defeated by the Voters"),
        new DropDownItem(5, "Partially approved by Voters")
    };
    
    public MultiValuedFilterTest() {
    }
    
    @Before
    public void setUp() {
        filter = new MultiValuedFilter(406, 3, "Constitution Ammendment", "Con_Ammend", "Con_Ammend", null);
        new Expectations() {{
            String query1 = "SELECT ID, Description FROM Con_Ammend ORDER BY ID";
            String query2 = "SELECT ID, Description FROM Con_Ammend";
            String query3 = "SELECT ID, Description FROM Con_Ammend WHERE ID IN (2)";
            String query4 = "SELECT ID, Description FROM Con_Ammend WHERE ID IN (2, 3, 4)";
            jdbcTemplate.query(query1, new DropDownItemMapper());minTimes=0;
            result = genDropDownItemResults(query1);
            jdbcTemplate.query(query2, new DropDownItemMapper());minTimes=0;
            result = genDropDownItemResults(query2);
            jdbcTemplate.query(query3, new DropDownItemMapper());minTimes=0;
            result = genDropDownItemResults(query3);
            jdbcTemplate.query(query4, new DropDownItemMapper());minTimes=0;
            result = genDropDownItemResults(query4);
        }};
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<fieldset><legend>Constitution Ammendment</legend>\n" +
"              <label><input type=\"radio\" name=\"F406\" value=\"587\" checked=\"checked\" />&nbsp;No Filter</label>\n" +
"              <label><input type=\"radio\" name=\"F406\" value=\"0\" />&nbsp;Exclude</label>\n" +
"              <label><input type=\"radio\" name=\"F406\" value=\"1\" />&nbsp;Include</label>\n" +
"        <dl><dd><label><input type=\"checkbox\" name=\"V406\" value=\"0\" checked=\"checked\" />\n" +
"                Not Passed by Legislature</label></dd>"
                + "<dd><label><input type=\"checkbox\" name=\"V406\" value=\"1\" checked=\"checked\" />\n" +
"                Passed Once by Legislature</label></dd>"
                + "<dd><label><input type=\"checkbox\" name=\"V406\" value=\"2\" checked=\"checked\" />\n" +
"                Passed Twice in Consecutive Sessions</label></dd>"
                + "<dd><label><input type=\"checkbox\" name=\"V406\" value=\"3\" checked=\"checked\" />\n" +
"                Approved by the Voters</label></dd><dd><label><input type=\"checkbox\" name=\"V406\" value=\"4\" checked=\"checked\" />\n" +
"                Defeated by the Voters</label></dd><dd><label><input type=\"checkbox\" name=\"V406\" value=\"5\" checked=\"checked\" />\n" +
"                Partially approved by Voters</label></dd></dl></fieldset>" +
"";
        String result = filter.getFilterFormInput();
        assertEquals(expected, result);
    }

    @Test
    public void testGetFilterQueryStringNoFilter() {
        setParameterValues("587", null);
        String expected = "";
        assertEquals(expected, filter.getFilterQuery().toString());
    }
    
    @Test
    public void testGetFilterQueryStringInclude2() {
        setParameterValues("1", new String[]{"2"});
        String expected = "Con_Ammend=2";
        assertEquals(expected, filter.getFilterQuery().toString());
    }
    
    @Test
    public void testGetFilterQueryStringExclude234() {
        setParameterValues("0", new String[]{"2", "3", "4"});
        String expected = "(Con_Ammend<>2 AND Con_Ammend<>3 AND Con_Ammend<>4)";
        assertEquals(expected, filter.getFilterQuery().toString());
    }

    @Test
    public void testGetFilterQueryStringInclude234() {
        setParameterValues("1", new String[]{"2", "3", "4"});
        String expected = "(Con_Ammend=2 OR Con_Ammend=3 OR Con_Ammend=4)";
        assertEquals(expected, filter.getFilterQuery().toString());
    }

    @Test
    public void testGetFilterQualifierStringNoFilter() {
        setParameterValues("587", null);
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    @Test
    public void testGetFilterQualifierStringInclude2() {
        setParameterValues("1", new String[]{"2"});
        String expected = "Include Passed Twice in Consecutive Sessions";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    @Test
    public void testGetFilterQualifierStringExclude234() {
        setParameterValues("0", new String[]{"2", "3", "4"});
        String expected = "Exclude Passed Twice in Consecutive Sessions AND Approved by the Voters AND Defeated by the Voters";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierStringInclude234() {
        setParameterValues("1", new String[]{"2", "3", "4"});
        String expected = "Include Passed Twice in Consecutive Sessions OR Approved by the Voters OR Defeated by the Voters";
        assertEquals(expected, filter.getFilterQualifier());
    }

    
    private List<DropDownItem> genDropDownItemResults(String query) {
        if (query.contains("WHERE ID IN")) {
            List<DropDownItem> selectedItems = new ArrayList<>();
            int poslp = query.indexOf("(");
            int posrp = query.indexOf(")");
            String vstb = query.substring(poslp+1, posrp);
            String[] selectedIDs = vstb.split(",");
            for (String idString : selectedIDs) {
                int id = Integer.parseInt(idString.trim());
                for (DropDownItem item: conAmmendTable) {
                    if (item.getID() == id) {
                        selectedItems.add(item);
                        break;
                    }
                }
            }
            return selectedItems;
        } else {
            return Arrays.asList(conAmmendTable);
        }   
    }
    
    private void setParameterValues(final String selectParameterValue, final String[] valueParameterValue) {
        new Expectations() {
            {
                request.getParameter("F406");
                result = selectParameterValue;

                request.getParameterValues("V406");
                result = valueParameterValue;
            }
        };
        filter.setFilterParameterValues(request);
    }
    
}
