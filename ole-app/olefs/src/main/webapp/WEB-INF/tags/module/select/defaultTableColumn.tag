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

<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>

<c:set var="documentAttributes" value="${DataDictionary.OleDefaultTableColumnValueDocument.attributes}" />

<kul:tab tabTitle="Edit Document Type" defaultOpen="true" tabErrorKey="${PurapConstants.DEFAULT_TABLE_COLUMN_TAB_ERRORS}">
   <div class="tab-container" align=center>
  
 <table cellpadding="0" cellspacing="0" class="datatable" summary="Default Table Column Section">

	<tr>
    	<td colspan="9" class="subhead">
	      <span class="subhead-left">New </span>
    	</td>
	</tr>
      <tr>
         <th align=right valign=middle class="bord-l-b" width="25%">
              <div align="right"><kul:htmlAttributeLabel forceRequired="true" attributeEntry="${documentAttributes.documentTypeId}" /></div>
         </th>
         <td align=left valign=middle class="datacell" width="25%">
               <kul:htmlControlAttribute 
                 	attributeEntry="${documentAttributes.documentTypeId}" property="document.documentTypeId"/>
               <kul:lookup boClassName="org.kuali.rice.kew.doctype.bo.DocumentType" fieldConversions="documentTypeId:document.documentTypeId"/>
               
         </td>
      </tr>
      
       <tr>
             <th align=right valign=middle class="bord-l-b" width="25%">
                  <div align="right"><kul:htmlAttributeLabel forceRequired="true" attributeEntry="${documentAttributes.documentColumn}" /></div>
              </th>
                
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.documentColumn}" property="document.documentColumn"/>
                </td>
             </tr>
             <tr>
             <th align=right valign=middle class="bord-l-b" width="25%">
                  <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.defaultValue}" /></div>
              </th>
                
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.defaultValue}" property="document.defaultValue"/>
                </td>
             </tr>
             <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"><kul:htmlAttributeLabel attributeEntry="${documentAttributes.active}" /></div>
                </th>
                
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute 
                    	attributeEntry="${documentAttributes.active}" property="document.active"/>
                </td>
             </tr>
       </table>
     </div>
 </kul:tab>
 
             
