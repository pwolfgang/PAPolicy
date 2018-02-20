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

import edu.temple.cla.papolicy.Utility;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.filters.HouseHearingsCommittee;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
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
                HouseHearingsCommittee houseHearingsFilter = (HouseHearingsCommittee)filter;
                committeeFilterSelected |= houseHearingsFilter.isSelected();
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
        List<String> drillDownColumnsList = new ArrayList<>();
        drillDownColumnsList.add("ID");
        drillDownColumnsList.addAll(Arrays.asList(drillDownColumns));
        this.drillDownColumns = drillDownColumnsList.toArray(new String[drillDownColumnsList.size()]);
    }
    
    /**
     * Create a deep copy of this object.
     * Note: the super class catches CloneNotSupported
     * @return A deep copy of this object
     */
    @Override
    public TranscriptTable clone() {
        return (TranscriptTable)super.clone();
    }
    
}
