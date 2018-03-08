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
     * @return String representation of a session or year.
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
