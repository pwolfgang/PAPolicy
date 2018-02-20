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

import edu.temple.cla.papolicy.dao.CommitteeAlias;
import edu.temple.cla.papolicy.dao.CommitteeAliasMapper;
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
 * @author Paul Wolfgang
 */
public class BillsCommitteeTest {

    BillsCommittee filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;

    public BillsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new BillsCommittee(401, 3, "House Committee", null, "CommitteeAliases", "House");
        new Expectations() {
            {
                CommitteeAlias committee = new CommitteeAlias();
                committee.setID(1);
                committee.setCtyCode(101);
                committee.setAlternateName("Aging and Older Adult Services");
                committee.setName("Aging and Older Adult Services");
                committee.setStartYear(2001);
                committee.setEndYear(9999);
                jdbcTemplate.query(anyString, new CommitteeAliasMapper());minTimes=0;
                result = Arrays.asList(new CommitteeAlias[]{committee});
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<fieldset><legend>Referred to House Committee</legend>\n" +
"<input name=\"hprimary\" value=\"1\" id=\"hprimary\" type=\"checkbox\"/><label for=\"hprimary\"> Primary Only</label>\n" +
"<br/><label for=\"F401\">Name</label><select name=\"F401\" id=\"F401\">"
                + "<option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n" +
"<option value=\"101\">Aging and Older Adult Services</option>\n</select></fieldset>";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringAll() {
        setParameterValue("ALL", null);
        String result = filter.getFilterQuery().toString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQueryBoth() {
        setParameterValue("101", "0");
        String result = filter.getFilterQuery().toString();
        assertEquals("(_101P<>0 OR _101O<>0)", result);
    }

    @Test
    public void testGetFilterQueryStringPrimary() {
        setParameterValue("101", "1");
        String result = filter.getFilterQuery().toString();
        assertEquals("_101P<>0", result);
    }

    @Test
    public void testGetFilterQualifierAll() {
        setParameterValue("ALL", null);
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierBoth() {
        setParameterValue("101", "0");
        String expected = "Referred to House Aging and Older Adult Services committee";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifierPrimary() {
        setParameterValue("101", "1");
        String expected = "Referred to House Aging and Older Adult Services committee as primary committee";
        assertEquals(expected, filter.getFilterQualifier());
    }

    private void setParameterValue(final String parameterValue, final String primaryValue) {
        new Expectations() {
            {
                request.getParameter("F401");
                result = parameterValue;

                request.getParameter("hprimary");
                result = primaryValue;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
