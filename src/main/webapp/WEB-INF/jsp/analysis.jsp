<%-- 
    Document   : analysis
    Created on : Dec 9, 2008, 1:08:21 PM
    Author     : Paul Wolfgang
    This is the form used to select the data sets and policy areas.
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="viewport" content="width=deice-width, initial-scale=1" />
        <title>Analysis Page</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
              rel="stylesheet" 
              integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" 
              crossorigin="anonymous">
        <link type="text/css" rel="stylesheet" href="oneform.css" />
        <link type="text/css" rel="stylesheet" href="policy.css" />
        <link type="text/css" rel="stylesheet" href="style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>
        <main>
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
                    <table role="region" aria-label="dataset select" class="table table-condensed">
                        <tr>
                            <th style="width:38%">DATASETS TO SEARCH:</th>
                            <th style="width:50%">DATASET FILTERS
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
                                                    <%
                                                        if (first) {
                                                            first = false;
                                                        } else {
                                                            %><br/><%
                                                        }
                                                    %>
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
                    </table>
                    <table role="region" aria-label="policy code selection" class="table table-condensed">
                        <tr>
                            <th>
                                <p>POLICY AREAS TO INCLUDE: </p>
                                <p>
                                    Each record includes a short description (a sentence
                                    or phrase) of the item.  This features allows a text
                                    search of that field and will help identify the best
                                    topic from the list below.
                                </p>
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
                            </th>
                        </tr>
                        <tr>
                            <td>
                                <c:forEach var="majorTopic" items="${topics}">
                                    <div class="header">
                                        <c:out value="${majorTopic.description}" />
                                    </div>
                                    <input type="checkbox" name="subtopics" value="${majorTopic.code}" id="subtopics${majorTopic.code}"/>
                                    <label for="subtopics${majorTopic.code}">All subtopics in ${majorTopic.description}</label>
                                    <br /><input type="button"
                                                 onclick="expandSubtopics(${majorTopic.code}, this);"
                                                 id="x${majorTopic.code}" value="Expand subtopics"/>
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
                    </table>
                    <table role="region" aria-label="free text search" class="table table-condensed">
                        <tr>
                            <td><fieldset><legend>
                                        FREE TEXT
                                    </legend>
                                    <label for="freetext">Key word(s)</label>
                                    <input type="text" size="100" id="freetext" name="freetext" value="" />
                                    <br/>
                                    <span class="smallRedText">
                                        Note: the text descriptions for each record are very short
                                        and generally not sufficient for accurate keyword search.
                                    </span>
                                    <fieldset><legend>Apply Search to</legend>
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
                                </fieldset>
                            </td>
                        </tr> 
                    </table>
                    <table role="region" aria-label="date range select" class="table table-condensed">
                        <tr>
                            <td>
                                <fieldset><legend>
                                        GROUP BY
                                    </legend>
                                    <div style="display:table-cell; width:50%">
                                        <fieldset><legend>Years</legend>
                                            <p>
                                                <input type="radio" name="span" value="years" id="spanYears" checked="checked" />
                                                <label for="spanYears">years</label>
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
                                        </fieldset>
                                    </div>
                                    <div style="display:table-cell; width:50%">
                                        <fieldset>
                                            <legend>Legislative Sessions</legend>
                                            <p>
                                                <input type="radio" name="span" value="sessions" id="spanSessions" />
                                                <label for="spanSessions">Legislative Sessions</label>
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
                                        </fieldset>
                                    </div>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fieldset><legend>GRAPH AS:</legend>
                                    <input type="radio" name="showResults" value="count" id="showResultsC" checked="checked" />
                                    <label for="showResultsC">Number of cases per year or legislative session</label>
                                    <br/>
                                    <input type="radio" name="showResults" value="percent_of_total" id="showResultsPT" />
                                    <label for="showResultsPT">Percent of all activity per year or legislative session</label>
                                    <br/>
                                    <input type="radio" name="showResults" value="percent_of_filtered" id="showResultsPF"/>
                                    <label for="showResultsPF">Percent of filtered activity per year or legislative session</label>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td class="center">
                                <input type="submit" name="searchButton"
                                       value="SEARCH" class="button" />
                                <input type="reset" name="clear" value="Reset Form" class="button"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </main>
        <script type="text/javascript" src="script.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" 
                integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" 
        crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    </body>
</html>
