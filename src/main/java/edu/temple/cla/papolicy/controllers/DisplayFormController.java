/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.papolicy.controllers;

import edu.temple.cla.papolicy.*;
import edu.temple.cla.papolicy.chart.Chart;
import edu.temple.cla.papolicy.chart.MyDataset;
import edu.temple.cla.papolicy.dao.Topic;
import edu.temple.cla.papolicy.tables.AbstractTable;
import edu.temple.cla.policydb.queryBuilder.QueryBuilder;
import edu.temple.cla.papolicy.tables.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller to generate the model and view to display the results of an
 * analysis query.
 * @author Paul Wolfgang
 */
public class DisplayFormController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(DisplayFormController.class);
    private JdbcTemplate jdbcTemplate;

    /**
     * Respond to the http post request.
     * @param request The request object
     * @param response The response object (not used).
     * @return A model that contains the data to be displayed and a direction to
     * the results.jsp page.
     * @throws Exception All possible exceptions are thrown.
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            // Load the selected table objects
            List<Table> tableList = new ArrayList<>();
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
                List<Table> tables = AbstractTable.getTable(tableId, qualifier, request, jdbcTemplate);
                tableList.addAll(tables);
            }
            // Load the selected topics.
            TopicList topics = new TopicList(request.getParameterValues("subtopics"), jdbcTemplate);
            // Get the freeText.
            String freeText = request.getParameter("freetext");
            boolean fFreeText = freeText != null && !freeText.equals("");
            // Get the display range and span (years or legislative session)
            String range = request.getParameter("range");
            boolean fRange = "1".equals(range);
            YearRange yearRange = new YearRange(request.getParameter("startYear"),
                    request.getParameter("endYear"),
                    request.getParameter("startSession"),
                    request.getParameter("endSession"),
                    request.getParameter("span"));
            // Get the units to be displayed.
            String showResults = request.getParameter("showResults");
            // Create a column for each dataset/topic pair and dataset/freetext.
            List<Column> columns = new ArrayList<>();
            tableList.forEach(table -> {
                if (table.isTopicSearchable()) {
                    Collection<Topic> topicValues;
                    if (table.isMajorOnly()) {
                        topicValues = topics.getMajorTopics().values();
                    } else {
                        topicValues = topics.getSelectedTopics().values();
                    }               
                    topicValues.forEach(topic -> {
                        columns.add(new Column(table, topic, null, showResults, yearRange));
                        if (fFreeText && fRange && table.getTextColumn() != null) {
                            columns.add(new Column(table, topic, freeText, showResults, yearRange));
                        }
                    });
                } else {
                    columns.add(new Column(table, null, null, showResults, yearRange));
                }
                if (fFreeText && !fRange && table.getTextColumn() != null) {
                    columns.add(new Column(table, null, freeText, showResults, yearRange));
                }
            });
            // Group the columns by units. (Budget data and public opinion have their own units)
            Map<Units, List<Column>> columnMap = new HashMap<>();
            columns.forEach((column) -> {
                List<Column> columnsList = columnMap.get(column.getUnits());
                if (columnsList == null) {
                    columnsList = new ArrayList<>();
                }
                if (column.getUnits() == Units.PERCENT_CHANGE) {
                    column.setValueMap(yearRange.getMinYear() - 2, yearRange.getMaxYear());
                    column.setInitialPrevValue(yearRange.getMinYearPredecessor(), yearRange.getMinYear());
                } else {
                    column.setValueMap(yearRange.getMinYear(), yearRange.getMaxYear());
                }
                if (column.getUnits() == Units.PERCENT
                        || column.getUnits() == Units.PERCENT_OF_FILTERED 
                        || column.getUnits() == Units.PERCENT_OF_TOTAL) {
                    column.setFilteredTotalMap(yearRange.getMinYear(), yearRange.getMaxYear());
                    column.setUnfilteredTotalMap(yearRange.getMinYear(), yearRange.getMaxYear());
                }
                QueryBuilder countQuery =
                        column.getTopicCountQuery(yearRange.getMinYear(), yearRange.getMaxYear());
                QueryBuilder downloadQuery = column.getTable().createDownloadQuery(countQuery);
                column.setDownloadQueryString(Utility.compressAndEncode(downloadQuery.build()));
                columnsList.add(column);
                columnMap.put(column.getUnits(), columnsList);
            });
            // Poplulate each row of each column.
            Iterator<YearOrSession> itr = yearRange.iterator();
            while (itr.hasNext()) {
                YearOrSession current = itr.next();
                String rowKey = current.toString();
                int currentMinYear = current.getMinYear();
                int currentMaxYear = current.getMaxYear();
                for (int j = 0; j < columns.size(); j++) {
                    Column column = columns.get(j);
                    if (null == column.getUnits()) {
                        column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                currentMaxYear));
                    } else switch (column.getUnits()) {
                        case COUNT:
                            QueryBuilder countQuery =
                                    column.getTopicCountQuery(current.getMinYear(),
                                            current.getMaxYear());
                            column.setDrillDown(rowKey, column.getTable().createDrillDownURL(countQuery));
                            column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                    currentMaxYear));
                            break;
                        case PERCENT:
                            column.setDisplayedValue(rowKey, column.getPercent(currentMinYear,
                                    currentMaxYear));
                            break;
                        case PERCENT_OF_FILTERED:
                            column.setDisplayedValue(rowKey, column.getPercent(currentMinYear, currentMaxYear));
                            break;
                        case PERCENT_OF_TOTAL:
                            column.setDisplayedValue(rowKey, column.getPercentOfTotal(currentMinYear, currentMaxYear));
                            break;
                        case PERCENT_CHANGE:
                            column.setDisplayedValue(rowKey, column.getPercentChange(currentMinYear,
                                    currentMaxYear));
                            break;
                        default:
                            column.setDisplayedValue(rowKey, column.getValue(currentMinYear,
                                    currentMaxYear));
                            break;
                    }
                }
            }
            // Create and load the model map.
            Map<String, Object> theMap = new HashMap<>();
            theMap.put("yearRange", yearRange);
            theMap.put("columns", columns);
            // Create MyDataset objects for each group of columns with common units
            List<MyDataset> datasetList = new ArrayList<>();
            columnMap.forEach((k,v)-> {
                datasetList.add(new MyDataset(v));
            });
            // Add the chart to the map.
            theMap.put("dataset", Chart.createChart(datasetList));
            return new ModelAndView("results", theMap);
        } catch (Exception ex) { // want to catch and log all exceptions
            LOGGER.error("Exception Caught", ex);
            throw ex;
        }
    }

    /**
     * Sets the jdbcTemplate for database access.  Called by Spring framework
     * @param jdbcTemplate The jdbcTemplate object.
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
