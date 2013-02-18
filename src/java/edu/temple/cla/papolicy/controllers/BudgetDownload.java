/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.Utility;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author Paul Wolfgang
 */
public class BudgetDownload extends AbstractController{

    private static Logger logger = Logger.getLogger(Download.class);

    private DataSource dataSource;

    /**
     * Create the ModelAndView
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        String query = Utility.decodeAndDecompress(request.getParameter("query"));
        String uri = request.getRequestURI();
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
            return null;
        }
        int posLastSlash = uri.lastIndexOf("/");
        String fileName = null;
        if (posLastSlash != -1) {
            fileName = uri.substring(posLastSlash+1);
        } else {
            fileName = uri;
        }
        response.setContentType("application/ms-excel");
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
