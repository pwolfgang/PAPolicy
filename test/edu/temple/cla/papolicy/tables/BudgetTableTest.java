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
public class BudgetTableTest {
    
    private BudgetTable testTable;
    
    public BudgetTableTest() {
    }
    
    @Before
    public void setUp() {
        testTable = new BudgetTable();
        testTable.setId(11);
        testTable.setTableName("BudgetTable");
        testTable.setTableTitle("Budget");
        testTable.setMajorOnly(true);
        testTable.setMinYear(1979);
        testTable.setMaxYear(2009);
        testTable.setTextColumn(null);
        testTable.setLinkColumn(null);
        testTable.setDrillDownColumns(null);
        testTable.setCodeColumn("Code");
        testTable.setNoteColumn(null);
        testTable.setYearColumn("TheYear");
    }

    @Test
    public void testGetTitleBox() {
        String expected = "\n" +
"<dl><input type=\"checkbox\" name=\"dataset\" value=\"11A\"\n" +
"        id=\"t11A\" onclick=\"expandBudget(11);\" />\n" +
"    <span class=\"strong\">Total Spending All Funds</strong></dl>\n" +
"<dl><input type=\"checkbox\" name=\"dataset\" value=\"11B\"\n" +
"        id=\"t11B\" onclick=\"expandBudget(11);\" />\n" +
"    <span class=\"strong\">General Fund Balance<span>\n" +
"    <div class=\"subtbl\" id=\"subtbl11B\">\n" +
"       <dd>\n" +
"       Include Rainy Day Fund\n" +
"       <br /><input type=\"radio\" name=\"rainyDay\" value=\"1\" /> Yes\n" +
"          <input type=\"radio\" name=\"rainyDay\" value=\"0\" checked=\"checked\" />No\n" +
"       </dd>\n" +
"    </div>\n" +
"</dl>\n";
        assertEquals(expected, testTable.getTitleBox());
    }

    @Test
    public void testIsTopicSearchable() {
        assertTrue(testTable.isTopicSearchable());
    }

    @Test
    public void testToString() {
    }

    @Test
    public void testGetDownloadTitle() {
    }

    @Test
    public void testGetAllocatedTotalQueryString() {
    }

    @Test
    public void testGetUnfilteredTotalQueryString() {
    }

    @Test
    public void testGetFilteredTotalQueryString() {
    }

    @Test
    public void testGetTopicQueryString() {
    }

    @Test
    public void testGetYearColumn() {
    }

    @Test
    public void testGetSubTable() {
    }

    @Test
    public void testGetTableName() {
    }

    @Test
    public void testGetTableTitle() {
    }

    @Test
    public void testGetTextColumn() {
    }

    @Test
    public void testGetUnits() {
    }

    @Test
    public void testGetAxisTitle() {
    }

    @Test
    public void testGetYearValueList() {
    }

    @Test
    public void testClone() {
    }

    @Test
    public void testCreateDownloadQuery() {
    }
}