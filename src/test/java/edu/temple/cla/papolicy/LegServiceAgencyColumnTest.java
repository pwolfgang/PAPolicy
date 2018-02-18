/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy;

import edu.temple.cis.wolfgang.simpledbtest.TestDatabase;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.filters.BinaryFilter;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import edu.temple.cla.papolicy.tables.StandardTable;
import edu.temple.cla.papolicy.tables.Table;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class LegServiceAgencyColumnTest {
    @Mocked
    HttpServletRequest request;
    private JdbcTemplate jdbcTemplate;
    private Column testColumn;
    private Table testTable;
    private String[] drillDownColumns;
    private Filter[] filterList = new Filter[]{
        new BinaryFilter(501, 6, "Legislative Request", "LegRequest", null, null),
        new BinaryFilter(502, 6, "Policy Recommendation", "Recommendation", null, null),
        new BinaryFilter(504, 6, "Dealing with Taxes", "Tax", null, null),
        new BinaryFilter(505, 6, "Concerning the Elderly", "Elderly", null, null)
    };
    private YearValue[] yearValues = new YearValue[] {
        new YearValue(1979, 4),
        new YearValue(1980, 5),
        new YearValue(1981, 2),
        new YearValue(1982, 4),
        new YearValue(1983, 4)
    };
    
    public LegServiceAgencyColumnTest() {
    }
    
    @BeforeClass
    public static void beforeClass() throws Exception {
                TestDatabase.beforeClass();
    }
    
    @Before
    public void setUp() throws Exception {
        TestDatabase.beforeTest();
        jdbcTemplate = TestDatabase.getJdbcTemplate();
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
        testTable.setJdbcTemplate(jdbcTemplate);
        new Expectations() {{
            request.getParameter("F501");
            result = "587";
            request.getParameter("F502");
            result = "587";
            request.getParameter("F504");
            result = "1";
            request.getParameter("F505");
            result = "587";
        }};
        for (Filter filter:filterList) {
            filter.setFilterParameterValues(request);
        }
        testTable.setFilterList(Arrays.asList(filterList));        
        Topic topic = new Topic();
        topic.setCode(12);
        topic.setDescription("Law, Crime, and Family Issues");
        String freeText = null;
        String showResults = "count";
        YearRange yearRange = new YearRange(1979, 1983);
        testColumn = new Column(testTable, topic, freeText, showResults, yearRange);
        QueryBuilder countQuery =
                testColumn.getTopicCountQuery(yearRange.getMinYear(), yearRange.getMaxYear());
        QueryBuilder downloadQuery = testColumn.getTable().createDownloadQuery(countQuery);
        testColumn.setDownloadQueryString(Utility.compressAndEncode(downloadQuery.build()));
    }

    @Test
    public void testToString() {
        String expected = "Law, Crime, and Family Issues Legislative Service "
                + "Agency Reports Include Dealing with Taxes";
        assertEquals(expected, testColumn.toString());
    }

    @Test
    public void testGetDownloadTitle() {
        String expected = "Law, Crime, and Family Issues Legislative Service "
                + "Agency Reports Include Dealing with Taxes";
        assertEquals(expected, testColumn.getDownloadTitle());
    }

    @Test
    public void testGetDownloadURL() {
        String expected = "<a href=\"Law%2C+Crime%2C+and+Family+Issues+"
                + "Legislative+Service+Agency+Reports+Include+Dealing+with+"
                + "Taxes+1979_1983.xlsx?query=H4sIAAAAAAAAAAt29XF1DlHQUnAL8v"
                + "dV8ElND04tKstMTnVMT81LrgxKLcgvKilWCPdwDXJVCEmssLEzUHD0c1F"
                + "wy8xLzHHOT0lV8PH0dtVQNzSKj1fXBEtFpiYWKTi5hoS7uvopGFqaW4JF"
                + "DS0tjBX8g1xcgxScIsFqABcZf6d6AAAA\">Law, Crime, and Family "
                + "Issues Legislative Service Agency Reports Include Dealing "
                + "with Taxes</a><br/>";
        assertEquals(expected, testColumn.getDownloadURL());
    }

    @Test
    public void testGetFilteredTotalQueryString_0args() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE Tax<>0";
        assertEquals(expected, testColumn.getFilteredTotalQueryString());
    }

    @Test
    public void testGetUnfilteredTotalQueryString_0args() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE ";
        assertEquals(expected, testColumn.getUnfilteredTotalQueryString());
    }

    @Test
    public void testGetUnfilteredTotalQueryString_int_int() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE Year BETWEEN 1979 AND 1983 "
                + "GROUP BY Year ORDER BY Year";
        assertEquals(expected, testColumn.getUnfilteredTotalQueryString(1979, 1983));
    }

    @Test
    public void testGetFilteredTotalQueryString_int_int() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE Tax<>0 AND Year "
                + "BETWEEN 1979 AND 1983 GROUP BY Year ORDER BY Year";
        assertEquals(expected, testColumn.getFilteredTotalQueryString(1979, 1983));
    }

    @Test
    public void testGetTopicCountQueryString_0args() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE Tax<>0 AND "
                + "FinalCode LIKE('12__')";
        assertEquals(expected, testColumn.getTopicCountQueryString());
    }

    @Test
    public void testGetTopicCountQueryString_int_int() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE Tax<>0 AND "
                + "FinalCode LIKE('12__') AND Year BETWEEN 1979 AND 1983 "
                + "GROUP BY Year ORDER BY Year";
        assertEquals(expected, testColumn.getTopicCountQueryString(1979, 1983));
    }

    @Test
    public void testGetUnits() {
        Units expected = Units.COUNT;
        assertEquals(expected, testColumn.getUnits());
    }

    @Test
    public void testGetValue() {
        testColumn.setValueMap(1979, 1983);
        Integer expected = 1;
        assertEquals(expected, testColumn.getValue(1981, 1981));
    }

    @Test
    public void testGetPercentOfTotal() {
        testColumn.setValueMap(1979, 1983);
        testColumn.setUnfilteredTotalMap(1979, 1983);
        double expected = 25;
        assertEquals(expected, testColumn.getPercentOfTotal(1979, 1979).doubleValue(), 0.05);
    }

    @Test
    public void testGetPercent() {
        testColumn.setValueMap(1979, 1983);
        testColumn.setFilteredTotalMap(1979, 1983);
        double expected = 100.0;
        assertEquals(expected, testColumn.getPercent(1979, 1979).doubleValue(), 0.05);
        
    }

    @Test
    public void testGetAxisTitle() {
        String expected = "Number of Cases";
        assertEquals(expected, testColumn.getAxisTitle());
    }

}