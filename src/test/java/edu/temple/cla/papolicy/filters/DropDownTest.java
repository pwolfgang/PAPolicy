/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.papolicy.dao.DropDownItem;
import edu.temple.cla.papolicy.dao.DropDownItemMapper;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Paul
 */
public class DropDownTest {
    
    DropDown filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;
    
    
    public DropDownTest() {
    }
    
    @Before
    public void setUp() {        
        filter = new DropDown(405, 3, "Senate Last Action", "Senate_Last_Action", "Last_Senate_Action", null);
        new Expectations() {{
            DropDownItem item = new DropDownItem();
            item.setID(2);
            item.setDescription("Passed on Floor");
            jdbcTemplate.query(anyString, new DropDownItemMapper());minTimes=0;
            result = Arrays.asList(new DropDownItem[]{item});
        }};
        filter.setJdbcTemplate(jdbcTemplate);    
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<label for=\"F405\"><br/>Senate Last Action</label><br /><select name=\"F405\" id=\"F405\">\n" +
"                <option value=\"-1\" selected=\"selected\">No Filter</option>\n" +
"<option value=\"2\">Passed on Floor</option></select>";
        String result = filter.getFilterFormInput();
        assertEquals(expected, result);
    }


    @Test
    public void testGetFilterQueryStringNoFilter() {
        setParameterValue("-1");
        String expected = "";
        assertEquals(expected, filter.getFilterQueryString());
    }

    @Test
    public void testGetFilterQueryStringValue2() {
        setParameterValue("2");
        String expected = "Senate_Last_Action='2'";
        assertEquals(expected, filter.getFilterQueryString());
    }
    
    
    @Test
    public void testGetFilterQualifierNoFilter() {
        setParameterValue("-1");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierValue2() {
        setParameterValue("2");
        String expected = "Senate Last Action is Passed on Floor";
        assertEquals(expected, filter.getFilterQualifier());
    }
    
    private void setParameterValue(final String value) {
        new Expectations() {
            {
                request.getParameter("F405");
                result = value;
            }
        };
        filter.setFilterParameterValues(request);
    }
    
}
