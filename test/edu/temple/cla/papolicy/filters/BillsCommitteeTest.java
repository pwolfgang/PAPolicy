/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class BillsCommitteeTest {

    BillsCommittee filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    SimpleJdbcTemplate jdbcTemplate;

    public BillsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new BillsCommittee(401, 3, "House Committee", null, "CommitteeAliases", "House");
        new NonStrictExpectations() {
            {
                CommitteeAlias committee = new CommitteeAlias();
                committee.setID(1);
                committee.setCtyCode(101);
                committee.setAlternateName("Aging and Older Adult Services");
                committee.setName("Aging and Older Adult Services");
                committee.setStartYear(2001);
                committee.setEndYear(9999);
                jdbcTemplate.query(anyString, new CommitteeAliasMapper());
                result = Arrays.asList(new CommitteeAlias[]{committee});
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = " Referred to House Committee\n" +
"<input name=\"hprimary\" value=\"1\" type=\"checkbox\"/> Primary Only\n" +
"<br /><select name=\"F401\">\n" +
"<option value=\"ALL\">ALL COMMITTEES</option>\n" +
"<option value=\"101\">Aging and Older Adult Services</option></select>";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringAll() {
        setParameterValue("ALL", null);
        String result = filter.getFilterQueryString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQueryBoth() {
        setParameterValue("101", "0");
        String result = filter.getFilterQueryString();
        assertEquals("(_101P<>0 OR _101O<>0)", result);
    }

    @Test
    public void testGetFilterQueryStringPrimary() {
        setParameterValue("101", "1");
        String result = filter.getFilterQueryString();
        assertEquals("_101P<>0", result);
    }

    @Test
    public void testGetFilterQualifierAll() {
        setParameterValue("ALL", null);
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierBoth() {
        setParameterValue("101", "0");
        String expected = "Referred to House Aging and Older Adult Services committee";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierPrimary() {
        setParameterValue("101", "1");
        String expected = "Referred to House Aging and Older Adult Services committee as primary committee";
        assertEquals(expected, filter.getFilterQualifier());
    }

    private void setParameterValue(final String parameterValue, final String primaryValue) {
        new Expectations() {
            {
                request.getParameter("F401");
                result = parameterValue;

                request.getParameter("hprimary");
                result = primaryValue;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
