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

    private static Logger logger = Logger.getLogger(Download.class);
    private DataSource dataSource;

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
        String query = Utility.decodeAndDecompress(request.getParameter("query"));
        response.setContentType("application/ms-excel");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
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
            for (int i = 0; i < numColumns; i++) {
                sheet.addCell(columnNames[i]);
            }
            sheet.endRow();
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
            logger.error("Error reading table", ex);
        } catch (IOException ioex) {
            logger.error(ioex);
        } catch (Throwable ex) {
            logger.error("Unexpected fatal condition", ex);
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

}
