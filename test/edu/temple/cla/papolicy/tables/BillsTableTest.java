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
        instance.chamber = new String[]{"house"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('HB%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testSenateBills() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BILLS";
        instance.chamber = new String[]{"senate"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('SB%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testSenateResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "RES";
        instance.chamber = new String[]{"senate"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('SR%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "RES";
        instance.chamber = new String[]{"house"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('HR%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseAndSenateResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "RES";
        instance.chamber = new String[]{"house", "senate"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('HR%') OR Bill LIKE ('SR%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseAndSenateBills() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BILLS";
        instance.chamber = new String[]{"house", "senate"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('SB%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseBillsAndResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"house"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('HB%') OR Bill LIKE ('HR%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testSenateBillsAndResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"senate"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE (Bill LIKE ('SB%') OR Bill LIKE ('SR%'))";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

    @Test
    public void testHouseAndSenateBillsAndResolutions() {
        BillsTable instance = new BillsTable();
        instance.setFilterList(new ArrayList<Filter>());
        instance.setTableName("Bills_Data");
        instance.billtype = "BOTH";
        instance.chamber = new String[]{"house", "senate"};
        String expected = "SELECT count(ID) FROM Bills_Data WHERE ";
        assertEquals(expected, instance.getUnfilteredTotalQueryString());
    }

}