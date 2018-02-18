/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.Utility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller to respond to drilldown requests.
 * @author Paul Wolfgang
 */
public class DrillDownController extends AbstractController {

    private static final Logger logger = Logger.getLogger(DrillDownController.class);
    private JdbcTemplate jdbcTemplate;

    /**
     * Populate the view map and forward to the drillDown jsp page.
     * @param request The HTTP request object
     * @param response The HTTP response object (not used)
     * @return A ModelAndView object that includes the map and drillDown.jsp as the view.
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            // Decode the query from the request
            String codedQuery = request.getParameter("query");
            String query = Utility.decodeAndDecompress(codedQuery);
            // Execute the query
            List<Map<String, Object>> theList = jdbcTemplate.queryForList(query);
            // Put the query results in the map.
            Map<String, Object> theMap = new HashMap<>();
            theMap.put("theList", theList);
            return new ModelAndView("drillDown", theMap);
        } catch (Exception ex) { // Want to catch unchecked exceptions as well
            logger.error(ex);
            return null;
        }
    }

    /**
     * Set the jdbcTemplate from the parameter in the dispatcher-servlet.xml file.
     * (Called by the Spring framework.)
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
