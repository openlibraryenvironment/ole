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
</head>
<%
	String pageTitle = "Linking Documents";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">	
	<%@ include file="oleHeader.jsp"%>
		<table align="center" border="0" width="994px" height="85%"
			cellpadding="0" cellspacing="0">
			<tr height="98%" valign="top">
				<td>
					<!--<form name="linkMarcAndMarcHolding" action="bulkLink" method="post">					
						<p>Link Marc and Marc-Holding records: <input type="hidden" name="linkPair" value="marc-marcHolding" />
						<input type="submit" value="Submit" /></p>
					</form>-->	
					
					<form name="linkItemAndBib" action="bulkLink" method="post">					
						<p>Link Item and Bib records: <input type="hidden" name="linkPair" value="item-bib" />
						<input type="submit" value="Submit" /></p>
					</form>	
							
				</td>
			</tr>			
		</table>
	<br />
	<%@ include file="oleFooter.jsp"%>
</body>
</html>
