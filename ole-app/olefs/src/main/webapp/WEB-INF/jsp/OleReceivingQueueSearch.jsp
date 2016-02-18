<%@ page import="org.kuali.ole.sys.OLEConstants" %>
<%@ page import="org.kuali.ole.select.businessobject.OlePurchaseOrderItem" %>
<%@ page import="java.util.List" %>
<%@ page import="org.kuali.ole.select.document.OlePurchaseOrderDocument" %>
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
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>
<style>
    #receiveandpaylink,#paylink{ cursor: pointer; cursor: hand; }
</style>
<script src="http://code.jquery.com/jquery-latest.min.js"type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function() {
        var poList="";
        var poListSelection="";
        var invoicePriceList="";
        var foreignInvoicePriceList;
        var titleListSelection="";
        var vendorListSelection="";
        var receiveLinkURL="<c:url value="${ConfigProperties.application.url}"/>/ole-kr-krad/oleTitlesToInvoiceController?viewId=OLEAddTitlesToInvoiceView&amp;methodToCall=pay";
        var createReceiveLinkURL="<c:url value="${ConfigProperties.application.url}"/>/ole-kr-krad/oleTitlesToInvoiceController?viewId=OLEAddTitlesToInvoiceView&amp;methodToCall=receiveAndPay";
        var claimingLink="<c:url value="${ConfigProperties.application.url}"/>/ole-kr-krad/oleClaimingController?viewId=OLEClaimingView";
        var isSelectedAtleatOne=false;
        $("#paylink").click(function () {

            $('#result tr').each(function () {
                $(this).find(':checkbox').each(function () {
                    if($(this).attr('name').indexOf('poAdded')!=-1){
                        poList = poList + ":" + $(this).is(":checked");
                        if($(this).is(":checked")==true){
                            isSelectedAtleatOne=true;
                        }
                    }
                });
                $(this).find('.poclass').each(function () {
                    poListSelection = poListSelection + ":" + $(this).val();
                });
                $(this).find('input#invoicePrice').each(function () {
                    if($(this).val().trim()=="") {
                        invoicePriceList = invoicePriceList + ":" +null;
                    }else {
                        invoicePriceList = invoicePriceList + ":" + $(this).val();
                    }
                });
                $(this).find('input#foreignInvoicePrice').each(function () {
                    if($(this).val().trim()=="") {
                        foreignInvoicePriceList = foreignInvoicePriceList + ":" +null;
                    }else {
                        foreignInvoicePriceList = foreignInvoicePriceList + ":" + $(this).val();
                    }
                });

            });
            if(isSelectedAtleatOne){
            var form = $('<form action=" ' + receiveLinkURL + ' " method="post">' + '<input type="hidden" name="poItemList" value=" ' + poList + '"/>' + '<input type="hidden" name="poItemListSelection" value=" ' + poListSelection + '"/>' + '<input type="hidden" name="invoicePriceList" value=" ' + invoicePriceList + '"/>' + '<input type="hidden" name="foreignInvoicePriceList" value=" ' + foreignInvoicePriceList + '"/>' +' "</form>');
            $('body').append(form);
            $(form).submit();
            } else{
                alert("select at least one purchase order");
            }

        });
        $("#receiveandpaylink").click(function () {

            $('#result tr').each(function () {
                $(this).find(':checkbox').each(function () {
                    if($(this).attr('name').indexOf('poAdded')!=-1){
                        poList = poList + ":" + $(this).is(":checked");
                        if($(this).is(":checked")==true){
                            isSelectedAtleatOne=true;
                        }
                    }
                });
                $(this).find('.poclass').each(function () {
                    poListSelection = poListSelection + ":" + $(this).val();
                });
                $(this).find('input#invoicePrice').each(function () {
                    if($(this).val().trim()=="") {
                        invoicePriceList = invoicePriceList + ":" +null;
                    }else {
                        invoicePriceList = invoicePriceList + ":" + $(this).val();
                    }
                });
                $(this).find('input#foreignInvoicePrice').each(function () {
                    if($(this).val().trim()=="") {
                        foreignInvoicePriceList = foreignInvoicePriceList + ":" +null;
                    }else {
                        foreignInvoicePriceList = foreignInvoicePriceList + ":" + $(this).val();
                    }
                });

            });
            if(isSelectedAtleatOne){
            var form = $('<form action=" ' + createReceiveLinkURL + ' " method="post">' + '<input type="hidden" name="poItemList" value=" ' + poList + '"/>' + '<input type="hidden" name="poItemListSelection" value=" ' + poListSelection + '"/>' + '<input type="hidden" name="invoicePriceList" value=" ' + invoicePriceList + '"/>' + '<input type="hidden" name="foreignInvoicePriceList" value=" ' + foreignInvoicePriceList + '"/>' + '"</form>');
            $('body').append(form);
            $(form).submit();
            } else{
                alert("select at least one purchase order");
            }

        });
        $("#claimingLink").click(function(){
            $('#result tr').each(function () {
                $(this).find(':checkbox').each(function () {
                    if($(this).attr('name').indexOf('claimPoAdded')!=-1){
                        poList = poList + "~" + $(this).is(":checked");
                        if($(this).is(":checked")==true){
                            isSelectedAtleatOne=true;
                        }
                    }
                });
                $(this).find('.poclass').each(function () {
                    poListSelection = poListSelection + "~" + $(this).val();
                });
                $(this).find('.titleClass').each(function () {
                    titleListSelection = titleListSelection + "~" + $(this).val();
                });
                $(this).find('.vendorClass').each(function () {
                    vendorListSelection = vendorListSelection + "~" + $(this).val();
                });
            });

            if(isSelectedAtleatOne){
                var form = $('<form action=" ' + claimingLink + ' " method="post">' + '<input type="hidden" name="poItemList" value=" ' + poList + '"/>' + '<input type="hidden" name="poItemListSelection" value=" ' + poListSelection + '"/>' +'<input type="hidden" name="titleListSelection" value=" ' + titleListSelection + '"/>' + '<input type="hidden" name="vendorListSelection" value=" ' + vendorListSelection + '"/>' + '"</form>');
                $('body').append(form);
                $(form).submit();
            } else{
                alert("select at least one purchase order");
            }
        });
        $("input#invoicePrice").keydown(function(event) {
            if(event.keyCode == 173 || event.keyCode == 190){
                return;
            } else {
                if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 || event.keyCode == 110 ||
                        (event.keyCode == 65 && event.ctrlKey === true) ||
                        (event.keyCode >= 35 && event.keyCode <= 39)) {
                    return;
                }
                else {
                    if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                        event.preventDefault();
                    }
                }

            }
        });
        $("input#foreignInvoicePrice").keydown(function(event) {
            if(event.keyCode == 173 || event.keyCode == 190){
                return;
            } else {
                if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 || event.keyCode == 110 ||
                        (event.keyCode == 65 && event.ctrlKey === true) ||
                        (event.keyCode >= 35 && event.keyCode <= 39)) {
                    return;
                }
                else {
                    if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                        event.preventDefault();
                    }
                }

            }
        });
 /*       $("div#globalbuttons input").on('click',function(e){
            if($(this).prop('name')=="methodToCall.claiming"){
                var claimNotes = prompt("Enter note for claiming") ;
                if(claimNotes){
                   // document.getElementById("document.standardNumber").value=claimNotes;
                } else {
                    return false;
                }
            }
        });*/

    });
