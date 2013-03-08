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
    
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (this.getClass() == o.getClass()) {
            YearValue other = (YearValue) o;
            return this.year == other.year && this.value.equals(other.value);
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return value.hashCode() * 31 + year;
    }
    
    public String toString() {
        return year + ":" + value;
    }
    
    
}
