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
 * A checkbox filter allows the selection of an option.  Actually two radio
 * buttons are displayed. The name checkbox is historical. The change to
 * radio buttons was made to support accessibility. 
 * @author Paul Wolfgang
 */
public class CheckBoxFilter extends Filter {

    private final String parameterName;
    private String parameterValue;

    private String filterQualifier;

    public CheckBoxFilter(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    /**
     * Construct the filter form input as a set of two radio buttons: 
     * NotSelected (default) Selected
     * @return HTML to generate the form input.
     */
    @Override
    public String getFilterFormInput() {
        return "\n"+
"            <fieldset><legend>"+getDescription()+"</legend>\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"0\" value=\"0\" checked=\"checked\"/>"
                + "&nbsp; <label for=\""+parameterName+"0\">Not Selected</label>\n"+
"                  <input type=\"radio\" name=\""+parameterName+"\" id=\""+parameterName+"1\" value=\"1\" />"
                + "&nbsp; <label for=\""+parameterName+"1\">Selected</label>\n" +
"            </fieldset>\n";
        }

    /**
     * Method to capture the request parameters. It then builds the filter
     * strings.
     * @param request 
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    /**
     * Method to build the filter expression and filter qualifier.
     */
    private void buildFilterStrings() {
        if ("1".equals(parameterValue)) {
            filterQuery = new Comparison(getColumnName(), "<>", "0");
            filterQualifier = getDescription();
        } else {
            filterQuery = new EmptyExpression();
            filterQualifier = "";
        }
    }

    /**
     * Return the filter qualifier
     * @return filterQualifier
     */
    @Override
    public String getFilterQualifier() {
        return filterQualifier;
    }
}
