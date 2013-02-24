/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Paul
 */
public class CheckBoxFilterTest {
    
    private CheckBoxFilter filter;
    @Mocked
    HttpServletRequest request;
    
    public CheckBoxFilterTest() {
    }
    
    @Before
    public void setUp() {
        filter = new CheckBoxFilter(403, 3, "Conference Committee", "_300", null, null);
    }

    @Test
    public void testGetFilterFormInput() {
    }

    @Test
    public void testGetFilterQueryStringNotChecked() {
        setParameterValue(null);
        String expected = "";
        assertEquals(expected, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQueryStringChecked() {
        setParameterValue("1");
        String expected = "_300<>0";
        assertEquals(expected, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQualifier() {
    }

    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("F403");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
