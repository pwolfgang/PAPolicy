/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeName;
import edu.temple.cla.papolicy.dao.CommitteeNameMapper;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class HouseHearingsCommitteeTest {

    HouseHearingsCommittee filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;

    public HouseHearingsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new HouseHearingsCommittee(1201, 4, "House Committee", "Committee", "CommitteeNames", "House");
        new NonStrictExpectations() {
            {
                CommitteeName committee = new CommitteeName();
                committee.setCtyCode(101);
                committee.setName("Aging and Older Adult Services");
                jdbcTemplate.query(anyString, new CommitteeNameMapper());
                result = Arrays.asList(new CommitteeName[]{committee});
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<label>House Hearings\n" +
"                <br/>\n" +
"<select name=\"F1201\">\"\n" +
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n" +
"<option value=\"101\">Aging and Older Adult Services</option>\n" +
"</select></label><br/>\n";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringAll() {
        setParameterValue("ALL");
        String result = filter.getFilterQuery().toString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQuery101() {
        setParameterValue("101");
        String result = filter.getFilterQuery().toString();
        assertEquals("CtyCode=101", result);
    }

    @Test
    public void testGetFilterQualifierAll() {
        setParameterValue("ALL");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifier101() {
        setParameterValue("101");
        String expected = "Committee: Aging and Older Adult Services";
        assertEquals(expected, filter.getFilterQualifier());
    }


    private void setParameterValue(final String parameterValue) {
        new Expectations() {
            {
                request.getParameter("F1201");
                result = parameterValue;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
