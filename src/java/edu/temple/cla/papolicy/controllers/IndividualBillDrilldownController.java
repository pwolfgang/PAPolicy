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
 *
 * @author Paul
 */
public class IndividualBillDrilldownController extends AbstractController{
    private static final Logger logger = Logger.getLogger(TranscriptDrillDownController.class);
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Create the ModelAndView
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        String billId = request.getParameter("billId");
        Table billsTable = AbstractTable.getTable("3", ' ', request, jdbcTemplate)[0];
        QueryBuilder initialQuery = new QueryBuilder();
        initialQuery.setTable(billsTable.getTableName());
        initialQuery.addToSelectCriteria(new Comparison("ID", "=", billId));
        String drillDownURL = billsTable.createDrillDownURL(initialQuery);
        try {
            response.sendRedirect(drillDownURL);
        } catch (Exception ex) {
            logger.error("Error when setting redirect " + drillDownURL, ex);
        }
        return null;
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
