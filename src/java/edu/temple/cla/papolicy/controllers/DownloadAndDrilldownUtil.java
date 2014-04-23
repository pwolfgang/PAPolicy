package edu.temple.cla.papolicy.controllers;

import edu.temple.cis.wolfgang.mycreatexlsx.MyWorksheet;
import edu.temple.cla.papolicy.dao.StringMapper;
import edu.temple.cla.papolicy.dao.TranscriptCommittee;
import edu.temple.cla.papolicy.dao.TranscriptCommitteeMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * This class contains utility methods common to the download and drill down
 * controllers. All methods are static and package private.
 *
 * @author Paul Wolfgang
 */
public class DownloadAndDrilldownUtil {

    private static final Logger logger = Logger.getLogger(DownloadAndDrilldownUtil.class);
    private static final String transcriptJoinQuery
            = "SELECT * from Transcript_Committee join CommitteeAliases on committeeID=ID WHERE transcriptID='";
    private static final String transcriptBillsQuery
            = "SELECT BillID from Transcript_BillID WHERE TranscriptID='";
    private static final ParameterizedRowMapper<TranscriptCommittee> transcriptCommitteeMapper
            = new TranscriptCommitteeMapper();
    private static final ParameterizedRowMapper<String> stringMapper = new StringMapper();

    static void addColumn(int columnType, int i, MyWorksheet sheet, ResultSet rs) {
        try {
            switch (columnType) {
                case Types.BIT:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                    addIntValue(sheet, i, rs);
                    break;
                case Types.BIGINT:
                    addLongValue(sheet, i, rs);
                    break;
                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.DECIMAL:
                    addDoubleValue(sheet, i, rs);
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    addStringValue(sheet, i, rs);
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    addDateValue(sheet, i, rs);
            }
        } catch (Exception ex) { // Want to catch unchecked exceptions.
            logger.error("Error converting cell", ex);
            sheet.addCell("null");
        }
    }

    /**
     * Method to add an int value to a worksheet
     * @param sheet The worksheet
     * @param i The column index
     * @param rs Result set containing the data
     * @throws SQLException 
     */
    private static void addIntValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        int intValue = rs.getInt(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(intValue);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to add a long value to a worksheet
     * @param sheet The worksheet
     * @param i The column index
     * @param rs Result set containing the data
     * @throws SQLException 
     */
    private static void addLongValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        long longValue = rs.getLong(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(longValue);
        } else {
            sheet.addCell("null");
        }

    }

    /**
     * Method to add a double value to a worksheet
     * @param sheet The worksheet
     * @param i The column index
     * @param rs Result set containing the data
     * @throws SQLException 
     */
    private static void addDoubleValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        double doubleValue = rs.getDouble(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(doubleValue);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to add a date value to a worksheet
     * @param sheet The worksheet
     * @param i The column index
     * @param rs Result set containing the data
     * @throws SQLException 
     */
    private static void addDateValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        String dateString = rs.getString(i + 1);
        if (!rs.wasNull()) {
            sheet.addDateCell(dateString);
        } else {
            sheet.addCell("null");
        }
    }

    /**
     * Method to add a string value to a worksheet
     * @param sheet The worksheet
     * @param i The column index
     * @param rs Result set containing the data
     * @throws SQLException 
     */
    private static void addStringValue(MyWorksheet sheet, int i, ResultSet rs)
            throws SQLException {
        String stringValue = rs.getString(i + 1);
        if (!rs.wasNull()) {
            sheet.addCell(stringValue);
        } else {
            sheet.addCell("null");
        }
    }

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
    static List<String> getCommitteeNames(SimpleJdbcTemplate jdbcTemplate,
            String transcriptId) {
        List<TranscriptCommittee> committeeList
                = jdbcTemplate.query(transcriptJoinQuery + transcriptId + "'",
                        transcriptCommitteeMapper);
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
    static List<String> getBillIdList(SimpleJdbcTemplate jdbcTemplate,
            String transcriptId) {
        List<String> billIdList
                = jdbcTemplate.query(transcriptBillsQuery + transcriptId + "'", 
                        stringMapper);
        fixBillId(billIdList);
        return billIdList;
    }

}
