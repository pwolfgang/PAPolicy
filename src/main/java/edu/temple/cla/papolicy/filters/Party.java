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
package edu.temple.cla.papolicy.filters;

import edu.temple.cla.policydb.queryBuilder.Comparison;
import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import javax.servlet.http.HttpServletRequest;

/**
 * The Party filter is a special case of the binary filter. It displays three
 * radio buttons: NO FILTER, Republican, Democrat. 
 * This filter is only used by the Bills/Acts dataset.
 * @author Paul Wolfgang
 */
public class Party extends Filter implements Cloneable {

    private String parameterName;
    private String parameterValue;

    private String filterQualifier;

    /**
     * Construct a Party object
     * @param id unique ID
     * @param tableId Table containing the dataset
     * @param description Description of the filter
     * @param columnName Column containing the data to be filtered
     * @param tableReference not used.
     * @param additionalParam not used.
     */
    public Party(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    /**
     * Construct the filter form input as a set of three radio buttons: 
     * No Filter, Republican, Democrat.
     * @return HTML to generate the form input.
     */
    @Override
    public String getFilterFormInput() {
        return "<fieldset><legend>"+getDescription()+"</legend>\n"+
"              <input type=\"radio\" id=\"F"+getId()+"N\" name=\"F"+getId()+"\" value=\"NOFILTER\" checked=\"checked\" />"
                + "&nbsp;<label for=\"F"+getId()+"N\">Both</label>\n"+
"              <input type=\"radio\" id=\"F"+getId()+"R\" name=\"F"+getId()+"\" value=\"0\" />"
                + "&nbsp;<label for=\"F"+getId()+"R\">Republican</label>\n"+
"              <input type=\"radio\" id=\"F"+getId()+"D\" name=\"F"+getId()+"\" value=\"1\" />"
                + "&nbsp;<label for=\"F"+getId()+"D\">Democrat</label>\n"
            + "</fieldset>";
    }

    /**
     * Method to capture the form input
     * @param request HTTP request object
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    /**
     * Method to build the filter query based on the form input.
     */
    private void buildFilterStrings() {
        if ("NOFILTER".equals(parameterValue)) {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        } else if ("0".equals(parameterValue)) { // Republican
            filterQuery = new Comparison(getColumnName(), "=", "0");
            filterQualifier = "Sponsored by a Republican";
        } else if ("1".equals(parameterValue)) { // Democrat
            filterQuery = new Comparison(getColumnName(), "=", "1");
            filterQualifier = "Sponsored by a Democrat";
        }
    }
    
//    @Override
//    /**
//     * Return an array of Filter instances when multiple filter choices
//     * have been selected. This feature allows for a filter to be used to
//     * select all variations which will be displayed in separate columns
//     * in the result table. It was added as a choice to the Party filter
//     * to allow comparisons between number of bills/acts introduced by
//     * one party vs the other. Since there are only two parties, the
//     * resulting graphs were mirror images. The choice has been removed
//     * from the filter choices.
//     * If "ALL" was selected, an array of two filter instances
//     * one selecting Republican and the other selecting Democrat is returned.
//     * Otherwise an array containing this is returned.
//     * @return An array of the filter choices.
//     */
//    public Party[] getFilterChoices() {
//        if ("ALL".equals(parameterValue)) {
//            Party[] result = new Party[2];
//            result[0] = clone();
//            result[1] = clone();
//            result[0].parameterValue = "0";
//            result[0].buildFilterStrings();
//            result[1].parameterValue = "1";
//            result[1].buildFilterStrings();
//            return result;
//        } else {
//            return new Party[]{this};
//        }
//    }
    
//    /**
//     * Return the number of filter choices.
//     * 
//     * @return Default return value of 1
//     */
//    @Override
//    public int getNumberOfFilterChoices() {
//        if ("ALL".equals(parameterValue)) {
//            return 2;
//        } else {
//            return 1;
//        }
//    }
    
    /**
     * Make a copy of this Party object. 
     * Since the data fields are all strings, a shallow copy is all that
     * is required.
     * @return A copy of this object.
     */
    @Override
    public Party clone() {
        try {
            return (Party) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("CloneNotSupportedException should never be thrown");
        }
    }

    /**
     * Return the string that describes the filtered data
     * @return The string that describes the filtered data
     */
    @Override
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
