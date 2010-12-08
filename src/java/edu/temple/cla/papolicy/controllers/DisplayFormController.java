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
import edu.temple.cla.papolicy.chart.Chart;
import edu.temple.cla.papolicy.chart.MyDataset;
import edu.temple.cla.papolicy.tables.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
public class DisplayFormController extends AbstractController {

    private static Logger logger = Logger.getLogger(DisplayFormController.class);
    private SimpleJdbcTemplate jdbcTemplate;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            ParameterizedRowMapper<Table> tableMapper = new TableMapper();
            ParameterizedRowMapper<Filter> filterMapper = new FilterMapper();
            ParameterizedRowMapper<Topic> topicMapper = new TopicMapper();
            List<Table> tables = new ArrayList<Table>();
            String[] tableIds = request.getParameterValues("dataset");
            if (tableIds == null) {
                response.sendRedirect("analysis.spg?error=1");
                return null;
            }
            for (String tableId : tableIds) {
                char qualifier = ' ';
                if (!Character.isDigit(tableId.charAt(tableId.length() - 1))) {
                    qualifier = tableId.charAt(tableId.length() - 1);
                    tableId = tableId.substring(0, tableId.length() - 1);
                }
                List<Table> aTableList =
                        jdbcTemplate.query("SELECT * FROM Tables WHERE ID=" + tableId,
                        tableMapper);
                if (aTableList.size() != 1) {
                    throw new Error("TableID " + tableId + " not in database");
                }
                Table table = aTableList.get(0);
                table = table.getSubTable(qualifier);
                table.setAdditionalParameters(request);
                String query = "SELECT * from Filters WHERE TableID=" + table.getId()
                        + " ORDER BY ID";
                List<Filter> filterList = jdbcTemplate.query(query, filterMapper);
                for (Filter filter : filterList) {
                    filter.setJdbcTemplate(jdbcTemplate);
                    filter.setFilterParameterValues(request);
                }
                table.setFilterList(filterList);
                table.setJdbcTemplate(jdbcTemplate);
                tables.add(table);
            }
            TopicList topics = new TopicList(request.getParameterValues("subtopics"), jdbcTemplate);
            String freeText = request.getParameter("freetext");
            boolean fFreeText = freeText != null && !freeText.equals("");
            String range = request.getParameter("range");
            boolean fRange = "1".equals(range);
            String span = request.getParameter("span");
            YearRange yearRange = new YearRange(request.getParameter("startYear"),
                    request.getParameter("endYear"),
                    request.getParameter("startSession"),
                    request.getParameter("endSession"),
                    request.getParameter("span"));
            String showResults = request.getParameter("showResults");
            ArrayList<Column> columns = new ArrayList<Column>();
            for (Table table : tables) {
                if (table.isTopicSearchable()) {
                    if (table.isMajorOnly()) {
                        for (Topic topic : topics.getMajorTopics().values()) {
                            columns.add(new Column(table, topic, null, showResults));
                            if (fFreeText && fRange && table.getTextColumn() != null) {
                                columns.add(new Column(table, topic, freeText, showResults));
                            }
                        }
                    } else {
                        for (Topic topic : topics.getSubTopics().values()) {
                            columns.add(new Column(table, topic, null, showResults));
                            if (fFreeText && fRange && table.getTextColumn() != null) {
                                columns.add(new Column(table, topic, freeText, showResults));
                            }
                        }
                    }
                } else {
                    columns.add(new Column(table, null, null, showResults));
                }
                if (fFreeText && !fRange && table.getTextColumn() != null) {
                    columns.add(new Column(table, null, freeText, showResults));
                }
            }
            Map<Units, ArrayList<Column>> columnMap = new HashMap<Units, ArrayList<Column>>();
            for (Column column : columns) {
                ArrayList<Column> columnsList = columnMap.get(column.getUnits());
                if (columnsList == null) {
                    columnsList = new ArrayList<Column>();
                }
                if (column.getUnits() == Units.PERCENT_CHANGE) {
                    column.setValueMap(jdbcTemplate, yearRange.getMinYear() - 2, yearRange.getMaxYear());
                    column.setInitialPrevValue(yearRange.getMinYearPredicessor(), yearRange.getMinYear());
                } else {
                    column.setValueMap(jdbcTemplate, yearRange.getMinYear(), yearRange.getMaxYear());
                }
                if (column.getUnits() == Units.PERCENT) {
                    column.setTotalMap(jdbcTemplate, yearRange.getMinYear(), yearRange.getMaxYear());
                }
                String countQuery =
                        column.getTopicCountQueryString(yearRange.getMinYear(), yearRange.getMaxYear());
                String downloadQuery = column.getTable().createDownloadQuery(countQuery);
                column.setDownloadQuery(Utility.compressAndEncode(downloadQuery));
                columnsList.add(column);
                columnMap.put(column.getUnits(), columnsList);
            }
            Iterator<YearOrSession> itr = yearRange.iterator();
            while (itr.hasNext()) {
                YearOrSession current = itr.next();
                String rowKey = current.toString();
                int currentMinYear = current.getMinYear();
                int currentMaxYear = current.getMaxYear();
                for (int j = 0; j < columns.size(); j++) {
                    Column column = columns.get(j);
                    if (column.getUnits() == Units.COUNT) {
                        String countQuery =
                                column.getTopicCountQueryString(current.getMinYear(),
                                current.getMaxYear());
                        String drillDownQueryString = column.getTable().createDrillDownQuery(countQuery);
                        column.setDrillDown(rowKey, Utility.compressAndEncode(drillDownQueryString));
                        column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                currentMaxYear));
                    } else if (column.getUnits() == Units.PERCENT) {
                        column.setDisplayedValue(rowKey, column.getPercent(currentMinYear,
                                currentMaxYear));
                    } else if (column.getUnits() == Units.PERCENT_CHANGE) {
                        column.setDisplayedValue(rowKey, column.getPercentChange(currentMinYear,
                                currentMaxYear));
                    } else {
                        column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                currentMaxYear));
                    }
                }
            }

            Map<String, Object> theMap = new HashMap<String, Object>();
            theMap.put("yearRange", yearRange);
            theMap.put("columns", columns);
            ArrayList<MyDataset> datasetList = new ArrayList<MyDataset>();
            for (Map.Entry<Units, ArrayList<Column>> entry : columnMap.entrySet()) {
                Units unit = entry.getKey();
                ArrayList<Column> theColumns = entry.getValue();
                datasetList.add(new MyDataset(theColumns));
            }
            theMap.put("dataset", Chart.createChart(datasetList));
            return new ModelAndView("results", theMap);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
