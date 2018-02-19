/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import java.util.Arrays;
import mockit.Expectations;
import mockit.Mocked;
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
    JdbcTemplate jdbcTemplate;

    public HearingsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new HearingsCommittee(102, 5, "Senate Committee", null, "CommitteeAliases", "Senate");
    }

    @Test
    public void testGetFilterFormInput() {
        new Expectations() {
            {
                CommitteeAlias committee = new CommitteeAlias();
                committee.setID(39);
                committee.setCtyCode(201);
                committee.setAlternateName("Aging and Youth");
                committee.setName("Aging and Youth");
                committee.setStartYear(0);
                committee.setEndYear(9999);
                jdbcTemplate.query(anyString, new CommitteeAliasMapper());
                result = Arrays.asList(committee);
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
        String expected = "<label for=\"F102\">Senate Hearings</label>\n" +
"                <br /><select name=\"F102\" id=\"F102\">\n" +
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n"
                + "<option value=\"201\">Aging and Youth</option>\n"
                + "</select><br/>";
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
