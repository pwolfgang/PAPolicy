<?xml version="1.0" encoding="UTF-8"?>
<%--
Copyright (c) 2018, Temple University
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.
* All advertising materials features or use of this software must display 
  the following  acknowledgement
  This product includes software developed by Temple University
* Neither the name of the copyright holder nor the names of its 
  contributors may be used to endorse or promote products derived 
  from this software without specific prior written permission. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
--%>
<%-- 
    Document   : results
    Created on : Jan 10, 2009, 12:02:39 PM
    Author     : Paul Wolfgang
    This displays the results both in the form of a graph and table.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.ArrayList, edu.temple.cla.papolicy.Column"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Analysis Page</title>
            <link type="text/css" rel="stylesheet" href="style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>
        <p>To download the raw data as an Excel spreadsheet, click on the link</p>
        <c:forEach var="column" items="${columns}">
            ${column.downloadURL}
        </c:forEach>
        <br/><br/>
        <img alt="graph goes here" height="600" width="800"
             src="<c:url value="chart">
                 <c:param name="dataset" value="${dataset}"/>
             </c:url>" />
        <p>To download the image, right-click on the image</p>
        <br/>
        <table border="2">
            <tr>
                <th>
                    <c:choose>
                        <c:when test="${yearRange.session}">Session</c:when>
                        <c:otherwise>Year</c:otherwise>
                    </c:choose>
                </th>
                <c:forEach var="column" items="${columns}">
                    <th>
                        <c:out value="${column}" />
                    </th>
                </c:forEach>
            </tr>
            <%
                ArrayList<Column> columns =
                        (ArrayList<Column>) request.getAttribute("columns");
                for (String rowKey : columns.get(0).getRowKeys()) {
            %>
            <tr>
                <td><%=rowKey%></td>
                <%
                    for (Column column : columns) {
                        String drillDown = column.getDrillDown(rowKey);
                %>
                <td>
                    <%
                                    String s = column.getDisplayedValueString(rowKey);
                                    if (!s.equals("null") && drillDown != null) {%>
                    <a href="<%=drillDown%>">
                        <%}
                            if (!s.equals("null")) {
                        %>
                        <%=s%>
                        <%
                        } else {
                        %>
                        &nbsp;
                        <%                                    }
                                    if (!s.equals("null") && drillDown != null) {%>
                    </a>
                    <%}%>
                </td>
                <%
                    }
                %>
            </tr>
            <%
                }
            %>
        </table>
        <a href="analysis.spg">Search Again</a>
    </body>
</html>