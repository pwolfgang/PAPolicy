/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.tables;

import edu.temple.cla.papolicy.Utility;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.filters.HouseHearingsCommittee;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author Paul Wolfgang
 */
public class TranscriptTable extends StandardTable {

    @Override
    public QueryBuilder getUnfilteredTotalQuery() {
        QueryBuilder builder = new QueryBuilder();
        builder.addColumn(getYearColumn() + " AS TheYear");
        builder.addColumn("count(ID) AS TheValue");
        builder.setTable(getTableReference());
        return builder;
    }
    
    private String getTableReference() {
        List<Filter> filterList = getFilterList();
        boolean committeeFilterSelected = false;
        for (Filter filter : filterList) {
            if (filter.getClass() == HouseHearingsCommittee.class) {
                committeeFilterSelected = true;
            }
        }
        if (committeeFilterSelected) {
            return "Transcript_Committee join Transcript on transcriptID=ID";
        } else {
            return "Transcript";
        }
    }

    /**
     * Method to create the drilldown url.
     * This method returns the string &quot;drilldown.spg?query=<i>queyr</i>&quot;
     * Tables that need a different DrillDownController can override this method.
     * @param query The query string that gets the count
     * @return the url that will invoke the DrillDownController
     */
    @Override
    public String createDrillDownURL(QueryBuilder query) {
        QueryBuilder drillDownQuery = createDrillDownQuery(query).clone();
        return "transcriptdrilldown.spg?query=" + Utility.compressAndEncode(drillDownQuery.build());
    }

    /**
     * @param drillDownColumns the array of columns to display in the
     * drill-down page
     */
    @Override
    public void setDrillDownColumns(String[] drillDownColumns) {
        List<String> drillDownColumnsList = new ArrayList<String>();
        drillDownColumnsList.add("ID");
        drillDownColumnsList.addAll(Arrays.asList(drillDownColumns));
        this.drillDownColumns = drillDownColumnsList.toArray(new String[drillDownColumnsList.size()]);
    }
    
    @Override
    public TranscriptTable clone() {
        return (TranscriptTable)super.clone();
    }

}
