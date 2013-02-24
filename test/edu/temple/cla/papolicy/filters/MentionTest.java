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
public class MentionTest {
    
    private Mention filter;
    @Mocked
    HttpServletRequest request;
    
    public MentionTest() {
    }
    
    @Before
    public void setUp() {
        filter = new Mention(301, 1, "Executive", "_Exec_", null, null);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "Executive\n" +
"        <br /><input type=\"radio\" name=\"F301\" value=\"587\" checked=\"checked\" />&nbsp;no filter\n" +
"              <input type=\"radio\" name=\"F301\" value=\"0\" />&nbsp;No Mention\n" +
"              <input type=\"radio\" name=\"F301\" value=\"2\" />&nbsp;Mention\n";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringNoFilter() {
        setParameterValue("587");
        String expected = "";
        assertEquals(expected, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQueryStringNoMention() {
        setParameterValue("0");
        String expected = "_Exec_=0";
        assertEquals(expected, filter.getFilterQueryString());
    }
    
    @Test
    public void testGetFilterQueryStringMention() {
        setParameterValue("2");
        String expected = "_Exec_<>0";
        assertEquals(expected, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQualifierNoFilter() {
        setParameterValue("587");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    @Test
    public void testGetFilterQualifierNoMention() {
        setParameterValue("0");
        String expected = "No Mention of Executive";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    @Test
    public void testGetFilterQualifierMention() {
        setParameterValue("2");
        String expected = "Mention of Executive";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("F301");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
