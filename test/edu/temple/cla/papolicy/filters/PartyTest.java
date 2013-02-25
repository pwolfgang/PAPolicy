/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Paul Wolfgang
 */
public class PartyTest {
    Party filter;
    @Mocked
    HttpServletRequest request;
    private String repQuery = "Sponsor_Party=0";
    private String repQualifier = "Sponsored by a Republican";
    private String demQuery = "Sponsor_Party=1";
    private String demQualifier = "Sponsored by a Democrat";
    
    public PartyTest() {
    }

    @Before
    public void setUp() {
        filter = new Party(407, 3, "Sponsor Party", "Sponsor_Party", null, null);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "Sponsor Party\n" +
"        <br /><input type=\"radio\" name=\"F407\" value=\"NOFILTER\" checked=\"checked\" />&nbsp;no filter\n" +
"              <input type=\"radio\" name=\"F407\" value=\"0\" />&nbsp;Republican\n" +
"              <input type=\"radio\" name=\"F407\" value=\"1\" />&nbsp;Democrat\n" +
"              <input type=\"radio\" name=\"F407\" value=\"ALL\" />&nbsp;Both\n";
        assertEquals(expected,filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringNoFilter() {
        setParameterValue("NOFILTER");
        String expected = "";
        assertEquals(expected, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQueryStringRep() {
        setParameterValue("0");
        assertEquals(repQuery, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQueryStringDem() {
        setParameterValue("1");
        assertEquals(demQuery, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterChoicesNotBoth() {
        setParameterValue("NOFILTER");
        Filter[] choices = filter.getFilterChoices();
        assertEquals(1, choices.length);
        assertEquals(filter, choices[0]);
    }
    
    @Test
    public void testGetFilerChoicesBoth() {
        setParameterValue("ALL");
        Filter[] choices = filter.getFilterChoices();
        assertEquals(2, choices.length);
        assertEquals(repQuery, choices[0].getFilterQueryString());
        assertEquals(repQualifier, choices[0].getFilterQualifier());
        assertEquals(demQuery, choices[1].getFilterQueryString());
        assertEquals(demQualifier, choices[1].getFilterQualifier());
    }

    @Test
    public void testGetNumberOfFilterChoicesNotBoth() {
        setParameterValue("NOFILTER");
        assertEquals(1, filter.getNumberOfFilterChoices());
    }

    @Test
    public void testGetNumberOfFilterChoicesBoth() {
        setParameterValue("ALL");
        assertEquals(2, filter.getNumberOfFilterChoices());
    }

    @Test
    public void testGetFilterQualifierNoFilter() {
        setParameterValue("NOFILTER");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierRep() {
        setParameterValue("0");
        assertEquals(repQualifier, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierDem() {
        setParameterValue("1");
        assertEquals(demQualifier, filter.getFilterQualifier());
    }

    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("F407");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
