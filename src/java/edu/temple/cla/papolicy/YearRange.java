/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import java.util.Iterator;

/**
 *
 * @author Paul Wolfgang
 */
public class YearRange {
    private int minYear;
    private int maxYear;
    private boolean span;

    public YearRange() {}

    public YearRange(int minYear, int maxYear) {
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    public YearRange(String startYear, String endYear, String startSession, String endSession, String span) {
        if (span.equals("years")) {
            this.span = false;
            minYear = Integer.parseInt(startYear);
            maxYear = Integer.parseInt(endYear);
        } else {
            this.span = true;
            minYear = Integer.parseInt(startSession);
            maxYear = Integer.parseInt(endSession);
        }
    }
    
    public boolean isSession() {return span;}


    /**
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
        if (span) {
            return minYear - 2;
        } else {
            return minYear - 1;
        }
    }

    /**
     * @param minYear the minYear to set
     */
    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    /**
     * @return the maxYear
     */
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * @param maxYear the maxYear to set
     */
    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    /** Get an asscending year option range
     *  @return A sequence of options from the min year to the max year
     */
    public String getAscendingYears() {
        StringBuilder stb = new StringBuilder();
        for (int i = minYear; i <= maxYear; i++) {
            stb.append("\r\n<option>");
            stb.append(i);
            stb.append("</option>");
        }
        return stb.toString();
    }

    /** Get a dedending year option range
     * @return A sequence of options from max year to the min year
     */
    public String getDecendingYears() {
        StringBuilder stb = new StringBuilder();
        for (int i = maxYear; i >= minYear; i--) {
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
        for (int i = minSessionYear; i <= maxSessionYear; i += 2) {
            stb.append("\r\n<option value=\"");
            stb.append(i);
            stb.append("\">");
            stb.append(String.format("%4d-%02d", i, (i + 1) % 100));
            stb.append("</option>");
        }
        return stb.toString();
    }

    /**
     * Get a decending sequence of legislative session options
     * @return A sequenc of options from the mix legislative session to
     * the minimum legislative session
     */
    public String getDecendingSessions() {
        int minSessionYear = (minYear-1)/2 * 2 + 1;
        int maxSessionYear = (maxYear+1)/2 * 2;
        StringBuilder stb = new StringBuilder();
        for (int i = maxSessionYear; i >= minSessionYear; i -= 2) {
            stb.append("\r\n<option value=\"");
            stb.append(i);
            stb.append("\">");
            stb.append(String.format("%4d-%02d", i-1, i%100));
            stb.append("</option>");
        }
        return stb.toString();
    }

    public Iterator<YearOrSession> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<YearOrSession> {

        private int i = 0;

        public boolean hasNext() {
            return i < maxYear - minYear + 1;
        }
        
        public YearOrSession next() {
            int nextVal = minYear + i;
            YearOrSession returnVal;
            if (span) {
                returnVal = new YearOrSession(nextVal, nextVal + 1, true);
                i += 2;
            } else {
                returnVal = new YearOrSession(nextVal, nextVal, false);
                i++;
            }
            return returnVal;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
