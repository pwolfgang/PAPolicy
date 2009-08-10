<%-- 
    Document   : results
    Created on : Jan 10, 2009, 12:02:39 PM
    Author     : Paul Wolfgang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
    import="java.util.ArrayList, edu.temple.cla.papolicy.Column"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Analysis Page</title>
        <link type="text/css" rel="stylesheet" href="style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>
        <h3>To download the raw data as an Excel spreadsheet, click on the link</h3>
        <c:forEach var="column" items="${columns}">
            <a href="<c:out value="${column}"/>.xls?query=${column.downloadQuery}">
            <c:out value="${column}"/></a><br/>
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
                    &nbsp;
                </th>
                <c:forEach var="column" items="${columns}">
                    <th>
                        <c:out value="${column}" />
                    </th>
                </c:forEach>
            </tr>
            <%
                ArrayList<Column> columns =
                        (ArrayList<Column>)request.getAttribute("columns");
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
                                    <a href="drilldown.spg?query=<%=drillDown%>">
                                <%}
                                if (!s.equals("null")) {
                                %>
                                <%=s%>
                                <%
                                } else {
                                %>
                                &nbsp;
                                <%
                                }
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
    </body>
</html>