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
public class BillsTable extends AbstractTable {

    String[] chamber;
    String billtype;
    String sessionType;


    /**
     * Generate title box
     */
    @Override
    public String getTitleBox(){
        return "\n"+
"<dl>\r\n<dt><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"\"\n"+
"        id=\"t"+getId()+"\" onclick=\"expandBills("+getId()+");\" />\n"+
"        <span class=\"strong\">"+getTableTitle()+"</span></dt>\r\n\n"+
"<div class=\"subtbl\" id=\"subtbl"+getId()+"\">\n"+
"    <dd><input type=\"checkbox\" name=\"chamber\" value=\"House\" checked=\"checked\" />\n"+
"        <span class=\"strong\">House</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"checkbox\" name=\"chamber\" value=\"Senate\" checked=\"checked\" />\n"+
"        <span class=\"strong\">Senate</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"billtype\" value=\"BOTH\" checked=\"checked\" />\n"+
"        <span class=\"strong\">Bills and Resolutions</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"billtype\" value=\"BILLS\" />\n"+
"        <span class=\"strong\">Bills</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"billtype\" value=\"RES\" />\n"+
"        <span class=\"strong\">Resolutions</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"sessiontype\" value=\"REGULAR\" checked=\"checked\" />\n"+
"        <span class=\"strong\">Regular Sessions Only</span></dd>\n"+
"    <dd><input type=\"radio\" name=\"sessiontype\" value=\"SPECIAL\" />\n"+
"        <span class=\"strong\">Special Sessions Only</span></dd>\n"+
"    <dd><input type=\"radio\" name=\"sessiontype\" value=\"BOTH\" />\n"+
"        <span class=\"strong\">Both Regular and Special Sessions</strong></dd>\n"+
"</dl>\n"+
"</div>\n"+
"<dl><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"a\"\n"+
"           id=\"t"+getId()+"a\" onclick=\"expandBills("+getId()+");\" />\n"+
"    <span class=\"strong\">Acts (Laws) and Adopted Resolutions</span>\n"+
"</dl>\n"+
"</dt>\n"+
"        ";
    }

    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        chamber = request.getParameterValues("chamber");
        if (chamber == null || chamber.length == 0) {
            chamber = new String[]{"House", "Senate"};
        }
        billtype = request.getParameter("billtype");
        sessionType = request.getParameter("sessiontype");
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (chamber != null && chamber.length == 1) {
            stb.append(chamber[0]);
            stb.append(" ");
        }
        if ("BILLS".equals(billtype)) {
            stb.append("Bills");
        } else if ("RES".equals(billtype)) {
            stb.append("Resolutions");
        } else {
            stb.append("Bills and Resolutions");
        }
        stb.append(getFilterQualifierString());
        return stb.toString();
    }

    @Override
    public String getTotalQueryString() {
        StringBuilder stb = new StringBuilder();
        stb.append("SELECT ");
        stb.append(getYearColumn());
        stb.append(" AS TheYear, count(ID) AS TheValue FROM ");
        stb.append(getTableName());
        stb.append(" WHERE ");
        if (!"BOTH".equals(billtype) || chamber.length != 2) {
            StringBuilder chamberSelect = new StringBuilder("(");
            for (String aChamber : chamber) {
                if ("BOTH".equals(billtype) || ("BILLS".equals(billtype))) {
                    if (aChamber.equals("House")) {
                        chamberSelect.append("Bill LIKE ('HB%')");
                    } else {
                        chamberSelect.append("Bill LIKE ('SB%')");
                    }
                    chamberSelect.append(" OR ");
                }
                if ("BOTH".equals(billtype) || ("RES".equals(billtype))) {
                    if (aChamber.equals("House")) {
                        chamberSelect.append("Bill LIKE ('HR%')");
                    } else {
                        chamberSelect.append("Bill LIKE ('SR%')");
                    }
                    chamberSelect.append(" OR ");
                }
            }
            stb.append(chamberSelect.subSequence(0, chamberSelect.length()-4));
            stb.append(")");
        }
        if (!sessionType.equals("BOTH")) {
            if (!stb.toString().endsWith(" WHERE ")) {
                stb.append(" AND ");
            }
            if (sessionType.equals("REGULAR")) {
                stb.append(" Session NOT LIKE('%-%-%')");
            } else {
                stb.append(" Session LIKE('%-%-%')");
            }
        }
        if (!stb.toString().endsWith(" WHERE ")
                && getFilterQueryString().length() != 0) {
            stb.append(" AND ");
        }
        stb.append(getFilterQueryString());
        return stb.toString();
    }

    @Override
    public String getYearColumn() {
        return "Year_Referred";
    }

    @Override
    public AbstractTable getSubTable(char qualifier) {
        if (qualifier == 'a') {
            AbstractTable newTable = new LawsTable();
            newTable.setId(getId());
            newTable.setTableName(getTableName());
            newTable.setTableTitle(getTableTitle());
            newTable.setMajorOnly(isMajorOnly());
            newTable.setTextColumn(getTextColumn());
            newTable.setDrillDownColumns(getDrillDownColumns());
            newTable.setLinkColumn(getLinkColumn());
            newTable.setMinYear(getMinYear());
            newTable.setMaxYear(getMaxYear());
            newTable.setCodeColumn(getCodeColumn());
            return newTable;
        } else {
            return this;
        }
    }

}
