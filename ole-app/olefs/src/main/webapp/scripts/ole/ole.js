function validateQuickSearch()
{ 
	
	if(document.forms[0].searchterm.value==null || document.forms[0].searchterm.value=="")
	{
		alert(" Search Term should not be empty\nPlease suffix * or ? for wildcard search");
		document.forms[0].searchterm.focus();
		return false;		
	}
	if(!validateWildcardSuffixes(0))
	{
		 alert("Search term cannot start with *, ? , !   ");
		 document.forms[0].searchterm.focus();
		return false;
	}
	if(!validateQuickSearchInput())
    {
		alert (" Search Term shouldn't contain the following characters .\n ~  ^  &  (  )  {  \"  }   ]  [  ~ ");
		 document.forms[0].searchterm.focus();
		return false;
	}

	document.forms[0].searchterm.value = document.forms[0].searchterm.value.replace(/:/g,"");
	
		document.forms[0].searchtype.value="quick";
		
	document.forms[0].submit();
}


function postToURL(path, params, method) {
	
    method = method || "post";

    var form = document.createElement("form");

    //move the submit function to another variable
    //so that it doesn't get over written
    form._submit_function_ = form.submit;

    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);

        form.appendChild(hiddenField);
    }

    document.body.appendChild(form);
    form._submit_function_(); //call the renamed function
    // remove the created form element
    document.body.removeChild(form); // to be tested
}

function clearSearch(){
	document.forms[0].searchtype.value=null;
	document.forms['KualiForm'].navType.value = null;
}


function initFacets(facetsChecked,facetsUnchecked){
	// alert("initFacets facetsChecked "+facetsChecked);
	// alert("initFacets facetsUnchecked "+facetsUnchecked);
	var facetsCheckedList="";
	var facetsUncheckedList="";
	var checkbox;
	
	
	
	
	if(facetsChecked!=null && facetsChecked != '' &&facetsUnchecked != '' && facetsUnchecked!=null){
		//alert("initfacets : if");
	 facetsCheckedList=facetsChecked.split(",");
	 facetsUncheckedList=facetsUnchecked.split(",");
	
	
	
	for(i=0;i<facetsCheckedList.length;i++){
		 checkbox=document.getElementById(facetsCheckedList[i]);
		 if(checkbox != null)
		 checkbox.checked=true;
	}
	
	
	for(i=0;i<facetsUncheckedList.length;i++){
		 checkbox=document.getElementById(facetsUncheckedList[i]);
		 if(checkbox != null)
		 checkbox.checked=false;
	}

}
	document.getElementById("facetsChecked").value=facetsChecked;
document.getElementById("facetsUnchecked").value=facetsUnchecked;
	//alert("end of initfacets");
		
	}
	
	
	// alert("nlist "+nlist);
	
	
	


function submitFacetSearch(oform,originalQuery,filterQuery)
{ 
	// alert("in submitfacetsearch "+oform);
	// var str = "Form Elements of form " + oform.name + ": \n";
	var checkedValues="";
	var unCheckedValues="";

	for (i = 0; i < oform.length; i++) {
	     if(oform.elements[i].type=='checkbox'){
	    	 if(oform.elements[i].checked){
	    		 checkedValues+=oform.elements[i].name+",";
	    	 }
	    	 else
	    		 unCheckedValues+=oform.elements[i].name+",";
	     }
	    
}
	// alert("before ");
	// alert("checkedValues length "+checkedValues.length);
	// alert("unCheckedValues length "+unCheckedValues.length);
	
	checkedValues= checkedValues.substr(0,checkedValues.length-1);
    unCheckedValues= unCheckedValues.substr(0,unCheckedValues.length-1);
	 // alert("checkedValues "+checkedValues);
	 // alert("unCheckedValues "+unCheckedValues);
	 
	// if(oform.filterQuery.checked==false){
		// oform.filterQuery.value="!"+filterQuery;
	// }
	// if(oform.filterQuery.checked==true){
		// oform.filterQuery.value=filterQuery;
	// }
    
	oform.originalQuery.value=originalQuery;
	oform.facetsChecked.value=checkedValues;
	oform.facetsUnchecked.value=unCheckedValues;
	
	//oform.searchtype.value="facet";
	document.forms['KualiForm'].searchtype.value = "facet";
	oform.submit();
}



