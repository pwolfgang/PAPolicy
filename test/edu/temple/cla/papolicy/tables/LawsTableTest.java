/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.filters.Filter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class LawsTableTest {

    @Mocked HttpServletRequest request;
    private LawsTable testTable;
    private String[] drillDownColumns;
    
    public LawsTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Session", "Bill", "Abstract", "Code"};
        Table billsTable = new BillsTable();
        billsTable.setId(3);
        billsTable.setTableName("Bills_Data");
        billsTable.setTableTitle("Bills and Resolutions");
        billsTable.setMajorOnly(false);
        billsTable.setMinYear(1975);
        billsTable.setMaxYear(2008);
        billsTable.setTextColumn("Abstract");
        billsTable.setLinkColumn("Hyperlink");
        billsTable.setDrillDownColumns(drillDownColumns);
        billsTable.setCodeColumn("Code");
        billsTable.setNoteColumn(null);
        billsTable.setYearColumn(null);
        billsTable.setFilterList(new ArrayList<Filter>());
        testTable = (LawsTable)billsTable.getSubTable('a');
        testTable.setFilterList(new ArrayList<Filter>());
    }

    @Test
    public void testToString() {
        String expected = "Acts and Resolutions Regular and Special Sessions ";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetYearColumn() {
        String expected = "Year_Enacted";
    }

    @Test
    public void testGetDrillDownColumns() {
        String[] expected = new String[]{"Act_No", "Session", "Bill", "Abstract", "Code"};
        String[] actual = testTable.getDrillDownColumns();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testClone() {
        LawsTable theClone = testTable.clone();
        assertEquals(testTable.getClass(), theClone.getClass());
    }
}