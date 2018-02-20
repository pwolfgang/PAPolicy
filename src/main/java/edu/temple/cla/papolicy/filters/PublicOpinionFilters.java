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

import edu.temple.cla.policydb.queryBuilder.EmptyExpression;
import javax.servlet.http.HttpServletRequest;

/**
 * The PublicOpinionFilters are not actual filters.
 * They display choices for the display of the public opinion data.
 * @author Paul Wolfgang
 */
public class PublicOpinionFilters extends Filter {

    private String mipdisp;

    /**
     * Construct a Filter
     * @param id The unique ID of this filter from the database
     * @param tableId The unique ID of the referencing table
     * @param description not used
     * @param columnName not used
     * @param tableReference not used
     * @param additionalParam not used
     */
    public PublicOpinionFilters(int id, int tableId, String description,
            String columnName, String tableReference, String additionalParam) {
        super(id, tableId, description, columnName, tableReference,
                additionalParam);
        filterQuery = new EmptyExpression();
    }

    /**
     * Generate the HTML to display the filter form input.
     * Displays a set of radio buttons to select either
     * Percent or Rank
     * @return HTML to display the filter form input.
     */

    @Override
    public String getFilterFormInput() {
        return "<fieldset><legend>Display as</legend>"
                + "<input type=\"radio\" id=\"mipdisp0\" name=\"mipdisp\" value=\"0\" "
                + "checked=\"checked\" /><label for=\"mipdisp0\">Percent</label>\n"
                + "<input type=\"radio\" id=\"mipdisp1\" name=\"mipdisp\" value=\"1\" />"
                + "<label for=\"mipdisp1\">Rank</label></fieldset>";
    }

    /**
     * Method to capture the form input from the HTTP request.
     * @param request 
     */
    @Override
    public void setFilterParameterValues(HttpServletRequest request){
        mipdisp = request.getParameter("mipdisp");
    }

    /**
     * FilterQualifier is not applicable to this filter.
     * @return The empty string
     */
    @Override
    public String getFilterQualifier() {
        return "";
    }

    /**
     * Return the selected choice.
     * @return the mipdisp
     */
    public String getMipdisp() {
        return mipdisp;
    }


}