function callAdvancedSearch(){
	
	document.forms[0].searchtype.value=null;
	document.forms['KualiForm'].navType.value = null;
	document.forms[0].submit();
}

function getParamNames(){
	
	
}

function validateAdvancedSearch()
{
	var temp;
	 
	
	// validate the advance search to check all the  advance search  fields or empty or not
	   //alert(hasEmptyFields());
	   
		if(hasEmptyFields())
		{
			
		alert("Please enter one or more fields for Advanced Search");
		document.forms['KualiForm'].modifyingAgency.focus();
		return false;
		}

	
	
	// validate wild card searches in  the advanceSearch 
	if(!validateWildcardSuffixes(1))
		{
		 alert("Advanced Serach field value cannot start with *, ? , !   ");
	       return false;
		}
	 //composite key values with spaces replaced by *
	  if( document.forms['KualiForm'].mainEntryPersonalNameComposite.value!=""){
			temp=document.forms['KualiForm'].mainEntryPersonalNameComposite.value;
			temp=replace(temp);
			// alert("temp value in the calling function"+temp);
			document.forms['KualiForm'].mainEntryPersonalNameComposite.value=temp;
		}
	  if( document.forms['KualiForm'].titleStatement.value!=""){
			temp=document.forms['KualiForm'].titleStatement.value;
			temp=replace(temp);
			// alert("temp value in the calling function"+temp);
			document.forms['KualiForm'].titleStatement.value=temp;
		}
	
	var message='';
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].modifyingAgency.value))
		  {
		  //alert("Advanced serach field \"ModifyingAgency\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		    message=displayMessage('Modifying Agency');
		   alert(message);
		    document.forms['KualiForm'].modifyingAgency.focus();
		    return false;
		  }
	
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].mainEntryPersonalNameComposite.value))
	  {
	  //alert("Advanced serach field \"Main Entry Personal Name\" cannot contain the following characters \n  ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Main Entry Personal Name');
		  alert(message);
	   document.forms['KualiForm'].mainEntryPersonalNameComposite.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].name.value))
	  {
	  //alert("Advanced serach field \"Name\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Name');
		  alert(message);
		document.forms['KualiForm'].name.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].fullerFormName.value))
	  {
	  //alert("Advanced serach field \"Fuller Form Name\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Fuller Form Name');
		  alert(message);
	   document.forms['KualiForm'].fullerFormName.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].dateAssociatedWithName.value))
	  {
	  //alert("Advanced serach field \"Dates Associated\" With Name cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~ ");
		  message=displayMessage('Dates Associated With Name');
		  alert(message);
		  document.forms['KualiForm'].dateAssociatedWithName.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].titleStatement.value))
	  {
	  //alert("Advanced serach field \"Title Statement\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Title Statement');
		  alert(message);
		document.forms['KualiForm'].titleStatement.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].title.value))
	  {
	  //alert("Advanced serach field \"Title\"  cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Title');
		  alert(message);
		document.forms['KualiForm'].title.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].remainderOfTitle.value))
	  {
	  //alert("Advanced serach field \"Remainder Of Title\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Remainder Of Title');
		  alert(message); 
		 document.forms['KualiForm'].remainderOfTitle.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].stmtOfResponsibility.value))
	  {
	  //alert("Advanced serach field \"Statement Of Responsibility\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Statement Of Responsibility');
		  alert(message);
		document.forms['KualiForm'].stmtOfResponsibility.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].placeOfPublication.value))
	  {
	  //alert("Advanced serach field \"Place Of Publication\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Place Of Publication');
		  alert(message);
		document.forms['KualiForm'].placeOfPublication.focus();
	    return false;
	  }
	  
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].nameOfPublisher.value))
	  {
	  //alert("Advanced serach field \"Name Of Publisher\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Name Of Publisher');
		  alert(message);
		document.forms['KualiForm'].nameOfPublisher.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].dateOfPublication.value))
	  {
	  //alert("Advanced serach field \"Date Of Publication\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Date Of Publication');
		  alert(message);
		document.forms['KualiForm'].dateOfPublication.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].extent.value))
	  {
	  //alert("Advanced serach field \"Extent\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Extent');
		  alert(message);
		document.forms['KualiForm'].extent.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].dimentions.value))
	  {
	  //alert("Advanced serach field \"Dimentions\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		 message=displayMessage('Dimentions');
		 alert(message);
		document.forms['KualiForm'].dimentions.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].generalNote.value))
	  {
	  //alert("Advanced serach field \"General Note\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~ ");
		message=displayMessage('General Note');
	    alert(message);
		document.forms['KualiForm'].generalNote.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].tropicalNameEntry.value))
	  {
	  //alert("Advanced serach field \"Tropcial Name Entry\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		    message=displayMessage('Tropcial Name Entry');
		    alert(message);
		document.forms['KualiForm'].tropicalNameEntry.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].generalSubdivision.value))
	  {
	  //alert("Advanced serach field \"General Subdivision\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		   message=displayMessage('General Subdivision');
		    alert(message);
		document.forms['KualiForm'].generalSubdivision.focus();
	    return false;
	  }
	 
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].personalName.value))
	  {
	  //alert("Advanced serach field \"Personal Name\"cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Personal Name');
		    alert(message);
		  document.forms['KualiForm'].personalName.focus();
	    return false;
	  }
	 
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].corporatename.value))
	  {
	  //alert("Advanced serach field \"Corporate Name\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Corporate Name');
		    alert(message);
		document.forms['KualiForm'].corporatename.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms['KualiForm'].subordinateUnit.value))
	  {
	  //alert("Advanced serach field \"Subordinate Unit\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Subordinate Unit');
		    alert(message);
		  document.forms['KualiForm'].subordinateUnit.focus();
	    return false;
	  }
	 
	 //price range checking
	if((document.forms['KualiForm'].minPrice.value!="")||(document.forms['KualiForm'].maxPrice.value!=""))
	{
		 var MinPrice=document.forms['KualiForm'].minPrice.value;
		 var MaxPrice=document.forms['KualiForm'].maxPrice.value;
	 
	  if(isNaN(MinPrice) ||(parseFloat(MinPrice)<0))
		  {
		  alert(" \"Price\" should be a positive numeric value.");
		  document.forms['KualiForm'].minPrice.focus();
		  return false;
		  }
	    
	  if(isNaN(MaxPrice) ||(parseFloat(MaxPrice)<0))
	  {
	  alert("\"Price\" should be a positive numeric value.");
	  document.forms['KualiForm'].maxPrice.focus();
	  return false;
	  }
	   
	  if((parseFloat(MinPrice)) > (parseFloat(MaxPrice)))
	  {
	  alert("\"Max Price\" should be higher than or equal to Min Price.");
	  document.forms['KualiForm'].maxPrice.focus();
	  return false;
	  }
	 
	} 

	
	
	//Date validation 
	if((document.forms['KualiForm'].dateMin.value!="") ||( document.forms['KualiForm'].dateMax.value!=""))
	
	{
		 var dateMax=document.forms['KualiForm'].dateMax.value;
		 var dateMin=document.forms['KualiForm'].dateMin.value;
		 if(dateMin!="")
			 {
			    if(!isDate(dateMin))
			    	{
			    	alert("Please enter a valid date for \"Min Date\" in the format 'mm/dd/yyyy'.");
			    	document.forms['KualiForm'].dateMin.focus();
				     return false;
			    	}
			    
			 }
		 if(dateMax!="")
		 {
		    if(!isDate(dateMax))
		    	{
		    	alert("Please enter a valid date for \"Max Date\" in the format 'mm/dd/yyyy'.");
		    	document.forms['KualiForm'].dateMax.focus();
			     return false;
		    	}
		    
		 }
		 if(dateMin!="" && dateMax!="")
		 {
		    if((getDateObject(dateMin, "/")) > (getDateObject(dateMax, "/")))
		    	{
		    	alert("\"Max Date\" should be later than or same as Min Date.");
		    	document.forms['KualiForm'].dateMax.focus();
			     return false;
		    	}
		    
		 }
		 
		
	}
	
		
	
			
			
	
	
	document.forms['KualiForm'].submit();
}

