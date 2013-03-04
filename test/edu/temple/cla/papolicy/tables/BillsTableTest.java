/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    }

    /**
     * Test of toString method, of class BillsTable.
     */
    @Test
    public void testToString() {
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HR%') OR Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%')) AND  Session LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%'))";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('HR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SB%') OR Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE  Session NOT LIKE('%-%-%')";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
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