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

import edu.temple.cla.papolicy.dao.CommitteeName;
import edu.temple.cla.papolicy.dao.CommitteeNameMapper;
import edu.temple.cla.papolicy.dao.FilterMapper;
import edu.temple.cla.papolicy.dao.TableMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.filters.CheckBoxFilter;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.filters.HouseHearingsCommittee;
import edu.temple.cla.papolicy.filters.WhereHeld;
import edu.temple.cla.policydb.queryBuilder.Between;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import static mockit.internal.expectations.ActiveInvocations.anyString;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class TranscriptTableTest {
    
    private Table testTable;
    private String[] drillDownColumns;
    private Filter[] filterList = new Filter[] {
        new HouseHearingsCommittee(1201, 4, "House Committee", "Committee", "CommitteeNames", "House"),
        new HouseHearingsCommittee(1202, 4, "Senate Committee", "Committee", "CommitteeNames", "Senate"),
        new CheckBoxFilter(1203, 4, "Annual Budget Hearings (Appropriations only)", "Budget", null, null),
        new WhereHeld(1204, 4, "Where Held", "WhereHeld", null, "Harrisburg")
    };
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;

    
    public TranscriptTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"hearingDate", "summary", "FinalCode"};
        testTable = new TranscriptTable();
        testTable.setId(4);
        testTable.setTableName("Transcript");
        testTable.setTableTitle("House Hearings");
        testTable.setMajorOnly(false);
        testTable.setMinYear(1979);
        testTable.setMaxYear(2008);
        testTable.setTextColumn("summary");
        testTable.setLinkColumn("transcriptURL");
        testTable.setDrillDownColumns(drillDownColumns);
        testTable.setCodeColumn("FinalCode");
        testTable.setNoteColumn(null);
        testTable.setYearColumn("Year");
        new Expectations() {{
            request.getParameter("F1201");
            result = "101";
            request.getParameter("F1202");
            result = "ALL";
            request.getParameter("F1203");
            result = "0";
            request.getParameter("F1204");
            result = "1";
            jdbcTemplate.query(anyString, new TableMapper());minTimes=0;
            result = Arrays.asList(new Table[]{testTable});
            jdbcTemplate.query(anyString, new FilterMapper());minTimes=0;
            result = Arrays.asList(filterList);
            CommitteeName committee = new CommitteeName();
            committee.setCtyCode(101);
            committee.setName("Aging and Older Adult Services");
            jdbcTemplate.query(anyString, new CommitteeNameMapper());minTimes=0;
            result = Arrays.asList(new CommitteeName[]{committee});
        }};
        testTable.setFilterList(Arrays.asList(filterList)); 
        for (Filter filter:filterList) {
            filter.setJdbcTemplate(jdbcTemplate);
            filter.setFilterParameterValues(request);
        }
    }

    @Test
    public void testGetUnfilteredTotalQueryString() {
        String expected = "SELECT Year AS TheYear, count(ID) AS TheValue "
                + "FROM Transcript_Committee join Transcript on transcriptID=ID";
        assertEquals(expected, testTable.getUnfilteredTotalQuery().build());
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
        String expected = "transcriptdrilldown.spg?query=H4sIAAAAAAAAAE2OwQqCQBi"
                + "EX2VuFnjQDkJQQbp_KFnBZogn2XKprVxj3Q69fWYgXucbvpkjpRRlSJiLmxRG"
                + "6SsTVrpo33UtzMfFRmnxjJqqy6wRur0Y9bInnkK0SJV-YMMPO2QDKqOmrpW1U"
                + "uLeKD0iaPRIkbBlwpDHxAmR_fwWlr7nY73v0ps0MpbParFyYmGMas9vc3V6Nv"
                + "xBmmxp4gRl6Ux7UnT_EVKWE-3hz-d_18zzAhw4I46w6DtfCodgx_MAAAA";
        assertEquals(expected, testTable.createDrillDownURL(query));
    }

}