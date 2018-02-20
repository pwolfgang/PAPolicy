/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
