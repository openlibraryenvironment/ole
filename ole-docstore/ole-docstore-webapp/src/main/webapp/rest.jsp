<html>
<head>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
    <link type="text/css" rel="Stylesheet"
          href="script/jquery-ui-1.8.16.custom/css/smoothness/jquery-ui-1.8.16.custom.css"/>
</head>

<%
    String pageTitle = "";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<%@ include file="oleHeader.jsp" %>

<script>
    $(function () {
        $("#sections").tabs({
            ajaxOptions:{
                error:function (xhr, status, index, anchor) {
                    $(anchor.hash)
                            .html("Couldn't load this tab. We'll try to fix this as soon as possible. "
                            + "If this wouldn't be a demo.");
                }
            }
        });
    });

    $(document).ready(function () {
        $("#restIngestResult").hide();
        $("#restCheckInResult").hide();
        $("#restDeleteResult").hide();
        $("#restCheckOutResult").hide();
        $("#bagItResult").hide();
        $.ajaxSetup({async:false});
        $.post("sampleIngestRequest.xml", function (data) {

            $("#sampleRestIngestRequestXML").html(data.toString());
        }, 'html');

        $.ajaxSetup({async:false});
        $.post("sampleCheckInRequest.xml", { doccategoryID:'all' }, function (data) {
            $("#sampleCheckInRequestXML").html(data.toString());
        }, 'html');


    });
</script>
<script>
    // var rootURL = "http://localhost:9080/oledocstore/rest/documents";
    var requestUri = "<%=request.getRequestURI()%>";
    var reqUrl = "<%=request.getRequestURL()%>";
    var rootURL = reqUrl.replace(requestUri, "") + "/oledocstore/rest/documents";
    var bagItUrl = reqUrl.replace(requestUri, "") + "/oledocstore/multiPartBagRequestClientServlet";

/*    function ingestStingContent(ingestContent) {

           var dataString = ingestContent.value;

           $.ajax({
            type:'POST',
            contentType:'application/xml',
            url:rootURL,
            dataType:"xml",
            data:dataString,
            success:function (data, textStatus, jqXHR) {
                var msg = new XMLSerializer().serializeToString(data);
                $("#restIngestResult").show();
                document.getElementById("showIngestResult").value = msg;
            },
            error:function (jqXHR, textStatus, errorThrown) {
                log.error('Ingest content error: ' + textStatus);
            }
        });

    }*/

    function buildCheckOutRequest(uuid,checkOutRequest)

    {
        var uuidValue = uuid.value;
        var  checkOutRequestContent = checkOutRequest.value;

        if(checkOutRequestContent.length > 0)
        {
            console.log('findById: ' + checkOutRequest);
            $.ajax({
                type:'GET',
                url:rootURL + '?requestXML=' + checkOutRequest.value,
               // contentType:'application/xml',
                //dataType:"multipart",
                success:function (data) {
                    var checkoutData = new XMLSerializer().serializeToString(data);
                    $("#restCheckOutResult").show();
                    document.getElementById("showCheckOutResult").value = checkoutData;
                },
                error:function (jqXHR, textStatus, errorThrown) {
                    log.error('checkout error: ' + textStatus);


                }

            });

        }
        else if(uuidValue.length > 0)
        {
            findById(uuid);
        }

    }


    function findById(checkOutUUID) {
        console.log('findById: ' + checkOutUUID);
        $.ajax({
            type:'GET',
            url:rootURL + '/' + checkOutUUID.value,
           // contentType:'application/xml',
            //dataType:"xml",
            success:function (data) {
               var checkoutData = new XMLSerializer().serializeToString(data);
                $("#restCheckOutResult").show();
                document.getElementById("showCheckOutResult").value = checkoutData;
            },
            error:function (jqXHR, textStatus, errorThrown) {
                log.error('checkout error: ' + textStatus);


            }

        });
    }
    function checkIn(checkInContent, docAction) {
        $.ajax({
            type:'PUT',
            contentType:'application/xml',
            url:rootURL,
            dataType:"xml",
            data:checkInContent.value,
            success:function (data, textStatus, jqXHR) {
                var msg = new XMLSerializer().serializeToString(data);
                $("#restCheckInResult").show();
                document.getElementById("showCheckInResult").value = msg;

            },
            error:function (jqXHR, textStatus, errorThrown) {
                var msg = "checkIn error:" + textStatus + errorThrown;
                $("#restCheckInResult").show();
                document.getElementById("showCheckInResult").value = msg;
            }
        });
    }


    $(document).ready(function () {
        $("a#userGuideLink").click(function () {
            var href = $(this).attr("href");
            var arrHref = href.split("|");
            window.open(arrHref[0], "PopUp", arrHref[1])
            return false;
        });
    });

    function processDeleteRequest(idsList,deleteRequest)
    {
        var idList = idsList.value;
        var delRequest = deleteRequest.value;
        if(idList.length > 0){
            buildDeleteRequest(idList);
        }
        else if(delRequest.length > 0){
            $.ajax({
                type:'DELETE',
                url:rootURL ,
                data:delRequest,
                success:function (data, textStatus, jqXHR) {
                    var msg = new XMLSerializer().serializeToString(data);
                    $("#restDeleteResult").show();
                    document.getElementById("showRestDeleteResult").value = msg;

                }

            });
        }
    }
    function buildDeleteRequest(idsList) {
        var rad_val;
        for (var i = 0; i < document.deleteForm.delete.length; i++) {
            if (document.deleteForm.delete[i].checked) {
                rad_val = document.deleteForm.delete[i].value;
            }
        }
        if (rad_val == 'UUID') {
            var identifierType = "UUID";
            var operation = document.getElementById("deleteDocAction").value;
        }

        if (rad_val == 'SCN') {
            var operation = document.getElementById("deleteDocAction").value;
            var identifierType = "SCN";
       }
        if (rad_val == 'ISBN') {
            var operation = document.getElementById("deleteDocAction").value;
            var identifierType = "ISBN";
        }
        showDeleteResult(idsList, operation, identifierType);
    }

    function showDeleteResult(ids, operation, identifierType) {
        console.log('delete IDs');
        $.ajax({
            type:'DELETE',
            url:rootURL + '/' + ids + '?identifierType=' + identifierType + '&operation=' + operation,
            identifierType:identifierType,
            success:function (data, textStatus, jqXHR) {
                var msg = new XMLSerializer().serializeToString(data);
                $("#restDeleteResult").show();
                document.getElementById("showRestDeleteResult").value = msg;

            }

        });
    }
    function showBagIt(requestFolderPath){
        $.ajax({
            type:'POST',
            contentType:'application/xml',
            url:bagItUrl+"?restUrl="+rootURL+"&requestFolderPath="+requestFolderPath.value,
            dataType:"xml",
            requestFolderPath:requestFolderPath,
            data:requestFolderPath.value,
            success:function (data, textStatus, jqXHR) {
                var msg = new XMLSerializer().serializeToString(data);
                $("#bagItResult").show();
                document.getElementById("showBagItResult").value = msg;
            },
            error:function (jqXHR, textStatus, errorThrown) {
                log.error('Ingest content error: ' + textStatus);
            }
        });
    }

