
<%-- 
    Document   : drillDown
    Created on : Apr 17, 2009, 2:29:26 PM
    Author     : Paul Wolfgang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
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
            List<Map<String, Object>> theList =
                (List<Map<String, Object>>) request.getAttribute("theList");
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
                                                    String txt = obj.toString();
                                                    if (txt.startsWith("#") && txt.endsWith("#")) {
                                                        txt = txt.substring(1, txt.length() - 1);
                                                        %><a href="<%=txt%>" target="_blank"><%=txt%></a><%
                                                    } else {
                                                        %><%=txt%><%
                                                    }
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
                    <%
                }
        %>
    </body>
</html>
