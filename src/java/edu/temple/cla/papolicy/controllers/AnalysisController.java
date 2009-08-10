/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.dao.TopicMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.dao.TableMapper;
import edu.temple.cla.papolicy.dao.FilterMapper;
import edu.temple.cla.papolicy.filters.Filter;
import edu.temple.cla.papolicy.*;
import edu.temple.cla.papolicy.tables.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author Paul Wolfgang
 */
public class AnalysisController extends AbstractController {

    private SimpleJdbcTemplate jdbcTemplate;
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
       Map<String, Object> model = new HashMap<String, Object>();
       ParameterizedRowMapper<Table> tableMapper = new TableMapper();
       ParameterizedRowMapper<Filter> filterMapper = new FilterMapper();
       ParameterizedRowMapper<Topic> topicMapper = new TopicMapper();

        List<Table> tables =
                jdbcTemplate.query("SELECT * from Tables ORDER BY ID", tableMapper);
        int minYear = Integer.MAX_VALUE;
        int maxYear = Integer.MIN_VALUE;
        for (Table table : tables) {
            if (table.getMinYear() < minYear) minYear = table.getMinYear();
            if (table.getMaxYear() > maxYear) maxYear = table.getMaxYear();
            String query = "SELECT * from Filters WHERE TableID=" + table.getId()
                    + " ORDER BY ID";
            List<Filter> filterList = jdbcTemplate.query(query, filterMapper);
            for (Filter filter:filterList) {
                filter.setJdbcTemplate(jdbcTemplate);
            }
            table.setFilterList(filterList);
            table.setJdbcTemplate(jdbcTemplate);
        }

        model.put("tables", tables);
        model.put("yearRange", new YearRange(minYear, maxYear));
        List<Topic> majorTopics =
                jdbcTemplate.query("SELECT * from MajorCode ORDER BY Description",
                                    topicMapper);
        StringBuilder stb = new StringBuilder();
        for (Topic majorTopic : majorTopics) {
            int majorCode = majorTopic.getCode();
            stb.append(majorCode);
            stb.append(", ");
            String topicQuery = "SELECT * from Code WHERE Code LIKE(\'" + majorCode + "__\') ORDER BY Description";
            List<Topic> subTopics = jdbcTemplate.query(topicQuery, topicMapper);
            majorTopic.setSubTopics(subTopics);
        }
        model.put("topics", majorTopics);
        stb.delete(stb.length()-2, stb.length());
        model.put("topicList", stb.toString());
        return new ModelAndView("analysis", model);
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
