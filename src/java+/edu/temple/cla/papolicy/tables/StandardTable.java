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
        return {{
        <dl><dt>
            <input type="checkbox" name="dataset" value="{{getId()}}"
                 id="t{{getId()}}" onclick="expandFilters({{getId()}});expandNote({{getId()}});" />
            <span class="strong">{{getTableTitle()}}</span>
        </dt></dl>
        <div id="note{{getId()}}" class="nnoottee"><p>{{getNoteColumn()}}</p></div>
        }};
    }
}
