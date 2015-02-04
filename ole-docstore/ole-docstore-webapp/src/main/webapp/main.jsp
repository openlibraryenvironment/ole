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
<html>
<head>
<script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script>

    function hideOnLoad() {

       <%
     if( "services".equals(request.getParameter("tab"))) { %>
            $("#service").show();
            $("#sections").hide();
        <%} else{ %>
            $("#service").hide();

        <%} %>

    }
    function hide() {
       $("#service").show();
       $("#sections").hide();
    }
    function show() {

      $("#service").hide();
      $("#sections").show();
    }
</script>
<link type="text/css" rel="Stylesheet" href="script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css" />
</head>
<script>
$(function() {
    $( "#sections" ).tabs({
        ajaxOptions: {
            error: function( xhr, status, index, anchor ) {
                $( anchor.hash ).html(
                    "Couldn't load this tab. We'll try to fix this as soon as possible. " +
                    "If this wouldn't be a demo." );
            }
        }
    });
});
</script>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="hideOnLoad()">
<div id="sections" style="margin: 10px !important;">
	<ul>
		<li><a href="summary-discovery.jsp">Summary</a></li>
		<li><a href="./testRestfulAPI.html">Test RESTful API</a></li>
		<li><a href="./TestUrls.jsp">Sample SOLR queries</a></li>
		<li><a href="bib/admin/utility.jsp">Admin Pages</a></li>
	</ul>
</div>

<% String tabField = request.getParameter("tab");
    if (tabField != null) {%>
<input type="hidden" id="services" value="<%=tabField%>"/>
<% }%>

</body>
</html>