function resetForm()
{
	// document.forms['KualiForm'].reset();
	
	for(i=0; i<document.forms['KualiForm'].elements.length; i++)
	{
		if(document.forms['KualiForm'].elements[i].type!="hidden")
		{
			document.forms['KualiForm'].elements[i].value="";
		}	
	}
	clearSearch();
}
function showError(message){
	document.getElementById("messageTd").innerHTML="<ul><li>"+message+"</li></ul>"
}
function validateNavigation(type) 
{
	
	//alert("forms name "+document.getElementById('facetSearchParam').name);
	
	//var elem = document.getElementById('kualiForm');
	//alert("elem "+elem);
	/*for(var i = 0; i < elem.length; i++)
    {
		if(elem[i].name=='pageNumber'){
		alert("name "+elem[i].name);
		alert("value "+elem[i].value);
		}
    }*/
	
	if (type == "Go") 
	{		
		//var item = document.forms['KualiForm'].pageNumber;
//		if (item.options[item.selectedIndex].value=="") 
//		{
//			alert("Please Select Page #");
//			return false;
//		}
	}
	else if (type == "Max")
	{
		alert("Result display is limited to 500 documents. Please narrow your search criteria.");
		return;
	}else if (type == "sort")	{
		alert("if sort "+document.getElementById("sortId").selectedIndex);
		if(document.getElementById("sortId").selectedIndex < 1)
			showError("Please select a valid sort parameter");
	}
	
	//alert("validateNavigation123 "+document.pageNumber);
	//alert("validateNavigation123 pageNumber "+document.forms['kualiForm'].pageNumber.value);
	document.forms['kualiForm'].searchtype.value = "advanced";
	
	document.forms['kualiForm'].navType.value = type;
	document.forms['kualiForm'].pageNumber1.value = document.getElementById('pageNumber').value;
	document.forms['kualiForm'].action.value="/docStoredirect.do";
	document.forms['kualiForm'].submit();
}

