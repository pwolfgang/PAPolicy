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

import edu.temple.cla.papolicy.dao.CommitteeName;
import edu.temple.cla.papolicy.dao.CommitteeNameMapper;
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
public class HouseHearingsCommitteeTest {

    HouseHearingsCommittee filter;
    @Mocked
    HttpServletRequest request;
    @Mocked
    JdbcTemplate jdbcTemplate;

    public HouseHearingsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new HouseHearingsCommittee(1201, 4, "House Committee", "Committee", "CommitteeNames", "House");
        new Expectations() {
            {
                CommitteeName committee = new CommitteeName();
                committee.setCtyCode(101);
                committee.setName("Aging and Older Adult Services");
                jdbcTemplate.query(anyString, new CommitteeNameMapper());minTimes=0;
                result = Arrays.asList(new CommitteeName[]{committee});
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testGetFilterFormInput() {
        String expected = "<label for=\"F1201\">House Hearings</label>\n" +
"                <br/>\n" +
"<select name=\"F1201\" id=\"F1201\">\n" +
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n" +
"<option value=\"101\">Aging and Older Adult Services</option>\n" +
"</select><br/>\n";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryStringAll() {
        setParameterValue("ALL");
        String result = filter.getFilterQuery().toString();
        assertEquals("", result);
    }

    @Test
    public void testGetFilterQuery101() {
        setParameterValue("101");
        String result = filter.getFilterQuery().toString();
        assertEquals("CtyCode=101", result);
    }

    @Test
    public void testGetFilterQualifierAll() {
        setParameterValue("ALL");
        String expected = "";
        assertEquals(expected, filter.getFilterQualifier());
    }

    @Test
    public void testGetFilterQualifier101() {
        setParameterValue("101");
        String expected = "Committee: Aging and Older Adult Services";
        assertEquals(expected, filter.getFilterQualifier());
    }


    private void setParameterValue(final String parameterValue) {
        new Expectations() {
            {
                request.getParameter("F1201");
                result = parameterValue;
            }
        };
        filter.setFilterParameterValues(request);
    }
}
