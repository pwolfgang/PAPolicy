/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

/**
 *
 * @author Paul Wolfgang
 */
public class StandardTable extends AbstractTable implements Table {

    /**
     * Method to generate the HTML code for the title box.
     */
    public String getTitleBox() {
        return "\n"+
"        <dl><dt>\n"+
"            <input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"\"\n"+
"                 id=\"t"+getId()+"\" onclick=\"expandFilters("+getId()+");\" />\n"+
"            <span class=\"strong\">"+getTableTitle()+"</span>\n"+
"        </dt></dl> ";
    }
}
