<%@page import="org.kuali.ole.select.testing.WebserviceClient"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Webservice Client</title>
</head>
<body>
<table width="50%">
<tr><td width="100%">
<%
WebserviceClient client = new WebserviceClient();
String userSelect = request.getParameter("userSelection");
String responseString = null;
if(userSelect!=null){ 
	if("citation".equalsIgnoreCase(userSelect)){
		//String []inputArr = new String[15];
		String citationInput = request.getParameter("citationInput");
		String routeRequesterReceipt = request.getParameter("routeRequesterReceipt");
		String requestorsNote = request.getParameter("requestorsNote");
		/* String requestorsFirstName = request.getParameter("requestorsFirstName");
		String requestorsLastName = request.getParameter("requestorsLastName");
		String requestorsAddress1 = request.getParameter("requestorsAddress1");
		String requestorsAddress2 = request.getParameter("requestorsAddress2");
		String requestorsCity = request.getParameter("requestorsCity");
		String requestorsState = request.getParameter("requestorsState");
		String requestorsZipCode = request.getParameter("requestorsZipCode");
		String requestorsCountryCode = request.getParameter("requestorsCountryCode");
		String requestorsPhone = request.getParameter("requestorsPhone");
		String requestorsEmail = request.getParameter("requestorsEmail");
		String requestorsSMS = request.getParameter("requestorsSMS"); */
		String requestorType = request.getParameter("requestorType");
		String requestorId = request.getParameter("requestorsId");
		/* responseString = client.processCitationInput(citationInput, routeRequesterReceipt,  requestorsNote, requestorsFirstName, 
	             requestorsLastName,  requestorsAddress1,  requestorsAddress2,  requestorsCity,  requestorsState, 
	             requestorsZipCode,  requestorsCountryCode,  requestorsPhone,  requestorsEmail,
	             requestorsSMS,  requestorType); */
		responseString = client.processCitationInput(citationInput, routeRequesterReceipt,  requestorsNote, requestorType,requestorId); 
	}else if("webform".equalsIgnoreCase(userSelect)){
		//String []inputArr = new String[23];
				String title = request.getParameter("title");
				String author = request.getParameter("author");
				String  edition = request.getParameter("edition");
				String series = request.getParameter("series");
				String publisher = request.getParameter("publisher");
				String placeOfPublication = request.getParameter("placeOfPublication");
				String yearOfPublication = request.getParameter("yearOfPublication");
				String standardNumber = request.getParameter("standardNumber");
				String typeOfStandardNumber = request.getParameter("standardNumberType");
				/* String routeRequesterReceipt = request.getParameter("routeRequesterReceipt"); */
				String requestorsNote = request.getParameter("frmRequestorsNote");
				/* String requestorsFirstName = request.getParameter("frmRequestorsFirstName");
				String requestorsLastName = request.getParameter("frmRequestorsLastName");
				String requestorsAddress1 = request.getParameter("frmRequestorsAddress1");
				String requestorsAddress2 = request.getParameter("frmRequestorsAddress2");
				String requestorsCity = request.getParameter("frmRequestorsCity");
				String requestorsState = request.getParameter("frmRequestorsState");
				String requestorsZipCode = request.getParameter("frmRequestorsZipCode");
				String requestorsCountryCode = request.getParameter("frmRequestorsCountryCode");
				String requestorsPhone = request.getParameter("frmRequestorsPhone");
				String requestorsEmail = request.getParameter("frmRequestorsEmail");
				String requestorsSMS = request.getParameter("frmRequestorsSMS"); */
				String requestorType = request.getParameter("frmRequestorType");
				String requestorId = request.getParameter("frmRequestorsId");
				/* responseString = client.processWebformInput(title, author, edition, series, 
			            publisher, placeOfPublication, yearOfPublication, standardNumber, 
			            typeOfStandardNumber, routeRequesterReceipt, requestorsNote,requestorsFirstName, 
			            requestorsLastName, requestorsAddress1, requestorsAddress2, requestorsCity, requestorsState, 
			            requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail,
			             requestorsSMS, requestorType); */
				responseString = client.processWebformInput(title, author, edition, series, 
			            publisher, placeOfPublication, yearOfPublication, standardNumber, 
			            typeOfStandardNumber,requestorsNote,requestorType,requestorId);
	}else if("openurl".equalsIgnoreCase(userSelect)){
		//String []inputArr = new String[15];
		String openUrlString = request.getParameter("openurlInput");
		String routeRequesterReceipt = request.getParameter("routeRequesterReceipt");
		String requestorsNote = request.getParameter("opUrlRequestorsNote");
		/* String requestorsFirstName = request.getParameter("opUrlRequestorsFirstName");
		String requestorsLastName = request.getParameter("opUrlRequestorsLastName");
		String requestorsAddress1 = request.getParameter("opUrlRequestorsAddress1");
		String requestorsAddress2 = request.getParameter("opUrlRequestorsAddress2");
		String requestorsCity = request.getParameter("opUrlRequestorsCity");
		String requestorsState = request.getParameter("opUrlRequestorsState");
		String requestorsZipCode = request.getParameter("opUrlRequestorsZipCode");
		String requestorsCountryCode = request.getParameter("opUrlRequestorsCountryCode");
		String requestorsPhone = request.getParameter("opUrlRequestorsPhone");
		String requestorsEmail = request.getParameter("opUrlRequestorsEmail");
		String requestorsSMS = request.getParameter("opUrlRequestorsSMS"); */
		String requestorType = request.getParameter("opUrlRequestorType");
		String requestorId = request.getParameter("opUrlRequestorId");
		/* responseString = client.processOpenUrlInput(openUrlString,routeRequesterReceipt, requestorsNote,requestorsFirstName, 
	            requestorsLastName, requestorsAddress1, requestorsAddress2, requestorsCity, requestorsState, 
	            requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail,
	            requestorsSMS, requestorType); */
		responseString = client.processOpenUrlInput(openUrlString,routeRequesterReceipt, requestorsNote,requestorType,requestorId);
	}
	
	if(responseString == null)
		responseString = "";
	
	out.println(responseString);
}%>
</td></tr>
<tr><td>
&nbsp;
</td></tr>
<tr><td>
&nbsp;
</td></tr>
</table>
</body>
</html>