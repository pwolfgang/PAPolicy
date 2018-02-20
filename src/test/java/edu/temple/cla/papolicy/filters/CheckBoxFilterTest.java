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
