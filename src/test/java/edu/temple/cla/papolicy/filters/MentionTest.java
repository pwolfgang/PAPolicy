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
