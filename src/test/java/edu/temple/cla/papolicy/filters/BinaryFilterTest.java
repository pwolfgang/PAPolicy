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
        String expected = "\n"
                + "            <fieldset><legend>Signed By Governor</legend>\n"
                + "                  <input type=\"radio\" name=\"F414\" id=\"F414B\" value=\"587\" checked=\"checked\" />&nbsp; <label for=\"F414B\">No Filter</label>\n"
                + "                  <input type=\"radio\" name=\"F414\" id=\"F4140\" value=\"0\" />&nbsp; <label for=\"F4140\">Exclude</label>\n"
                + "                  <input type=\"radio\" name=\"F414\" id=\"F4141\" value=\"1\" />&nbsp; <label for=\"F4141\">Include</label>\n"
                + "            </fieldset>\n";

        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringBoth() {
        setParameterValue("587");
        String result = filter.getFilterQuery().toString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQueryStringInclude() {
        setParameterValue("1");
        String result = filter.getFilterQuery().toString();
        assertEquals("SignedbyGov<>0", result);
    }

    @Test
    public void testGetFilterQueryStringExclude() {
        setParameterValue("0");
        String result = filter.getFilterQuery().toString();
        assertEquals("SignedbyGov=0", result);
    }

    @Test
    public void testGetFilterQualifierBoth() {
        setParameterValue("587");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }
    @Test
    public void testGetFilterQualifierInclude() {
        setParameterValue("1");
        String expected = "Include Signed By Governor";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    @Test
    public void testGetFilterQualifierExclude() {
        setParameterValue("0");
        String expected = "Exclude Signed By Governor";
        assertEquals(expected, filter.getFilterQualifier());
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
