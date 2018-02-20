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
