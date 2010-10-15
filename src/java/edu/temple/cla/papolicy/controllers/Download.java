/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import java.io.OutputStream;
import java.util.Date;
import edu.temple.cla.papolicy.Utility;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("sheet 1");
            CreationHelper helper = wb.getCreationHelper();
            int rowNum = 0;
            Row row = sheet.createRow(rowNum);
            for (int i = 0; i < numColumns; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(helper.createRichTextString(columnNames[i]));
            }
            while (rs.next()) {
                rowNum++;
                row = sheet.createRow(rowNum);
                for (int i = 0; i < numColumns; i++) {
                    Cell cell = row.createCell(i);
                    switch (columnTypes[i]) {
                        case BIT:
                        case TINYINT:
                        case SMALLINT:
                        case INTEGER:
                            int intValue = rs.getInt(i+1);
                            if (!rs.wasNull()) {
                                cell.setCellValue(intValue);
                            }
                            break;
                        case BIGINT:
                            long longValue = rs.getLong(i+1);
                            if (!rs.wasNull()) {
                                cell.setCellValue(longValue);
                            }
                            break;
                        case FLOAT:
                        case REAL:
                        case DOUBLE:
                        case NUMERIC:
                        case DECIMAL:
                            double doubleValue = rs.getDouble(i+1);
                            if (!rs.wasNull()) {
                                cell.setCellValue(doubleValue);
                            }
                            break;
                        case CHAR:
                        case VARCHAR:
                        case LONGVARCHAR:
                            String stringValue = rs.getString(i+1);
                            if (!rs.wasNull()) {
                                cell.setCellValue(helper.createRichTextString(stringValue));
                            }
                            break;
                        case DATE:
                        case TIME:
                        case TIMESTAMP:
                             Date dateValue = rs.getDate(i+1);
                             if (!rs.wasNull()) {
                                cell.setCellValue(dateValue);
                            }
                    }
                }
            }
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.close();
        } catch (SQLException ex) {
            
        } finally {
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
