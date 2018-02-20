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
package edu.temple.cla.papolicy.tables;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class StandardTableTest {
    
    private StandardTable testTable;
    private String[] drillDownColumns;
    
    public StandardTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Title", "Orginization", "Year", "Month", "Day", "Abstract", "FinalCode"};
        testTable = new StandardTable();
        testTable.setId(6);
        testTable.setTableName("LegServiceAgencyReports");
        testTable.setTableTitle("Legislative Service Agency Reports");
        testTable.setMajorOnly(false);
        testTable.setMinYear(1991);
        testTable.setMaxYear(2006);
        testTable.setTextColumn("Abstract");
        testTable.setLinkColumn("Hyperlink");
        testTable.setDrillDownColumns(drillDownColumns);
        testTable.setCodeColumn("FinalCode");
        testTable.setNoteColumn(null);
        testTable.setYearColumn("Year");
    }

    @Test
    public void testGetTitleBox() {
        String expected = "\n" +
"        \n" +
"            <input type=\"checkbox\" name=\"dataset\" value=\"6\"\n" +
"                 id=\"t6\" onclick=\"expandFilters(6);expandNote(6);\" />\n" +
"            <label for=\"t6\"><span class=\"strong\">Legislative Service Agency Reports</span></label>\n" +
"        \n" +
"        <div id=\"note6\" class=\"nnoottee\"><p></p></div>\n" +
"        ";
        String result = testTable.getTitleBox();
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        Object theClone = testTable.clone();
        assertEquals(testTable.getClass(), theClone.getClass());
    }
}