</script>
<div id="sections">
<ul>
    <li><a href="#section-IngestStringContent">Create Document</a></li>
    <li><a href="#section-CheckIn">Update Document</a></li>
    <li><a href="restCheckOut.jsp">Get Document</a></li>
    <li><a href="#deleteDiv"> Delete Document</a></li>
    <li><a href="getUUIDsForRest.jsp">Get UUIDs</a></li>
    <li><a href="#bagIt-Requests">BagIt Requests</a></li>
</ul>

<div id="section-IngestStringContent">
    <a id="userGuideLink"
       href="https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
        <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>
    <table>
        <tr>
            <td>
                <form enctype="multipart/form-data" method="POST" action="./rest/documents">
                    Please choose a file containing the request XML(should not exceed 100KB):<br>
                    <input type="file" name="file" id="file" size="30">  <br>
                    <br>
                    <input type="submit" value="Submit"></button>
                </form>


            </td>
        </tr>
        <%--
                <tr>
                    <td>
                        <input type="hidden" name="docAction" value="ingestContent">

                        <h2>Create Document </h2> <br/>
                        <textarea id="ingestContent" name="ingestContent" cols="80" rows="15"></textarea>
                        <br/>
                        <br/>
                        <input type="button" value="Submit" onclick="ingestStingContent(ingestContent);"></button>
                        <input type="button" value="Clear" onclick='document.getElementById("ingestContent").value=""'/>


                    </td>
                    <td style="padding-left: 10;">
                        <h2>Sample data for Ingest</h2>

                        <br>
                        <br>
                        <textarea rows="15" cols="80" id="sampleRestIngestRequestXML" readonly="true" style="overflow: scroll;">
                        </textarea>
                        <br>
                        <br>

                    </td>
                </tr>
                <tr>
                    <td colspan="2" id="restIngestResult">
                        <h2>Result</h2>
                        <textarea rows="25" cols="100" readonly="true" id="showIngestResult"></textarea>
                        <br/>
                        <input type="button" value="Clear" onclick='document.getElementById("showIngestResult").value=""'/>
                    </td>
                </tr>
        --%>
    </table>
    <br/>
    <br/>

</div>
<!--
    <div id="resultIngest">
        <h2>Result</h2>
        <textarea rows="25" cols="100" readonly="true" id="showIngestResult"></textarea>
        <br/>
        <input type="button" value="Clear" onclick='document.getElementById("showIngestResult").value=""'/>


    </div>
