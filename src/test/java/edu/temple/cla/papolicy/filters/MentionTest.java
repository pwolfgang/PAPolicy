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
        String expected = "            <fieldset><legend>Executive</legend>\n" +
"              <input type=\"radio\" name=\"F301\" value=\"587\" id=\"F301B\" checked=\"checked\" /><label for=\"F301B\">No Filter</label>\n" +
"              <input type=\"radio\" name=\"F301\" value=\"0\" id=\"F3010\" /><label for=\"F3010\">No Mention</label>\n" +
"              <input type=\"radio\" name=\"F301\" value=\"2\" id=\"F3012\" /><label for=\"F3012\">Mention</label>\n" +
"</fieldset>\n" +
"";
        String result = filter.getFilterFormInput();
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringNoFilter() {
        setParameterValue("587");
        String expected = "";
        assertEquals(expected, filter.getFilterQuery().toString());
    }

    @Test
    public void testGetFilterQueryStringNoMention() {
        setParameterValue("0");
        String expected = "_Exec_=0";
        assertEquals(expected, filter.getFilterQuery().toString());
    }
    
    @Test
    public void testGetFilterQueryStringMention() {
        setParameterValue("2");
        String expected = "_Exec_<>0";
        assertEquals(expected, filter.getFilterQuery().toString());
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
