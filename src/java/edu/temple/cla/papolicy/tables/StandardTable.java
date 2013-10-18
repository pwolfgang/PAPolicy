package edu.temple.cla.papolicy.tables;

/**
 * The standard table uses the default implementaiton of the Table interface
 * The title box is the simplest.
 * @author Paul Wolfgang
 */
public class StandardTable extends AbstractTable implements Table {

    /**
     * Method to generate the HTML code for the title box. The title box
     * displays a single checkbox with the table title. It also includes
     * calls to the expandFilters and expandNote JavaScript functions which
     * will then expand the filters box when the checkbox is checked. Also,
     * if a note is provided in the Tables table, then it will also be
     * displayed when the box is checked.
     * @return HTML for the title box.
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
    
    /**
     * Make a copy of this table object. For the standard table a shalow copy is adequate.
     * @return copy of this table object.
     */
    @Override
    public StandardTable clone() {
        return (StandardTable) super.clone();
   }
}
