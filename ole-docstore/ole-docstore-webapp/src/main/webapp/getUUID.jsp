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
    <script type="text/javascript">
    <!--

        function loadSelectElement(selObjId, options) {
            var selObj = document.getElementById(selObjId);

            // clear the target select element apart from the "select your..." option
            selObj.options.length = 1;

            // copy options from array of [value, pair] arrays to select box
            // IE doesn't work if you use the DOM-standard method, however...
            if (typeof(window.clientInformation) != 'undefined') {
                // IE doesn't take the second "before" parameter...
                for (var loop=0; loop<options.length; loop++) selObj.add(new Option(options[loop][1], options[loop][0]));
            } else {
                for (var loop=0; loop<options.length; loop++) selObj.add(new Option(options[loop][1], options[loop][0]), null);
            }
        }

        function madeSelection(selObj) {
            var selectedValue = selObj.options[selObj.selectedIndex].value;
            var selectedText = selObj.options[selObj.selectedIndex].text;
            if (selectedValue == '--') return;

            if (selObj.name == 'category') {
                document.getElementById('select02Container').style.display = 'block';
                document.getElementById('type').options[0].text = '--Select--';


                switch(selectedValue) {
                    case 'bibliographic':
                        loadSelectElement('type', [
                            ['bibliographic', 'bibliographic'],
                            ['item', 'item'],
							 ['holdings','holdings']

                        ]);
                        return;

                    case 'authority':
                        loadSelectElement('type', [
                            ['person', 'person'],
                            ['family', 'family'],
                            ['corporate', 'corporate'],
                            ['meeting', 'meeting'],
                            ['topical', 'topical']
						    ['subject', 'subject']
							['geographic', 'geographic']
                        ]);
                        return;


                        return;
                }
            } // select01

            if (selObj.name == 'type') {
                document.getElementById('select03Container').style.display = 'block';
                document.getElementById('format').options[0].text = '--Select-- ';

                switch(selectedValue) {
                    case 'bibliographic':

                        loadSelectElement('format', [
                            ['marc', 'marc'],
                            ['dublin', 'dublin']


                        ]);
                        return;
                    case 'holdings':
                        loadSelectElement('format', [
                            ['marc', 'marc']


                        ]);
                        return;

                    case 'item':
                        loadSelectElement('format', [
                           ['marc', 'marc']
                        ]);
                        return;

                    case 'person':return;
                    case 'family':   return;
                    case 'corporate': return;
                    case 'meeting':  return;
                    case 'topical':   return;
                    case 'subject':   return;
					case 'geographic':  return;



                }
            } // select02
        }

    //-->
    </script>

</head>
<%
String pageTitle="Get UUIDs";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<%@ include file="oleHeader.jsp" %>
	<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
	<tr height="98%" valign="top">
		<td>
		     <form action="getUUIDs" method="POST">
			  <table>
			   <tr>
			    <td>&nbsp;</td>
			   </tr>
			    <tr>
			    <td>
			        Category (e.x. Bibliographic, Authority etc.)
			        </td>
			        <td>
                        <select name="category" id="category" onchange="madeSelection(this);">
                            <option value="--">--Select--</option>
                            <option value="bibliographic">bibliographic</option>
                            <option value="authority">authority </option>
                        </select>
			        </td>
			    </tr>
                  <tr>
			    <td>
			        Type (e.x. Bibliographic, Item etc.)
			        </td>
			        <td id="select02Container">

                           <select name="type" id="type" onchange="madeSelection(this);">
                             <option value="--">--Select--</option>
                         </select>

			        </td>
			    </tr>
			    <tr>
			        <td>
			        Format (e.x. Marc, Eac etc.)
			        </td>
			        <td>
                        <select name="format" id="format" onchange="madeSelection(this);">
                            <option value="--">--Select--</option>
                        </select>
			        </td>
			    </tr>
			    <tr>
			        <td>
			        Num UUIDs
			        </td>
			        <td id="select03Container">
			            <input type="text" name="numUUIDs"/>
			        </td>
			    </tr>
			    <tr>
			        <td></td>
			        <td>
			            <input type="submit" value="submit"/>
			        </td>
			    </tr>
			  </table>
			  </form>
		</td>
	</tr>

</table>

	<br/>
</div> <!-- close <div id="iframe_portlet_container_div"> from newHeader.jsp -->
<%@ include file="oleFooter.jsp" %>
</body>
</html>
