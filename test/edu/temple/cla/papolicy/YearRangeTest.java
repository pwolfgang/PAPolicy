/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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