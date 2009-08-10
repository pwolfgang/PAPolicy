/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 *
 * @author Paul Wolfgang
 */
public class YearValue {
    private int year;
    private Number value;

    public YearValue(int year, Number value) {
        this.year = year;
        this.value = value;
    }

    public int getYear() {return year;}

    public Number getValue() {return value;}

    public void setValue(Number value) {
        this.value = value;
    }
}
