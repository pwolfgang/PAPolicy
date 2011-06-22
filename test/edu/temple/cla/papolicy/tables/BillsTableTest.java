/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.filters.Filter;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class BillsTableTest {

    public BillsTableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getTitleBox method, of class BillsTable.
     */
    @Test
    public void testGetTitleBox() {
    }

    /**
     * Test of setAdditionalParameters method, of class BillsTable.
     */
    @Test
    public void testSetAdditionalParameters() {
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
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BILLS";
        instance.chamber = new String[]{"House"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testSenateBills() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BILLS";
        instance.chamber = new String[]{"Senate"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testSenateResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "RES";
        instance.chamber = new String[]{"Senate"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "RES";
        instance.chamber = new String[]{"House"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseAndSenateResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "RES";
        instance.chamber = new String[]{"House", "Senate"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HR%') OR Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseAndSenateBills() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BILLS";
        instance.chamber = new String[]{"House", "Senate"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseBillsAndResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"House"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('HR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testSenateBillsAndResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"Senate"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE (Bill LIKE ('SB%') OR Bill LIKE ('SR%')) AND  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseAndSenateBillsAndResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"House", "Senate"};
        instance.sessionType = "REGULAR";
        String expected = "SELECT Year_Referred AS TheYear, count(ID) AS TheValue FROM Bills_Data WHERE  Session NOT LIKE('%-%-%')";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    
    @Test
    public void testClone() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"house", "senate"};
        BillsTable theClone = instance.clone();
        assertEquals(instance.billtype, theClone.billtype);
        assertArrayEquals(instance.chamber, theClone.chamber);
    }
    
}