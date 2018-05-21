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
        <%-- Google Analytics Script --%>
        <script>
          (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
          (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
          m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
          })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

          ga('create', 'UA-22450319-16', 'temple.edu');
          ga('send', 'pageview');
        </script>        
    </body>
</html>
