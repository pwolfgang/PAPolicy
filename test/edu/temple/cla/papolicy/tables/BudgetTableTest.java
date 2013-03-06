/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.dao.Deflator;
import edu.temple.cla.papolicy.dao.DeflatorMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.BudgetFilters;
import edu.temple.cla.papolicy.filters.Filter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class BudgetTableTest {
    
    @Mocked HttpServletRequest request;
    @Mocked SimpleJdbcTemplate jdbcTemplate;
    
    private YearValue[] yearValues = new YearValue[] {
        new YearValue(1979, 3490),
        new YearValue(1980, 2717),
        new YearValue(1981, 3840),
        new YearValue(1982, 4055),
        new YearValue(1983, 4164)
    };
    
    private YearValue[] expectedValues = new YearValue[] {
        new YearValue(1979, 4172),
        new YearValue(1980, 4084),
        new YearValue(1981, 3840),
        new YearValue(1982, 3795),
        new YearValue(1983, 3733)
    };
    
    private Deflator[] deflatorValues = new Deflator[] {
        new Deflator(1979, 2501.4, 0.4325),
        new Deflator(1980, 2724.2, 0.4707),
        new Deflator(1981, 3057.0, 0.5171),
        new Deflator(1982, 3223.7, 0.5525),
        new Deflator(1983, 3440.7, 0.5768)
    };
   
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
        new NonStrictExpectations() {{
            jdbcTemplate.query(anyString, new YearValueMapper());
            result = Arrays.asList(yearValues);
            jdbcTemplate.query(anyString, new DeflatorMapper());
            result = Arrays.asList(deflatorValues);
        }};
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
    public void testToStringDollars() {
        BudgetFilters filter = createBudgetFiltersInstance("0", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "Total Spending Un-Adjusted Dollars (×1,000,000)";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testToStringPercent() {
        BudgetFilters filter = createBudgetFiltersInstance("1", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "Percent of Total Spending";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testToStringPercentChange() {
        BudgetFilters filter = createBudgetFiltersInstance("2", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "Total Spending Percent Change";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetDownloadTitle() {
        String expected = "Total Spending Un-Adjusted Dollars (×1,000,000)";
        assertEquals(expected, testTable.getDownloadTitle());
    }

    @Test
    public void testGetUnfilteredTotalQueryString() {
        String expected = "SELECT TheYear, Sum(BudgetTable.TheValue*Crosswalk.PercentMatch/100)/1000 AS TheValue "
                + "FROM MajorCode INNER JOIN (Crosswalk INNER JOIN BudgetTable ON (Crosswalk.FC=BudgetTable.FC) "
                + "AND (Crosswalk.OC=BudgetTable.OC)) ON MajorCode.Code = Crosswalk.PolicyCode ";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
    }

    @Test
    public void testGetFilteredTotalQueryString() {
        String expected = "SELECT TheYear, Sum(BudgetTable.TheValue*Crosswalk.PercentMatch/100)/1000 AS TheValue "
                + "FROM MajorCode INNER JOIN (Crosswalk INNER JOIN BudgetTable ON (Crosswalk.FC=BudgetTable.FC) "
                + "AND (Crosswalk.OC=BudgetTable.OC)) ON MajorCode.Code = Crosswalk.PolicyCode ";
        assertEquals(expected, testTable.getFilteredTotalQueryString());
    }

    @Test
    public void testGetTopicQueryString() {
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        String expected = "SELECT TheYear, Sum(BudgetTable.TheValue*Crosswalk.PercentMatch/100)/1000 AS TheValue "
                + "FROM MajorCode INNER JOIN (Crosswalk INNER JOIN BudgetTable ON (Crosswalk.FC=BudgetTable.FC) "
                + "AND (Crosswalk.OC=BudgetTable.OC)) ON MajorCode.Code = Crosswalk.PolicyCode "
                + "WHERE MajorCode.Code=6";
        assertEquals(expected, testTable.getTopicQueryString(topic));
    }

    @Test
    public void testGetYearColumn() {
        String expected = "TheYear";
        assertEquals(expected, testTable.getYearColumn());
    }

    @Test
    public void testGetSubTableA() {
        Table result = testTable.getSubTable('A');
        assertSame(testTable, result);
    }

    @Test
    public void testGetSubTableB() {
        Table result = testTable.getSubTable('B');
        assertEquals(GeneralFundBalance.class, result.getClass());
    }

    @Test
    public void testGetTableName() {
        String expected = "BudgetTable";
        assertEquals(expected, testTable.getTableName());
    }

    @Test
    public void testGetTableTitle() {
        String expected = "Total Spending";
        assertEquals(expected, testTable.getTableTitle());
    }

    @Test
    public void testGetTextColumn() {
        assertNull(testTable.getTextColumn());
    }

    @Test
    public void testGetUnitsDollars() {
        BudgetFilters filter = createBudgetFiltersInstance("0", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        Units expected = Units.DOLLARS;
        assertEquals(expected, testTable.getUnits(null));
    }

    @Test
    public void testGetUnitsPercent() {
        BudgetFilters filter = createBudgetFiltersInstance("1", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        Units expected = Units.PERCENT;
        assertEquals(expected, testTable.getUnits(null));
    }

    @Test
    public void testGetUnitsPercentChange() {
        BudgetFilters filter = createBudgetFiltersInstance("2", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        Units expected = Units.PERCENT_CHANGE;
        assertEquals(expected, testTable.getUnits(null));
    }

    @Test
    public void testGetAxisTitleDollarsUnAdjusted() {
        BudgetFilters filter = createBudgetFiltersInstance("0", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "Un-Adjusted Dollars (×1,000,000)";
        assertEquals(expected, testTable.getAxisTitle(Units.DOLLARS));
    }

    @Test
    public void testGetAxisTitleDollarsAdjusted() {
        BudgetFilters filter = createBudgetFiltersInstance("0", "1", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "1999 Dollars (×1,000,000)";
        assertEquals(expected, testTable.getAxisTitle(Units.DOLLARS));
    }

    @Test
    public void testGetAxisTitlePercent() {
        BudgetFilters filter = createBudgetFiltersInstance("1", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "Percent";
        assertEquals(expected, testTable.getAxisTitle(Units.PERCENT));
    }

    @Test
    public void testGetAxisTitlePercentChange() {
        BudgetFilters filter = createBudgetFiltersInstance("2", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String expected = "Percent Change";
        assertEquals(expected, testTable.getAxisTitle(Units.PERCENT_CHANGE));
    }

    @Test
    public void testGetYearValueList() {
        BudgetFilters filter = createBudgetFiltersInstance("0", "1", "1981");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        List<YearValue> expected = Arrays.asList(expectedValues);
        List<YearValue> actual = testTable.getYearValueList(jdbcTemplate, "dummy query");
        assertEquals(expected, actual);
        
    }

    @Test
    public void testClone() {
    }

    @Test
    public void testCreateDownloadQuery() {
    }
    
    private BudgetFilters createBudgetFiltersInstance(final String dispValue, final String adjust, final String baseYear) {
        BudgetFilters filter = new BudgetFilters(1001, 11, null, null, null, "Deflator");
        new NonStrictExpectations() {{
            request.getParameter("disp");
            result = dispValue;
            request.getParameter("adjust");
            result = adjust;
            request.getParameter("baseYear");
            result = baseYear;
        }};
        filter.setFilterParameterValues(request);
        return filter;
    }
}