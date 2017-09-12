package edu.temple.cla.papolicy.controllers;

import java.io.IOException;
import org.apache.log4j.Logger;
import edu.temple.cis.wolfgang.mycreatexlsx.Util;
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
            Util.BuildSpreadsheetFromQuery(dataSource, query, out);
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
