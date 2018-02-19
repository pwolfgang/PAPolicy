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
        String expected = "\n" +
"            <fieldset><legend>Conference Committee</legend>\n" +
"                  <input type=\"radio\" name=\"F403\" id=\"F4030\" value=\"0\" checked=\"checked\"/>&nbsp;"
                + " <label for=\"F4030\">Not Selected</label>\n" +
"                  <input type=\"radio\" name=\"F403\" id=\"F4031\" value=\"1\" />&nbsp;"
                + " <label for=\"F4031\">Selected</label>\n" +
"            </fieldset>\n";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringNotChecked() {
        setParameterValue(null);
        String expected = "";
        assertEquals(expected, filter.getFilterQuery().toString());
    }

    @Test
    public void testGetFilterQueryStringChecked() {
        setParameterValue("1");
        String expected = "_300<>0";
        assertEquals(expected, filter.getFilterQuery().toString());
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
