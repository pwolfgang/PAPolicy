/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * @param session Indicates whether this is years or session.
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
     * Get the predicessor to the min year. If sessions this is the minYear -2
     * @return the predicessor to the min year.
     */
    public int getMinYearPredicessor() {
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
        stb.append("\r\n<option selected=\"selected\">");
        stb.append(minSessionYear);
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
        stb.append("\r\n<option selected=\"selected\">");
        stb.append(maxSessionYear);
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
