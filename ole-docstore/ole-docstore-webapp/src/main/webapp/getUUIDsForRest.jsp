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
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
    <script type="text/javascript">

    <!--

    $(document).ready(function () {
        $("a#userGuideLink").click(function () {
            var href = $(this).attr("href");
            var arrHref = href.split("|");
            window.open(arrHref[0], "PopUp", arrHref[1])
            return false;
        });
    });
    var requestUri = "<%=request.getRequestURI()%>";
    var reqUrl = "<%=request.getRequestURL()%>";
    var rootURL = reqUrl.replace(requestUri, "") + "/oledocstore/rest/documents";

    function displayUUIDs(documentCategory,documentType,documentFormat,staringUUID,endingUUID) {

        var category = documentCategory.value;
        var type = documentType.value;
        var format =documentFormat.value;
       var dataString = 'staringUUID='+staringUUID.value+'&endingUUID='+endingUUID.value+'&category='+category+'&type='+type+'&format='+format;
          $.ajax({
            type:'GET',
            url:rootURL + '?staringUUID='+staringUUID.value+'&endingUUID='+endingUUID.value+'&category='+category+'&type='+type+'&format='+format,
            contentType:'application/xml',
            dataType:"xml",
            success:function (data) {
                var uuids = new XMLSerializer().serializeToString(data);
                $("#showResult").show();
                document.getElementById("displayUUIDs").value = uuids;
            },
            error:function (jqXHR, textStatus, errorThrown) {
                log.error('checkout error: ' + textStatus);


            }

        });

    }
    var configInfoXml = null;

    $(document).ready(function() {
        $("#showResult").hide();
        $.ajaxSetup({async:false});
        $.post("./getDocumentConfigInfo", { doccategoryID: 'all' }, function(data) {
            configInfoXml = data;
        });
        onChangeCategory();
        $("#documentCategory").change(function() {
            var doccategoryId = $("#documentCategory").val();
            populateDocTypeDropdown($("#documentType"), configInfoXml, doccategoryId);
        });
    });


    function onChangeCategory() {
        var doccategoryId = $("#documentCategory").val();
        populateDocTypeDropdown($("#documentType"), configInfoXml, doccategoryId);
    }

    function onChangeDocType() {
        var doccategoryId = $("#documentCategory").val();
        var doctypeId = $("#documentType").val();
        populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId, doctypeId);
    }


    function populateDocTypeDropdown(select, data, doccategoryId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType", doccategoryId);
        $(data).find(nodePath).each(function() {
            select.append($('<option></option>').val($(this).attr('id')).html($(this).attr('name')));
        });
        $("#documentType").change(function() {
            var doctypeId = $(this).val();
            populateFormatDropdown($("#documentFormat"), configInfoXml, doccategoryId, doctypeId);
        });
        onChangeDocType();
    }
    function populateFormatDropdown(select, data, doccategoryId, doctypeId) {
        select.html('');
        var nodePath = JQUERY4U.UTIL.formatVarString("documentCategory[id='{1}'] documentType[id='{2}'] documentFormat",
                                                     doccategoryId, doctypeId);
        $(data).find(nodePath).each(function() {
            var idField = $(this).attr('id');
            if (idField != "all") {
                select.append($('<option></option>').val(idField).html($(this).attr('name')));
            }
        });
    }


    var JQUERY4U = {};
    JQUERY4U.UTIL = {
        formatVarString: function() {
            var args = [].slice.call(arguments);
            if (this.toString() != '[object Object]') {
                args.unshift(this.toString());
            }
            var pattern = new RegExp('{([1-' + args.length + '])}', 'g');
            return String(args[0]).replace(pattern, function(match, index) {
                return args[index];
            });
        }
    };

    //-->
    </script>
<a id="userGuideLink"
   href= "https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
    <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>
<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
	<tr height="98%" valign="top">
		<td>
		     <form  method="POST">
			  <table>
			   <tr>
			    <td><h2>Get UUIDs of documents with the following criteria:</h2></td>
			   </tr>
                  <tr>
                   <td>&nbsp;</td>
                  </tr>
			    <tr>
			    <td>
			        Category (e.x. Work, Authority etc.)
			        </td>
			        <td>
                        <select name="documentCategory" id="documentCategory" class="category">
                            <option value="work">Work</option>
                        </select>
			        </td>
			    </tr>
                  <tr>
			    <td>
			        Type (e.x. Bibliographic, Item etc.)
			        </td>
			        <td>

                           <select name="documentType" id="documentType" name="documentType" class="category">

                         </select>

			        </td>
			    </tr>
			    <tr>
			        <td>
			        Format (e.x. Marc, Eac etc.)
			        </td>
			        <td>
                        <select name="documentFormat" id="documentFormat" class="category">
                        </select>
			        </td>
			    </tr>
			    <tr>
			        <td>
			       Start
			        </td>
                    <td>
                        <input type="text" id="staringUUID" name="staringUUID"/>
                    </td>
			    </tr>
                  <tr>
                      <td>
                         Num UUIDs
                      </td>
                      <td>
                          <input type="text" id="endingUUID" name="endingUUID"/>
                      </td>
                  </tr>

                  <tr>
			        <td></td>
			        <td>
			            <input type="button" value="Submit" id="UUIDs" onclick="displayUUIDs(documentCategory,documentType,documentFormat,staringUUID,endingUUID);">
			        </td>
			    </tr>

              </table>
             </form>
                 <div id="showResult">
                    <h2>Result</h2>
                    <textarea rows="20" cols="70" id="displayUUIDs" readonly="true"></textarea>
                    <br/>
                    <input type="button" value="Clear" onclick='document.getElementById("displayUUIDs").value = ""'/>
                 </div>
		</td>
	</tr>

</table>
