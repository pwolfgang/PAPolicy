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
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author Paul Wolfgang
 */
public class Download extends AbstractController{

    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Create the ModelAndView
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String query = Utility.decodeAndDecompress(request.getParameter("query"));
        List<Map<String, Object>> theList = jdbcTemplate.queryForList(query);
        Map<String, Object> theMap = new HashMap<String, Object>();
        theMap.put("theList", theList);
        return new ModelAndView("download", theMap);
    }


}
