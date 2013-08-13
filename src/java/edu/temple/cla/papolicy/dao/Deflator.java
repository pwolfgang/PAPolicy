/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 * The Deflator is the chained GDP deflator using the data from the
 * Office of Management of the Budget. It provides inflation adjustments relative
 * to a base year.
 * http://www.whitehouse.gov/sites/default/files/omb/budget/fy2013/assets/hist.pdf
 * @author Paul Wolfgang
 */
public class Deflator {

    private int year;
    private double GDP;
    private double priceIndex;

    public Deflator(int year, double GDP, double priceIndex) {
        this.year = year;
        this.GDP = GDP;
        this.priceIndex = priceIndex;
    }
    
    public Deflator() {}
    
    /**
     * The fiscal year
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * The fiscal year
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gross Domestic Product in billions of dollars (un adjusted)
     * @return the GDP
     */
    public double getGDP() {
        return GDP;
    }

    /**
     * Gross Domestic Product in billions of dollars (un adjusted)
     * @param GDP the GDP to set
     */
    public void setGDP(double GDP) {
        this.GDP = GDP;
    }

    /**
     * Chained price index with 2005=1
     * @return the priceIndex
     */
    public double getPriceIndex() {
        return priceIndex;
    }

    /**
     * Chained price index with 2005=1
     * @param priceIndex the priceIndex to set
     */
    public void setPriceIndex(double priceIndex) {
        this.priceIndex = priceIndex;
    }

}
