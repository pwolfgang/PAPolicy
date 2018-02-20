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

import java.io.IOException;
import org.apache.log4j.Logger;
import edu.temple.cla.policydb.wolfgang.mycreatexlsx.Util;
import edu.temple.cla.papolicy.Utility;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Class to generate the excel workbook of the selected data.
 *
 * @author Paul Wolfgang
 */
public class Download extends AbstractController {

    private static Logger LOGGER = Logger.getLogger(Download.class);


    private DataSource dataSource;
    private AbstractController transcriptDownloadController;

    /**
     * Create the ModelAndView
     *
     * @param request The HttpServletRequest. The query parameter is a coded
     * compressed SQL query to select the data to be entered into the
     * spreadsheet.
     * @param response The HttpServletResponse. It provides the output stream to
     * which the downloaded data is written.
     * @return null. This controller does not create a ModleAndView.
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        if (requestURI.matches(".*House.?Hearings.*")) {
            try {
                return transcriptDownloadController.handleRequest(request, response);
            } catch (Throwable ex) {
                LOGGER.error(ex);
            }
        }
        // Extract the query from the request.
        String query = Utility.decodeAndDecompress(request.getParameter("query"));
        response.setContentType("application/ms-excel");
        try {
        OutputStream out = response.getOutputStream();
            Util.buildSpreadsheetFromQuery(dataSource, query, out);
        } catch (IOException ioex) {
            LOGGER.error(ioex);
        }
        return null;
    }

    /**
     * Method to set the DataSource. This method is called by the Spring
     * framework.
     * @param dataSource the datasource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * Method to set the transcriptDownloadControler. This method is
     * called by the Spring framework.
     * @param transcriptDownloadController
     */
    public void setTranscriptDownloadController(AbstractController transcriptDownloadController) {
        this.transcriptDownloadController = transcriptDownloadController;
    }

}
