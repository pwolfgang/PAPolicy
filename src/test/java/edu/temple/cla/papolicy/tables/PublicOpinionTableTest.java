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
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.YearValue;
import edu.temple.cla.papolicy.dao.YearValueMapper;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.filters.PublicOpinionFilters;
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
public class PublicOpinionTableTest {
    
    @Mocked HttpServletRequest request;
    @Mocked JdbcTemplate jdbcTemplate;
    private YearValue[] yearValues = new YearValue[] {
        new YearValue(2001, 22.0),
        new YearValue(2002, 23.0),
        new YearValue(2003, 22.5),
        new YearValue(2004, 22.0),
        new YearValue(2005, 21.5)
    };
    private YearValue[] yearValuesExpected = new YearValue[] {
        new YearValue(2001, 3.0),
        new YearValue(2002, 2.0),
        new YearValue(2003, 2.5),
        new YearValue(2004, 3.0),
        new YearValue(2005, 3.5)
    };

    PublicOpinionTable testTable;
    PublicOpinionFilters filter;
    
    public PublicOpinionTableTest() {
    }
    
    @Before
    public void setUp() {
        testTable = new PublicOpinionTable();
        testTable.setId(20);
        testTable.setTableName("PublicOpinion");
        testTable.setTableTitle("Most Important Problem");
        testTable.setMajorOnly(true);
        testTable.setMinYear(1994);
        testTable.setMaxYear(2011);
        testTable.setTextColumn("null");
        testTable.setLinkColumn("Source");
        testTable.setDrillDownColumns(null);
        testTable.setCodeColumn("Code");
        testTable.setNoteColumn(null);
        testTable.setYearColumn("Year");
        filter = new PublicOpinionFilters(901, 10, null, null, null, null);
        testTable.setFilterList(Arrays.asList(new Filter[]{filter}));
        new Expectations() {{
            jdbcTemplate.query(anyString, new YearValueMapper());minTimes=0;
            result = Arrays.asList(yearValues);            
        }};
        testTable.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetUnfilteredTotalQueryStringPercent() {
        setParameterValue("0");
        String expected = "SELECT Year AS TheYear, AVG(Percentage) AS TheValue FROM PublicOpinion";
        assertEquals(expected,testTable.getUnfilteredTotalQuery().build());
    }

    @Test
    public void testGetUnfilteredTotalQueryStringRank() {
        setParameterValue("1");
        String expected = "SELECT Year AS TheYear, AVG(25 - Rank_With_25) AS TheValue FROM PublicOpinion";
        assertEquals(expected,testTable.getUnfilteredTotalQuery().build());
    }


    @Test
    public void testGetTopicQueryString() {
        String expected = "SELECT Year AS TheYear, AVG(25 - Rank_With_25) AS TheValue FROM PublicOpinion WHERE Code=6";
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        setParameterValue("1");
        assertEquals(expected, testTable.getTopicQuery(topic).build());
    }

    @Test
    public void testGetUnitsPercent() {
        setParameterValue("0");
        assertEquals(Units.PERCENT, testTable.getUnits(null));
    }

    @Test
    public void testGetUnitsRank() {
        setParameterValue("1");
        assertEquals(Units.RANK, testTable.getUnits(null));
    }

    @Test
    public void testGetValueForRange() {
        SortedMap<Integer, Number> map = new TreeMap<>();
        map.put(2003, 2.5);
        assertEquals(2.5, testTable.getValueForRange(map));
        map.put(2004, 3.0);
        assertEquals(2.75, testTable.getValueForRange(map));
    }

    @Test
    public void testGetPercentForRange() {
        SortedMap<Integer, Number> map = new TreeMap<>();
        map.put(2003, 0.13);
        assertEquals(0.13, testTable.getValueForRange(map).doubleValue(), 0.001);
        map.put(2004, 0.15);
        assertEquals(0.14, testTable.getValueForRange(map).doubleValue(), 0.001);
    }

    @Test
    public void testGetYearValueList() {
        List<YearValue> expected = Arrays.asList(yearValuesExpected);
        setParameterValue("1");
        List<YearValue> result = testTable.getYearValueList("query");
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        Object theClone = testTable.clone();
        assertEquals(testTable.getClass(), theClone.getClass());
    }

    @Test
    public void testGetDisplayedValue2009() {
        String expected = "<span title=\"Most Important Problem data from 2008 "
                + "and 2009 were constructed from national Gallup polls using "
                + "only the responses of PA residents. Please see the User Guide "
                + "for more information.\">2.5%*</span>";
        String actual = testTable.getDisplayedValue("2009-10", 2.5, Units.PERCENT);
        assertEquals(expected, actual);
    }

    public void testGetDisplayedValue2008() {
        String expected = "<span title=\"Most Important Problem data from 2008 "
                + "and 2009 were constructed from national Gallup polls using "
                + "only the responses of PA residents. Please see the User Guide "
                + "for more information.\">1.0%*</span>";
        String actual = testTable.getDisplayedValue("2008", 1.0, Units.PERCENT);
        assertEquals(expected, actual);
    }

    public void testGetDisplayedValue2007() {
        String expected = "10.0%";
        String actual = testTable.getDisplayedValue("2007", 10, Units.PERCENT);
        assertEquals(expected, expected);
    }
    
    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("mipdisp");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
    
}