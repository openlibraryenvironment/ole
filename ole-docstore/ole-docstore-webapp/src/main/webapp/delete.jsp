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
    <script type="text/javascript">
    <!--



    //-->
    $(document).ready(function () {
        $("#deleteResult").hide();
        $("a#userGuideLink").click(function () {
            var href = $(this).attr("href");
            var arrHref = href.split("|");
            window.open(arrHref[0], "PopUp", arrHref[1])
            return false;
        });
    });

    function buildDeleteRequest(idsList) {
        var rad_val;
        for (var i = 0; i < document.deleteForm.delete.length; i++) {
            if (document.deleteForm.delete[i].checked) {
                rad_val = document.deleteForm.delete[i].value;
            }
        }
        var operation = document.getElementById("deleteDocAction").value;
        showDeleteResult(idsList, operation, rad_val);
    }

    function showDeleteResult(ids, operation, identifierType) {
        document.getElementById("showDeleteResult").value = "";
        $.ajaxSetup({async:false});
        $.post("./document", {docAction:operation, requestContent:ids.value, identifierType:identifierType},
               function (data) {
                   $("#deleteResult").show();
                   document.getElementById("showDeleteResult").value = data.toString();
               }, 'html');
    }


    </script>
<a id="userGuideLink"
   href=" https://wiki.kuali.org/display/OLE/User+Guide+-+OLE+Document+Store#UserGuide-OLEDocumentStore-2.7Delete|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
    <img align="right" src="images/user-guide.jpg" title="User Guide"/> </a>
<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
	<tr height="98%" valign="top">
		<td>
		     <form  method="POST" name="deleteForm">
                 <input type="hidden" id="requestContent" name="requestContent" value="">
                 <input type="hidden" id="identifierType" name="identifierType" value="">
<%--
                 <input type="hidden" name="docAction" value=docAction>
--%>


			  <table>
			   <tr>
			    <td><h2>Delete documents</h2></td>
			   </tr>
                  <tr>
                   <td>&nbsp;</td>
                  </tr>
			    <tr>
                    <td>
                        DocType :
                    </td>
                    <td>
                        Bibliographic
                    </td>
                    </tr>
                  <tr>
			    <td>
			        Operation :
			        </td>
			        <td>
                        <select name="deleteDocAction" id="deleteDocAction">
                            <option value="--">--Select--</option>
                            <option value="delete">Delete</option>
                            <option value="deleteWithLinkedDocs">DeleteWithLinkedDocs </option>
                        </select>

			        </td>
			    </tr>
                  <tr>

                  </tr><tr></tr><tr></tr>
                  <tr>
                      <td>Identifier Type :</td>

                      <td><input type="radio" id="delete" name="delete" checked="checked" value="UUID"/>
                          UUID
                      </td>
                      <td><input type="radio" name="delete" value="SCN"/>
                          SCN

                      </td>

                      <td><input type="radio" name="delete" value="ISBN"/>
                          ISBN

                      </td>

                  </tr>
                  <tr>

                      <td>Identifier Value (use comma as separator) :</td>
                      <td id="select03Container">
                          <textarea id="IDs" name="IDs" rows="4" cols="25"></textarea>
                      </td>
                  </tr>

                  <tr>
                      <td></td>
                      <td>
                          <input type="button" value="Submit" onclick="buildDeleteRequest(IDs);"/>
                      </td>
                  </tr>


                    <tr>
                        <td colspan="2" id="deleteResult"> <br/>
                            <h2>Result</h2>
                            <textarea rows="15" cols="60" readonly="true" id="showDeleteResult"></textarea>
                            <br/>
                            <input type="button" value="Clear" onclick='document.getElementById("showDeleteResult").value = ""'/>
                               </td>
                    </tr>
			  </table>

			  </form>
		</td>
	</tr>

</table>

