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
<%@ page import="org.kuali.ole.docstore.repository.NodeCountManager" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.kuali.ole.docstore.repository.RdbmsNodeCountManager" %>
<%@ page import="org.kuali.ole.docstore.service.BeanLocator" %>
<%@ page import="org.kuali.ole.docstore.factory.DocstoreFactory" %>
<%@ page import="org.kuali.ole.docstore.factory.RdbmsJcrDocstoreFactory" %>
<%@ page import="org.kuali.ole.docstore.factory.JcrDocstoreFactory" %>
<%@ page import="java.util.HashSet" %>

<jsp:useBean id="repositoryBrowser" class="org.kuali.ole.RepositoryBrowser" scope="session"/>

<table border="0" width="70%" height="50%" align="center" cellspacing="0" cellpadding="0">
    <tr valign="top" bgcolor="#FFFFFF">
        <th align="left"><%out.println("Category"); %></th>
        <th align="left"><%out.println("Type"); %></th>
        <th align="left"><%out.println("Format"); %></th>
        <th align="left"><%out.println(" File Count"); %></th>
    </tr>

    <%--<% String data = repositoryBrowser.generateNodeCount();
        String[] splitData = data.split("\\n");
        for (int i = 2; i < splitData.length; i++) {
            if (splitData[i].contains("=")) {
                String key = splitData[i].substring(0, splitData[i].indexOf("="));
                String value = "";
                String cat = "";
                String type = "";
                String format = "";
                String[] splitLine = key.split("/");

                if (splitLine.length == 3) {
                    cat = splitLine[1];
                }
                else if (splitLine.length == 4) {
                    type = splitLine[2];
                }
                else if (splitLine.length == 5) {
                    format = splitLine[3];
                    value = splitData[i].substring(splitData[i].indexOf("=") + 1);
                }
                if (!(cat == "" && format == "" && type == "" && value == "")) {
    %>--%>

    <%

        DocstoreFactory docstoreFactory = BeanLocator.getDocstoreFactory();
//        Map<String, Long> jcrMap = NodeCountManager.getNodeCountManager().generateNodeCountMap();
        Map<String, Long> rdbmsMap = RdbmsNodeCountManager.getNodeCountManager().generateNodeCountMap();

        if(docstoreFactory instanceof JcrDocstoreFactory){
            Set<Map.Entry<String, Long>> keys = new HashSet<Map.Entry<String, Long>>();// jcrMap.entrySet();
            Iterator<Map.Entry<String, Long>> it = keys.iterator();
            Map.Entry<String, Long> keyEntry = null;
            while (it.hasNext()) {
                keyEntry = it.next();
                String key = keyEntry.getKey();
                Long value = keyEntry.getValue();
                String cat = "";
                String type = "";
                String format = "";
                String[] splitLine = key.split("/");

                if (splitLine.length == 2) {
                    cat = splitLine[1];
                }
                else if (splitLine.length == 3) {
                    type = splitLine[2];
                }
                else if (splitLine.length == 4) {
                    format = splitLine[3];
                }
                if (!(cat == "" && format == "" && type == "" && value != 0)) {
    %>
    <tr valign="top" bgcolor="#FFFFFF">
        <td class="content" valign="top"><%=cat%></td>
        <td class="content" valign="top"><%=type%></td>
        <td class="content" valign="top"><%=format%></td>
        <td class="content" valign="top"><%=value%></td>
    </tr>
    <%
            }
        }
    }
    else if(docstoreFactory instanceof RdbmsJcrDocstoreFactory) {

        Set<Map.Entry<String, Long>> keys = rdbmsMap.entrySet();
        Iterator<Map.Entry<String, Long>> it = keys.iterator();
        Map.Entry<String, Long> keyEntry = null;
        while (it.hasNext()) {
            keyEntry = it.next();
            String key = keyEntry.getKey();
            Long value = keyEntry.getValue();
            String cat = "";
            String type = "";
            String format = "";
            if(key.equalsIgnoreCase("work"))
            {
                cat = key;
            }
            else if(key.equalsIgnoreCase("bibliographic")){
                type = key;
                format="marc";
            }
            else if(key.equalsIgnoreCase("holdings"))
            {
                type = key;
                format = "oleml";

            }
            else if(key.equalsIgnoreCase("items"))
            {
                type = key;
                format = "oleml";

            }

    %>
    <tr valign="top" bgcolor="#FFFFFF">
        <td class="content" valign="top"><%=cat%></td>
        <td class="content" valign="top"><%=type%></td>
        <td class="content" valign="top"><%=format%></td>
        <td class="content" valign="top"><%=value%></td>
    </tr>
    <%

            }
        }
    %>
</table>


