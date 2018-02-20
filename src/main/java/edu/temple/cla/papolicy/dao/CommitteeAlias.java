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
 * CommitteeAlias represents the table that contains the different names
 * that a committee had over its lifetime.
 * @author Paul Wolfgang
 */
public class CommitteeAlias {

    private int ID;
    private int ctyCode;
    private String name;
    private String alternateName;
    private int startYear;
    private int endYear;

    /**
     * Unique ID used as primary key.
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Unique ID used as primary key.
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * The committee code. Unique ID of the committee.
     * @return the ctyCode
     */
    public int getCtyCode() {
        return ctyCode;
    }

    /**
     * The committee code.
     * @param ctyCode the ctyCode to set
     */
    public void setCtyCode(int ctyCode) {
        this.ctyCode = ctyCode;
    }

    /**
     * The committee current name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The committee current name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * An alternate name for this committee.
     * @return the alternateName
     */
    public String getAlternateName() {
        return alternateName;
    }

    /**
     * An alternate name for this committee.
     * @param alternateName the alternateName to set
     */
    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    /**
     * The year that the committee started using this alternate name.
     * @return the startYear
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * The year that the committee started using this alternate name.
     * @param startYear the startYear to set
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     * The last year that the committee used this alternate name.
     * @return the endYear
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * The last year that the committee used this alternate name.
     * @param endYear the endYear to set
     */
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

}
