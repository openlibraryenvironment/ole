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
<%@ page import="org.springframework.util.StopWatch" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>

<jsp:useBean id="repositoryBrowser" class="org.kuali.ole.RepositoryBrowser" scope="session"/>

<div id="showCount">
    <table border="0" width="70%" height="50%" align="center" cellspacing="0" cellpadding="0">
        <tr valign="top" bgcolor="#FFFFFF">
            <th align="left"><%out.println("Category"); %></th>
            <th align="left"><%out.println("Type"); %></th>
            <th align="left"><%out.println("Format"); %></th>
            <th align="left"><%out.println("Levels"); %></th>
            <th align="left"><%out.println("Node File Count"); %></th>
        </tr>
        <br>
        <%--
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("building node count tree");
            String data = repositoryBrowser.generateNodeCount();
            stopWatch.stop();
            stopWatch.start("iterating node count tree");
            String[] splitData = data.split("\\n");

            for (int i = 2; i < splitData.length; i++) {
                if (splitData[i].contains("=")) {
                    String key = splitData[i].substring(0, splitData[i].indexOf("="));
                    String value = splitData[i].substring(splitData[i].indexOf("=") + 1);
                    String cat = "";
                    String type = "";
                    String format = "";
                    String levels = "";
                    String[] splitLine = key.split("/");

                    if (splitLine.length == 3) {
                        cat = splitLine[1];
                    }
                    else if (splitLine.length == 4) {
                        cat = splitLine[1];
                        type = splitLine[2];
                    }
                    else if (splitLine.length == 5) {
                        cat = splitLine[1];
                        type = splitLine[2];
                        format = splitLine[3];
                    }
                    else if (splitLine.length > 5) {
                        cat = splitLine[1];
                        type = splitLine[2];
                        format = splitLine[3];
                        StringBuilder sb = new StringBuilder();
                        for (int j = 4; j < splitLine.length - 1; j++) {
                            sb.append(splitLine[j] + " ");
                        }
                        levels = sb.toString();
                    }
        <tr valign="top" bgcolor="#FFFFFF">
            <td class="content" valign="top"><%=cat%></td>
            <td class="content" valign="top"><%=type%></td>
            <td class="content" valign="top"><%=format%></td>
            <td class="content" valign="top"><%=levels%></td>
            <td class="content" valign="top"><%=value%></td>
        </tr>
                }
            }
            stopWatch.stop();
        --%>
        <!--
        <tr valign="top" bgcolor="#FFFFFF">
            <td class="content" colspan="10" valign="top">************************************</td>
        </tr>
        -->
        <%
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("building node count map");
            Map<String, Long> map = NodeCountManager.getNodeCountManager().generateNodeCountMap();
            stopWatch.stop();

            stopWatch.start("iterating node count map");
            Set<Map.Entry<String, Long>> keys = map.entrySet();
            Iterator<Map.Entry<String, Long>> it = keys.iterator();
            Map.Entry<String, Long> keyEntry = null;
            while (it.hasNext()) {
                keyEntry = it.next();
                String key = keyEntry.getKey();
                Long value = keyEntry.getValue();
                String cat = "";
                String type = "";
                String format = "";
                String levels = "";
                String[] splitLine = key.split("/");

                if (splitLine.length == 2) {
                    cat = splitLine[1];
                }
                else if (splitLine.length == 3) {
                    cat = splitLine[1];
                    type = splitLine[2];
                }
                else if (splitLine.length == 4) {
                    cat = splitLine[1];
                    type = splitLine[2];
                    format = splitLine[3];
                }
                else if (splitLine.length > 4) {
                    cat = splitLine[1];
                    type = splitLine[2];
                    format = splitLine[3];
                    StringBuilder sb = new StringBuilder();
                    for (int j = 4; j < splitLine.length; j++) {
                        sb.append(splitLine[j] + " ");
                    }
                    levels = sb.toString();
                }
        %>
        <tr valign="top" bgcolor="#FFFFFF">
            <td class="content" valign="top"><%=cat%>
            </td>
            <td class="content" valign="top"><%=type%>
            </td>
            <td class="content" valign="top"><%=format%>
            </td>
            <td class="content" valign="top"><%=levels%>
            </td>
            <td class="content" valign="top"><%=value%>
            </td>
        </tr>
        <%
            }
            stopWatch.stop();
            //            stopWatch.start("map to string");
            //            System.out.println(map.toString());
            //            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        %>

    </table>
</div>


