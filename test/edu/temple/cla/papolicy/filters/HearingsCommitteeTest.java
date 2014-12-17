/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
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
public class HearingsCommitteeTest {

    HearingsCommittee filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;

    public HearingsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new HearingsCommittee(102, 5, "Senate Committee", null, "CommitteeAliases", "Senate");
        new NonStrictExpectations() {
            {
                CommitteeAlias committee = new CommitteeAlias();
                committee.setID(39);
                committee.setCtyCode(201);
                committee.setAlternateName("Aging and Youth");
                committee.setName("Aging and Youth");
                committee.setStartYear(0);
                committee.setEndYear(9999);
                jdbcTemplate.query(anyString, new CommitteeAliasMapper());
                result = Arrays.asList(new CommitteeAlias[]{committee});
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<label>Senate Hearings\n" +
"                <br /><select name=\"F102\">\n" +
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n"
                + "<option value=\"201\">Aging and Youth</option>\n"
                + "</select></label><br/>";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryString() {
        String expected = "";
        String result = filter.getFilterQuery().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetFilterQualifier() {
        String expected = "";
        String result = filter.getFilterQualifier();
        assertEquals(expected, result);
    }

}
