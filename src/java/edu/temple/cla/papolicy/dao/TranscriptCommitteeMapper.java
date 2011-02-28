/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author Paul Wolfgang
 */
public class TranscriptCommitteeMapper 
        implements ParameterizedRowMapper<TranscriptCommittee>{

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    public TranscriptCommittee mapRow(ResultSet rs, int colNum) throws SQLException {

        TranscriptCommittee item = new TranscriptCommittee();
        try {
            item.setTranscriptID(rs.getInt("transcriptID"));
            item.setCommitteeAlias(new CommitteeAliasMapper().mapRow(rs, colNum));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
    }

}
