/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

/**
 * A year-value pair
 * @author Paul Wolfgang
 */
public class YearValue {
    private int year;
    private Number value;

    public YearValue(int year, Number value) {
        this.year = year;
        this.value = value;
    }

    /**
     * The year
     * @return the year.
     */
    public int getYear() {return year;}

    /**
     * The value
     * @return the value
     */
    public Number getValue() {return value;}

    /**
     * Set the value
     * @param value the value to be set
     */
    public void setValue(Number value) {
        this.value = value;
    }
    
    /**
     * Determine of two YearValue objects are equal
     * @param o The other object
     * @return true if the year and value are equal
     */
    @Override
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
    
    /**
     * Hash code is combination of value and year.
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return value.hashCode() * 31 + year;
    }
    
    /**
     * Return a string representation
     * @return a string representation
     */
    @Override
    public String toString() {
        return year + ":" + value;
    }
    
    
}
