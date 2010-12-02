/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

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
 *
 * @author Paul Wolfgang
 */
public class Download extends AbstractController{

    private DataSource dataSource;


    /**
     * Create the ModelAndView
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String query = Utility.decodeAndDecompress(request.getParameter("query"));
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
                columnNames[i] = rsmd.getColumnName(i+1);
                columnTypes[i] = rsmd.getColumnType(i+1);
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
                    try {
                    switch (columnTypes[i]) {
                        case BIT:
                        case TINYINT:
                        case SMALLINT:
                        case INTEGER:
                            int intValue = rs.getInt(i+1);
                            if (!rs.wasNull()) {
                                sheet.addCell(intValue);
                            } else {
                                sheet.addCell("null");
                            }
                            break;
                        case BIGINT:
                            long longValue = rs.getLong(i+1);
                            if (!rs.wasNull()) {
                                sheet.addCell(longValue);
                            } else {
                                sheet.addCell("null");
                            }
                            break;
                        case FLOAT:
                        case REAL:
                        case DOUBLE:
                        case NUMERIC:
                        case DECIMAL:
                            double doubleValue = rs.getDouble(i+1);
                            if (!rs.wasNull()) {
                                sheet.addCell(doubleValue);
                            } else {
                                sheet.addCell("null");
                            }
                            break;
                        case CHAR:
                        case VARCHAR:
                        case LONGVARCHAR:
                            String stringValue = rs.getString(i+1);
                            if (!rs.wasNull()) {
                                sheet.addCell(stringValue);
                            } else {
                                sheet.addCell("null");
                            }
                            break;
                        case DATE:
                        case TIME:
                        case TIMESTAMP:
                            String dateString = rs.getString(i+1);
                             if (!rs.wasNull()) {
                                 sheet.addDateCell(dateString);
                            } else {
                                 sheet.addCell("null");
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("Error converting cell", ex);
                        sheet.addCell("null");
                    }
                }
                sheet.endRow();
            }
            sheet.close();
            sheet = null;
            wb.close();
            wb = null;
        } catch (SQLException ex) {
            logger.error("Error reading table", ex);
        } finally {
            if (sheet != null) sheet.close();
            if (wb != null) wb.close();
            try {if (rs != null) rs.close();} catch (Exception e) { }
            try {if (stmt != null) stmt.close();} catch (Exception e) { }
            try {if (conn != null) conn.close();} catch (Exception e) { }
        }
        return null;
    }

    /**
     * @param datasource the datasource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {return dataSource;}

}