function removeCollen(i)
{
	  for(j=0; j<document.forms['KualiForm'].elements.length; j++)
	  {
		  document.document.forms['KualiForm'].value = document.forms['KualiForm'].elements[j].value.replace(/:/g,""); 
	  }	
}

function validateWildcardSuffixes(i)
{
	  for(j=0; j<document.forms['KualiForm'].elements.length; j++)
	  {
		  if(document.forms['KualiForm'].elements[j].value.charAt(0)=="*" || 
				  document.forms['KualiForm'].elements[j].value.charAt(0)=="?" || 
				  document.forms['KualiForm'].elements[j].value.charAt(0)=="!")
		  {
			  document.forms['KualiForm'].elements[j].focus();
			  return false;
		  }	  
	  }
	return true;
}


function KeyPress(searchType,e) 
{
	var key;

	if(window.event){
	key = window.event.keyCode;     // IE
	}
	else if(e.which){
	key = e.which;     // firefox
	}
	// if enter is pressed
	if (key == "13")
	{	
		// call appropriate function based on search type
		if(searchType=="QuickSearch"){
		return validateQuickSearch();
		}
		else if(searchType=="AdvancedSearch"){
		return validateAdvancedSearch();
		}
	}	
} 
function isDate(txtDate) {
    var objDate,  // date object initialized from the txtDate string
        mSeconds, // txtDate in milliseconds
        day,      // day
        month,    // month
        year;     // year
  
    if (txtDate.length !== 10) {
        return false;
    }
      if (txtDate.substring(2, 3) !== '/' || txtDate.substring(5, 6) !== '/') {
        return false;
    }
      month = txtDate.substring(0, 2) - 1; 
    day = txtDate.substring(3, 5) - 0;
    year = txtDate.substring(6, 10) - 0;
      if (year < 1000 || year > 3000) {
        return false;
    }
        mSeconds = (new Date(year, month, day)).getTime();
        objDate = new Date();
    objDate.setTime(mSeconds);
       if (objDate.getFullYear() !== year ||
        objDate.getMonth() !== month ||
        objDate.getDate() !== day) {
        return false;
    }
    // otherwise return true
    return true;
}
function getDateObject(dateString,dateSeperator)
{
// This function return a date object after accepting
// a date string ans dateseparator as arguments
var curValue=dateString;
var sepChar=dateSeperator;
var curPos=0;
var cDate,cMonth,cYear;
curPos=dateString.indexOf(sepChar);
cDate=dateString.substring(0,curPos);
endPos=dateString.indexOf(sepChar,curPos+1); cMonth=dateString.substring(curPos+1,endPos);
curPos=endPos;
endPos=curPos+5; 
cYear=curValue.substring(curPos+1,endPos);
dtObject=new Date(cYear,cMonth,cDate); 
return dtObject;
}
//validates the special characters for quick search
function validateQuickSearchInput(){


	var iChars = ":~^&(){\"}][~";

	for (var i = 0; i < document.forms[0].searchterm.value.length; i++) {
		if (iChars.indexOf(document.forms[0].searchterm.value.charAt(i)) != -1) {
			//alert("in the condition");
		
		return false;
		}
		
	}
	return true;
}

	//validates the special characters for advanced search
	function validateSpecialCharsAdvancedSearch(inputStr)
	{

            
          
		var iChars = ":~^&(){\"}][~";
		if(inputStr!="" && inputStr != null)
			{
			

		for (var i = 0; i < inputStr.length; i++) {
			
			if (iChars.indexOf(inputStr.charAt(i)) != -1) {
				
			
			return false;
		}
	  }
		
    }
		return true;
}


