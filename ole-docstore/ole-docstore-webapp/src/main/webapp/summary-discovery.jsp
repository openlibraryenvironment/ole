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
	<script type="text/javascript" src="./script/jquery/jquery-1.4.2.min.js"></script>
    <style type="text/css">
        table,th,td {
            font-size:14px;
            text-align: left;
        }
    </style>
</head>
<body>
<script>
$.ajaxSetup({async:false});
var configInfoXml = "";
var count = 0;

function getDocumentCount(category, type, format){
    $.ajax({
	    url: "bib/select?q=DocType:" + type + " AND DocFormat:" + format + "&rows=0",
	    cache: false,
	    success: function(resultXml){

	    	configInfoXml = resultXml;
	    	$(configInfoXml).find("result").each(function(){
	    		count = $(this).attr("numFound");	    			    			    	
	    	});	    		 
	    } 
	});
    return count;
}

var categoryId = "";
var typeId = "";
var formatId = "";
var configHtmlText = [];
var i = 0;
var flag = false;
function displayTable(){
    $.ajax({
	    url: "getDocumentConfigInfo",
	    cache: false,
	    success: function(configInfo){
	    	configInfoXml = configInfo;
	    }
	});
	configHtmlText[i++] = "<tr valign='top'>";
	configHtmlText[i++] = "<th>Category</th>";
	configHtmlText[i++] = "<th>Type</th>";
	configHtmlText[i++] = "<th>Format</th>";
	configHtmlText[i++] = "<th>Record Count</th></tr>";
	$(configInfoXml).find("documentCategory").each(function(){
		var category = $(this).attr("name");
		categoryId = $(this).attr("id");
		configHtmlText[i++] = "<tr valign='top'>";
		configHtmlText[i++] = "<td valign='top'>" + category + "</td>";
		configHtmlText[i++] = "<td valign='top'></td>";
		configHtmlText[i++] = "<td valign='top'></td></tr>";
		$(this).find("documentType").each(function(){
			var typeName = $(this).attr("name");
			typeId = $(this).attr("id");
			$(this).find("documentFormat").each(function(){
				var formatName = $(this).attr("name");
                var allFormat="ALL";
                if(formatName!=allFormat)
                {
                  formatId = $(this).attr("id");
				  configHtmlText[i++] = "<tr valign='top'>";
				  configHtmlText[i++] = "<td valign='top'></td>";
				  configHtmlText[i++] = "<td valign='top'>" + typeName + "</td>";
				  configHtmlText[i++] = "<td valign='top'>" + formatName + "</td>";
				  var countFieldId = getCountFieldId(categoryId, typeId, formatId);
				  configHtmlText[i++] = "<td valign='top'><span id='" + countFieldId + "'></span></td></tr>";

                }
			});
		});	
	});
	$("#results").html(configHtmlText.join(''));
}

function getCountFieldId(category, typeName, formatName){
 var countField = "CountField-" + category + "-" + typeName + "-" + formatName;
 return countField;
}

function displaySummaryData(){
    if(!flag){
        displayTable();
        flag=true;
    }
    $.ajax({
	    url: "getDocumentConfigInfo",
	    cache: false,
	    success: function(configInfo){
	    	
            configInfoXml = configInfo;
	    }
	});
	$(configInfoXml).find("documentCategory").each(function(){
		categoryId = $(this).attr("id");	
		$(this).find("documentType").each(function(){
			typeId = $(this).attr("id");
			$(this).find("documentFormat").each(function(){
				formatId = $(this).attr("id");			
				var numFound = getDocumentCount(categoryId, typeId, formatId);				
				//alert("num Found="+numFound);
				var countFieldId = getCountFieldId(categoryId, typeId, formatId);
				//alert("CountFieldId ="+countFieldId);			
				$('#'+countFieldId).html(numFound);														
			});			
		});			
	});
}	

/*$(document).ready(function(){
	displayTable();
});*/

</script>
	<h3>Summary of Record Count </h3>
	<table width="100%" id="results">
    </table>
<br/>
<input type="button" value="Refresh" onclick="displaySummaryData();">
</body>
</html>