<html>
<head>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="script/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
    <script type="text/javascript"></script>
    <script>
        // var rootURL = "http://localhost:9080/oledocstore/rest/documents";
        var requestUri = "<%=request.getRequestURI()%>";
        var reqUrl = "<%=request.getRequestURL()%>";
        var rootURL = reqUrl.replace(requestUri, "") + "/oledocstore/rest/documents";
        var bagItUrl = reqUrl.replace(requestUri, "") + "/oledocstore/multiPartBagRequestClientServlet";


        function checkoutData() {
            if (document.checkOutForm.uuid.value.length > 0) {
                var uuidValue = document.checkOutForm.uuid.value;
                document.checkOutForm.action = rootURL + '/' + uuidValue;
                document.checkOutForm.submit();
            }
            else {
                var requestXML = document.checkOutForm.checkOutRequest.value;
                document.checkOutForm.action = rootURL + '?' + encodeURI(requestXML);
                document.checkOutForm.submit();
            }

            return false;
        }


    </script>

    <a id="userGuideLink"
       href="https://wiki.kuali.org/display/OLE/DocStore+and+Discovery+Service+Contracts#DocStoreandDiscoveryServiceContracts-6.%26nbsp%3B%26nbsp%3B%26nbsp%3BRESTAPI|toolbar=0,menubar=0,width=1000,height=1000,scrollbars=1">
        <img align="right" src="images/icon_guide.gif" title="User Guide"/> </a>
<table>
    <tr>
        <td></td>
        <td></td>
        <td>

            <h2>Get Document</h2>
            <br>
            <br>

            <form name="checkOutForm" method="GET" onsubmit="checkoutData();">
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

                <input type="submit" value="Submit" <%--onclick="buildCheckOutRequest(uuid,checkOutRequest);"--%>>
                <br><br>
            </form>
            <%--    <div id="restCheckOutResult">
                <h2>Result</h2>
                <textarea rows="25" cols="80" id="showCheckOutResult" readonly="true"></textarea>
                <br/>
                <input type="button" value="Clear" onclick='document.getElementById("showCheckOutResult").value = ""'/>
            </div>--%>
        </td>
    </tr>
</table>
</body>
</html>