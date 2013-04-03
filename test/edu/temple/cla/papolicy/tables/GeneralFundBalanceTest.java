/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.dao.Deflator;
import edu.temple.cla.papolicy.dao.DeflatorMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.BudgetFilters;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.queryBuilder.Between;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
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
public class GeneralFundBalanceTest {
    
    @Mocked HttpServletRequest request;
    @Mocked SimpleJdbcTemplate jdbcTemplate;
    private YearValue[] yearValues = new YearValue[] {
        new YearValue(1979,   30),
        new YearValue(1980,   68),
        new YearValue(1981,   72),
        new YearValue(1982,    8),
        new YearValue(1983, -235)
    };
    
    private YearValue[] expectedValues = new YearValue[] {
        new YearValue(1979,   36),
        new YearValue(1980,   75),
        new YearValue(1981,   72),
        new YearValue(1982,    7),
        new YearValue(1983, -211)
    };
            
    private Deflator[] deflatorValues = new Deflator[] {
        new Deflator(1979, 2501.4, 0.4325),
        new Deflator(1980, 2724.2, 0.4707),
        new Deflator(1981, 3057.0, 0.5171),
        new Deflator(1982, 3223.7, 0.5525),
        new Deflator(1983, 3440.7, 0.5768)
    };

    private GeneralFundBalance testTable;
    
    public GeneralFundBalanceTest() {
    }
    
    @Before
    public void setUp() {
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
        new NonStrictExpectations() {{
            jdbcTemplate.query(anyString, new YearValueMapper());
            result = Arrays.asList(yearValues);
            jdbcTemplate.query(anyString, new DeflatorMapper());
            result = Arrays.asList(deflatorValues);
        }};
    }

    @Test
    public void testGetTitleBox() {
        String expected = "<dl><dt><input type=\"checkbox\" name=\"dataset\" "
                + "value=\"11\"/><span class=\"strong\">General Fund Balance\r\n"
                + "<br/>Include Rainy Day Fund <input type=\"radio\" "
                + "name=\"rainyDay\" value=\"0\" checked />No <input type=\"radio\" "
                + "name=\"rainyDay\" value=\"1\" />Yes</span></dd></span></dt></dl>";
        assertEquals(expected, testTable.getTitleBox());
    }

    @Test
    public void testIsTopicSearchable() {
        assertFalse(testTable.isTopicSearchable());
    }

    @Test
    public void testToStringNoRainyDay() {
        String expected = "General Fund Balance Un-Adjusted Dollars (×1,000,000)";
        new Expectations() {{
            request.getParameter("rainyDay");
            result = "0";
        }};
        BudgetFilters filter = createBudgetFiltersInstance("0", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));        
        testTable.setAdditionalParameters(request);
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testToStringRainyDay() {
        String expected = "General Fund Balance Including Rainy Day Fund Un-Adjusted Dollars (×1,000,000)";
        new Expectations() {{
            request.getParameter("rainyDay");
            result = "1";
        }};
        BudgetFilters filter = createBudgetFiltersInstance("0", "0", "1999");
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));        
        testTable.setAdditionalParameters(request);
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetDownloadTitle() {
        String expected = "General Fund Balance Un-Adjusted Dollars (×1,000,000)";
        assertEquals(expected, testTable.getDownloadTitle());
    }

    @Test
    public void testCreateDownloadQuery() {
        String expected = "SELECT * FROM PennsylvaniaGeneralFundBalance WHERE "
                + "Year BETWEEN 1991 AND 2006 ORDER BY Year";
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        new Expectations() {{
            request.getParameter("rainyDay");
            result = "0";
        }};
        testTable.setAdditionalParameters(request);
        QueryBuilder query = testTable.getTopicQuery(topic).clone();
        query.setBetween(new Between("Year", 1991, 2006));
        query.setGroupBy("Year");
        query.setOrderBy("Year");
        assertEquals(expected, testTable.createDownloadQuery(query).build());
    }

    @Test
    public void testGetTopicQueryStringNoRainyDay() {
        String expected = "SELECT Year AS TheYear, SUM(Ending_Balance) AS TheValue "
                + "FROM PennsylvaniaGeneralFundBalance WHERE ";
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        new Expectations() {{
            request.getParameter("rainyDay");
            result = "0";
        }};
        testTable.setAdditionalParameters(request);
        assertEquals(expected, testTable.getTopicQueryString(topic));
    }

    @Test
    public void testGetTopicQueryStringRainyDay() {
        String expected = "SELECT Year AS TheYear, SUM(Ending_Balance + Budget_Stabilization_Fund) "
                + "AS TheValue FROM PennsylvaniaGeneralFundBalance WHERE ";
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        new Expectations() {{
            request.getParameter("rainyDay");
            result = "1";
        }};
        testTable.setAdditionalParameters(request);
        assertEquals(expected, testTable.getTopicQueryString(topic));
    }

    @Test
    public void testGetUnfilteredTotalQueryString() {
        String expected = "SELECT Year AS TheYear, Expenditures AS TheValue FROM "
                + "PennsylvaniaGeneralFundBalance WHERE ";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
    }

    @Test
    public void testGetYearColumn() {
        String expected = "Year";
        assertEquals(expected, testTable.getYearColumn());
    }

    @Test
    public void testGetTableName() {
        String expected = "PennsylvaniaGeneralFundBalance";
        assertEquals(expected, testTable.getTableName());
    }

    @Test
    public void testGetTableTitle() {
        String expected = "General Fund Balance";
        assertEquals(expected, testTable.getTableTitle());
    }

    @Test
    public void testGetTextColumn() {
        assertNull(testTable.getTextColumn());
    }

    @Test
    public void testClone() {
        GeneralFundBalance theClone = testTable.clone();
        assertEquals(testTable.getClass(), theClone.getClass());
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