function setAllReceivingQueueResults(checked) {
	for (i = 0; i < KualiForm.elements.length; i++) {
		if (KualiForm.elements[i].type == 'checkbox' && KualiForm.elements[i].id == '' && KualiForm.elements[i].style.display!='none') {
			KualiForm.elements[i].checked = checked;
		}
	}
}
</script>

<kul:documentPage showDocumentInfo="true"
     documentTypeName="OLE_QUEUESEARCH"
     htmlFormAction="oleReceivingQueueSearch"
     renderMultipart="true"
     showTabButtons="false">
      
     
     <select:oleReceivingQueueSearch />
    <c:set var="counter" value="0" />
	 <logic:notEmpty name="KualiForm" property="document.purchaseOrderItems">
	 <c:if test="${KualiForm.document.purchaseOrderDocumentAdded}">
	 <kul:tab tabTitle="Search Results" defaultOpen="true">

	 <div class="tab-container" align=center>
		 <display:table class="datatable-100" cellpadding="2" cellspacing="0"
			 name="${KualiForm.document.purchaseOrderItems}" pagesize="100"
			 id="result" excludedParams="*" requestURI="oleReceivingQueueSearch.do" sort="list" defaultsort="2">
			 <c:set var="itemPrice" value=""/>
			 <display:column title="Receive">
				<html:checkbox property="document.purchaseOrderItems[${result_rowNum-1}].poAdded" />
			 </display:column>
             <display:column sortable="true" title="PO#">
