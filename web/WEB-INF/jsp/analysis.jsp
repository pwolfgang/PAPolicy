<%-- 
    Document   : analysis
    Created on : Dec 9, 2008, 1:08:21 PM
    Author     : Paul Wolfgang
    This is the form used to select the data sets and policy areas.
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Analysis Page</title>
        <link type="text/css" rel="stylesheet" href="oneform.css" />
        <link type="text/css" rel="stylesheet" href="policy.css" />
        <link type="text/css" rel="stylesheet" href="style.css" />
        <script type="text/javascript" src="script.js"></script>
    </head>
    <body>
        <%@include file="header.jspf"%>
        <div class="tool">
            <h2>
                Build Your Own Policy Analysis Query
            </h2>
            <p>
                This form enables you to graph trends and download data from
                the main Pennsylvania Policy Project datasets.
                Scroll down the page for the available search options.
                <%if ("1".equals(request.getParameter("error"))) {%>
                <br /><span class="error">At least one dataset must be chosen!</span>
                <%}%>
            </p>
            <form id="analysisForm" method="post" action="display.spg">
                <table class="border">
                    <tr>
                        <td rowspan="<%=((java.util.List) request.getAttribute("tables")).size() + 1%>">
                            <p>DATASETS TO SEARCH:</p>
                            <p class="smallText">
                                Keep scrolling down the page to select:
                            </p>
                            <ul>
                                <li>additional datasets</li>
                                <li>policy topics and key word search </li>
                                <li>time frame</li>
                            </ul>

                        </td>
                        <td width="34%">&nbsp;</td>
                        <th width="50%">DATASET FILTERS
                            <br/><span class="smallText">
                                Pre-set to default values
                            </span>
                        </th>
                    </tr>
                    <c:forEach var="table" items="${tables}">
                        <tr>
                            <td>
                                ${table.titleBox}
                            </td>
                            <td class="advanced">
                                <c:choose>
                                    <c:when test="${table.filterListSize != 0}">
                                        <div id="filters${table.id}" class="filter">
                                            <%boolean first = true;%>
                                            <c:forEach var="filter" items="${table.filterList}">
                                                ${filter.filterFormInput}
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        No Filters
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>
                            <p>POLICY AREAS TO INCLUDE: </p>
                            <p>
                                Each record includes a short description (a sentence
                                or phrase) of the item.  This features allows a text
                                search of that field and will help identify the best
                                topic from the list to the right.
                            </p>
                        </td>
                        <td colspan="2">
                            <ul>
                                <li>
                                    To select multiple subtopics/topics, press the
                                    control (ctrl) key or Apple Command key while
                                    using your mouse to click on desired topics.
                                </li>
                                <li>
                                    To select just major topic(s), select the option
                                    "All subtopics in ..."
                                </li>
                                <li>
                                    To select <em>all</em> major topics, check the
                                    box "Search ALL topics ..." this will give you a
                                    breakdown of each policy topic
                                </li>
                                <li>
                                    To combine all topics in to one figure and download
                                    link, do <em>not</em> select any of the policy topics 
                                    check boxes or the "Search ALL topics" box.
                                </li>
                            </ul>
                            <p>
                                <input type="checkbox" name="subtopics" value="${topicList}" id="subtopicsAll"/>
                                <label for="subtopicsAll">Search ALL Topics, or select specific topics/subtopics
                                    from the lists below:</label>
                            </p>
                            <c:forEach var="majorTopic" items="${topics}">
                                <div class="header">
                                    <c:out value="${majorTopic.description}" />
                                </div>
                                <input type="checkbox" name="subtopics" value="${majorTopic.code}" id="subtopics${majorTopic.code}"/>
                                <label for="subtopics${majorTopic.code}">All subtopics in ${majorTopic.description}</label>
                                <br /><input type="checkbox"
                                             onclick="expandSubtopics(${majorTopic.code});"
                                             id="x${majorTopic.code}" />
                                <label for="x${majorTopic.code}">Expand subtopics</label>
                                <div id="s${majorTopic.code}" class="subcodelist">
                                    <label for="ss${majorTopic.code}">${majorTopic.description} Subtopics</label><br/>
                                    <select name="subtopics" multiple="multiple" class="selectSize" id="ss${majorTopic.code}">
                                        <c:forEach var="subTopic" items="${majorTopic.subTopics}">
                                            <option value="${subTopic.code}" >
                                                <c:out value="${subTopic.description}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="freetext">
                                INCLUDE KEYWORD(S)
                            </label></td>
                        <td><input type="text" id="freetext" name="freetext" value="" />
                            <br/>
                            <span class="smallRedText">
                                Note: the text descriptions for each record are very short
                                and generally not sufficient for accurate keyword search.
                            </span>
                        </td>
                        <td>
                            <fieldset><legend>Free Text Search</legend>
                                <input name="range" value="1" id="range1" checked="checked" type="radio" />
                                <label for="range1"><span class="smallText">
                                        Only selected topics/subtopics
                                    </span></label>
                                <br/>
                                <input name="range" value="2" id="range2" type="radio" />
                                <label for="range2"><span class="smallText">
                                        All topics/subtopics
                                    </span></label>
                            </fieldset>
                        </td>
                    </tr>              
                    <tr>
                        <td>
                            FOR THESE YEARS OR LEGISLATIVE SESSION:
                        </td>
                        <td>
                            <p>
                                <input type="radio" name="span" value="years" id="spanYears" checked="checked" />
                                <label for="spanYears">the years</label>
                            </p>
                            <p>
                                <label for="startYear">from</label>
                                <select id="startYear" name="startYear">
                                    ${yearRange.ascendingYears}
                                </select>
                                <br/>
                                <br/>
                                <label for="endYear">to&nbsp;&nbsp;&nbsp;</label>
                                <select id="endYear" name="endYear">
                                    ${yearRange.descendingYears}
                                </select>
                            </p>
                        </td>
                        <td>
                            <p>
                                <input type="radio" name="span" value="sessions" id="spanSessions" />
                                <label for="spanSessions">the Legislative Sessions</label>
                            </p>
                            <p>
                                <label for="startSession">from</label>
                                <select id="startSession" name="startSession">
                                    ${yearRange.ascendingSessions}
                                </select>
                                <br/>
                                <br/>
                                <label for="endSession">to&nbsp;&nbsp;&nbsp;</label>
                                <select id="endSession" name="endSession">
                                    ${yearRange.descendingSessions}
                                </select>
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <td>GRAPH AS:</td>
                        <td colspan="2">
                            <input type="radio" name="showResults" value="count" id="showResultsC" checked="checked" />
                            <label for="showResultsC">Number of cases per year or legislative session</label>
                            <br/>
                            <input type="radio" name="showResults" value="percent_of_total" id="showResultsPT" />
                            <label for="showResultsPT">Percent of all activity per year or legislative session</label>
                            <br/>
                            <input type="radio" name="showResults" value="percent_of_filtered" id="showResultsPF"/>
                            <label for="showResultsPF">Percent of filtered activity per year or legislative session</label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" class="center">
                            <input type="submit" name="searchButton"
                                   value="SEARCH" class="button" />
                            <input type="reset" name="clear" value="Reset Form" class="button"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
