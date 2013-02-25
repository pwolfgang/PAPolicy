/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 *
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
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the GDP
     */
    public double getGDP() {
        return GDP;
    }

    /**
     * @param GDP the GDP to set
     */
    public void setGDP(double GDP) {
        this.GDP = GDP;
    }

    /**
     * @return the priceIndex
     */
    public double getPriceIndex() {
        return priceIndex;
    }

    /**
     * @param priceIndex the priceIndex to set
     */
    public void setPriceIndex(double priceIndex) {
        this.priceIndex = priceIndex;
    }

}
