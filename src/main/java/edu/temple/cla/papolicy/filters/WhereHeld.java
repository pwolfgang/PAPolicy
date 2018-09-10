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
 * The WhereHeld filter is a special case of the BinaryFilter. It displays a set
 * of radio buttons BOTH, &lt;city&gt;, Outside &lt;city&gt;. The &lt;city&gt; is
 * set to "Harrisburg" by the additionalParameter from the Filters database row.
 * This allows this to be used in other states.
 *
 * @author Paul Wolfgang
 */
public class WhereHeld extends Filter {

    private static final String BOTH = "587";
    private final String parameterName;
    private String parameterValue;
    private String filterQualifier;

    /**
     * Construct a Filter
     *
     * @param id The unique ID of this filter from the database
     * @param tableId The unique ID of the referencing table
     * @param description The description displayed on the form
     * @param columnName The column of the database which this filter is based
     * on.
     * @param tableReference not used.
     * @param additionalParam Specifies the capital city.
     */
    public WhereHeld(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        parameterName = "F" + getId();
    }

    /**
     * Generate the HTML to display the filter form input. It displays a set of
     * radio buttons BOTH, &lt;city&gt;, Outside &lt;city&gt;.
     *
     * @return HTML to display the filter form input.
     */
    @Override
    public String getFilterFormInput() {
        return "       <fieldset><legend>" + getDescription() + "</legend>\n"
                + "              <input type=\"radio\" name=\"" + parameterName + "\" value=\"" + BOTH + "\" id=\"" + parameterName + "B\" checked=\"checked\" />"
                + "<label for=\"" + parameterName + "B\">Both</label>\n"
                + "              <input type=\"radio\" name=\"" + parameterName + "\" value=\"0\" id=\"" + parameterName + "0\" />"
                + "<label for=\"" + parameterName + "0\">" + getAdditionalParam() + "</label>\n"
                + "              <input type=\"radio\" name=\"" + parameterName + "\" value=\"1\" id=\"" + parameterName + "1\" />"
                + "<label for=\"" + parameterName + "1\">Outside " + getAdditionalParam() + "</label>"
                + "</fieldset>";
    }

    /**
     * Method to capture the form input from the HTTP request.
     * @param request  The HTTP request.
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request) {
        parameterValue = request.getParameter(parameterName);
        buildFilterStrings();
    }

    /**
     * Method to build the filterQuery and filterQualifier based on the
     * filter form input.
     */
    private void buildFilterStrings() {
        if (null == parameterValue) {
            filterQuery = new Comparison(getColumnName(), "<>", "\'" 
                    + getAdditionalParam() + "\'");
            filterQualifier = "Held outside " + getAdditionalParam();
        } else switch (parameterValue) {
            case BOTH:
                filterQuery = new EmptyExpression();
                filterQualifier = "";
                break;
            case "0":
                filterQuery = new Comparison(getColumnName(), "=", "\'"
                        + getAdditionalParam() + "\'");
                filterQualifier = "Held in " + getAdditionalParam();
                break;
            default:
                filterQuery = new Comparison(getColumnName(), "<>", "\'"
                        + getAdditionalParam() + "\'");
                filterQualifier = "Held outside " + getAdditionalParam();
                break;
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
