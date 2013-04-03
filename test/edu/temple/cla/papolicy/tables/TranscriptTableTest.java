/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import edu.temple.cla.papolicy.queryBuilder.Between;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import java.util.Arrays;
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
    SimpleJdbcTemplate jdbcTemplate;

    
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
        new NonStrictExpectations() {{
            request.getParameter("F1201");
            result = "101";
            request.getParameter("F1202");
            result = "ALL";
            request.getParameter("F1203");
            result = "0";
            request.getParameter("F1204");
            result = "1";
            jdbcTemplate.query(anyString, new TableMapper());
            result = Arrays.asList(new Table[]{testTable});
            jdbcTemplate.query(anyString, new FilterMapper());
            result = Arrays.asList(filterList);
            CommitteeName committee = new CommitteeName();
            committee.setCtyCode(101);
            committee.setName("Aging and Older Adult Services");
            jdbcTemplate.query(anyString, new CommitteeNameMapper());
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
                + "FROM Transcript_Committee join Transcript on transcriptID=ID WHERE ";
        assertEquals(expected, testTable.getUnfilteredTotalQueryString());
    }

    @Test
    public void testCreateDrillDownURL() {
        Topic topic = new Topic();
        topic.setCode(6);
        topic.setDescription("Education");
        QueryBuilder query = testTable.getTopicQuery(topic).clone();
        query.setBetween(new Between("Year", "1991", "2006"));
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