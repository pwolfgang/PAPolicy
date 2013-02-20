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
 * @author Paul Wolfgang
 */
public class BinaryFilterTest {

    BinaryFilter filter;
    @Mocked
    HttpServletRequest request;

    public BinaryFilterTest() {
    }

    @Before
    public void setUp() {
        filter = new BinaryFilter(414, 3, "Signed By Governor", "SignedbyGov", null, null);
    }

    @Test
    public void testGetFilterFormInput() {
    }

    @Test
    public void testSetFilterParameterValues() {
    }

    @Test
    public void testGetFilterQueryStringBoth() {
        setParameterValue("587");
        String result = filter.getFilterQueryString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQueryStringInclude() {
        setParameterValue("1");
        String result = filter.getFilterQueryString();
        assertEquals("SignedbyGov<>0", result);
    }

    @Test
    public void testGetFilterQueryStringExclude() {
        setParameterValue("0");
        String result = filter.getFilterQueryString();
        assertEquals("SignedbyGov=0", result);
    }

    @Test
    public void testGetFilterQualifier() {
    }

    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("F414");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
