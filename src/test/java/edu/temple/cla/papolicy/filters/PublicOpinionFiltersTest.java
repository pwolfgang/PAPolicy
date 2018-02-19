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
public class PublicOpinionFiltersTest {
    
    private PublicOpinionFilters filter;
    @Mocked
    HttpServletRequest request;
    
    public PublicOpinionFiltersTest() {
    }
    
    @Before
    public void setUp() {
        filter = new PublicOpinionFilters(901, 10, null, null, null, null);
    }

    @Test
    public void testGetFilterFormInput() {
        System.out.println("getFilterFormInput");
        String expResult = "<fieldset><legend>Display as</legend>"
                + "<input type=\"radio\" id=\"mipdisp0\" name=\"mipdisp\" value=\"0\" checked=\"checked\" />"
                + "<label for=\"mipdisp0\">Percent</label>\n"
                + "<input type=\"radio\" id=\"mipdisp1\" name=\"mipdisp\" value=\"1\" />"
                + "<label for=\"mipdisp1\">Rank</label></fieldset>";
        String result = filter.getFilterFormInput();
        assertEquals(expResult, result);
    }


    @Test
    public void testGetFilterQueryString() {
        System.out.println("getFilterQueryString");
        PublicOpinionFilters instance = filter;
        String expResult = "";
        String result = instance.getFilterQuery().toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFilterQualifier() {
        System.out.println("getFilterQualifier");
        PublicOpinionFilters instance = filter;
        String expResult = "";
        String result = instance.getFilterQualifier();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMipdispZero() {
        System.out.println("getMipdisp");
        PublicOpinionFilters instance = filter;
        setParameterValue("0");
        String expResult = "0";
        String result = instance.getMipdisp();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMipdispOne() {
        System.out.println("getMipdisp");
        PublicOpinionFilters instance = filter;
        setParameterValue("1");
        String expResult = "1";
        String result = instance.getMipdisp();
        assertEquals(expResult, result);
    }

    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("mipdisp");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
