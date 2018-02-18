/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.tables;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class StandardTableTest {
    
    private StandardTable testTable;
    private String[] drillDownColumns;
    
    public StandardTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Title", "Orginization", "Year", "Month", "Day", "Abstract", "FinalCode"};
        testTable = new StandardTable();
        testTable.setId(6);
        testTable.setTableName("LegServiceAgencyReports");
        testTable.setTableTitle("Legislative Service Agency Reports");
        testTable.setMajorOnly(false);
        testTable.setMinYear(1991);
        testTable.setMaxYear(2006);
        testTable.setTextColumn("Abstract");
        testTable.setLinkColumn("Hyperlink");
        testTable.setDrillDownColumns(drillDownColumns);
        testTable.setCodeColumn("FinalCode");
        testTable.setNoteColumn(null);
        testTable.setYearColumn("Year");
    }

    @Test
    public void testGetTitleBox() {
        String expected = "\n" +
"        <dl><dt>\n" +
"            <input type=\"checkbox\" name=\"dataset\" value=\"6\"\n" +
"                 id=\"t6\" onclick=\"expandFilters(6);expandNote(6);\" />\n" +
"            <label for=\"t6\"><span class=\"strong\">Legislative Service Agency Reports</span></label>\n" +
"        </dt></dl>\n" +
"        <div id=\"note6\" class=\"nnoottee\"><p></p></div>\n" +
"        ";
        String result = testTable.getTitleBox();
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        Object theClone = testTable.clone();
        assertEquals(testTable.getClass(), theClone.getClass());
    }
}