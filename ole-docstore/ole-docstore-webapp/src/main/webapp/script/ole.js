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
	document.forms[0].submit();
}


function initFacets(facetsChecked,facetsUnchecked){
	// alert("initFacets facetsChecked "+facetsChecked);
	// alert("initFacets facetsUnchecked "+facetsUnchecked);
	
	var facetsCheckedList="";
	var facetsUncheckedList="";
	var checkbox;
	
	
	
	
	if(facetsChecked!='null' && facetsUnchecked!='null'){
		//alert("initfacets : if");
	 facetsCheckedList=facetsChecked.split(",");
	 facetsUncheckedList=facetsUnchecked.split(",");
	
	
	
	for(i=0;i<facetsCheckedList.length;i++){
		 checkbox=document.getElementById(facetsCheckedList[i]);
		 checkbox.checked=true;
	}
	
	
	for(i=0;i<facetsUncheckedList.length;i++){
		 checkbox=document.getElementById(facetsUncheckedList[i]);
		 checkbox.checked=false;
	}

}
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
	
	oform.submit();
}




function validateAdvancedSearch()
{
	document.forms[0].searchtype.value="";
	var temp;
	 
	
	// validate the advance search to check all the  advance search  fields or empty or not
	   //alert(hasEmptyFields());
	   
		if(hasEmptyFields())
		{
			
		alert("Please enter one or more fields for Advanced Search");
		document.forms[1].modifyingAgency.focus();
		return false;
		}

	
	
	// validate wild card searches in  the advanceSearch 
	if(!validateWildcardSuffixes(1))
		{
		 alert("Advanced Serach field value cannot start with *, ? , !   ");
	       return false;
		}
	 //composite key values with spaces replaced by *
	  if( document.forms[1].mainEntryPersonalNameComposite.value!=""){
			temp=document.forms[1].mainEntryPersonalNameComposite.value;
			temp=replace(temp);
			// alert("temp value in the calling function"+temp);
			document.forms[1].mainEntryPersonalNameComposite.value=temp;
		}
	  if( document.forms[1].titleStatementComposite.value!=""){
			temp=document.forms[1].titleStatementComposite.value;
			temp=replace(temp);
			// alert("temp value in the calling function"+temp);
			document.forms[1].titleStatementComposite.value=temp;
		}
	
	var message='';
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].modifyingAgency.value))
		  {
		  //alert("Advanced serach field \"ModifyingAgency\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		    message=displayMessage('Modifying Agency');
		   alert(message);
		    document.forms[1].modifyingAgency.focus();
		    return false;
		  }
	
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].mainEntryPersonalNameComposite.value))
	  {
	  //alert("Advanced serach field \"Main Entry Personal Name\" cannot contain the following characters \n  ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Main Entry Personal Name');
		  alert(message);
	   document.forms[1].mainEntryPersonalNameComposite.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].name.value))
	  {
	  //alert("Advanced serach field \"Name\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Name');
		  alert(message);
		document.forms[1].name.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].fullerFormName.value))
	  {
	  //alert("Advanced serach field \"Fuller Form Name\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Fuller Form Name');
		  alert(message);
	   document.forms[1].fullerFormName.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].datesAssociatedWithName.value))
	  {
	  //alert("Advanced serach field \"Dates Associated\" With Name cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~ ");
		  message=displayMessage('Dates Associated With Name');
		  alert(message);
		  document.forms[1].datesAssociatedWithName.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].titleStatementComposite.value))
	  {
	  //alert("Advanced serach field \"Title Statement\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Title Statement');
		  alert(message);
		document.forms[1].titleStatementComposite.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].title.value))
	  {
	  //alert("Advanced serach field \"Title\"  cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Title');
		  alert(message);
		document.forms[1].title.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].remainderOfTitle.value))
	  {
	  //alert("Advanced serach field \"Remainder Of Title\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Remainder Of Title');
		  alert(message); 
		 document.forms[1].remainderOfTitle.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].stmtOfResponsibility.value))
	  {
	  //alert("Advanced serach field \"Statement Of Responsibility\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Statement Of Responsibility');
		  alert(message);
		document.forms[1].stmtOfResponsibility.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].placeOfPublication.value))
	  {
	  //alert("Advanced serach field \"Place Of Publication\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Place Of Publication');
		  alert(message);
		document.forms[1].placeOfPublication.focus();
	    return false;
	  }
	  
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].nameOfPublisher.value))
	  {
	  //alert("Advanced serach field \"Name Of Publisher\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Name Of Publisher');
		  alert(message);
		document.forms[1].nameOfPublisher.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].dateOfPublication.value))
	  {
	  //alert("Advanced serach field \"Date Of Publication\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Date Of Publication');
		  alert(message);
		document.forms[1].dateOfPublication.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].extent.value))
	  {
	  //alert("Advanced serach field \"Extent\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Extent');
		  alert(message);
		document.forms[1].extent.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].dimentions.value))
	  {
	  //alert("Advanced serach field \"Dimentions\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		 message=displayMessage('Dimentions');
		 alert(message);
		document.forms[1].dimentions.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].generalNote.value))
	  {
	  //alert("Advanced serach field \"General Note\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~ ");
		message=displayMessage('General Note');
	    alert(message);
		document.forms[1].generalNote.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].tropcialNameEntry.value))
	  {
	  //alert("Advanced serach field \"Tropcial Name Entry\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		    message=displayMessage('Tropcial Name Entry');
		    alert(message);
		document.forms[1].tropcialNameEntry.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].generalSubdivision.value))
	  {
	  //alert("Advanced serach field \"General Subdivision\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		   message=displayMessage('General Subdivision');
		    alert(message);
		document.forms[1].generalSubdivision.focus();
	    return false;
	  }
	 
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].personalName.value))
	  {
	  //alert("Advanced serach field \"Personal Name\"cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Personal Name');
		    alert(message);
		  document.forms[1].personalName.focus();
	    return false;
	  }
	 
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].corporatename.value))
	  {
	  //alert("Advanced serach field \"Corporate Name\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~  ");
		  message=displayMessage('Corporate Name');
		    alert(message);
		document.forms[1].corporatename.focus();
	    return false;
	  }
	  if(!validateSpecialCharsAdvancedSearch(document.forms[1].subordinateUnit.value))
	  {
	  //alert("Advanced serach field \"Subordinate Unit\" cannot contain the following characters \n ~ ^  & (  )  {  \" }   ]  [  ~   ");
		  message=displayMessage('Subordinate Unit');
		    alert(message);
		  document.forms[1].subordinateUnit.focus();
	    return false;
	  }
	 
	 //price range checking
	if((document.forms[1].minPrice.value!="")||(document.forms[1].maxPrice.value!=""))
	{
		 var MinPrice=document.forms[1].minPrice.value;
		 var MaxPrice=document.forms[1].maxPrice.value;
	 
	  if(isNaN(MinPrice) ||(parseFloat(MinPrice)<0))
		  {
		  alert(" \"Price\" should be a positive numeric value.");
		  document.forms[1].minPrice.focus();
		  return false;
		  }
	    
	  if(isNaN(MaxPrice) ||(parseFloat(MaxPrice)<0))
	  {
	  alert("\"Price\" should be a positive numeric value.");
	  document.forms[1].maxPrice.focus();
	  return false;
	  }
	   
	  if((parseFloat(MinPrice)) > (parseFloat(MaxPrice)))
	  {
	  alert("\"Max Price\" should be higher than or equal to Min Price.");
	  document.forms[1].maxPrice.focus();
	  return false;
	  }
	 
	} 

	
	
	//Date validation 
	if((document.forms[1].dateMin.value!="") ||( document.forms[1].dateMax.value!=""))
	
	{
		 var dateMax=document.forms[1].dateMax.value;
		 var dateMin=document.forms[1].dateMin.value;
		 if(dateMin!="")
			 {
			    if(!isDate(dateMin))
			    	{
			    	alert("Please enter a valid date for \"Min Date\" in the format 'mm/dd/yyyy'.");
			    	document.forms[1].dateMin.focus();
				     return false;
			    	}
			    
			 }
		 if(dateMax!="")
		 {
		    if(!isDate(dateMax))
		    	{
		    	alert("Please enter a valid date for \"Max Date\" in the format 'mm/dd/yyyy'.");
		    	document.forms[1].dateMax.focus();
			     return false;
		    	}
		    
		 }
		 if(dateMin!="" && dateMax!="")
		 {
		    if((getDateObject(dateMin, "/")) > (getDateObject(dateMax, "/")))
		    	{
		    	alert("\"Max Date\" should be later than or same as Min Date.");
		    	document.forms[1].dateMax.focus();
			     return false;
		    	}
		    
		 }
		 
		
	}
	
		
	
			
			
	
	
	document.forms[1].submit();
}

