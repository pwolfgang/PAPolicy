/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.queryBuilder.Comparison;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import edu.temple.cla.papolicy.tables.AbstractTable;
import edu.temple.cla.papolicy.tables.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller to respond to drilldown requests for a specific bill which
 * is listed in the Transcript drilldown results.
 * @author Paul
 */
public class IndividualBillDrilldownController extends AbstractController{
    private static final Logger logger = Logger.getLogger(TranscriptDrillDownController.class);
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Create the ModelAndView.
     * @param request The HTTP request object
     * @param response The HTTP response object, used to redirect to the DrillDown controller.
     * @return null.
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        // Get the requested bill id.
        String billId = request.getParameter("billId");
        // Load the BillsTable.
        Table billsTable = AbstractTable.getTable("3", ' ', request, jdbcTemplate)[0];
        // Create a select query for the specified bill.
        QueryBuilder initialQuery = new QueryBuilder();
        initialQuery.setTable(billsTable.getTableName());
        initialQuery.addToSelectCriteria(new Comparison("ID", "=", "\"" + billId + "\""));
        // Convert this query to a drilldown URL.
        String drillDownURL = billsTable.createDrillDownURL(initialQuery);
        try {
            // Forward to DrillDown controller.
            response.sendRedirect(drillDownURL);
        } catch (Exception ex) {
            logger.error("Error when setting redirect " + drillDownURL, ex);
        }
        return null;
    }

    /**
     * Set the jdbcTemplate from the parameter in the dispatcher-servlet.xml file.
     * (Called by the Spring framework.)
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
