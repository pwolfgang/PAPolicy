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
 * Map queries of the Transcript_Committee table to TranscriptCommittee objects.
 * The a TranscriptCommittee object represents that specified transcript is
 * a transcript of a hearing held before the specified committee.  A given
 * hearing may be held before multiple committees.
 * @author Paul Wolfgang
 */
public class TranscriptCommitteeMapper 
        implements ParameterizedRowMapper<TranscriptCommittee>{

    private static final Logger logger = Logger.getLogger(CommitteeAliasMapper.class);

    /**
     * Perform the mapping.
     * @param rs ResultSet set to the current row of the table
     * @param rowNum index of the current row (not used)
     * @return The mapped object.
     * @throws SQLException 
     */
    public TranscriptCommittee mapRow(ResultSet rs, int rowNum) throws SQLException {

        TranscriptCommittee item = new TranscriptCommittee();
        try {
            item.setTranscriptID(rs.getInt("transcriptID"));
            item.setCommitteeAlias(new CommitteeAliasMapper().mapRow(rs, rowNum));
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
        return item;
    }

    /**
     * Determine of two ParameterizedRowmapper objects are equal.  Since
     * the row mapper is stateless, equality of class is sufficient.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.getClass() == o.getClass();
    }

}
