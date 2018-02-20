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

import edu.temple.cla.papolicy.filters.Filter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class LawsTableTest {

    @Mocked HttpServletRequest request;
    private LawsTable testTable;
    private String[] drillDownColumns;
    
    public LawsTableTest() {
    }
    
    @Before
    public void setUp() {
        drillDownColumns = new String[]{"Session", "Bill", "Abstract", "Code"};
        Table billsTable = new BillsTable();
        billsTable.setId(3);
        billsTable.setTableName("Bills_Data");
        billsTable.setTableTitle("Bills and Resolutions");
        billsTable.setMajorOnly(false);
        billsTable.setMinYear(1975);
        billsTable.setMaxYear(2008);
        billsTable.setTextColumn("Abstract");
        billsTable.setLinkColumn("Hyperlink");
        billsTable.setDrillDownColumns(drillDownColumns);
        billsTable.setCodeColumn("Code");
        billsTable.setNoteColumn(null);
        billsTable.setYearColumn(null);
        billsTable.setFilterList(new ArrayList<Filter>());
        testTable = (LawsTable)billsTable.getSubTable('a');
        testTable.setFilterList(new ArrayList<Filter>());
    }

    @Test
    public void testToString() {
        String expected = "Acts and Resolutions Regular and Special Sessions ";
        assertEquals(expected, testTable.toString());
    }

    @Test
    public void testGetYearColumn() {
        String expected = "Year_Enacted";
    }

    @Test
    public void testGetDrillDownColumns() {
        String[] expected = new String[]{"Act_No", "Session", "Bill", "Abstract", "Code"};
        String[] actual = testTable.getDrillDownColumns();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testClone() {
        LawsTable theClone = testTable.clone();
        assertEquals(testTable.getClass(), theClone.getClass());
    }
}