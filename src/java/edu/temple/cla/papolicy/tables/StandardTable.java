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
"                 id=\"t"+getId()+"\" onclick=\"expandFilters("+getId()+");expandNote("+getId()+");\" />\n"+
"            <label for=\"t"+getId()+"\"><span class=\"strong\">"+getTableTitle()+"</span></label>\n"+
"        </dt></dl>\n"+
"        <div id=\"note"+getId()+"\" class=\"nnoottee\"><p>"+getNoteColumn()+"</p></div>\n"+
"        ";
    }
    
    @Override
    public StandardTable clone() {
        return (StandardTable) super.clone();
   }
}
