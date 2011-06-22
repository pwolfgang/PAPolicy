/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        if ("BILLS".equals(billtype)) {
            stb.append("Acts");
        } else if ("RES".equals(billtype)) {
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
        return "Year_Enacted";
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
        billtype = request.getParameter("billtypea");
        sessionType = request.getParameter("sessiontypea");
    }
    
    @Override
    public LawsTable clone() {
        LawsTable theClone = (LawsTable)clone();
        if (drillDownColumns != null) {
            theClone.drillDownColumns = drillDownColumns.clone();
        }
        return theClone;
    }
    
}
