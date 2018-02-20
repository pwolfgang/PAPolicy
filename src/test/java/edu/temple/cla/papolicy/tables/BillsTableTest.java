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
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.filters.Filter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Paul Wolfgang
 */
public class BillsTableTest {
    
    @Mocked HttpServletRequest request;
    private BillsTable testTable;
    private String[] drillDownColumns;

    public BillsTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Session", "Bill", "Abstract", "Code"};
        testTable = new BillsTable();
        testTable.setId(3);
        testTable.setTableName("Bills_Data");
        testTable.setTableTitle("Bills and Resolutions");
        testTable.setMajorOnly(false);
        testTable.setMinYear(1975);
        testTable.setMaxYear(2008);
        testTable.setTextColumn("Abstract");
        testTable.setLinkColumn("Hyperlink");
        testTable.setDrillDownColumns(drillDownColumns);
        testTable.setCodeColumn("Code");
        testTable.setNoteColumn(null);
        testTable.setYearColumn(null);
        testTable.setFilterList(new ArrayList<Filter>());
    }

    /**
     * Test of getTitleBox method, of class BillsTable.
     */
    @Test
    public void testGetTitleBox() {
        String expected = "\n\n" +
"<input type=\"checkbox\" name=\"dataset\" value=\"3\"\n" +
"        id=\"t3\" onclick=\"expandBills(3);\" />\n" +
"        <label for=\"t3\"><span class=\"strong\">Bills and Resolutions</span></label>\n" +
"\n" +
"<div class=\"subtbl\" id=\"subtbl3\">\n" +
"    <fieldset><legend>Chamber</legend>\n" +
"    <input type=\"checkbox\" name=\"chamber\" value=\"House\" id=\"houseChamber\" checked=\"checked\" />\n" +
"        <label for=\"houseChamber\"><span class=\"strong\">House</span></label>\n" +
"    \n" +
"    <input type=\"checkbox\" name=\"chamber\" value=\"Senate\" id=\"senateChamber\" checked=\"checked\" />\n" +
"        <label for=\"senateChamber\"><span class=\"strong\">Senate</span></label>\n" +
"        </fieldset>\n" +
"<fieldset><legend>Type</legend>\n" +
"    <input type=\"radio\" name=\"billtype\" value=\"BOTH\" id=\"billtypeBoth\" checked=\"checked\" />\n" +
"        <label for=\"billtypeBoth\"><span class=\"strong\">Bills and Resolutions</span></label>\n" +
"    <br/>\n" +
"    <input type=\"radio\" name=\"billtype\" value=\"BILLS\" id=\"billtypeBills\" />\n" +
"        <label for=\"billtypeBills\"><span class=\"strong\">Bills</span></label>\n" +
"    <br/>\n" +
"    <input type=\"radio\" name=\"billtype\" value=\"RES\" id=\"billtypeRes\" />\n" +
"        <label for=\"billtypeRes\"><span class=\"strong\">Resolutions</span></label>\n" +
"    \n" +
"    </fieldset>\n" +
"    <fieldset><legend>Session Type</legend>\n" +
"    <input type=\"radio\" name=\"sessiontype\" value=\"REGULAR\" id=\"regularSessions\" checked=\"checked\" />\n" +
"        <label for=\"regularSessions\"><span class=\"strong\">Regular Sessions Only</span></label>\n" +
"    <br/><input type=\"radio\" name=\"sessiontype\" value=\"SPECIAL\" id=\"specialSessions\"/>\n" +
"        <label for=\"specialSessions\"><span class=\"strong\">Special Sessions Only</span></label>\n" +
"    <br/><input type=\"radio\" name=\"sessiontype\" value=\"BOTH\" id=\"bothSessionTypes\"/>\n" +
"        <label for=\"bothSessionTypes\"><span class=\"strong\">Both Regular and Special Sessions</span></label>\n" +
"    </fieldset>\n" +
"</div>\n" +
"\n" +
"\n" +
"<br/><input type=\"checkbox\" name=\"dataset\" value=\"3a\"\n" +
"           id=\"t3a\" onclick='expandBills(\"3a\");' />\n" +
"    <label for=\"t3a\"><span class=\"strong\">Acts (Laws) and Adopted Resolutions</span></label>\n" +
"<div class=\"subtbl\" id=\"subtbl3a\">\n" +
"    <fieldset><legend>Chamber</legend>\n" +
"    <input type=\"checkbox\" name=\"chambera\" value=\"House\" id=\"houseChambera\" checked=\"checked\" />\n" +
"        <label for=\"houseChambera\"><span class=\"strong\">House</span></label>\n" +
"    \n" +
"    <br/><input type=\"checkbox\" name=\"chambera\" value=\"Senate\" id=\"senateChambera\" checked=\"checked\" />\n" +
"        <label for=\"senateChambera\"><span class=\"strong\">Senate</span></label>\n" +
"        </fieldset>\n" +
"<fieldset><legend>Type</legend>\n" +
"    <br/><input type=\"radio\" name=\"billtypea\" value=\"BOTH\" id=\"billtypeBotha\" checked=\"checked\" />\n" +
"        <label for=\"billtypeBotha\"><span class=\"strong\">Acts and Adopted Resolutions</span></label>\n" +
"    \n" +
"    <br/><input type=\"radio\" name=\"billtypea\" value=\"BILLS\" id=\"billtypeBillsa\" />\n" +
"        <label for=\"billtypeBillsa\"><span class=\"strong\">Acts</span></label>\n" +
"    \n" +
"    <br/><input type=\"radio\" name=\"billtypea\" value=\"RES\" id=\"billtypeResa\" />\n" +
"        <label for=\"billtypeResa\"><span class=\"strong\">Adopted Resolutions</span></label>\n" +
"    \n" +
"    </fieldset>\n" +
"    <fieldset><legend>Session Type</legend>\n" +
"    <input type=\"radio\" name=\"sessiontypea\" value=\"REGULAR\" id=\"regularSessionsa\" checked=\"checked\" />\n" +
"        <label for=\"regularSessionsa\"><span class=\"strong\">Regular Sessions Only</span></label>\n" +
"    <br/><input type=\"radio\" name=\"sessiontypea\" value=\"SPECIAL\" id=\"specialSessionsa\"/>\n" +
"        <label for=\"specialSessionsa\"><span class=\"strong\">Special Sessions Only</span></label>\n" +
"    <br/><input type=\"radio\" name=\"sessiontypea\" value=\"BOTH\" id=\"bothSessionTypesa\"/>\n" +
"        <label for=\"bothSessionTypesa\"><span class=\"strong\">Both Regular and Special Sessions</span></label>\n" +
"    </fieldset>\n" +
"</div>\n" +
"\n        " +
"";
        String result = testTable.getTitleBox();
        assertEquals(expected, result);
    }

    /**
     * Test of toString method, of class BillsTable.
     */
    @Test
    public void testToString() {
        String expected = "Bills and Resolutions Regular and Special Sessions ";
        assertEquals(expected, testTable.toString());
    }

    /**
     * Test of getTotalQueryString
     */
    @Test
    public void testHouseBills() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House"};
            request.getParameter("billtype");
            result = "BILLS";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE Bill LIKE('HB%') AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testSenateBills() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"Senate"};
            request.getParameter("billtype");
            result = "BILLS";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE Bill LIKE('SB%') AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testSenateResolutions() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"Senate"};
            request.getParameter("billtype");
            result = "RES";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE Bill LIKE('SR%') AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testHouseResolutions() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House"};
            request.getParameter("billtype");
            result = "RES";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE Bill LIKE('HR%') AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testHouseAndSenateResolutions() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House", "Senate"};
            request.getParameter("billtype");
            result = "RES";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE (Bill LIKE('HR%') OR Bill LIKE('SR%')) "
                + "AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testHouseAndSenateBills() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House", "Senate"};
            request.getParameter("billtype");
            result = "BILLS";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE (Bill LIKE('HB%') OR Bill LIKE('SB%')) "
                + "AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }
    
    @Test
    public void testHouseAndSenateBillsSpecialSession() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House", "Senate"};
            request.getParameter("billtype");
            result = "BILLS";
            request.getParameter("sessiontype");
            result = "SPECIAL";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE (Bill LIKE('HB%') OR Bill LIKE('SB%')) AND Session LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testHouseAndSenateBillsRegularAndSpecialSession() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House", "Senate"};
            request.getParameter("billtype");
            result = "BILLS";
            request.getParameter("sessiontype");
            result = "BOTH";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE (Bill LIKE('HB%') OR Bill LIKE('SB%'))";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testHouseBillsAndResolutions() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House"};
            request.getParameter("billtype");
            result = "BOTH";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE (Bill LIKE('HB%') OR Bill LIKE('HR%')) "
                + "AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testSenateBillsAndResolutions() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"Senate"};
            request.getParameter("billtype");
            result = "BOTH";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE (Bill LIKE('SB%') OR Bill LIKE('SR%')) "
                + "AND Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testHouseAndSenateBillsAndResolutions() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House", "Senate"};
            request.getParameter("billtype");
            result = "BOTH";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        String expected = "SELECT year(Date_Referred) AS TheYear, count(ID) AS TheValue "
                + "FROM Bills_Data WHERE Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    
    @Test
    public void testClone() {
        new Expectations() {{
            request.getParameterValues("chamber");
            result = new String[]{"House", "Senate"};
            request.getParameter("billtype");
            result = "BILLS";
            request.getParameter("sessiontype");
            result = "REGULAR";
        }};
        testTable.setAdditionalParameters(request);
        BillsTable theClone = testTable.clone();
        assertEquals(testTable.billType, theClone.billType);
        assertArrayEquals(testTable.chamber, theClone.chamber);
    }
    
}