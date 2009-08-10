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
        return {{
<dl>
  <dt><input type="checkbox" name="dataset" value="{{getId()}}" id="t{{getId()}}"
  onclick="expandFilters({{getId()}});expandSubTables({{getId()}});"/>
          <span class="strong">{{getTableTitle()}}</span></dt>
  <div class="subtbl" id="subtbl{{getId()}}">
    <dd><input type="checkbox" name="hchmbr" value="House">
          <span class="strong">House</span></dd>
    <dd><input type="checkbox" name="hchmbr" value="Senate">
          <span class="strong">Senate</span></dd>
  </div>
</dl>
        }};
    }

}
