/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.filters.BinaryFilter;
import edu.temple.cla.papolicy.filters.Filter;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class AbstractTableTest {
    
    private Table testTable;
    private String[] drillDownColumns;
    private Filter[] filterList = new Filter[]{
        new BinaryFilter(501, 6, "Legislative Request", "LegRequest", null, null),
        new BinaryFilter(502, 6, "Policy Recommendation", "Recommendation", null, null),
        new BinaryFilter(504, 6, "Dealing with Taxes", "Tax", null, null),
        new BinaryFilter(505, 6, "Concerning the Elderly", "Elderly", null, null)
    };
    @Mocked
    HttpServletRequest request;
    
    public AbstractTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Title", "Orginization", "Year", "Month", "Day", "Abstract", "FinalCode"};
        testTable = new AbstractTable(){
            public String getTitleBox(){return null;}
        };
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
        new NonStrictExpectations() {{
            request.getParameter("F501");
            result = "587";
            request.getParameter("F502");
            result = "587";
            request.getParameter("F504");
            result = "0";
            request.getParameter("F505");
            result = "1";
        }};
        for (Filter filter:filterList) {
            filter.setFilterParameterValues(request);
        }
        testTable.setFilterList(Arrays.asList(filterList)); 
    }

    @Test
    public void testGetId() {
        assertEquals(6, testTable.getId());
    }

    @Test
    public void testGetTableName() {
        assertEquals("LegServiceAgencyReports", testTable.getTableName());
    }

    @Test
    public void testGetTableTitle() {
        assertEquals("Legislative Service Agency Reports", testTable.getTableTitle());
    }

    @Test
    public void testIsMajorOnly() {
        assertFalse(testTable.isMajorOnly());
    }

    @Test
    public void testGetMinYear() {
        assertEquals(1991, testTable.getMinYear());
    }

    @Test
    public void testGetMaxYear() {
        assertEquals(2006, testTable.getMaxYear());
    }

    @Test
    public void testGetFilterList() {
        assertEquals(Arrays.asList(filterList), testTable.getFilterList());
    }

    @Test
    public void testGetFilterListSize() {
        assertEquals(filterList.length, testTable.getFilterListSize());
    }

    @Test
    public void testGetQualifier() {
        assertEquals('\u0000', testTable.getQualifier());
    }

    @Test
    public void testGetTextColumn() {
        assertEquals("Abstract", testTable.getTextColumn());
    }

    @Test
    public void testToString() {
        String expected = "Legislative Service Agency Reports Exclude Dealing with Taxes and Include Concerning the Elderly";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetDownloadTitle() {
        String expected = "Legislative Service Agency Reports Exclude Dealing with Taxes and Include Concerning the Elderly";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetFilterQualifierString() {
        String expected = " Exclude Dealing with Taxes and Include Concerning the Elderly";
        assertEquals(expected, testTable.getFilterQualifierString().toString());
    }

    @Test
    public void testGetFilterQueryString() {
        String expected = "Tax=0 AND Elderly<>0";
        assertEquals(expected, testTable.getFilterQueryString());
    }

    @Test
    public void testIsTopicSearchable() {
        assertTrue(testTable.isTopicSearchable());
    }

    @Test
    public void testGetUnfilteredTotalQueryString() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM LegServiceAgencyReports WHERE ";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
    }

    @Test
    public void testGetFilteredTotalQueryString() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM LegServiceAgencyReports WHERE Tax=0 AND Elderly<>0";
        assertEquals(expected, testTable.getFilteredTotalQueryString());
    }

    @Test
    public void testGetTopicQueryString() {
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM LegServiceAgencyReports" +
                " WHERE Tax=0 AND Elderly<>0 AND FinalCode LIKE('6__')";
        assertEquals(expected, testTable.getTopicQueryString(topic));
    }

    @Test
    public void testGetYearColumn() {
        assertEquals("Year", testTable.getYearColumn());
    }

    @Test
    public void testGetUnits() {
        assertEquals(Units.PERCENT, testTable.getUnits("percent"));
        assertEquals(Units.PERCENT_OF_TOTAL, testTable.getUnits("percent_of_total"));
        assertEquals(Units.PERCENT_OF_FILTERED, testTable.getUnits("percent_of_filtered"));
        assertEquals(Units.COUNT, testTable.getUnits("count"));
    }

    @Test
    public void testGetSubTable() {
        assertSame(testTable, testTable.getSubTable('a'));
    }

    @Test
    public void testGetValueForRange() {
        fail("Test not written");
    }

    @Test
    public void testGetPercentForRange() {
        fail("Test not written");
    }

    @Test
    public void testGetAxisTitle() {
        String expected = "Number of Cases";
        assertEquals(expected, testTable.getAxisTitle(Units.COUNT));
    }

    @Test
    public void testGetYearValueList() {
        fail("Test not written");
    }

    @Test
    public void testGetDrillDownColumns() {
        assertArrayEquals(drillDownColumns, testTable.getDrillDownColumns());
    }

    @Test
    public void testGetLinkColumn() {
        assertEquals("Hyperlink", testTable.getLinkColumn());
    }

    @Test
    public void testGetNoteColumn() {
        assertEquals("", testTable.getNoteColumn());
    }

    @Test
    public void testCreateDrillDownURL() {
        fail("Test not written");
    }

    @Test
    public void testCreateDownloadQuery() {
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        String query = testTable.getTopicQueryString(topic) + 
                " AND Year BETWEEN 1991 AND 2006 GROUP BY Year ORDER BY Year";
        String expected = "SELECT *FROM LegServiceAgencyReports WHERE Tax=0 AND "
                + "Elderly<>0 AND FinalCode LIKE('6__') "
                + "AND Year BETWEEN 1991 AND 2006 ORDER BY Year";
        assertEquals(expected, testTable.createDownloadQuery(query));
    }

    @Test
    public void testGetCodeColumn() {
        assertEquals("FinalCode", testTable.getCodeColumn());
    }

    @Test
    public void testGetTable() throws Exception {
        fail("Test not written");
    }

    @Test
    public void testGetDownloadURL() {
        fail("Test not written");
    }

    @Test
    public void testGetDisplayedValue() {
        fail("Test not written");
    }

}
