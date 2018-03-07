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

import static edu.temple.cla.papolicy.controllers.TranscriptDownloadAndDrilldownUtil.getCommitteeNames;
import static edu.temple.cla.papolicy.controllers.TranscriptDownloadAndDrilldownUtil.getBillIdList;
import edu.temple.cla.policydb.wolfgang.mycreatexlsx.MyWorkbook;
import edu.temple.cla.policydb.wolfgang.mycreatexlsx.MyWorksheet;
import edu.temple.cla.papolicy.Utility;
import java.io.IOException;
import java.io.OutputStream;
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

    private void buildSpreadsheetFromQuery(String query, OutputStream out) {
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    ResultSetMetaData rsmd = rs.getMetaData();
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
                    try (MyWorkbook wb = new MyWorkbook(out)) {
                        try (MyWorksheet sheet = wb.getWorksheet()) {
                            sheet.startRow();
                            // for each column in the query results, create a spreadsheet column
                            for (int i = 0; i < numColumns; i++) {
                                sheet.addCell(columnNames[i]);
                            }
                            sheet.endRow();
                            // For each row in the query results, create a spreadsheet row
                            while (rs.next()) {
                                String transcriptId = rs.getString("ID");
                                sheet.startRow();
                                for (int i = 0; i < numColumns; i++) {
                                    DownloadUtility.addColumn(columnTypes[i], i, sheet, rs);
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
                        }
                    }
                } catch (SQLException ex) {
                    LOGGER.error("Error reading table", ex);
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Error opening connection", ex);
        } catch (Throwable ex) {
            // Catch any unexcpetied condition
            LOGGER.error("Unexpected fatal condition", ex);
        }
    }

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
        try {
            OutputStream out = response.getOutputStream();
            buildSpreadsheetFromQuery(query, out);
        } catch (IOException ioex) {
            LOGGER.error("Error obtaining OutputStream from response", ioex);
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
     * Method to format the list of names into a single string. An empty list is
     * converted to the empty string. A list with one name is converted to its
     * first entry, and a list with more than one name is converted to a comma
     * separated list
     *
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
     *
     * @param billIds The List<String> to be converted
     */
    private void formatBillIds(List<String> billIds) {
        for (int i = 0; i < billIds.size(); i++) {
            billIds.set(i, formatBillId(billIds.get(i)));
        }
    }

    /**
     * Method to convert a billId from yyyysxxnnnn into either yyyy xx nn or
     * yyyy-s xx nn where yyyy is the session start year s is the special
     * session number xx is the type (HB, HR, SB, SR) and nnnn is the leading
     * zero padded bill number. If s is zero, then it is omitted. The leading
     * zeros are also omitted from the bill number.
     *
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
