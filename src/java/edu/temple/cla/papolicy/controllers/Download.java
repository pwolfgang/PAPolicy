/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.controllers;

import java.io.IOException;
import org.apache.log4j.Logger;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorksheet;
import edu.temple.cis.wolfgang.mycreatexlsx.MyWorkbook;
import edu.temple.cla.papolicy.Utility;
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
import static java.sql.Types.*;

/**
 * Controller to download requested datasets as ms-excel spreadsheet.
 * @author Paul Wolfgang
 */
public class Download extends AbstractController {

    private static Logger logger = Logger.getLogger(Download.class);
    private DataSource dataSource;

    /**
     * Respond to the http get request.
     * @param request The request object
     * @param response The response object
     * @return null to indicate no view.
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        // Extract the query from the request.
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
            // for each column in the query results, create a spreadsheet column
            for (int i = 0; i < numColumns; i++) {
                sheet.addCell(columnNames[i]);
            }
            sheet.endRow();
            // For each row in the query results, create a spreadsheet row
            while (rs.next()) {
                addRowToSheet(sheet, numColumns, columnTypes, rs);
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
            } catch (Exception e) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }
    /**
     * Method to add a row to the spreadsheet
     * @param sheet The spreadsheet object
     * @param numColumns Number of columns
     * @param columnTypes Array of column types
     * @param rs The result set pointing to the current row.
     */
    private void addRowToSheet(MyWorksheet sheet, int numColumns, int[] columnTypes, ResultSet rs) {
        sheet.startRow();
        for (int i = 0; i < numColumns; i++) {
            try {
                switch (columnTypes[i]) {
                    case BIT:
                    case TINYINT:
                    case SMALLINT:
                    case INTEGER:
                        addIntValue(sheet, i, rs);
                        break;
                    case BIGINT:
                        addLongValue(sheet, i, rs);
                        break;
                    case FLOAT:
                    case REAL:
                    case DOUBLE:
                    case NUMERIC:
                    case DECIMAL:
                        addDoubleValue(sheet, i, rs);
                        break;
                    case CHAR:
                    case VARCHAR:
                    case LONGVARCHAR:
                        addStringValue(sheet, i, rs);
                        break;
                    case DATE:
                    case TIME:
                    case TIMESTAMP:
                        addDateValue(sheet, i, rs);
                }
            } catch (Exception ex) {
                logger.error("Error converting cell", ex);
                sheet.addCell("null");
            }
        }
        sheet.endRow();
    }

    /**
     * Method to add an integer value to a cell in the spreadsheet
     * @param sheet The spreadsheet object
     * @param i The column index
     * @param rs The result set
     * @throws SQLException 
     */
    private void addIntValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        int intValue = rs.getInt(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(intValue);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to add a long integer value to a cell in the spreadsheet
     * @param sheet The spreadsheet object
     * @param i The column index
     * @param rs The result set
     * @throws SQLException 
     */
    private void addLongValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        long longValue = rs.getLong(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(longValue);
        } else {
            sheet.addCell("null");
        }

    }

    /**
     * Method to add a double value to a cell in the spreadsheet
     * @param sheet The spreadsheet object
     * @param i The column index
     * @param rs The result set
     * @throws SQLException 
     */
    private void addDoubleValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        double doubleValue = rs.getDouble(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(doubleValue);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to add a date value to a cell in the spreadsheet
     * @param sheet The spreadsheet object
     * @param i The column index
     * @param rs The result set
     * @throws SQLException 
     */
    private void addDateValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        String dateString = rs.getString(i + 1);
        if (!rs.wasNull()) {
            sheet.addDateCell(dateString);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to add a string value to a cell in the spreadsheet
     * @param sheet The spreadsheet object
     * @param i The column index
     * @param rs The result set
     * @throws SQLException 
     */
    private void addStringValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        String stringValue = rs.getString(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(stringValue);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to set the datasource object. (Called by Spring framework)
     * @param datasource the datasource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Method to return the datasource object. (Probably not used).
     * @return The datasource
     */
    public DataSource getDataSource() {
        return dataSource;
    }
}
