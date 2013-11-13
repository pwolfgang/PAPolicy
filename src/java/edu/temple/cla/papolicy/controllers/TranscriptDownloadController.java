package edu.temple.cla.papolicy.controllers;

import static edu.temple.cla.papolicy.controllers.DownloadAndDrilldownUtil.getCommitteeNames;
import static edu.temple.cla.papolicy.controllers.DownloadAndDrilldownUtil.getBillIdList;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorkbook;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorksheet;
import edu.temple.cla.papolicy.Utility;
import static edu.temple.cla.papolicy.controllers.DownloadAndDrilldownUtil.addColumn;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Class to generate the excel spreadsheet of the Transcript (House Hearing)
 * data. This controller duplicates what the standard download controller does
 * and then adds columns to identify the committee(s) and bill(s).
 *
 * @author Paul Wolfgang
 */
public class TranscriptDownloadController extends AbstractController {

    private static final Logger logger = Logger.getLogger(TranscriptDownloadController.class);
    private DataSource dataSource;
    private SimpleJdbcTemplate jdbcTemplate;

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
            String[] columnNames = new String[numColumns + 2];
            int[] columnTypes = new int[numColumns + 2];
            for (int i = 0; i < numColumns; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
                columnTypes[i] = rsmd.getColumnType(i + 1);
            }
            columnNames[numColumns] = "Committees";
            columnTypes[numColumns] = Types.VARCHAR;
            columnNames[numColumns + 1] = "Bills";
            columnTypes[numColumns + 1] = Types.VARCHAR;
            wb = new MyWorkbook(response.getOutputStream());
            sheet = wb.getWorksheet();
            sheet.startRow();
            for (int i = 0; i < numColumns + 2; i++) {
                sheet.addCell(columnNames[i]);
            }
            sheet.endRow();
            while (rs.next()) {
                String transcriptId = rs.getString("ID");
                sheet.startRow();
                for (int i = 0; i < numColumns; i++) {
                    addColumn(columnTypes[i], i, sheet, rs);
                }
                List<String> committeeNamesList = getCommitteeNames(jdbcTemplate, transcriptId);
                String committeeNames = formatNames(committeeNamesList);
                sheet.addCell(committeeNames);
                List<String> billIdList = getBillIdList(jdbcTemplate, transcriptId);
                String billIds = formatNames(billIdList);
                sheet.addCell(billIds);
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
     * @param datasource the datasource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private String formatNames(List<String> names) {
        if (names.isEmpty()) {
            return "";
        }
        if (names.size() == 1) {
            return names.get(0);
        }
        StringBuilder stb = new StringBuilder(names.get(0));
        for (int i = 1; i < names.size(); i++) {
            stb.append("\n");
            stb.append(names.get(i));
        }
        return stb.toString();
    }
}
