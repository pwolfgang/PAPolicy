/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Paul Wolfgang
 */
public class AnalysisControllerTest {

    public AnalysisControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testHandleRequestInternal() throws Exception {
        System.out.println("handleRequestInternal");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        AnalysisController instance = new AnalysisController();
        ModelAndView expResult = null;
        ModelAndView result = instance.handleRequestInternal(request, response);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetJdbcTemplate() {
        System.out.println("setJdbcTemplate");
        SimpleJdbcTemplate jdbcTemplate = null;
        AnalysisController instance = new AnalysisController();
        instance.setJdbcTemplate(jdbcTemplate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}