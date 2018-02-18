/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

/**
 * Encapsulation of a row label.  A row in the results may be either an
 * individual year or a legislative session consisting of an odd-even pair.
 * @author Paul Wolfgang
 */
public class YearOrSession {

    private final int minYear;
    private final int maxYear;
    private final boolean session;

    /**
     * Constructor.
     * @param minYear The first year of the session or the year
     * @param maxYear The second year of the session or the year
     * @param session  True if this represents a session.
     */
    public YearOrSession(int minYear, int maxYear, boolean session) {
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.session = session;
    }

    /**
     * Construct a string representation. If this is a session then the
     * result is yyyy-yy, otherwise it is a single yyyy.
     * @return 
     */
    @Override
    public String toString() {
        if (isSession()) {
            return String.format("%4d-%02d",getMinYear(), getMaxYear()%100);
        } else {
            return String.format("%4d", getMinYear());
        }
    }

    /**
     * Return the year, or the first year of the session.
     * @return the minYear
     */
    public int getMinYear() {
        return minYear;
    }

    /**
     * Return the year, or the second year of the session.
     * @return the maxYear
     */
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * Return true if this is a session.
     * @return the session
     */
    public boolean isSession() {
        return session;
    }



}
