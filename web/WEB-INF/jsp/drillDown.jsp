<%-- 
    Document   : drillDown
    Created on : Apr 17, 2009, 2:29:26 PM
    Author     : Paul Wolfgang
    This displays the details for a selected cell in the results table.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link type="text/css" rel="stylesheet" href="oneform.css" />
        <link type="text/css" rel="stylesheet" href="policy.css" />
        <link type="text/css" rel="stylesheet" href="style.css" />
        <script type="text/javascript" src="script.js"></script>
        <title>Drilldown Results</title>
    </head>
    <body>
        <%@include file="header.jspf"%>
        <h2>Drilldown Results</h2>
        <%
            List<Map<String, Object>> theList
                    = (List<Map<String, Object>>) request.getAttribute("theList");
            if (theList.size() > 0) {
                Set<String> columnTitles = theList.get(0).keySet();
        %>
        <table border="1">
            <tr>
                <c:forEach var="columnTitle" items="<%=columnTitles%>">
                    <th>${columnTitle}</th>
                    </c:forEach>
            </tr>
            <%
                for (Map<String, Object> rowData : theList) {
            %>
            <tr valign="top">
                <%
                    for (Object obj : rowData.values()) {
                %>
                <td>
                    <%
                        if (obj != null) {
                            String txt;
                            if (obj instanceof Date) {
                                txt = String.format("%1$tb&nbsp;%1$te,%1$tY", obj);
                            } else {
                                txt = obj.toString();
                            }
                    %><%=edu.temple.cla.papolicy.Utility.reformatHyperlink(txt)%><%
                        } else {
                    %>&nbsp;<%
                        }
                    %>
                </td>
                <%
                    }
                %>
            </tr>
            <%
                }
            %>
        </table>
        <%
        } else {
        %>
        <h1> No Drilldown Data Available </h1>
        <%                        }
        %>
    </body>
</html>
