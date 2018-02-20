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
        filter = new MultiValuedFilter(406, 3, "Constitution Amendment", "Con_Ammend", "Con_Ammend", null);
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
        String expected = "<fieldset><legend>Constitution Amendment</legend>\n" +
"              <input type=\"radio\" id=\"F406BOTH\" name=\"F406\" value=\"587\" checked=\"checked\" />&nbsp;<label for=\"F406BOTH\">No Filter</label>\n" +
"              <input type=\"radio\" id=\"F4060\" name=\"F406\" value=\"0\" />&nbsp;<label for=\"F4060\">Exclude</label>\n" +
"              <input type=\"radio\" id=\"F4061\" name=\"F406\" value=\"1\" />&nbsp;<label for=\"F4061\">Include</label>\n" +
"        <br/><input type=\"checkbox\" id=\"V4060\" name=\"V406\" value=\"0\" checked=\"checked\" />\n" +
"<label for=\"V4060\">Not Passed by Legislature</label><br/><input type=\"checkbox\" id=\"V4061\" name=\"V406\" value=\"1\" checked=\"checked\" />\n" +
"<label for=\"V4061\">Passed Once by Legislature</label><br/><input type=\"checkbox\" id=\"V4062\" name=\"V406\" value=\"2\" checked=\"checked\" />\n" +
"<label for=\"V4062\">Passed Twice in Consecutive Sessions</label><br/><input type=\"checkbox\" id=\"V4063\" name=\"V406\" value=\"3\" checked=\"checked\" />\n" +
"<label for=\"V4063\">Approved by the Voters</label><br/><input type=\"checkbox\" id=\"V4064\" name=\"V406\" value=\"4\" checked=\"checked\" />\n" +
"<label for=\"V4064\">Defeated by the Voters</label><br/><input type=\"checkbox\" id=\"V4065\" name=\"V406\" value=\"5\" checked=\"checked\" />\n" +
"<label for=\"V4065\">Partially approved by Voters</label></fieldset>" +
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
