/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.policydb.queryBuilder.Disjunction;
import edu.temple.cla.policydb.queryBuilder.Like;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
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
"\n<input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"\"\n"+
"        id=\"t"+getId()+"\" onclick=\"expandBills("+getId()+");\" />\n"+                
"        <label for=\"t"+getId()+"\"><span class=\"strong\">"+getTableTitle()+"</span></label>\n\n"+
"<div class=\"subtbl\" id=\"subtbl"+getId()+"\">\n"+
"    <fieldset><legend>Chamber</legend>\n"+              
"    <input type=\"checkbox\" name=\"chamber\" value=\"House\" id=\"houseChamber\" checked=\"checked\" />\n"+
"        <label for=\"houseChamber\"><span class=\"strong\">House</span></label>\n"+
"    \n"+
"    <input type=\"checkbox\" name=\"chamber\" value=\"Senate\" id=\"senateChamber\" checked=\"checked\" />\n"+
"        <label for=\"senateChamber\"><span class=\"strong\">Senate</span></label>\n"+
"    "+
"    </fieldset>\n"+
     "<fieldset><legend>Type</legend>\n"+           
"    <input type=\"radio\" name=\"billtype\" value=\"BOTH\" id=\"billtypeBoth\" checked=\"checked\" />\n"+
"        <label for=\"billtypeBoth\"><span class=\"strong\">Bills and Resolutions</span></label>\n"+
"    <br/>\n"+
"    <input type=\"radio\" name=\"billtype\" value=\"BILLS\" id=\"billtypeBills\" />\n"+
"        <label for=\"billtypeBills\"><span class=\"strong\">Bills</span></label>\n"+
"    <br/>\n"+
"    <input type=\"radio\" name=\"billtype\" value=\"RES\" id=\"billtypeRes\" />\n"+
"        <label for=\"billtypeRes\"><span class=\"strong\">Resolutions</span></label>\n"+
"    \n"+
"    </fieldset>\n"+
"    <fieldset><legend>Session Type</legend>\n"+                
"    <input type=\"radio\" name=\"sessiontype\" value=\"REGULAR\" id=\"regularSessions\" checked=\"checked\" />\n"+
"        <label for=\"regularSessions\"><span class=\"strong\">Regular Sessions Only</span></label>\n"+
"    <br/><input type=\"radio\" name=\"sessiontype\" value=\"SPECIAL\" id=\"specialSessions\"/>\n"+
"        <label for=\"specialSessions\"><span class=\"strong\">Special Sessions Only</span></label>\n"+
"    <br/><input type=\"radio\" name=\"sessiontype\" value=\"BOTH\" id=\"bothSessionTypes\"/>\n"+
"        <label for=\"bothSessionTypes\"><span class=\"strong\">Both Regular and Special Sessions</span></label>\n"+
"    </fieldset>\n"+
"</div>\n"+
"\n"+
"\n"+
"<br/><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"a\"\n"+
"           id=\"t"+getId()+"a\" onclick=\'expandBills(\""+getId()+"a\");\' />\n"+
"    <label for=\"t"+getId()+"a\"><span class=\"strong\">Acts (Laws) and Adopted Resolutions</span></label>\n"+
"<div class=\"subtbl\" id=\"subtbl"+getId()+"a\">\n"+
"    <fieldset><legend>Chamber</legend>\n"+              
"    <input type=\"checkbox\" name=\"chambera\" value=\"House\" id=\"houseChambera\" checked=\"checked\" />\n"+
"        <label for=\"houseChambera\"><span class=\"strong\">House</span></label>\n"+
"    \n"+
"    <br/><input type=\"checkbox\" name=\"chambera\" value=\"Senate\" id=\"senateChambera\" checked=\"checked\" />\n"+
"        <label for=\"senateChambera\"><span class=\"strong\">Senate</span></label>\n"+
"    "+
"    </fieldset>\n"+
     "<fieldset><legend>Type</legend>\n"+           
"    <br/><input type=\"radio\" name=\"billtypea\" value=\"BOTH\" id=\"billtypeBotha\" checked=\"checked\" />\n"+
"        <label for=\"billtypeBotha\"><span class=\"strong\">Acts and Adopted Resolutions</span></label>\n"+
"    \n"+
"    <br/><input type=\"radio\" name=\"billtypea\" value=\"BILLS\" id=\"billtypeBillsa\" />\n"+
"        <label for=\"billtypeBillsa\"><span class=\"strong\">Acts</span></label>\n"+
"    \n"+
"    <br/><input type=\"radio\" name=\"billtypea\" value=\"RES\" id=\"billtypeResa\" />\n"+
"        <label for=\"billtypeResa\"><span class=\"strong\">Adopted Resolutions</span></label>\n"+
"    \n"+
"    </fieldset>\n"+
"    <fieldset><legend>Session Type</legend>\n"+                
"    <input type=\"radio\" name=\"sessiontypea\" value=\"REGULAR\" id=\"regularSessionsa\" checked=\"checked\" />\n"+
"        <label for=\"regularSessionsa\"><span class=\"strong\">Regular Sessions Only</span></label>\n"+
"    <br/><input type=\"radio\" name=\"sessiontypea\" value=\"SPECIAL\" id=\"specialSessionsa\"/>\n"+
"        <label for=\"specialSessionsa\"><span class=\"strong\">Special Sessions Only</span></label>\n"+
"    <br/><input type=\"radio\" name=\"sessiontypea\" value=\"BOTH\" id=\"bothSessionTypesa\"/>\n"+
"        <label for=\"bothSessionTypesa\"><span class=\"strong\">Both Regular and Special Sessions</span></label>\n"+
"    </fieldset>\n"+
"</div>\n"+
"\n"+
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
        return "year(Date_Referred)";
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
