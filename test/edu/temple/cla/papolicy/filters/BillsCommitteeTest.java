/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

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
        new NonStrictExpectations() {{  
            jdbcTemplate.query(anyString, new CommitteeAliasMapper());
            result = Arrays.asList(new String[]{"Aging and Older Adult Services"});
        }};
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
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
        assertEquals("(_101P<>0)", result);
    }

    @Test
    public void testGetFilterQualifier() {
    }

    @Test
    public void testSetFilterParameterValues() {
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