<%--                 <a href="<c:url value="${ConfigProperties.application.url}${KualiForm.url}=${result.purchaseOrder.documentNumber}" ></c:url>"
                    target="_blank"
                    class="showvisit"> <c:out value="${result.purchaseOrder.purapDocumentIdentifier}" />
                 </a>--%>
                                                              <a href="<c:url value="${ConfigProperties.application.url}${KualiForm.url}=${result.olePurchaseOrderDocument.documentNumber}" ></c:url>"
                                                                 target="_blank"
                                                                 class="showvisit"> <c:out value="${result.olePurchaseOrderDocument.purapDocumentIdentifier}" />
                                                              </a>
                 <input type="hidden" class="poclass" value="${result.itemIdentifier}" style="display:none">
                 <input type="hidden" class="titleClass" value="${result.docData.itemTitle}" style="display:none">
                 <input type="hidden" class="vendorClass" value="${result.olePurchaseOrderDocument.vendorName}" style="display:none">
             </display:column>
<%--             <display:column property="purchaseOrder.vendorName" sortable="true"
                             title="Vendor Name" >--%>
                                                          <display:column property="olePurchaseOrderDocument.vendorName" sortable="true"
                                                                          title="Vendor Name" >
                 <c:set var="bibEditorUrl" value="${result.olePurchaseOrderDocument.bibeditorViewURL}?docAction=checkOut" />
                 <c:set var="bibUUID" value="${result.olePurchaseOrderDocument.items[0].bibUUID}" />
                 <c:set var="title" value="${result.olePurchaseOrderDocument.items[0].itemDescription}" />
                 <c:set var="bibDetails" value="${fn:split(title,',')}"/>
                 <c:set var="docId" value="${fn:split(bibUUID,'-')}"/>
             </display:column>
             <%--<display:column property="docData.itemTitle" sortable="true"
				 title="Title" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />--%>
             <display:column  comparator="org.kuali.ole.select.businessobject.options.OLEOHQComparator" sortable="true"
                              title="Title" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" >
                 <a href="<c:url value="${bibEditorUrl}&Title_display=${bibDetails[0]}&docId=${docId[1]}&uuid=${bibUUID} "></c:url>" target="_blank" class="showvisit">
                     <c:out value="${KualiForm.document.purchaseOrderItems[counter].docData.title}"/>
                     <c:set var="counter" value="${counter+1 }"/>
                 </a>
             </display:column>
			 <display:column property="docData.author" sortable="true"
				 title="Author" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
		     <display:column property="docData.publisher" sortable="true"
				 title="Publisher" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
		     <display:column sortable="true"
				 title="Edition" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
             <display:column title="Claim">
                 <c:if test="${result.claimFilter}">
                     <html:checkbox property="document.purchaseOrderItems[${result_rowNum-1}].claimPoAdded" style="display:block" />
                </c:if>
                 <c:if test="${!result.claimFilter}">
                     <html:checkbox property="document.purchaseOrderItems[${result_rowNum-1}].claimPoAdded" style="display:none" />
                 </c:if>
             </display:column>
		 	 <display:column property="itemPoQty" sortable="true"
				 title="QTY Ordered" />
			 <display:column property="itemNoOfParts" sortable="true"
				 title="# Pts" />
			<%-- <display-el:column property="purchaseOrder.purapDocumentIdentifier" sortable="true"
				 title="PO#" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />--%>

			 <display:column property="noOfCopiesReceived" sortable="true"
				 title="No of Copies Rcvd" />
			 <display:column property="noOfPartsReceived" sortable="true"
				 title="No of Parts Rcvd" /> 
			 <display:column property="docData.isbn" sortable="true"
				 title="ISxN" decorator="org.kuali.rice.kns.web.ui.FormatAwareDecorator" />
			 <display:column property="itemUnitPrice" sortable="true"
				 title="Price" />
			 <display:column title="Invoice Price" sortable="true">
				<input type="text" name="Invoice Price" id="invoicePrice" value="${result.itemUnitPrice}" size="20px">
			</display:column>
             <display:column title="Foreign Invoice Price" sortable="true">
                 <input type="text" name="Foreign Invoice Price" id="foreignInvoicePrice" value="${result.itemForeignUnitCost}" size="20px">
             </display:column>

         </display:table>


		 <p>
					<html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_selectallfromallpages.png" alt="Select all rows from all pages" title="Select all rows from all pages" styleClass="tinybutton" property="methodToCall.selectAll" value="Select All Rows"/>
					<html:image src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_deselectallfromallpages.png" alt="Deselect all rows from all pages" title="Unselect all rows from all pages" styleClass="tinybutton" property="methodToCall.unselectAll" value="Unselect All Rows"/>					
					<script>
						document.write('\n');
						document.write('<a href="javascript:void(0)" onclick="setAllReceivingQueueResults(true);"><img src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_selectallfromthispage.png" alt="Select all rows from this page" title="Select all rows from this page" class="tinybutton"/></a>');
						document.write('\n');
						document.write('<a href="javascript:void(0)" onclick="setAllReceivingQueueResults(false);"><img src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_deselectallfromthispage.png" alt="Deselect all rows from this page" title="Deselect all rows from this page" class="tinybutton" onclick="setAllOrderHoldQueueResults(false)"/></a>');
					</script>						
		 </p>

	</div>

	</kul:tab>
	</c:if>

	<c:if test="${KualiForm.document.purchaseOrderDocumentAdded}">
		<kul:tab tabTitle="Actions" defaultOpen="true">
			<div class="tab-container" align=center>
				<div id="globalbuttons" >
		     		<center>
                		<html:image src="${ConfigProperties.externalizable.images.url}receive_btn.gif" styleClass="globalbuttons" property="methodToCall.completeReceiving" title="Receive" alt="Receive" value="search"/>
                 		<html:image src="${ConfigProperties.externalizable.images.url}create receiving.gif" styleClass="globalbuttons" property="methodToCall.createReceiving" title="Create Receive" alt="Create Receive" value="search"/>
                		<%-- <a href="selectOlePaymentRequest.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_PREQ" ><img src="${ConfigProperties.externalizable.images.url}buttonsmall_payment request.gif" styleClass="globalbuttons" title="Paymant Request" alt="Paymant Request"/></a>--%>
                		<%--<html:image src="${ConfigProperties.externalizable.images.url}buttonsmall_payment request.gif" styleClass="globalbuttons" property="methodToCall.createPaymentRequestDocument" title="Payment Request" alt="Payment Request" value="search"/>--%>
                		<%-- <html:image src="${ConfigProperties.externalizable.images.url}buttonsmall_recei.gif" styleClass="globalbuttons" property="methodToCall.receiveAndPay" title="Receive & Pay" alt="Receive & Pay" value="search"/>--%>
                         <a id="paylink" ><img src="${ConfigProperties.externalizable.images.url}buttonsmall_pay.gif" styleClass="globalbuttons" title="Paymant Request" alt="Paymant Request"/></a>
                         <a id="receiveandpaylink"><img src="${ConfigProperties.externalizable.images.url}buttonsmall_receive and pay.gif" styleClass="globalbuttons" title="Paymant Request" alt="Paymant Request"/></a>
                         <a id="claimingLink"><img src="${ConfigProperties.externalizable.images.url}buttonsmall_claim.gif" styleClass="globalbuttons" title="Claim" alt="Claim"/></a>
                         <%--<html:image src="${ConfigProperties.externalizable.images.url}buttonsmall_claim.gif" styleClass="globalbuttons" property="methodToCall.claiming" title="Claim" alt="Claim" value="search"/>--%>
                     </center>
	    		</div>
	    		
	    	</div>
	    </kul:tab>
	 </c:if>
	 </logic:notEmpty>
	 <c:if test="${fn:length(KualiForm.document.receivingDocumentsList) > 0}">
	 <div class="tab-container" align=left>
	 	Receiving Complete : Document Ids :
         <logic:iterate id="receivingDoc" name="KualiForm" property="document.receivingDocumentsList" indexId="i" >
         	
        		<a  href="<c:url value="${ConfigProperties.application.url}${KualiForm.url}=${receivingDoc}"></c:url>"
								target="_blank" class="showvisit"> 
								<c:out value="${receivingDoc}"/> </a>
         
         </logic:iterate> 
         </div>
     
	 </c:if>

	 <kul:panelFooter />
	  
</kul:documentPage>
