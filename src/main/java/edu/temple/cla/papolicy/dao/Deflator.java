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
