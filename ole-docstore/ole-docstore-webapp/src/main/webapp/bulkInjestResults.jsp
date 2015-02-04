<%--
   - Copyright 2011 The Kuali Foundation.
   - 
   - Licensed under the Educational Community License, Version 2.0 (the "License");
   - you may not use this file except in compliance with the License.
   - You may obtain a copy of the License at
   - 
   - http://www.opensource.org/licenses/ecl2.php
   - 
   - Unless required by applicable law or agreed to in writing, software
   - distributed under the License is distributed on an "AS IS" BASIS,
   - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   - See the License for the specific language governing permissions and
   - limitations under the License.
--%>
<%@ page language="java" import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/ole.css" />
</head>
<body >

	<br/>
<div id="header">

        <!-- Logotyp -->
        <span id="logo"><strong>OLE Doc Store - Ingester</strong><br/>
        &nbsp;  <div id="copyright">POC by  HTC Global Services, Inc.</div>
        </span>

    </div>
	<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
	<tr height="98%" valign="top">
		<td>
              <table>
		      <%
                    Map uuidMap = (Map)request.getAttribute("results");
                    String fileName;
                    for (Iterator<String> iterator = uuidMap.keySet().iterator(); iterator.hasNext();) {
                        fileName = iterator.next();
                        Integer size = (Integer)uuidMap.get(fileName);
                    %>
                     <tr>
                        <td>
                            <%
                      out.println("Successfully ingested " + fileName + "containing " + size + " files");
                            %>

                        </td>
                     </tr>

                    <%
                    }

		      %>
		      </table>
		</td>
	</tr>

</table>

	<br/>
</body>
</html>
