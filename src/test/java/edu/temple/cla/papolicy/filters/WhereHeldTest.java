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
