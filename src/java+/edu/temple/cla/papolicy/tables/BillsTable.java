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
        return {{
<dl>\r\n<dt><input type="checkbox" name="dataset" value="{{getId()}}"
        id="t{{getId()}}" onclick="expandBills({{getId()}});" />
        <span class="strong">{{getTableTitle()}}</span></dt>\r\n
<div class="subtbl" id="subtbl{{getId()}}">
    <dd><input type="checkbox" name="chamber" value="House" checked="checked" />
        <span class="strong">House</span>
    </dd>
    <dd><input type="checkbox" name="chamber" value="Senate" checked="checked" />
        <span class="strong">Senate</span>
    </dd>
    <dd><input type="radio" name="billtype" value="BOTH" checked="checked" />
        <span class="strong">Bills and Resolutions</span>
    </dd>
    <dd><input type="radio" name="billtype" value="BILLS" />
        <span class="strong">Bills</span>
    </dd>
    <dd><input type="radio" name="billtype" value="RES" />
        <span class="strong">Resolutions</span>
    </dd>
    <dd><input type="radio" name="sessiontype" value="REGULAR" checked="checked" />
        <span class="strong">Regular Sessions Only</span></dd>
    <dd><input type="radio" name="sessiontype" value="SPECIAL" />
        <span class="strong">Special Sessions Only</span></dd>
    <dd><input type="radio" name="sessiontype" value="BOTH" />
        <span class="strong">Both Regular and Special Sessions</strong></dd>
</dl>
</div>
<dl><input type="checkbox" name="dataset" value="{{getId()}}a"
           id="t{{getId()}}a" onclick="expandBills({{getId()}});" />
    <span class="strong">Laws</span>
</dl>
</dt>
        }};
    }

    @Override
    public void setAdditionalParameters(HttpServletRequest request) {
        chamber = request.getParameterValues("chamber");
        if (chamber == null || chamber.length == 0) {
            chamber = new String[]{"house", "senate"};
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
        if ("BOTH".equals(billtype) && chamber.length == 2) {
            // do nothing
        } else {
            StringBuilder chamberSelect = new StringBuilder("(");
            for (String aChamber : chamber) {
                if ("BOTH".equals(billtype) || ("BILLS".equals(billtype))) {
                    if (aChamber.equals("house")) {
                        chamberSelect.append("Bill LIKE ('HB%')");
                    } else {
                        chamberSelect.append("Bill LIKE ('SB%')");
                    }
                    chamberSelect.append(" OR ");
                }
                if ("BOTH".equals(billtype) || ("RES".equals(billtype))) {
                    if (aChamber.equals("house")) {
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
        if (!stb.toString().endsWith(" WHERE ")
                && getFilterQueryString().length() != 0) {
            stb.append(" AND ");
        }
        if (sessionType.equals("BOTH")) {
            // do nothing
        } else if (sessionType.equals("REGULAR")) {
            stb.append(" Session NOT LIKE('%-%-%')");
        } else {
            stb.append(" Session LIKE('%-%-%')");
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
            return newTable;
        } else {
            return this;
        }
    }

}
