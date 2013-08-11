/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.dao.TopicMapper;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.*;
import edu.temple.cla.papolicy.chart.Chart;
import edu.temple.cla.papolicy.chart.MyDataset;
import edu.temple.cla.papolicy.queryBuilder.QueryBuilder;
import edu.temple.cla.papolicy.tables.AbstractTable;
import edu.temple.cla.papolicy.tables.Table;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Controller to generate the model and view to display the results of an
 * analysis query.
 * @author Paul Wolfgang
 */
public class DisplayFormController extends AbstractController {

    private static Logger logger = Logger.getLogger(DisplayFormController.class);
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Respond to the http post request.
     * @param request The request object
     * @param response The response object (not used).
     * @return A model that contains the data to be displayed and a direction to
     * the results.jsp page.
     * @throws Exception 
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            // Load the selected table objects
            ParameterizedRowMapper<Topic> topicMapper = new TopicMapper();
            List<Table> tableList = new ArrayList<Table>();
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
                Table[] tables = AbstractTable.getTable(tableId, qualifier, request, jdbcTemplate);
                tableList.addAll(Arrays.asList(tables));
            }
            // Load the selected topics.
            TopicList topics = new TopicList(request.getParameterValues("subtopics"), jdbcTemplate);
            // Get the freeText.
            String freeText = request.getParameter("freetext");
            boolean fFreeText = freeText != null && !freeText.equals("");
            // Get the display range and span (years or legislative session)
            String range = request.getParameter("range");
            boolean fRange = "1".equals(range);
            String span = request.getParameter("span");
            YearRange yearRange = new YearRange(request.getParameter("startYear"),
                    request.getParameter("endYear"),
                    request.getParameter("startSession"),
                    request.getParameter("endSession"),
                    request.getParameter("span"));
            // Get the units to be displayed.
            String showResults = request.getParameter("showResults");
            // Create a column for each dataset/topic pair and dataset/freetext.
            List<Column> columns = new ArrayList<>();
            for (Table table : tableList) {
                if (table.isTopicSearchable()) {
                    if (table.isMajorOnly()) {
                        for (Topic topic : topics.getMajorTopics().values()) {
                            columns.add(new Column(table, topic, null, showResults, yearRange));
                            if (fFreeText && fRange && table.getTextColumn() != null) {
                                columns.add(new Column(table, topic, freeText, showResults, yearRange));
                            }
                        }
                    } else {
                        for (Topic topic : topics.getSelectedTopics().values()) {
                            columns.add(new Column(table, topic, null, showResults, yearRange));
                            if (fFreeText && fRange && table.getTextColumn() != null) {
                                columns.add(new Column(table, topic, freeText, showResults, yearRange));
                            }
                        }
                    }
                } else {
                    columns.add(new Column(table, null, null, showResults, yearRange));
                }
                if (fFreeText && !fRange && table.getTextColumn() != null) {
                    columns.add(new Column(table, null, freeText, showResults, yearRange));
                }
            }
            // Group the columns by units. (Budget data and public opinion have their own units)
            Map<Units, List<Column>> columnMap = new HashMap<>();
            for (Column column : columns) {
                List<Column> columnsList = columnMap.get(column.getUnits());
                if (columnsList == null) {
                    columnsList = new ArrayList<Column>();
                }
                if (column.getUnits() == Units.PERCENT_CHANGE) {
                    column.setValueMap(jdbcTemplate, yearRange.getMinYear() - 2, yearRange.getMaxYear());
                    column.setInitialPrevValue(yearRange.getMinYearPredicessor(), yearRange.getMinYear());
                } else {
                    column.setValueMap(jdbcTemplate, yearRange.getMinYear(), yearRange.getMaxYear());
                }
                if (column.getUnits() == Units.PERCENT 
                        || column.getUnits() == Units.PERCENT_OF_FILTERED 
                        || column.getUnits() == Units.PERCENT_OF_TOTAL) {
                    column.setFilteredTotalMap(jdbcTemplate, yearRange.getMinYear(), yearRange.getMaxYear());
                    column.setUnfilteredTotalMap(jdbcTemplate, yearRange.getMinYear(), yearRange.getMaxYear());
                }
                QueryBuilder countQuery =
                        column.getTopicCountQuery(yearRange.getMinYear(), yearRange.getMaxYear());
                QueryBuilder downloadQuery = column.getTable().createDownloadQuery(countQuery);
                column.setDownloadQueryString(Utility.compressAndEncode(downloadQuery.build()));
                columnsList.add(column);
                columnMap.put(column.getUnits(), columnsList);
            }
            // Poplulate each row of each column.
            Iterator<YearOrSession> itr = yearRange.iterator();
            while (itr.hasNext()) {
                YearOrSession current = itr.next();
                String rowKey = current.toString();
                int currentMinYear = current.getMinYear();
                int currentMaxYear = current.getMaxYear();
                for (int j = 0; j < columns.size(); j++) {
                    Column column = columns.get(j);
                    if (column.getUnits() == Units.COUNT) {
                        QueryBuilder countQuery =
                                column.getTopicCountQuery(current.getMinYear(),
                                current.getMaxYear());
                        column.setDrillDown(rowKey, column.getTable().createDrillDownURL(countQuery));
                        column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                currentMaxYear));
                    } else if (column.getUnits() == Units.PERCENT) {
                        column.setDisplayedValue(rowKey, column.getPercent(currentMinYear,
                                currentMaxYear));
                    } else if (column.getUnits() == Units.PERCENT_OF_FILTERED) {
                        column.setDisplayedValue(rowKey, column.getPercent(currentMinYear, currentMaxYear));
                    } else if (column.getUnits() == Units.PERCENT_OF_TOTAL) {
                        column.setDisplayedValue(rowKey, column.getPercentOfTotal(currentMinYear, currentMaxYear));
                    } else if (column.getUnits() == Units.PERCENT_CHANGE) {
                        column.setDisplayedValue(rowKey, column.getPercentChange(currentMinYear,
                                currentMaxYear));
                    } else {
                        column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                currentMaxYear));
                    }
                }
            }
            // Create and load the model map.
            Map<String, Object> theMap = new HashMap<>();
            theMap.put("yearRange", yearRange);
            theMap.put("columns", columns);
            // Create MyDataset objects for each group of columns with common units
            List<MyDataset> datasetList = new ArrayList<>();
            for (Map.Entry<Units, List<Column>> entry : columnMap.entrySet()) {
                Units unit = entry.getKey();
                List<Column> theColumns = entry.getValue();
                datasetList.add(new MyDataset(theColumns));
            }
            // Add the chart to the map.
            theMap.put("dataset", Chart.createChart(datasetList));
            return new ModelAndView("results", theMap);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    /**
     * Sets the jdbcTemplate for database access.  Called by Spring framework
     * @param jdbcTemplate The jdbcTemplate object.
     */
    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
