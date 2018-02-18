/*
 * To change this template, choose Tools | Templates
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Class to respond to requests to download a selected governor&apos;s budget
 * address
 *
 * @author Paul Wolfgang
 */
public class DownloadGovernorsBudgetAddress extends HttpServlet {

    private String path;
    private static final Logger LOGGER = Logger.getLogger(DownloadGovernorsBudgetAddress.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName = request.getParameter("year");
        File pathFile = new File(path);
        File inputFile = new File(path, fileName
                + " Governors Budget Address.pdf");
        try (InputStream in = new BufferedInputStream(
                new FileInputStream(inputFile));
                OutputStream out = response.getOutputStream()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + inputFile.getName() + "\"");
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            out.flush();
        } catch (FileNotFoundException ex) {
            try (PrintWriter pw = response.getWriter()) {
                response.setContentType("text/html");
                pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
                pw.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
                pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
                pw.println("<head>");
                pw.println("<title>Error Page</title>");
                pw.println("</head>");
                pw.println("<body>");
                pw.println("<img src=\"images/cla_201_4c.jpg\" alt=\"CLA Logo\" width=\"592\" height=\"77\" />");
                pw.println("<h1>Pennsylvania Policy Database Project</h1>");
                pw.println("<p>We are sorry, but " + fileName + " is not available.</p>");
                pw.println("</body>");
                pw.println("</html>");
            } catch (Throwable t) { // Want to log any error
                LOGGER.error(t);
            }
        }
    }

    /**
     * Initialize the path where the governor&apos;s budget address files are
     * located. Value is specified in the web.xml file.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        path = getServletConfig().getInitParameter("path");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
