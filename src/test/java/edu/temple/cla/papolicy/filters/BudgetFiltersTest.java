/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.Deflator;
import edu.temple.cla.papolicy.dao.DeflatorMapper;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class BudgetFiltersTest {
    
    private BudgetFilters filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;

    private Deflator[] deflators = new Deflator[] {
        new Deflator(1997,  8211.7, 0.8495),
        new Deflator(1998,  8663.0, 0.8603),
        new Deflator(1999,  9208.4, 0.8717),
        new Deflator(2000,  9821.0, 0.8889),
        new Deflator(2001, 10225.3, 0.9099),
        new Deflator(2002, 10543.9, 0.9249)
    };
    
    public BudgetFiltersTest() {
    }
    
    @Before
    public void setUp() {
        filter = new BudgetFilters(1001, 11, null, null, null, "Deflator");
        new Expectations() {{
            jdbcTemplate.query(anyString, new DeflatorMapper());minTimes=0;
            result = Arrays.asList(deflators);
        }};
        filter.setJdbcTemplate(jdbcTemplate);
        new Expectations() {{
            request.getParameter("disp");
            result = "1";
            request.getParameter("adjust");
            result = "0";
            request.getParameter("baseYear");
            result = "1999";
        }};
        filter.setFilterParameterValues(request);
    }

    @Test
    public void testGetFilterFormInput() {
        System.out.println("getFilterFormInput");
        BudgetFilters instance = filter;
        String expResult = "<fieldset><legend>Display</legend>&nbsp;&nbsp;"
                + "<input type=\"radio\" id=\"disp0\" name=\"disp\" value=\"0\" checked=\"checked\" />\n" +
"                <label for=\"disp0\">Dollar Values</label>\n" +
"                <br/>&nbsp;&nbsp;<input type=\"radio\" id=\"disp1\" name=\"disp\" value=\"1\" />\n" +
"                <label for=\"disp1\">Percent of Total Spending</label>\n" +
"                <br />&nbsp;&nbsp;<input type=\"radio\" id=\"disp2\" name=\"disp\" value=\"2\" />\n" +
"                <label for=\"disp2\">Display Percent Change</label>\n" +
"                </fieldset><br />\n" +
"                <fieldset><legend>Inflation Adjustment</legend>                &nbsp;&nbsp;<input type=\"radio\" id=\"adjust0\" name=\"adjust\" value=\"0\" />\n" +
"                <label for=\"adjust0\">Un-adjusted Dollars</label>\n" +
"                <br/>&nbsp;&nbsp;<input type=\"radio\" id=\"adjust1\" name=\"adjust\" value=\"1\" checked=\"checked\" />\n" +
"                <label for=\"adjust1\">Inflation-adjusted</label><label for=\"baseYear\"> Dollars Base Year</label>\n" +
"                <select id=\"baseYear\" name=\"baseYear\">\n" +
"        <option value=\"1997\">1997</option>\n" +
"<option value=\"1998\">1998</option>\n" +
"<option value=\"1999\">1999</option>\n" +
"<option value=\"2000\" selected=\"selected\">2000</option>\n" +
"<option value=\"2001\">2001</option>\n" +
"<option value=\"2002\">2002</option>\n" +
"</select>\n" +
"</fieldset>\n" +
"";
        String result = instance.getFilterFormInput();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFilterQueryString() {
        System.out.println("getFilterQueryString");
        BudgetFilters instance = filter;
        String expResult = "";
        String result = instance.getFilterQuery().toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFilterQualifier() {
        System.out.println("getFilterQualifier");
        BudgetFilters instance = filter;
        String expResult = "";
        String result = instance.getFilterQualifier();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDisp() {
        System.out.println("getDisp");
        BudgetFilters instance = filter;
        String expResult = "1";
        String result = instance.getDisp();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAdjust() {
        System.out.println("getAdjust");
        BudgetFilters instance = filter;
        String expResult = "0";
        String result = instance.getAdjust();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetBaseYear() {
        System.out.println("getBaseYear");
        BudgetFilters instance = filter;
        String expResult = "1999";
        String result = instance.getBaseYear();
        assertEquals(expResult, result);
    }
}
