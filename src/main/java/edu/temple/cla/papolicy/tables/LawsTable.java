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
package edu.temple.cla.papolicy.tables;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class LawsTable extends BillsTable {

    private String[] drillDownColumns = null;

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (chamber != null && chamber.length == 1) {
            stb.append(chamber[0]);
            stb.append(" ");
        }
        if ("BILLS".equals(billType)) {
            stb.append("Acts");
        } else if ("RES".equals(billType)) {
            stb.append("Adopted Resolutions");
        } else {
            stb.append("Acts and Resolutions");
        }
        if ("REGULAR".equals(sessionType)) {
            stb.append(" Regular Sessions ");
        } else if ("SPECIAL".equals(sessionType)) {
            stb.append(" Special Sessions ");
        } else {
            stb.append(" Regular and Special Sessions ");
        }
        stb.append(getFilterQualifierString());
        return stb.toString();
    }


    @Override
    public String getYearColumn() {
        return "year(Date_Enacted)";
    }

    @Override
    public String[] getDrillDownColumns() {
        if (drillDownColumns == null) {
            String[] superDrillDownColumns = super.getDrillDownColumns();
            drillDownColumns = new String[superDrillDownColumns.length + 1];
            drillDownColumns[0] = "Act_No";
            System.arraycopy(superDrillDownColumns, 0,
                    drillDownColumns, 1, superDrillDownColumns.length);
        }
        return drillDownColumns;
    }

    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        chamber = request.getParameterValues("chambera");
        if (chamber == null || chamber.length == 0) {
            chamber = new String[]{"House", "Senate"};
        }
        billType = request.getParameter("billtypea");
        sessionType = request.getParameter("sessiontypea");
    }
    
    @Override
    public LawsTable clone() {
        LawsTable theClone = (LawsTable)super.clone();
        if (drillDownColumns != null) {
            theClone.drillDownColumns = drillDownColumns.clone();
        }
        return theClone;
    }
    
}
