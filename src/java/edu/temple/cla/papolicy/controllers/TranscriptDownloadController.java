package edu.temple.cla.papolicy.controllers;

import static edu.temple.cla.papolicy.controllers.DownloadAndDrilldownUtil.getCommitteeNames;
import static edu.temple.cla.papolicy.controllers.DownloadAndDrilldownUtil.getBillIdList;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorkbook;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorksheet;
import edu.temple.cis.wolfgang.mycreatexlsx.Util;
import edu.temple.cla.papolicy.Utility;
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
import org.springframework.jdbc.core.JdbcTemplate;
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

    private static final Logger LOGGER = Logger.getLogger(TranscriptDownloadController.class);
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

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
        ResultSetMetaData rsmd;
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
                    Util.addColumn(columnTypes[i], i, sheet, rs);
                }
                List<String> committeeNamesList = getCommitteeNames(jdbcTemplate, transcriptId);
                String committeeNames = formatNames(committeeNamesList);
                sheet.addCell(committeeNames);
                List<String> billIdList = getBillIdList(jdbcTemplate, transcriptId);
                formatBillIds(billIdList);
                String billIds = formatNames(billIdList);
                sheet.addCell(billIds);
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
        } catch (Throwable ex) { // Want to catch any additional errors
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
     * Method to set the data source. This is called by the Spring framework.
     *
     * @param dataSource the datasource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Method to set the jdbcTemplate. This is called by the Spring framework.
     *
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Method to format the list of names into a single string. An empty list
     * is converted to the empty string. A list with one name is converted
     * to its first entry, and a list with more than one name is converted
     * to a comma separated list
     * @param names The List<String> to be formated
     * @return The formatted string.
     */
    private String formatNames(List<String> names) {
        if (names.isEmpty()) {
            return "";
        }
        if (names.size() == 1) {
            return names.get(0);
        }
        StringBuilder stb = new StringBuilder(names.get(0));
        for (int i = 1; i < names.size(); i++) {
            stb.append(", ");
            stb.append(names.get(i));
        }
        return stb.toString();
    }

    /**
     * Method to format the list of BillIDs Each billId is converted in place
     * @param billIds The List<String> to be converted
     */
    private void formatBillIds(List<String> billIds) {
        for (int i = 0; i < billIds.size(); i++) {
            billIds.set(i, formatBillId(billIds.get(i)));
        }
    }

    /**
     * Method to convert a billId from yyyysxxnnnn into either yyyy xx nn or
     * yyyy-s xx nn where yyyy is the session start year s is the special session
     * number xx is the type (HB, HR, SB, SR) and nnnn is the leading zero
     * padded bill number. If s is zero, then it is omitted. The leading
     * zeros are also omitted from the bill number.
     * @param billId String to be converted
     * @return Converted result.
     */
    private String formatBillId(String billId) {
        String sessionYear = billId.substring(0, 4);
        String specialSession = billId.substring(4, 5);
        String type = billId.substring(5, 7);
        int number = Integer.parseInt(billId.substring(7));
        if (!"0".equals(specialSession)) {
            return String.format("%s-%s %s %d",
                    sessionYear, specialSession, type, number);
        } else {
            return String.format("%s %s %d", sessionYear, type, number);
        }
    }

}
