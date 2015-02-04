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
<% response.setContentType("text/xml"); %><?xml version="1.0" encoding="iso-8859-1"?>

<%@ page language="java" %>

<jsp:useBean id="documentStoreContentManager" class="org.kuali.ole.DocumentStoreContentManager" />

<%
//out.println(documentStoreContentManager.browseRepository());
out.println("Browsing disabled due to volume of data");
%>
