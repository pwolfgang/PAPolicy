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
        String expected = "<input type=\"checkbox\" name=\"F403\" value=\"1\" /> Conference Committee";
        assertEquals(expected, filter.getFilterFormInput());
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
    public void testGetFilterQualifierNotChecked() {
        setParameterValue(null);
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierChecked() {
        setParameterValue("1");
        String expected = "Conference Committee";
        assertEquals(expected, filter.getFilterQualifier());
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
