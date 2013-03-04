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

/**
 *
 * @author Paul Wolfgang
 */
public class BillsTableTest {
    
    @Mocked HttpServletRequest request;

    public BillsTableTest() {
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HR%') OR Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%')) AND  Session LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('HR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SB%') OR Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.setAdditionalParameters(request);
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    
    @Test
    public void testClone() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billType = "BOTH";
        instance.chamber = new String[]{"house", "senate"};
        BillsTable theClone = instance.clone();
        assertEquals(instance.billType, theClone.billType);
        assertArrayEquals(instance.chamber, theClone.chamber);
    }
    
}