-->
<div id="section-CheckIn">
    <a id="userGuideLink"
       href="https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
        <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>
    <table>
        <tr>
            <td>
                <h2>Update Document</h2>
                <br>
                <br>

                <form name="checkInForm" method="POST">
                    <input type="hidden" name="docAction" id="checkIndocAction" value="checkIn">
                    <!--
                    File UUID to be checked in:<input type="text" name="uuid" value="" size="50">
                    <br>
                    -->
                    Request xml with the document to be checked-in (updated):<br>
                    <textarea rows="15" cols="60" id="checkinStringContent" name="checkInContent"></textarea>
                    <br>
                    <br>
                    <input type="button" value="Submit" onclick="checkIn(checkInContent,docAction)">
                    <input type="button" value="Clear"
                           onclick='document.getElementById("checkinStringContent").value = ""'/>
                </form>
            </td>
            <td style="padding-left: 10;">
                <h2> Sample data for Check-in </h2>
                <br>
                <input type="hidden" name="docAction" value="checkIn"> <br>
                <textarea rows="15" cols="80" id="sampleCheckInRequestXML" readonly="true"
                          style="overflow: scroll;">
                </textarea>
                <br>
                <br>
            </td>
        </tr>
        <tr>
            <td colspan="2" id="restCheckInResult">
                <h2>Result</h2>
                <textarea rows="15" cols="60" readonly="true" id="showCheckInResult"
                          name="showCheckInResult"></textarea>
                <br/>
                <input type="button" value="Clear" onclick='document.getElementById("showCheckInResult").value = ""'/>
            </td>
        </tr>


    </table>


</div>

<%--<div id="section-CheckOut">
    <a id="userGuideLink"
       href="https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
        <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>
    <h2>Get Document</h2>
    <br>
    <br>

    <form name="checkOutForm" method="POST">
        <input type="hidden" name="docAction" value="checkOut">
        UUID of the document to be checked out:
        <br>
        <input type="text" name="uuid" id="uuid" value="" size="50">
        <br> <br>
        <b>&nbsp;&nbsp;(OR) </b>
        <br><br>
        Request XML
        <br>
        <textarea rows="15" cols="40" id="checkOutRequest" name="checkOutRequest"></textarea>
        <br>
        <br>

        <input type="button" value="Submit" onclick="buildCheckOutRequest(uuid,checkOutRequest);">
        <br><br>
    </form>
    <div id="restCheckOutResult">
        <h2>Result</h2>
        <textarea rows="25" cols="80" id="showCheckOutResult" readonly="true"></textarea>
        <br/>
        <input type="button" value="Clear" onclick='document.getElementById("showCheckOutResult").value = ""'/>
    </div>

</div>--%>

<div id="deleteDiv">
    <a id="userGuideLink"
       href="https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
        <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>
    <table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
        <tr height="98%" valign="top">
            <td>
                <form method="POST" name="deleteForm">
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
                                Operation :
                            </td>
                            <td>
                                <select name="deleteDocAction" id="deleteDocAction">
                                    <option value="--">--Select--</option>
                                    <option value="delete">Delete</option>
                                    <option value="deleteWithLinkedDocs">DeleteWithLinkedDocs</option>
                                </select>

                            </td>
                        </tr>
                        <tr>

                        </tr>
                        <tr></tr>
                        <tr></tr>
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
                               <td></td> <td>
                                 <b>&nbsp;&nbsp;(OR) </b></td></tr>
                                 <br><br><tr><td>
                                 Request XML</td><td>

                                 <textarea rows="15" cols="40" id="deleteRequest" name="deleteRequest"></textarea>
                                 <br>
                                 <br>

                             </td>
                         </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="button" value="Submit" onclick="processDeleteRequest(IDs,deleteRequest);"/>
                            </td>
                        </tr>


                        <tr>
                            <td colspan="2" id="restDeleteResult"><br/>

                                <h2>Result</h2>
                                <textarea rows="15" cols="60" readonly="true" id="showRestDeleteResult"></textarea>
                                <br/>
                                <input type="button" value="Clear"
                                       onclick='document.getElementById("showRestDeleteResult").value = ""'/>
                            </td>
                        </tr>
                    </table>

                </form>
            </td>
        </tr>

    </table>


</div>
<div id="bagIt-Requests">
    <a id="userGuideLink"
       href="https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
        <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>

    <form name="bagItForm" method="POST">
        BagIt Requests Directory Path:
        <input type="text" name="requestFolderPath" value="/opt/docstore/bagIt" size="60">
        <br>
        <br>
        <input type="button" value="Submit" onclick="showBagIt(requestFolderPath)">
        <br><br>
    </form>
    <div id="bagItResult">
        <h2>Result</h2>
        <textarea rows="25" cols="80" id="showBagItResult" readonly="true"></textarea>
        <br/>
        <input type="button" value="Clear" onclick='document.getElementById("showBagItResult").value=""'/>
    </div>
</div>
</div>

</div>

</body>
</html>