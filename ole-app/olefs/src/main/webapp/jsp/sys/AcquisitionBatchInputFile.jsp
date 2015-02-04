<%--
 Copyright 2007 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/jsp/sys/kfsTldHeader.jsp"%>

<kul:documentPage showDocumentInfo="false"
	htmlFormAction="acqBatchUpload"
	documentTypeName="OLE_ACQBTHUPLOAD" 
	renderMultipart="true"
	showTabButtons="true" >

	<html:hidden property="acquisitionBatchUpload.batchInputTypeName" />
	<html:hidden property="backLocation"/>
    <c:set var="batchUploadAttributes" value="${DataDictionary.BatchUpload.attributes}" />
    <c:set var="acquisitionBatchUploadAttributes" value="${DataDictionary.AcquisitionBatchUpload.attributes}" />
	<strong><h2>	
	  <bean:message key="${KualiForm.titleKey}"/> 
	  </h2></strong>
	</br>
	
	
	</br>
		
	<kul:tabTop tabTitle="Manage Batch Files" defaultOpen="true" tabErrorKey="">
      <div class="tab-container" align="center">
          <h3>Upload Your File</h3>
                  
            
            
            
      <table cellpadding="0" cellspacing="0" class="datatable" summary="Default Table Column Section">
        <tr>
         <th align=right valign=middle class="bord-l-b" width="25%">
              <div align="right"> <kul:htmlAttributeLabel attributeEntry="${acquisitionBatchUploadAttributes.batchLoadProfile}" /></div>
         </th>
         <td align=left valign=middle class="datacell" width="25%">
               <kul:htmlControlAttribute 
                 	attributeEntry="${acquisitionBatchUploadAttributes.batchLoadProfile}" property="acquisitionBatchUpload.batchLoadProfile"/>
         </td>
      </tr>
      
       <tr>
             <th align=right valign=middle class="bord-l-b" width="25%">
                  <div align="right"><kul:htmlAttributeLabel attributeEntry="${acquisitionBatchUploadAttributes.batchFilePath}" /></div>
              </th>
                
                <td align=left valign=middle class="datacell" width="25%">
                    <html:file styleId="uploadFile" property="uploadFile"/>
                </td>
             </tr>
             <tr>
             <th align=right valign=middle class="bord-l-b" width="25%">
                  <div align="right"><kul:htmlAttributeLabel attributeEntry="${acquisitionBatchUploadAttributes.batchDescription}" /></div>
              </th>
                
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${acquisitionBatchUploadAttributes.batchDescription}" property="acquisitionBatchUpload.batchDescription"/>
                </td>
             </tr>
           
       </table>
       
       <div id="globalbuttons" class="globalbuttons" align="center">
		&nbsp;<html:image src="${ConfigProperties.externalizable.images.url}buttonsmall_add.gif" styleClass="globalbuttons" property="methodToCall.upload" title="Upload Now" alt="Upload Now"/>
		&nbsp;<html:image property="methodToCall.clearValues" value="clearValues" src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_clear.gif" styleClass="tinybutton" alt="clear" title="clear" border="0" /> 
	    &nbsp;<c:if test="${!empty KualiForm.backLocation}"><a
							href='<c:out value="${KualiForm.backLocation}?methodToCall=refresh"/>'  title="cancel"><img
							src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_close.gif" class="tinybutton" alt="cancel" title="cancel"
							border="0" /></a></c:if>
	   </div>
    
            
            
      </div>
	</kul:tabTop>
	
	<kul:panelFooter />
	
</kul:documentPage>
