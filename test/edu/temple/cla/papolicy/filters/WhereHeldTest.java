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
public class WhereHeldTest {

    WhereHeld filter;
    @Mocked
    HttpServletRequest request;

    public WhereHeldTest() {
    }

    @Before
    public void setUp() {
        filter = new WhereHeld(1204, 4, "Where Held", "City", null, "Harrisburg");
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "       <fieldset><legend>Where Held</legend>\n" +
"              <input type=\"radio\" name=\"F1204\" value=\"587\" id=\"F1204B\" checked=\"checked\" /><label for=\"F1204B\">Both</label>\n" +
"              <input type=\"radio\" name=\"F1204\" value=\"0\" id=\"F12040\" /><label for=\"F12040\">Harrisburg</label>\n" +
"              <input type=\"radio\" name=\"F1204\" value=\"1\" id=\"F12041\" /><label for=\"F12041\">Outside Harrisburg</label></fieldset>";
        String result = filter.getFilterFormInput();
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringBoth() {
        setParameterValue("587");
        String result = filter.getFilterQuery().toString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQueryStringOut() {
        setParameterValue("1");
        String result = filter.getFilterQuery().toString();
        assertEquals("City<>'Harrisburg'", result);
    }

    @Test
    public void testGetFilterQueryStringIn() {
        setParameterValue("0");
        String result = filter.getFilterQuery().toString();
        assertEquals("City='Harrisburg'", result);
    }

    @Test
    public void testGetFilterQualifierBoth() {
        setParameterValue("587");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }
    @Test
    public void testGetFilterQualifierIn() {
        setParameterValue("0");
        String expected = "Held in Harrisburg";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    @Test
    public void testGetFilterQualifierOut() {
        setParameterValue("1");
        String expected = "Held outside Harrisburg";
        assertEquals(expected, filter.getFilterQualifier());
    }

    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("F1204");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
