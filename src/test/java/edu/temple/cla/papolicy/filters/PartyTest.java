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
    private final String repQuery = "Sponsor_Party=0";
    private final String repQualifier = "Sponsored by a Republican";
    private final String demQuery = "Sponsor_Party=1";
    private final String demQualifier = "Sponsored by a Democrat";
    
    public PartyTest() {
    }

    @Before
    public void setUp() {
        filter = new Party(407, 3, "Sponsor Party", "Sponsor_Party", null, null);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<fieldset><legend>Sponsor Party</legend>\n" +
"              <input type=\"radio\" id=\"F407N\" name=\"F407\" value=\"NOFILTER\" checked=\"checked\" />&nbsp;<label for=\"F407N\">Both</label>\n" +
"              <input type=\"radio\" id=\"F407R\" name=\"F407\" value=\"0\" />&nbsp;<label for=\"F407R\">Republican</label>\n" +
"              <input type=\"radio\" id=\"F407D\" name=\"F407\" value=\"1\" />&nbsp;<label for=\"F407D\">Democrat</label>\n" +
"</fieldset>";
        assertEquals(expected,filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringNoFilter() {
        setParameterValue("NOFILTER");
        String expected = "";
        assertEquals(expected, filter.getFilterQuery().toString());
    }

    @Test
    public void testGetFilterQueryStringRep() {
        setParameterValue("0");
        assertEquals(repQuery, filter.getFilterQuery().toString());
    }

    @Test
    public void testGetFilterQueryStringDem() {
        setParameterValue("1");
        assertEquals(demQuery, filter.getFilterQuery().toString());
    }

//    @Test
//    public void testGetFilterChoicesNotBoth() {
//        setParameterValue("NOFILTER");
//        Filter[] choices = filter.getFilterChoices();
//        assertEquals(1, choices.length);
//        assertEquals(filter, choices[0]);
//    }
//    
//    @Test
//    public void testGetFilerChoicesBoth() {
//        setParameterValue("ALL");
//        Filter[] choices = filter.getFilterChoices();
//        assertEquals(2, choices.length);
//        assertEquals(repQuery, choices[0].getFilterQuery().toString());
//        assertEquals(repQualifier, choices[0].getFilterQualifier());
//        assertEquals(demQuery, choices[1].getFilterQuery().toString());
//        assertEquals(demQualifier, choices[1].getFilterQualifier());
//    }
//
//    @Test
//    public void testGetNumberOfFilterChoicesNotBoth() {
//        setParameterValue("NOFILTER");
//        assertEquals(1, filter.getNumberOfFilterChoices());
//    }
//
//    @Test
//    public void testGetNumberOfFilterChoicesBoth() {
//        setParameterValue("ALL");
//        assertEquals(2, filter.getNumberOfFilterChoices());
//    }

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
