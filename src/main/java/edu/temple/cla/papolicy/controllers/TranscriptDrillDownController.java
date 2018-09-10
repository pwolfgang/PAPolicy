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
package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.Utility;
import static edu.temple.cla.papolicy.controllers.TranscriptDownloadAndDrilldownUtil.getCommitteeNames;
import static edu.temple.cla.papolicy.controllers.TranscriptDownloadAndDrilldownUtil.getBillIdList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller to respond to requests for drilldown of transcript (house hearing) data
 * @author Paul Wolfgang
 */
public class TranscriptDrillDownController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(TranscriptDrillDownController.class);
    private JdbcTemplate jdbcTemplate;

    /**
     * Create the ModelAndView. The model will contain the referenced transcript
     * and the committees and bills referenced.
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @return Model containing the data and a reference to the TranscriptDrillDown jsp page
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String codedQuery = request.getParameter("query");
            String query = Utility.decodeAndDecompress(codedQuery);
            List<Map<String, Object>> theList = jdbcTemplate.queryForList(query);
            theList.forEach(row -> {
                String transcriptId = (String) row.get("ID");
                List<String> committeeNamesList = getCommitteeNames(jdbcTemplate, transcriptId);
                row.put("Committees", committeeNamesList);
                List<String> billIdList = getBillIdList(jdbcTemplate, transcriptId);
                row.put("Bills", billIdList);
            });
            Map<String, Object> theMap = new HashMap<>();
            theMap.put("theList", theList);
            return new ModelAndView("transcriptDrillDown", theMap);
        } catch (Exception ex) { // catch all exceptions
            LOGGER.error(ex);
            return null;
        }
    }

    /**
     * Set the jdbcTemplate from the parameter in the dispatcher-servlet.xml file.
     * (Called by the Spring framework.)
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
