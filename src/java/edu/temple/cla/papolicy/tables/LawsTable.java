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

    private String[] drillDownColumns = null;

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

    @Override
    public String[] getDrillDownColumns() {
        if (drillDownColumns == null) {
            String[] superDrillDownColumns = super.getDrillDownColumns();
            drillDownColumns = new String[superDrillDownColumns.length + 1];
            drillDownColumns[0] = "Act_No";
            System.arraycopy(superDrillDownColumns, 0,
                    drillDownColumns, 1, superDrillDownColumns.length);
        }
        return drillDownColumns;
    }

}
