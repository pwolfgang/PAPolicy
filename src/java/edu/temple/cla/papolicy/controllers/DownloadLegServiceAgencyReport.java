/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller to download a select Legislative Service Agency Report
 *
 * @author Paul
 */
public class DownloadLegServiceAgencyReport extends AbstractController {

    private String path;
    private static final Logger LOGGER = Logger.getLogger(DownloadLegServiceAgencyReport.class);
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @return
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        String id = request.getParameter("ID");
        String[] agencyAndFileName = jdbcTemplate.query(
                "select Agency, FileName from LSAReportsText where ID=?",
                (PreparedStatement preparedStatement) -> {
                    preparedStatement.setString(1, id);
                }, (ResultSet rs) -> {
                    if (rs.next()) {
                        String[] result = new String[2];
                        result[0] = rs.getString("Agency");
                        result[1] = rs.getString("FileName");
                        return result;
                    } else {
                        return null;
                    }
                });
        if (agencyAndFileName != null) {
            File filePath = new File(path);
            File inputFile = new File(filePath, agencyAndFileName[0] + File.separator + agencyAndFileName[1]);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + agencyAndFileName[1] + "\"");
            try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
                    OutputStream out = response.getOutputStream()) {
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                in.close();
                out.flush();
            } catch (FileNotFoundException fileNotFound) {
                reportError(response, inputFile.getName());
            }
        } else {
            reportError(response, id);
        }
        return null;
    }

    private void reportError(HttpServletResponse response, String id) {
        try {
            response.setContentType("text/html");
            try (PrintWriter pw = response.getWriter()) {
                pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
                pw.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
                pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
                pw.println("<head>");
                pw.println("<title>Error Page</title>");
                pw.println("</head>");
                pw.println("<body>");
                pw.println("<img src=\"images/cla_201_4c.jpg\" alt=\"CLA Logo\" width=\"592\" height=\"77\" />");
                pw.println("<h1>Pennsylvania Policy Database Project</h1>");
                pw.println("<p>We are sorry, but Legslative Service Agency Report " + id + " is not available.</p>");
                pw.println("</body>");
                pw.println("</html>");
                pw.flush();
            }
        } catch (Throwable t) { // Want to log any error
            logger.error(t);
        }
    }

    /**
     * Method to set the DataSource. This method is called by the Spring
     * framework.
     *
     * @param dataSource the datasource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Method to set the JdbcTemplate. This method is called by the Spring
     * framework.
     *
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Method to set the path to the files. This method is called by the Spring
     * framework.
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

}
