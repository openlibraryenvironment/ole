<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PreOrder Service</title>
<script language="Javascript">
	function showDisplay(obj){
		if(obj == 'citation'){
			document.getElementById("citationDiv").style.display = "block";
			document.getElementById("formDiv").style.display = "none";
			document.getElementById("openurlDiv").style.display = "none";
			document.webserviceForm.userSelection.value = "citation";
		}else if(obj == 'form'){
			document.getElementById("formDiv").style.display = "block";
			document.getElementById("citationDiv").style.display = "none";
			document.getElementById("openurlDiv").style.display = "none";
			document.webserviceForm.userSelection.value = "webform";
		}else if(obj == 'openurl'){
			document.getElementById("openurlDiv").style.display = "block";
			document.getElementById("formDiv").style.display = "none";
			document.getElementById("citationDiv").style.display = "none";
			document.webserviceForm.userSelection.value = "openurl";
		}
		return;
	}
</script>
</head>

<body>
<form name="webserviceForm" action="wsresult.jsp" method="post">
<input type="hidden" name="userSelection" value="citation">
<input type="hidden" name="routeRequesterReceipt" value="N" />
<table width="50%">
<tr>
<td>Select an Option</td>
<td>
<input type="radio" name="option" value="citation" checked onclick="javascript:showDisplay('citation');">Citation<br>
<input type="radio" name="option" value="form" onclick="javascript:showDisplay('form');">Web Form<br>
<input type="radio" name="option" value="openurl" onclick="javascript:showDisplay('openurl');">Open URL
</td>
</tr>
<tr>
	<td colspan="2">
	<div id="citationDiv" style="display:block">
		<table width="100%">
				<tr>
					<td>Citation String</td>
					<td><textarea name="citationInput" rows="8" cols="50"></textarea></td>
				</tr>
				<tr>
					<td>Requestor Type</td>
					<td>
						<select name="requestorType">
							<option value="PATRON">PATRON</option>
							<!--<option value="CLIENT">CLIENT</option>
							<option value="RESEARCHER">RESEARCHER</option>
						--></select>
					</td>
				</tr>
				<tr>
					<td>Requestor Notes</td>
					<td><textarea name="requestorsNote" rows="5" cols="50"/></textarea></td>
				</tr>
				<tr>
					<td>Requestor Id</td>
					<td><input type="text" name="requestorsId" size="45" maxlength="45"/></td>
				</tr>
				<!-- <tr>
					<td>Requestor Last Name</td>
					<td><input type="text" name="requestorsLastName" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Address1</td>
					<td><input type="text" name="requestorsAddress1" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Address2</td>
					<td><input type="text" name="requestorsAddress2" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor City</td>
					<td><input type="text" name="requestorsCity" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor State</td>
					<td><input type="text" name="requestorsState" size="2" maxlength="2"/></td>
				</tr>
				<tr>
					<td>Requestor Zip Code</td>
					<td><input type="text" name="requestorsZipCode" size="20" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Requestor Country Code</td>
					<td><input type="text" name="requestorsCountryCode" size="20" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Requestor Phone</td>
					<td><input type="text" name="requestorsPhone" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Email</td>
					<td><input type="text" name="requestorsEmail" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor SMS</td>
					<td><input type="text" name="requestorsSMS" size="45" maxlength="45"/></td>
				</tr> -->
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Submit"/></td>
				</tr>
		</table>
	</div>
	<div id="formDiv" style="display:none">
		<table width="100%">
				<tr>
					<td>Title</td>
					<td><textarea name="title" rows="2" cols="50"></textarea></td>
				</tr>
				<tr>
					<td>Author</td>
					<td><input type="text" name="author" size="45"/></td>
				</tr>
				<tr>
					<td>Edition</td>
					<td><input type="text" name="edition" size="45"/></td>
				</tr>
				<tr>
					<td>Series</td>
					<td><input type="text" name="series" size="45"/></td>
				</tr>
				<tr>
					<td>Publisher</td>
					<td><input type="text" name="publisher" size="45"/></td>
				</tr>
				<tr>
					<td>Place of Publication</td>
					<td><input type="text" name="placeOfPublication" size="45"/></td>
				</tr>
				<tr>
					<td>Year of Publication</td>
					<td><input type="text" name="yearOfPublication" size="45"/></td>
				</tr>
				<tr>
					<td>Standard Number</td>
					<td><input type="text" name="standardNumber" size="45"/></td>
				</tr>
				<tr>
					<td>Type Of Standard Number</td>
					<td><input type="text" name="standardNumberType" size="45"/></td>
				</tr>
				<tr>
					<td>Requestor Type</td>
					<td>
						<select name="frmRequestorType">
							<option value="PATRON">PATRON</option>
							<!--<option value="CLIENT">CLIENT</option>
							<option value="RESEARCHER">RESEARCHER</option>
						--></select>
					</td>
				</tr>
				<tr>
					<td>Requestor Notes</td>
					<td><textarea name="frmRequestorsNote" rows="5" cols="50"></textarea></td>
				</tr>
				<tr>
					<td>Requestor Id</td>
					<td><input type="text" name="frmRequestorsId" size="45" maxlength="45"/></td>
				</tr>
				<!-- <tr>
					<td>Requestor Last Name</td>
					<td><input type="text" name="frmRequestorsLastName" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Address1</td>
					<td><input type="text" name="frmRequestorsAddress1" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Address2</td>
					<td><input type="text" name="frmRequestorsAddress2" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor City</td>
					<td><input type="text" name="frmRequestorsCity" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor State</td>
					<td><input type="text" name="frmRequestorsState" size="2" maxlength="2"/></td>
				</tr>
				<tr>
					<td>Requestor Zip Code</td>
					<td><input type="text" name="frmRequestorsZipCode" size="20" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Requestor Country Code</td>
					<td><input type="text" name="frmRequestorsCountryCode" size="20" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Requestor Phone</td>
					<td><input type="text" name="frmRequestorsPhone" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Email</td>
					<td><input type="text" name="frmRequestorsEmail" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor SMS</td>
					<td><input type="text" name="frmRequestorsSMS" size="45" maxlength="45"/></td>
				</tr> -->
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Submit"/></td>
				</tr>
		</table>
	</div>
	<div id="openurlDiv" style="display:none">
		<table width="100%">
				<tr>
					<td>Open URL String</td>
					<td><textarea name="openurlInput" rows="8" cols="50"></textarea></td>
				</tr>
				<tr>
					<td>Requestor Type</td>
					<td>
						<select name="opUrlRequestorType">
							<option value="PATRON">PATRON</option>
							<!--<option value="CLIENT">CLIENT</option>
							<option value="RESEARCHER">RESEARCHER</option>
						--></select>
					</td>
				</tr>
				<tr>
					<td>Requestor Notes</td>
					<td><textarea name="opUrlRequestorsNote" rows="5" cols="50"></textarea></td>
				</tr>
				<tr>
					<td>Requestor Id</td>
					<td><input type="text" name="opUrlRequestorsId" size="45" maxlength="45"/></td>
				</tr>
				<!-- <tr>
					<td>Requestor Last Name</td>
					<td><input type="text" name="opUrlRequestorsLastName" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Address1</td>
					<td><input type="text" name="opUrlRequestorsAddress1" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Address2</td>
					<td><input type="text" name="opUrlRequestorsAddress2" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor City</td>
					<td><input type="text" name="opUrlRequestorsCity" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor State</td>
					<td><input type="text" name="opUrlRequestorsState" size="2" maxlength="2"/></td>
				</tr>
				<tr>
					<td>Requestor Zip Code</td>
					<td><input type="text" name="opUrlRequestorsZipCode" size="20" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Requestor Country Code</td>
					<td><input type="text" name="opUrlRequestorsCountryCode" size="20" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Requestor Phone</td>
					<td><input type="text" name="opUrlRequestorsPhone" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor Email</td>
					<td><input type="text" name="opUrlRequestorsEmail" size="45" maxlength="45"/></td>
				</tr>
				<tr>
					<td>Requestor SMS</td>
					<td><input type="text" name="opUrlRequestorsSMS" size="45" maxlength="45"/></td>
				</tr> -->
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Submit"/></td>
				</tr>
		</table>
	</div>
	</td>
</tr>
</table>
</form>
</body>
</html>
<script>
    parent.document.title="PreOrder Service";
</script>