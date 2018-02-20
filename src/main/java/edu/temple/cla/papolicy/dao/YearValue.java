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
package edu.temple.cla.papolicy.dao;

/**
 * A year-value pair
 * @author Paul Wolfgang
 */
public class YearValue {
    private final int year;
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
