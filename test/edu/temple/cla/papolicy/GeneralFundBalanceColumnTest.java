/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy;

import edu.temple.cis.wolfgang.simpledbtest.TestDatabase;
import edu.temple.cla.papolicy.filters.BudgetFilters;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import edu.temple.cla.papolicy.tables.GeneralFundBalance;
import edu.temple.cla.papolicy.tables.Table;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class GeneralFundBalanceColumnTest {
    @Mocked
    HttpServletRequest request;
    private JdbcTemplate jdbcTemplate;
    private Column testColumn;
    private Table testTable;
    private String[] drillDownColumns;
    private YearRange yearRange;
    
    public GeneralFundBalanceColumnTest() {
    }
    
    @BeforeClass
    public static void beforeClass() throws Exception {
                TestDatabase.beforeClass();
    }
//    
    @Before
    public void setUp() throws Exception {
        jdbcTemplate = TestDatabase.getJdbcTemplate();
        testTable = new GeneralFundBalance();
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
        testTable.setJdbcTemplate(jdbcTemplate);
        BudgetFilters filter = createBudgetFiltersInstance("2", "0", "2000");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        String freeText = null;
        String showResults = "count";
        yearRange = new YearRange(1979, 2009);
        testColumn = new Column(testTable, null, freeText, showResults, yearRange);
        QueryBuilder countQuery =
                testColumn.getTopicCountQuery(yearRange.getMinYear(), yearRange.getMaxYear());
        QueryBuilder downloadQuery = testColumn.getTable().createDownloadQuery(countQuery);
        testColumn.setDownloadQueryString(Utility.compressAndEncode(downloadQuery.build()));
        TestDatabase.beforeTest();
    }

    @Test
    public void testToString() {
        String expected = "General Fund Balance Percent Change";
        assertEquals(expected, testColumn.toString());
    }


    @Test
    public void testGetUnits() {
        Units expected = Units.PERCENT_CHANGE;
        assertEquals(expected, testColumn.getUnits());
    }

    @Test
    public void testGetPercentChange() {
      testColumn.setValueMap(yearRange.getMinYear() - 2, yearRange.getMaxYear());
      testColumn.setInitialPrevValue(yearRange.getMinYearPredecessor(), yearRange.getMinYear());
      Number expected1979 = null;
      Number expected1980 = new Double(126.7);
      Number expected1981 = new Double(5.9);
      Number value1979 = testColumn.getPercentChange(1979, 1979);
      Number value1980 = testColumn.getPercentChange(1980, 1980);
      Number value1981 = testColumn.getPercentChange(1981, 1981);
      assertNull(value1979);
      assertEquals(expected1980.doubleValue(), value1980.doubleValue(),0.05);
      assertEquals(expected1981.doubleValue(), value1981.doubleValue(),0.05);
    }
    
    @Test
    public void testGetMinValue() {
      testColumn.setValueMap(yearRange.getMinYear() - 2, yearRange.getMaxYear());
      Number expected = new Integer(-2030);
      assertEquals(expected, testColumn.getMinValue());
    }

    @Test
    public void testGetMaxValue() {
      testColumn.setValueMap(yearRange.getMinYear() - 2, yearRange.getMaxYear());
      Number expected = new Integer(611);
      assertEquals(expected, testColumn.getMaxValue());
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