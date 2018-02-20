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
public class HearingsCommitteeTest {

    HearingsCommittee filter;
    @Mocked
    JdbcTemplate jdbcTemplate;

    public HearingsCommitteeTest() {
    }

    @Before
    public void setUp() {
        filter = new HearingsCommittee(102, 5, "Senate Committee", null, "CommitteeAliases", "Senate");
    }

    @Test
    public void testGetFilterFormInput() {
        new Expectations() {
            {
                CommitteeAlias committee = new CommitteeAlias();
                committee.setID(39);
                committee.setCtyCode(201);
                committee.setAlternateName("Aging and Youth");
                committee.setName("Aging and Youth");
                committee.setStartYear(0);
                committee.setEndYear(9999);
                jdbcTemplate.query(anyString, new CommitteeAliasMapper());
                result = Arrays.asList(committee);
            }
        };
        filter.setJdbcTemplate(jdbcTemplate);
        String expected = "<label for=\"F102\">Senate Hearings</label>\n" +
"                <br /><select name=\"F102\" id=\"F102\">\n" +
"                <option value=\"ALL\" selected=\"selected\">ALL COMMITTEES</option>\n"
                + "<option value=\"201\">Aging and Youth</option>\n"
                + "</select><br/>";
        assertEquals(expected, filter.getFilterFormInput());
    }

    @Test
    public void testGetFilterQueryString() {
        String expected = "";
        String result = filter.getFilterQuery().toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetFilterQualifier() {
        String expected = "";
        String result = filter.getFilterQualifier();
        assertEquals(expected, result);
    }

}
