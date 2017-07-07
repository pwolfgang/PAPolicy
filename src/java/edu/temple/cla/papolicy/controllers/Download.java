package edu.temple.cla.papolicy.controllers;

import java.io.IOException;
import org.apache.log4j.Logger;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorksheet;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorkbook;
import edu.temple.cla.papolicy.Utility;
import static edu.temple.cla.papolicy.controllers.DownloadAndDrilldownUtil.addColumn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd;
        MyWorkbook wb = null;
        MyWorksheet sheet = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            String[] columnNames = new String[numColumns];
            int[] columnTypes = new int[numColumns];
            for (int i = 0; i < numColumns; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
                columnTypes[i] = rsmd.getColumnType(i + 1);
            }
            wb = new MyWorkbook(response.getOutputStream());
            sheet = wb.getWorksheet();
            sheet.startRow();
            // for each column in the query results, create a spreadsheet column
            for (int i = 0; i < numColumns; i++) {
                sheet.addCell(columnNames[i]);
            }
            sheet.endRow();
            // For each row in the query results, create a spreadsheet row
            while (rs.next()) {
                sheet.startRow();
                for (int i = 0; i < numColumns; i++) {
                    addColumn(columnTypes[i], i, sheet, rs);
                }
                sheet.endRow();
            }
            sheet.close();
            sheet = null;
            wb.close();
            wb = null;
        } catch (SQLException ex) {
            LOGGER.error("Error reading table", ex);
        } catch (IOException ioex) {
            LOGGER.error(ioex);
        } catch (Throwable ex) {  // Catch any unexcpetied condition
            LOGGER.error("Unexpected fatal condition", ex);
        } finally {
            if (sheet != null) {
                sheet.close();
            }
            if (wb != null) {
                wb.close();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) { /* nothing more can be done */ }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) { /* nothing more can be done */ }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) { /* nothing more can be done */ }
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
