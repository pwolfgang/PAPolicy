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

import edu.temple.cla.papolicy.YearRange;
import edu.temple.cla.papolicy.dao.TopicMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.TableMapper;
import edu.temple.cla.papolicy.dao.FilterMapper;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.tables.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller that responds to the analysis page request.  This controller
 * creates the model map that is used to generate the analysis form.
 * @author Paul Wolfgang
 */
public class AnalysisController extends AbstractController {

    private static final Logger logger = Logger.getLogger(AnalysisController.class);

    private JdbcTemplate jdbcTemplate;

    /**
     * Method to handle the HTTP request. This method builds the model object
     * that is used by the analysis.jsp page to display the analysis form.
     * @param request The servlet request object
     * @param response The servlet response object
     * @return A ModelAndView object.
     * @throws Exception All possible exceptions are thrown.
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> model = new HashMap<>();
            ParameterizedRowMapper<Table> tableMapper = new TableMapper();
            ParameterizedRowMapper<Filter> filterMapper = new FilterMapper();
            ParameterizedRowMapper<Topic> topicMapper = new TopicMapper();
            // Read the list of tables to be displayed.
            List<Table> tables =
                    jdbcTemplate.query("SELECT * from Tables ORDER BY ID", tableMapper);
            int minYear = Integer.MAX_VALUE;
            int maxYear = Integer.MIN_VALUE;
            for (Table table : tables) {
                if (table.getMinYear() < minYear) {
                    minYear = table.getMinYear();
                }
                if (table.getMaxYear() > maxYear) {
                    maxYear = table.getMaxYear();
                }
                // Read the filtest associated with this table.
                String query = "SELECT * from Filters WHERE TableID=" + table.getId()
                        + " ORDER BY ID";
                List<Filter> filterList = jdbcTemplate.query(query, filterMapper);
                for (Filter filter : filterList) {
                    filter.setJdbcTemplate(jdbcTemplate);
                }
                table.setFilterList(filterList);
                table.setJdbcTemplate(jdbcTemplate);
            }

            model.put("tables", tables);
            model.put("yearRange", new YearRange(minYear, maxYear));
            // Read the list of major topics
            List<Topic> majorTopics =
                    jdbcTemplate.query("SELECT * from MajorCode ORDER BY Description",
                    topicMapper);
            StringBuilder stb = new StringBuilder();
            // Read the minor topics associated with each major topic
            for (Topic majorTopic : majorTopics) {
                int majorCode = majorTopic.getCode();
                stb.append(majorCode);
                stb.append(", ");
                String topicQuery = "SELECT * from Code WHERE Code LIKE(\'" + majorCode + "__\') ORDER BY Description";
                List<Topic> subTopics = jdbcTemplate.query(topicQuery, topicMapper);
                majorTopic.setSubTopics(subTopics);
            }
            model.put("topics", majorTopics);
            stb.delete(stb.length() - 2, stb.length());
            model.put("topicList", stb.toString());
            return new ModelAndView("analysis", model);
        } catch (Exception ex) { // What to catch all exceptions
            logger.error(ex);
            throw ex;
        }
    }

    /**
     * Method to set the jdbcTemplate. This method is called by the Spring
     * framework to provide the dependency injected value. The jdbcTemplate
     * provides access to the database.
     * @param jdbcTemplate The jdbcTemplate object.
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
