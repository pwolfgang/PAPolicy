/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

/**
 *
 * @author Paul Wolfgang
 */
public class LawsTable extends BillsTable {

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (chamber != null && chamber.length == 1) {
            stb.append(chamber[0]);
            stb.append(" ");
        }
        stb.append("Laws");
        return stb.toString();
    }


    @Override
    public String getYearColumn() {
        return "Year_Enacted";
    }


}
