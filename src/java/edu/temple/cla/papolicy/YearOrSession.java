/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

/**
 *
 * @author Paul Wolfgang
 */
public class YearOrSession {

    private int minYear;
    private int maxYear;
    private boolean session;

    public YearOrSession(int minYear, int maxYear, boolean session) {
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.session = session;
    }

    @Override
    public String toString() {
        if (isSession()) {
            return String.format("%4d-%02d",getMinYear(), getMaxYear()%100);
        } else {
            return Integer.toString(getMinYear());
        }
    }

    /**
     * @return the minYear
     */
    public int getMinYear() {
        return minYear;
    }

    /**
     * @return the maxYear
     */
    public int getMaxYear() {
        return maxYear;
    }

    /**
     * @return the session
     */
    public boolean isSession() {
        return session;
    }



}
