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
import java.util.NoSuchElementException;

/**
 * An encapsulation of a range of years or sessions.
 * @author Paul Wolfgang
 */
public class YearRange {
    private int minYear;
    private int maxYear;
    private boolean session;

    /**
     * No-argument constructor. Creates an un-initialized YearRange object.
     */
    public YearRange() {}

    /**
     * Construct a YearRange object from a pair of years.
     * @param minYear The start year
     * @param maxYear The end year
     */
    public YearRange(int minYear, int maxYear) {
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    /**
     * Construct a YearRange object from the form response
     * @param startYear The selected start year
     * @param endYear The selected end year
     * @param startSession The selected start session
     * @param endSession The selected end session
     * @param span Indicates whether this is years or session.
     */
    public YearRange(String startYear, String endYear, String startSession, String endSession, String span) {
        if (span.equals("years")) {
            this.session = false;
            minYear = Integer.parseInt(startYear);
            maxYear = Integer.parseInt(endYear);
        } else {
            this.session = true;
            minYear = Integer.parseInt(startSession);
            maxYear = Integer.parseInt(endSession);
        }
    }
    
    /**
     * Return true if this year range is a range of sessions.
     * @return true if this year range is a range of sessions.
     */
    public boolean isSession() {return session;}


    /**
     * Return the start year
     * @return the minYear
     */
    public int getMinYear() {
        return minYear;
    }

    /**
     * Get the predecessor to the min year. If sessions this is the minYear -2
     * @return the predecessor to the min year.
     */
    public int getMinYearPredecessor() {
        if (session) {
            return minYear - 2;
        } else {
            return minYear - 1;
        }
    }

    /**
     * Set the start year.
     * @param minYear the minYear to set
     */
    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    /**
     * Return the end year
     * @return the maxYear
     */
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * Set the end year
     * @param maxYear the maxYear to set
     */
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    /** Get an ascending year option range
     *  @return A sequence of options from the min year to the max year
     */
    public String getAscendingYears() {
        StringBuilder stb = new StringBuilder();
        stb.append("\r\n<option selected=\"selected\">");
        stb.append(minYear);
        stb.append("</option>");
        for (int i = minYear+1; i <= maxYear; i++) {
            stb.append("\r\n<option>");
            stb.append(i);
            stb.append("</option>");
        }
        return stb.toString();
    }

    /** Get a descending year option range
     * @return A sequence of options from max year to the min year
     */
    public String getDescendingYears() {
        StringBuilder stb = new StringBuilder();
        stb.append("\r\n<option selected=\"selected\">");
        stb.append(maxYear);
        stb.append("</option>");
        for (int i = maxYear-1; i >= minYear; i--) {
            stb.append("\r\n<option>");
            stb.append(i);
            stb.append("</option>");
        }
        return stb.toString();
    }

    /**
     * Get an ascending sequence of legislative session options
     * @return A sequence of options from the minimum legislative session
     * to the maximum legislative session
     */
    public String getAscendingSessions() {
        int minSessionYear = (minYear-1)/2 * 2 + 1;
        int maxSessionYear = (maxYear+1)/2 * 2;
        StringBuilder stb = new StringBuilder();
        stb.append("\r\n<option value=\"");
        stb.append(minSessionYear);
        stb.append("\" selected=\"selected\">");
        stb.append(String.format("%4d-%02d",minSessionYear, (minSessionYear+1) % 100));
        stb.append("</option>");
        for (int i = minSessionYear+2; i <= maxSessionYear; i += 2) {
            stb.append("\r\n<option value=\"");
            stb.append(i);
            stb.append("\">");
            stb.append(String.format("%4d-%02d", i, (i + 1) % 100));
            stb.append("</option>");
        }
        return stb.toString();
    }

    /**
     * Get a descending sequence of legislative session options
     * @return A sequence of options from the mix legislative session to
     * the minimum legislative session
     */
    public String getDescendingSessions() {
        int minSessionYear = (minYear-1)/2 * 2 + 1;
        int maxSessionYear = (maxYear+1)/2 * 2;
        StringBuilder stb = new StringBuilder();
        stb.append("\r\n<option value=\"");
        stb.append(maxSessionYear);
        stb.append("\" selected=\"selected\">");
        stb.append(String.format("%4d-%02d", maxSessionYear-1, maxSessionYear%100));
        stb.append("</option>");
        for (int i = maxSessionYear-2; i >= minSessionYear; i -= 2) {
            stb.append("\r\n<option value=\"");
            stb.append(i);
            stb.append("\">");
            stb.append(String.format("%4d-%02d", i-1, i%100));
            stb.append("</option>");
        }
        return stb.toString();
    }

    /**
     * Create and return an iterator of YearOrSession objects included
     * in this YearRange.
     * @return An Iterator of YearOrSession objects.
     */
    public Iterator<YearOrSession> iterator() {
        return new Iter();
    }

    /**
     * Internal class to implement the iterator.
     */
    private class Iter implements Iterator<YearOrSession> {

        private int i = 0;

        /**
         * Determine if there are more items to be returned by the iterator.
         * @return True if next returns a next object
         */
        @Override
        public boolean hasNext() {
            return i < maxYear - minYear + 1;
        }
        
        /**
         * Advance the iterator and return the next item in the iteration.
         * @return The next item in the iteration.
         * @throws NoSuchElementExcepiton if no more items are available.
         */
        @Override
        public YearOrSession next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int nextVal = minYear + i;
            YearOrSession returnVal;
            if (session) {
                returnVal = new YearOrSession(nextVal, nextVal + 1, true);
                i += 2;
            } else {
                returnVal = new YearOrSession(nextVal, nextVal, false);
                i++;
            }
            return returnVal;
        }

        /**
         * The remove method is not supported.
         * @throws UnsupportedOperationException
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