function  replace(inputString){

  // alert("in the replace method");
   // alert("In put is"+inputString);
    inputString=trim(inputString);
    // alert("after trim operation"+inputString);
    
	inputString=inputString.replace(/\s+/g,"*");
  	return inputString;
	}

function trim (str) {    
	return str.replace(/^\s\s*/, '').replace(/\s\s*$/, '');  
}  
// to check the advance search all advanced search fields are empty or not
function hasEmptyFields()
{
 if( 
	 document.forms['KualiForm'].mainEntryPersonalNameComposite.value==""
	&&document.forms['KualiForm'].modifyingAgency.value==""
	&&document.forms['KualiForm'].name.value==""
	&&document.forms['KualiForm'].fullerFormName.value==""
	&&document.forms['KualiForm'].dateAssociatedWithName.value==""
	&&document.forms['KualiForm'].titleStatement.value==""
	&&document.forms['KualiForm'].dateOfPublication.value==""
	&&document.forms['KualiForm'].title.value==""
	&&document.forms['KualiForm'].remainderOfTitle.value==""
	&&document.forms['KualiForm'].stmtOfResponsibility.value==""
    &&document.forms['KualiForm'].placeOfPublication.value==""
	&&document.forms['KualiForm'].nameOfPublisher.value==""
	&&document.forms['KualiForm'].dimentions.value==""
	&&document.forms['KualiForm'].generalNote.value==""
	&&document.forms['KualiForm'].tropicalNameEntry.value==""
	&&document.forms['KualiForm'].generalSubdivision.value==""
	&&document.forms['KualiForm'].personalName.value==""
	&&document.forms['KualiForm'].corporatename.value==""
	&&document.forms['KualiForm'].subordinateUnit.value==""
	&&document.forms['KualiForm'].maxPrice.value==""
	&&document.forms['KualiForm'].minPrice.value==""	
	&&document.forms['KualiForm'].dateMax.value==""
	&&document.forms['KualiForm'].dateMin.value==""
    &&document.forms['KualiForm'].ISBN.value==""
    &&document.forms['KualiForm'].ISSN.value==""
    &&document.forms['KualiForm'].extent.value==""
		)
 {
	
	return true;
}
return false;	
}
 function displayMessage(k)
 {
	 
	 var temp='';
	temp+='Advanced Search field "';
	 temp+=k;
	 temp+='" cannot contain the following characters \n : ~ ^  & (  )  {  \" }   ]  [  ~ ';
	 return temp;
 }

 function showHideSearchResultDetails(buttonObj) {
		// Get the div element (parent) of the buttonObj.
		var parent = buttonObj.parentNode;
		// Get the table child of the div element
		var detailsNode = parent.childNodes[5];
		// Toggle the visibility of detailsNode
		if (detailsNode.style.display == "none") {
			detailsNode.style.display = "block";
			buttonObj.value = "Hide details";
		}
		else {
			detailsNode.style.display = "none";
			buttonObj.value = "Show details";
		}
	}	





