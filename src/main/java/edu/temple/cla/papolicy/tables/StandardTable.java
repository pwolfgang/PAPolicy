/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
"        \n"+
"            <input type=\"checkbox\" name=\"dataset\" value=\""+getId()+"\"\n"+
"                 id=\"t"+getId()+"\" onclick=\"expandFilters("+getId()+");expandNote("+getId()+");\" />\n"+
"            <label for=\"t"+getId()+"\"><span class=\"strong\">"+getTableTitle()+"</span></label>\n"+
"        \n"+
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
