<%-- 
    Document   : drillDown
    Created on : Apr 17, 2009, 2:29:26 PM
    Author     : Paul Wolfgang
    Special drilldown page for the transcript data. It displays the committees and bills for each transcript selected
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
            List<Map<String, Object>> theList =
                    (List<Map<String, Object>>) request.getAttribute("theList");
            Set<String> columnTitles = theList.get(0).keySet();
        %>
        <table border="1">
            <tr>
                <c:forEach var="columnTitle" items="<%=columnTitles%>">
                    <th>
                        ${columnTitle}
                    </th>
                </c:forEach>
            </tr>
            <%
                for (Map<String, Object> rowData : theList) {
            %>
            <tr valign="top">
                <%
                    for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                %>
                <td>
                    <%
                        if (entry.getValue() != null) {
                            if (entry.getKey().equals("Committees")) {
                                List<String> committeeNamesList = (List<String>) entry.getValue();
                                for (String committeeName : committeeNamesList) {
                    %><%=committeeName%><br/><%
                        }
                    } else if (entry.getKey().equals("Bills")) {
                        List<String> billsIdList = ((List<String>) entry.getValue());
                        for (String billId : billsIdList) {
                    %><a href="billDrillDown.spg?billId=<%=billId%>"><%=billId%></a><br/><%
                        }
                    } else {
                        String txt = entry.getValue().toString();
                        if (txt.startsWith("#") && txt.endsWith("#")) {
                            txt = txt.substring(1, txt.length() - 1);
                    %><a href="<%=txt%>" target="_blank"><%=txt%></a><%
                    } else {
                    %><%=txt%><%
                            }
                        }
                    } else {
                    %>&nbsp;<%                                        }
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
    </body>
</html>
