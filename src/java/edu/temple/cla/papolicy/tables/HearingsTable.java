/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.*;

/**
 *
 * @author Paul Wolfgang
 */
public class HearingsTable extends AbstractTable {

    /**
     * Generate table title box. Overrides method in super.
     */
    @Override
    public String getTitleBox() {
        return "\n"+
"<dl>\n"+
"  <dt><input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"\" id=\"t"+getId()+"\"\n"+
"  onclick=\"expandFilters("+getId()+");expandSubTables("+getId()+");\"/>\n"+
"          <span class=\"strong\">"+getTableTitle()+"</span></dt>\n"+
"  <div class=\"subtbl\" id=\"subtbl"+getId()+"\">\n"+
"    <dd><input type=\"checkbox\" name=\"hchmbr\" value=\"House\">\n"+
"          <span class=\"strong\">House</span></dd>\n"+
"    <dd><input type=\"checkbox\" name=\"hchmbr\" value=\"Senate\">\n"+
"          <span class=\"strong\">Senate</span></dd>\n"+
"  </div>\n"+
"</dl>\n"+
"        ";
    }

}
