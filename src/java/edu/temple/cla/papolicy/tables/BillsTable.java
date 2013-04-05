/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.queryBuilder.Disjunction;
import edu.temple.cla.papolicy.queryBuilder.Like;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Paul Wolfgang
 */
public class BillsTable extends AbstractTable {

    String[] chamber;
    String billType;
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
"<dl><dt><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"a\"\n"+
"           id=\"t"+getId()+"a\" onclick=\'expandBills(\""+getId()+"a\");\' />\n"+
"    <span class=\"strong\">Acts (Laws) and Adopted Resolutions</span></dt>\n"+
"<div class=\"subtbl\" id=\"subtbl"+getId()+"a\">\n"+
"    <dd><input type=\"checkbox\" name=\"chambera\" value=\"House\" checked=\"checked\" />\n"+
"        <span class=\"strong\">House</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"checkbox\" name=\"chambera\" value=\"Senate\" checked=\"checked\" />\n"+
"        <span class=\"strong\">Senate</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"billtypea\" value=\"BOTH\" checked=\"checked\" />\n"+
"        <span class=\"strong\">Acts and Adopted Resolutions</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"billtypea\" value=\"BILLS\" />\n"+
"        <span class=\"strong\">Acts</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"billtypea\" value=\"RES\" />\n"+
"        <span class=\"strong\">Adopted Resolutions</span>\n"+
"    </dd>\n"+
"    <dd><input type=\"radio\" name=\"sessiontypea\" value=\"REGULAR\" checked=\"checked\" />\n"+
"        <span class=\"strong\">Regular Sessions Only</span></dd>\n"+
"    <dd><input type=\"radio\" name=\"sessiontypea\" value=\"SPECIAL\" />\n"+
"        <span class=\"strong\">Special Sessions Only</span></dd>\n"+
"    <dd><input type=\"radio\" name=\"sessiontypea\" value=\"BOTH\" />\n"+
"        <span class=\"strong\">Both Regular and Special Sessions</strong></dd>\n"+
"</dl>\n"+
"</div>\n"+
"</dt>\n"+
"        ";
    }

    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        chamber = request.getParameterValues("chamber");
        if (chamber == null || chamber.length == 0) {
            chamber = new String[]{"House", "Senate"};
        }
        billType = request.getParameter("billtype");
        sessionType = request.getParameter("sessiontype");
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (chamber != null && chamber.length == 1) {
            stb.append(chamber[0]);
            stb.append(" ");
        }
        if ("BILLS".equals(billType)) {
            stb.append("Bills");
        } else if ("RES".equals(billType)) {
            stb.append("Resolutions");
        } else {
            stb.append("Bills and Resolutions");
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
    public QueryBuilder getUnfilteredTotalQuery() {
        QueryBuilder builder = new QueryBuilder();
        builder.setTable(getTableName());
        builder.addColumn(getYearColumn() + " AS TheYear");
        builder.addColumn("count(ID) AS TheValue");
        if (!"BOTH".equals(billType) || chamber.length != 2) {
            Disjunction chamberSelect = new Disjunction();
            for (String aChamber : chamber) {
                if ("BOTH".equals(billType) || ("BILLS".equals(billType))) {
                    if (aChamber.equals("House")) {
                        chamberSelect.addTerm(new Like("Bill", "HB%"));
                    } else {
                        chamberSelect.addTerm(new Like("Bill", "SB%"));
                    }
                }
                if ("BOTH".equals(billType) || ("RES".equals(billType))) {
                    if (aChamber.equals("House")) {
                        chamberSelect.addTerm(new Like("Bill", "HR%")); 
                   } else {
                        chamberSelect.addTerm(new Like("Bill", "SR%"));
                    }
                }
            }
            builder.addToSelectCriteria(chamberSelect);
        }
        if (!sessionType.equals("BOTH")) {
            if (sessionType.equals("REGULAR")) {
                builder.addToSelectCriteria(new Like("Session", "%-%-%", true));
            } else {
                builder.addToSelectCriteria(new Like("Session", "%-%-%"));
            }
        }
        return builder;
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
    
    @Override
    public BillsTable clone() {
        BillsTable theClone = (BillsTable)super.clone();
        if (chamber != null) {
            theClone.chamber = chamber.clone();
        }
        return theClone;
    }

}
