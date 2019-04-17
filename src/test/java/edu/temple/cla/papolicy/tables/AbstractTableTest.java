/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Units;
import edu.temple.cla.papolicy.Utility;
import edu.temple.cla.papolicy.YearRange;
import edu.temple.cla.papolicy.dao.FilterMapper;
import edu.temple.cla.papolicy.dao.TableMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.BinaryFilter;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.policydb.queryBuilder.Between;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class AbstractTableTest {
    
    private Table testTable;
    private String[] drillDownColumns;
    private final Filter[] filterList = new Filter[]{
        new BinaryFilter(501, 6, "Legislative Request", "LegRequest", null, null),
        new BinaryFilter(502, 6, "Policy Recommendation", "Recommendation", null, null),
        new BinaryFilter(504, 6, "Dealing with Taxes", "Tax", null, null),
        new BinaryFilter(505, 6, "Concerning the Elderly", "Elderly", null, null)
    };
    private final YearValue[] yearValues = new YearValue[] {
        new YearValue(1979, 4),
        new YearValue(1980, 5),
        new YearValue(1981, 2),
        new YearValue(1982, 4),
        new YearValue(1983, 4)
    };
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;
    
    public AbstractTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Title", "Orginization", "Year", "Month", "Day", "Abstract", "FinalCode"};
        testTable = new AbstractTable(){
            @Override
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
        new Expectations() {{
            request.getParameter("F501");
            result = "587";
            request.getParameter("F502");
            result = "587";
            request.getParameter("F504");
            result = "0";
            request.getParameter("F505");
            result = "1";
            jdbcTemplate.query(anyString, new YearValueMapper());minTimes=0;
            result = Arrays.asList(yearValues);
            jdbcTemplate.query(anyString, new TableMapper());minTimes=0;
            result = Arrays.asList(new Table[]{testTable});
            jdbcTemplate.query(anyString, new FilterMapper());minTimes=0;
            result = Arrays.asList(filterList);
        }};
        testTable.setJdbcTemplate(jdbcTemplate);
        testTable.setFilterList(Arrays.asList(filterList)); 
        for (Filter filter:filterList) {
            filter.setFilterParameterValues(request);
        }
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
        String expected = "Legislative Service Agency Reports Exclude Dealing "
                + "with Taxes and Include Concerning the Elderly";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetDownloadTitle() {
        String expected = "Legislative Service Agency Reports Exclude Dealing "
                + "with Taxes and Include Concerning the Elderly";
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
        assertEquals(expected, testTable.getFilterQuery().toStringNoParen());
    }

    @Test
    public void testIsTopicSearchable() {
        assertTrue(testTable.isTopicSearchable());
    }

    @Test
    public void testGetUnfilteredTotalQueryString() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testGetFilteredTotalQueryString() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports WHERE Tax=0 AND Elderly<>0";
        assertEquals(expected, testTable.getFilteredTotalQuery().build());
    }

    @Test
    public void testGetTopicQueryString() {
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue FROM "
                + "LegServiceAgencyReports" +
                " WHERE Tax=0 AND Elderly<>0 AND FinalCode LIKE('6__')";
        assertEquals(expected, testTable.getTopicQuery(topic).build());
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
        SortedMap<Integer, Number> values = new TreeMap<>();
        values.put(1, 100);
        values.put(2, 200);
        Integer expected = 300;
        assertEquals(expected, testTable.getValueForRange(values));
    }

    @Test
    public void testGetPercentForRange() {
        SortedMap<Integer, Number> values = new TreeMap<>();
        SortedMap<Integer, Number> totals = new TreeMap<>();
        values.put(1, 100);
        values.put(2, 200);
        totals.put(1, 500);
        totals.put(2, 500);
        Double expected = 30.0;
        assertEquals(expected, testTable.getPercentForRange(values, totals));
    }

    @Test
    public void testGetAxisTitle() {
        String expected = "Number of Cases";
        assertEquals(expected, testTable.getAxisTitle(Units.COUNT));
    }

    @Test
    public void testGetYearValueList() {
        List<YearValue> expected = Arrays.asList(yearValues);
        List<YearValue> result = testTable.getYearValueList("query");
        assertEquals(expected, result);
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
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        QueryBuilder query = testTable.getTopicQuery(topic).clone();
        query.setBetween(new Between("Year", 1991, 2006));
        query.setGroupBy("Year");
        query.setOrderBy("Year");
        String expected = "drilldown.spg?query="
                + "H4sIAAAAAAAAAD1OzQqCQBh8lblVsAfrIAgVWH5i5A9sgniSzRZbkt1Yl8ie"
                + "vjLoNMwvc6KU9iVK5XrJUNhOafUSThnNUEthGTKj3ZUhEiNDeB6cFa1jiJUW"
                + "_d5cPqVkvEvbK32DGJB-MeZFhlR2J2kfqpVhJ3U7cnk31g2oEuKEUjw3HsI8"
                + "AvWXT31cb3_0P4z0cKT5zG-a2WJyvnewo7IiyrEMguWkrjzPR8Ej4tjVU-YN"
                + "cCYED9MAAAA";
        assertEquals(expected, testTable.createDrillDownURL(query));
    }

    @Test
    public void testCreateDownloadQuery() {
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        QueryBuilder query = testTable.getTopicQuery(topic).clone();
        query.setBetween(new Between("Year", 1991, 2006));
        query.setGroupBy("Year");
        query.setOrderBy("Year");
        String expected = "SELECT * FROM LegServiceAgencyReports WHERE Tax=0 AND "
                + "Elderly<>0 AND FinalCode LIKE('6__') "
                + "AND Year BETWEEN 1991 AND 2006 ORDER BY Year";
        assertEquals(expected, testTable.createDownloadQuery(query).build());
    }

    @Test
    public void testGetCodeColumn() {
        assertEquals("FinalCode", testTable.getCodeColumn());
    }

    @Test
    public void testGetTable() throws Exception {
        List<Table> result = AbstractTable.getTable("6", '\u0000', request, jdbcTemplate);
        List<Table> expected = Arrays.asList(testTable);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDownloadURL() {
        String tableName = testTable.getTableTitle();
        YearRange yearRange = new YearRange(1991, 2006);
        String downloadQuery = Utility.compressAndEncode("SELECT *FROM LegServiceAgencyReports WHERE Tax=0 AND " +
                                    "Elderly<>0 AND FinalCode LIKE('6__') " +
                                    "AND Year BETWEEN 1991 AND 2006 ORDER BY Year");
        String expected = "<a href=\"Legislative+Service+Agency+Reports+1991_2006.xlsx?"
                + "table=" + testTable.getTableName() + "&"
                + "query=H4sIAAAAAAAAACWKywqCQBSGX-XfWa7GFoJggZcjRZPCOCCuZNCDCIPGFJFvH0z"
                + "L79KSpEIjrFTzgOS5ZfdZRs5mXsdd8XNz7xe6KymCNt-zQFaXIDuxs3t6-WO1rMYW28SQ"
                + "tzsdgngYgqMvPRuHnHRHVCNKksjbkxAxGlWSQt775wfXKREchgAAAA\">"
                + "Legislative Service Agency Reports</a><br/>";
        assertEquals(expected, testTable.getDownloadURL(tableName, downloadQuery, yearRange));      
    }

    @Test
    public void testGetDisplayedValue() {
        assertEquals("250", testTable.getDisplayedValue(null, new Integer(250), Units.COUNT));
        assertEquals("$1,234", testTable.getDisplayedValue(null, new Double(1234.0), Units.DOLLARS));
        assertEquals("5.3%", testTable.getDisplayedValue(null, new Double(5.3), Units.PERCENT));
        assertEquals("5.5", testTable.getDisplayedValue(null, new Double(5.5), Units.RANK));
    }
    
    @Test
    public void testClone() {
        Table theClone = testTable.clone();
        assertEquals(theClone.getClass(), testTable.getClass());
        assertEquals(testTable.getFilterList(), theClone.getFilterList());
        if (testTable.getDrillDownColumns() == null) {
            assertNull(theClone.getDrillDownColumns());
        } else {
            assertArrayEquals(testTable.getDrillDownColumns(), theClone.getDrillDownColumns());
        }
    }

}
