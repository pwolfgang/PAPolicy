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
package edu.temple.cla.papolicy;

import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class YearRangeTest {

    public YearRangeTest() {
    }

    /**
     * Test the parameterized constuctor for a year range
     */
    @Test
    public void testYearRange() {
        YearRange instance = new YearRange("1998", "2005", "1979", "2005", "years");
        assertEquals(1998, instance.getMinYear());
        assertEquals(2005, instance.getMaxYear());
    }

    /**
     * Test the parameterized constuctor for a year range
     */
    @Test
    public void testSessionRange() {
        YearRange instance = new YearRange("1998", "2005", "1979", "2002", "session");
        assertEquals(1979, instance.getMinYear());
        assertEquals(2002, instance.getMaxYear());
    }

    /**
     * Test iterator over a range of years
     */
    @Test
    public void testIteratorYears() {
        YearRange instance = new YearRange("1998", "2000", "1977", "2004", "years");
        Iterator<YearOrSession> itr = instance.iterator();
        assertTrue(itr.hasNext());
        assertEquals("1998", itr.next().toString());
        assertTrue(itr.hasNext());
        assertEquals(1999, itr.next().getMaxYear());
        assertTrue(itr.hasNext());
        assertEquals(2000, itr.next().getMinYear());
        assertTrue(!itr.hasNext());
    }

    /**
     * Test iterator over a range of years
     */
    @Test
    public void testIteratorSessions() {
        YearRange instance = new YearRange("1998", "2000", "1979", "1984", "sessions");
        Iterator<YearOrSession> itr = instance.iterator();
        assertTrue(itr.hasNext());
        assertEquals("1979-80", itr.next().toString());
        assertTrue(itr.hasNext());
        assertEquals(1982, itr.next().getMaxYear());
        assertTrue(itr.hasNext());
        assertEquals(1983, itr.next().getMinYear());
        assertTrue(!itr.hasNext());
    }

}