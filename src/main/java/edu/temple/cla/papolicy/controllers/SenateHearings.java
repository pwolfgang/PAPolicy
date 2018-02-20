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
 * Servlet to download a selected senate hearing file.
 *
 * @author Paul Wolfgang
 */
public class SenateHearings extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SenateHearings.class);
    private String path;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        String fileName = request.getParameter("file");
        try {
            File pathFile = new File(path);
            File inputFile = new File(path, fileName + ".pdf");
            InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
            response.setContentType("application/pdf");
            out = response.getOutputStream();
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
        } catch (FileNotFoundException ex) {
            try {
                response.setContentType("text/html");
                PrintWriter pw = response.getWriter();
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
                pw.close();
            } catch (Throwable t) { // Log all errors
                logger.error(t);
            }
        } catch (IOException ioex) {
            logger.error(ioex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioex) {
                }
            }
        }
    }

    /**
     * Initialize the path where the governor&apos;s budget address files are
     * located. Value is specified in the web.xml file.
     *
     */
    @Override
    public void init() {
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
