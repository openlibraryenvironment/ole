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
<%@ page errorPage="errorPage.jsp" %>
<html>
<head>
<style type="text/css">
    #header {
        height: 51px !important;
    }
</style>
<!--
<link rel="stylesheet" type="text/css" href="./css/ole.css" />
-->
<script src="./script/validation.js" language="JavaScript"></script>
<!--
<script src="./script/ole.js" language="JavaScript"></script>
-->
</head>
<body >

	<br/>
 	<jsp:include page="/jsp/tiles/header.jsp"/>
<!--
	<table  id="main" width="994px" height="75%" cellspacing="0" cellpadding="0" border="0" align="center" >

	<tr height="110%" >
		<td>
-->
			<jsp:include page="home.jsp"/> </div>
<!--
		</td>
	</tr>
	<tr height="2%">
		<td>
-->
  			<jsp:include page="/jsp/tiles/footer.jsp"/>
<!--
		</td>
	</tr>
</table>
-->
	<br/>
</body>
</html>
