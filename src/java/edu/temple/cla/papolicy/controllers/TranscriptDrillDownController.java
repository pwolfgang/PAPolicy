/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.Utility;
import edu.temple.cla.papolicy.dao.StringMapper;
import edu.temple.cla.papolicy.dao.TranscriptCommittee;
import edu.temple.cla.papolicy.dao.TranscriptCommitteeMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author Paul Wolfgang
 */
public class TranscriptDrillDownController extends AbstractController{
    private static final Logger logger = Logger.getLogger(TranscriptDrillDownController.class);
    private SimpleJdbcTemplate jdbcTemplate;
    private static final String transcriptJoinQuery =
            "SELECT * from Transcript_Committee join CommitteeAliases on committeeID=ID WHERE transcriptID='";
    private static final String transcriptBillsQuery =
            "SELECT BillID from Transcript_BillID WHERE TranscriptID='";

    /**
     * Create the ModelAndView
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ParameterizedRowMapper<TranscriptCommittee> transcriptCommitteeMapper
                    = new TranscriptCommitteeMapper();
            String codedQuery = request.getParameter("query");
            String query = Utility.decodeAndDecompress(codedQuery);
            List<Map<String, Object>> theList = jdbcTemplate.queryForList(query);
            for (Map<String, Object> row : theList) {
                String transcriptId = (String) row.get("ID");
                List<TranscriptCommittee> committeeList =
                        jdbcTemplate.query(transcriptJoinQuery+transcriptId+"'",
                        transcriptCommitteeMapper);
                List<String> committeeNamesList = new ArrayList<String>();
                for (TranscriptCommittee transcriptCommittee : committeeList) {
                    committeeNamesList.add(transcriptCommittee.getCommitteeAlias().getAlternateName());
                    row.put("Committees", committeeNamesList);
                }
                ParameterizedRowMapper<String> stringMapper = new StringMapper();
                List<String> billIDList =
                        jdbcTemplate.query(transcriptBillsQuery+transcriptId+"'", stringMapper);
                row.put("Bills", billIDList);
            }
            Map<String, Object> theMap = new HashMap<String, Object>();
            theMap.put("theList", theList);
            return new ModelAndView("transcriptDrillDown", theMap);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