function resetForm()
{
	// document.forms[1].reset();
	
	for(i=0; i<document.forms[1].elements.length; i++)
	{
		if(document.forms[1].elements[i].type!="hidden")
		{
			document.forms[1].elements[i].value="";
		}	
	}
}

function validateNavigation(type) 
{
	if (type == "Go") 
	{		
		var item = document.forms['navigate'].pageNumber;
		if (item.options[item.selectedIndex].value=="") 
		{
			alert("Please Select Page #");
			return false;
		}
	}
	else if (type == "Max")
	{
		alert("Result display is limited to 500 documents. Please narrow your search criteria.");
		return;
	}	
	document.forms['navigate'].navType.value = type;
	document.forms['navigate'].submit();
}

function removeCollen(i)
{
	  for(j=0; j<document.forms[i].elements.length; j++)
	  {
		  document.forms[i].elements[j].value = document.forms[i].elements[j].value.replace(/:/g,""); 
	  }	
}

function validateWildcardSuffixes(i)
{
	  for(j=0; j<document.forms[i].elements.length; j++)
	  {
		  if(document.forms[i].elements[j].value.charAt(0)=="*" || document.forms[i].elements[j].value.charAt(0)=="?" || document.forms[i].elements[j].value.charAt(0)=="!")
		  {
			  document.forms[i].elements[j].focus();
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
		if(inputStr!="")
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
	 document.forms[1].mainEntryPersonalNameComposite.value==""
	&&document.forms[1].modifyingAgency.value==""
	&&document.forms[1].name.value==""
	&&document.forms[1].fullerFormName.value==""
	&&document.forms[1].datesAssociatedWithName.value==""
	&&document.forms[1].titleStatementComposite.value==""
	&&document.forms[1].dateOfPublication.value==""
	&&document.forms[1].title.value==""
	&&document.forms[1].remainderOfTitle.value==""
	&&document.forms[1].stmtOfResponsibility.value==""
    &&document.forms[1].placeOfPublication.value==""
	&&document.forms[1].nameOfPublisher.value==""
	&&document.forms[1].dimentions.value==""
	&&document.forms[1].generalNote.value==""
	&&document.forms[1].tropcialNameEntry.value==""
	&&document.forms[1].generalSubdivision.value==""
	&&document.forms[1].personalName.value==""
	&&document.forms[1].corporatename.value==""
	&&document.forms[1].subordinateUnit.value==""
	&&document.forms[1].maxPrice.value==""
	&&document.forms[1].minPrice.value==""	
	&&document.forms[1].dateMax.value==""
	&&document.forms[1].dateMin.value==""
    &&document.forms[1].ISBN.value==""
    &&document.forms[1].ISSN.value==""
    &&document.forms[1].extent.value==""
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

	
 function submitForm() {
	 document.newSearch.submit();
 }

 /*
  * This functions simulates a POST request submission.
  * Usage: postToURL("http://google.com/", { field1: "value1" } ); 
  */
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
