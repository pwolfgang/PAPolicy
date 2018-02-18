package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.dao.StringMapper;
import edu.temple.cla.papolicy.dao.TranscriptCommittee;
import edu.temple.cla.papolicy.dao.TranscriptCommitteeMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * This class contains utility methods common to the download and drill down
 * controllers. All methods are static and package private.
 *
 * @author Paul Wolfgang
 */
public class DownloadAndDrilldownUtil {

    private static final Logger LOGGER = Logger.getLogger(DownloadAndDrilldownUtil.class);
    private static final String TRANSCRIPT_COMMITTEE_QUERY
            = "SELECT * from Transcript_Committee join CommitteeAliases on committeeID=ID WHERE transcriptID='";
    private static final String TRANSCRIPT_BILL_QUERY
            = "SELECT BillID from Transcript_BillID WHERE TranscriptID='";
    private static final ParameterizedRowMapper<TranscriptCommittee> TRANSCRIPT_COMMITTEE_MAPPER
            = new TranscriptCommitteeMapper();
    private static final ParameterizedRowMapper<String> STRING_MAPPER = new StringMapper();


    /**
     * Ensure that all of the year part of the BillId's are odd.
     *
     * @param billIdList List of BillId's to be modified.
     */
    static void fixBillId(List<String> billIdList) {
        for (int i = 0; i < billIdList.size(); i++) {
            String billId = billIdList.get(i);
            int billYear = Integer.parseInt(billId.substring(0, 4));
            if (billYear % 2 == 0) {
                billYear--;
                billId = Integer.toString(billYear) + billId.substring(4);
                billIdList.set(i, billId);
            }
        }
    }

    /**
     * Method to get the committees that are referenced by a given transcript
     * @param jdbcTemplate JdbcTemplate to access the database
     * @param transcriptId ID of the transcript
     * @return List of committees
     */
    static List<String> getCommitteeNames(JdbcTemplate jdbcTemplate,
            String transcriptId) {
        List<TranscriptCommittee> committeeList
                = jdbcTemplate.query(TRANSCRIPT_COMMITTEE_QUERY + transcriptId + "'",
                        TRANSCRIPT_COMMITTEE_MAPPER);
        List<String> committeeNames = new ArrayList<>();
        for (TranscriptCommittee transcriptCommittee : committeeList) {
            committeeNames.add(transcriptCommittee.getCommitteeAlias().getAlternateName());
        }
        return committeeNames;
    }

    /**
     * Method to get the billIds that are referenced by a given transcript
     * @param jdbcTemplate JdbcTemplate to access the database
     * @param transcriptId ID of the transcript
     * @return List of billIds
     */
    static List<String> getBillIdList(JdbcTemplate jdbcTemplate,
            String transcriptId) {
        List<String> billIdList
                = jdbcTemplate.query(TRANSCRIPT_BILL_QUERY + transcriptId + "'", 
                        STRING_MAPPER);
        fixBillId(billIdList);
        return billIdList;
    }

}
