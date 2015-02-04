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
<kul:documentPage showDocumentInfo="true"
                  htmlFormAction="acqBatchUpload"
                  documentTypeName="OLE_ACQBTHUPLOAD" renderMultipart="true"
                  showTabButtons="true" >

    <html:hidden property="acquisitionBatchUpload.batchInputTypeName" />
    <html:hidden property="oleLoadSumRecords.acqLoadSumId" />
    <html:hidden property="fileName" />
    <html:hidden property="backLocation"/>

    <c:set var="oleLoadSumRecords" value="${DataDictionary.OleLoadSumRecords.attributes}" />
    <c:set var="oleLoadFailureRecords" value="${DataDictionary.OleLoadFailureRecords.attributes}" />

    <sys:documentOverview editingMode="${KualiForm.editingMode}"/>

    <kul:tab tabTitle="Load Summary" defaultOpen="true" tabErrorKey="">

        <table cellpadding="0" cellspacing="0" class="datatable" summary="Load Sum Records Section">
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.profileId}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.profileId}" property="oleLoadSumRecords.profileFile.profile" readOnly="true"/>
                </td>
            </tr>

            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.principalId}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.principalId}" property="oleLoadSumRecords.principalId" readOnly="true"/>
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> Counts :</div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <c:if test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount == -1}">
                        <blink> <font size="2" color="blue"><b>In Progress.. Please try later</b></font></blink>
                    </c:if>
                    <c:if test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount != -1}">
                        <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.acqLoadTotCount}" />
                        <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.acqLoadTotCount}" property="oleLoadSumRecords.acqLoadTotCount" readOnly="true"/>--
                        <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.acqLoadSuccCount}" />
                        <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.acqLoadSuccCount}" property="oleLoadSumRecords.acqLoadSuccCount" readOnly="true"/>--
                        <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.acqLoadFailCount}" />
                        <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.acqLoadFailCount}" property="oleLoadSumRecords.acqLoadFailCount" readOnly="true"/>
                    </c:if>
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.fileName}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.fileName}" property="oleLoadSumRecords.fileName" readOnly="true"/>
                </td>
            </tr>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.acqLoadPoTotCount}" /></div>
                </th>
                <c:if	test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount == -1}">
                    <td align=left valign=middle class="datacell" width="25%">
                        <blink> <font size="2" color="blue"><b>In Progress..Please try later</b></font></blink>
                    </td>
                </c:if>
                <c:if	test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount != -1}">
                    <td align=left valign=middle class="datacell" width="25%">
                        <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.acqLoadPoTotCount}" property="oleLoadSumRecords.acqLoadPoTotCount" readOnly="true"/>
                    </td>
                </c:if>
            </tr>

            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.acqLoadTotBibCount}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.acqLoadTotBibCount}" property="oleLoadSumRecords.acqLoadTotBibCount" readOnly="true"/>
                </td>
            </tr>

            <c:if test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount != '0' }">
                <tr>
                    <th align=right valign=middle class="bord-l-b" width="25%">
                        <div align="right"> List Of All POs:</div>
                    </th>
                    <c:if	test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount == -1}">
                        <td align=left valign=middle class="datacell" width="25%">
                            <blink>  <font size="2" color="blue"><b>In Progress..Please try later</b></font></blink>
                        </td>
                    </c:if>
                    <c:if	test="${KualiForm.oleLoadSumRecords.acqLoadPoTotCount != -1}">
                        <td align=left valign=middle class="datacell" width="25%">
                            <a
                                    href="<c:url value="${ConfigProperties.application.url}/"></c:url>portal.do?channelTitle=Acquisitions%20Search&channelUrl=oleAcquisitionsSearch.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_ACQS&loadSumId=<c:out value="${KualiForm.oleLoadSumRecords.acqLoadSumId}"/>"
                            target="_blank" class="showvisit"> List Of All POs
                            </a>
                        </td>
                    </c:if>
                </tr>
            </c:if>
            <c:if test="${KualiForm.oleLoadSumRecords.acqLoadTotBibCount != '0' }">
                <tr>
                    <th align=right valign=middle class="bord-l-b" width="25%">
                        <div align="right"> List Of All Bibs:</div>
                    </th>
                    <td align=left valign=middle class="datacell" width="25%">
                        <a
                                href="<c:url value="${ConfigProperties.application.url}/"></c:url>portal.do?channelTitle=Docstore%20Search&channelUrl=${ConfigProperties.application.url}/ole-kr-krad/olesearchcontroller?viewId=OLESearchView&methodToCall=showBibList&listOfBib=<c:out value="${KualiForm.oleLoadSumRecords.listOfAllBibs}"/>"
                        target="_blank" class="showvisit"> List Of All Bibs
                        </a>
                    </td>
                </tr>
            </c:if>
            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.acqLoadDescription}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.acqLoadDescription}" property="oleLoadSumRecords.acqLoadDescription" readOnly="true"/>
                </td>
            </tr>

            <tr>
                <th align=right valign=middle class="bord-l-b" width="25%">
                    <div align="right"> <kul:htmlAttributeLabel attributeEntry="${oleLoadSumRecords.loadCreatedDate}" /></div>
                </th>
                <td align=left valign=middle class="datacell" width="25%">
                    <kul:htmlControlAttribute attributeEntry="${oleLoadSumRecords.loadCreatedDate}" property="oleLoadSumRecords.loadCreatedDate" readOnly="true"/>
                </td>
            </tr>

        </table>
    </kul:tab>
    <c:if	test="${not empty KualiForm.oleLoadFailureRecordsList}">
        <kul:tab tabTitle="Load Failure Details" defaultOpen="true" tabErrorKey="">

            <table cellpadding="0" cellspacing="0" class="datatable" summary="Load Failure Records Section">
                <tr>
                    <th align=left valign=middle class="bord-l-b" width="25%">
                        <div align="left"> <kul:htmlAttributeLabel attributeEntry="${oleLoadFailureRecords.isbn}" /></div>
                    </th>
                    <th align=left valign=middle class="bord-l-b" width="25%">
                        <div align="left"> <kul:htmlAttributeLabel attributeEntry="${oleLoadFailureRecords.title}" /></div>
                    </th>
                    <%--  <th align=left valign=middle class="bord-l-b" width="25%">
                         <div align="left"> <kul:htmlAttributeLabel attributeEntry="${oleLoadFailureRecords.vendorId}" /></div>
                    </th> --%>
                    <th align=left valign=middle class="bord-l-b" width="25%">
                        <div align="left"> <kul:htmlAttributeLabel attributeEntry="${oleLoadFailureRecords.errorId}" /></div>
                    </th>


                </tr>

                <logic:iterate indexId="ctr" name="KualiForm" property="oleLoadFailureRecordsList" id="FailureList">
                    <tr>
                        <td align=left valign=middle class="datacell" width="25%">
                            <kul:htmlControlAttribute attributeEntry="${oleLoadFailureRecords.isbn}" property="oleLoadFailureRecordsList[${ctr}].isbn" readOnly="true"/>
                        </td>
                        <td align=left valign=middle class="datacell" width="25%">
                            <kul:htmlControlAttribute attributeEntry="${oleLoadFailureRecords.title}" property="oleLoadFailureRecordsList[${ctr}].title" readOnly="true"/>
                        </td>

                        <%--  <td align=left valign=middle class="datacell" width="25%">
                                <kul:htmlControlAttribute attributeEntry="${oleLoadFailureRecords.vendorId}" property="oleLoadFailureRecordsList[${ctr}].vendorId" readOnly="true"/>
                         </td> --%>
                        <td align=left valign=middle class="datacell" width="25%">
                            <kul:htmlControlAttribute attributeEntry="${oleLoadFailureRecords.errorId}" property="oleLoadFailureRecordsList[${ctr}].error" readOnly="true"/>
                        </td>
                    </tr>
                </logic:iterate>

            </table>


        </kul:tab>
    </c:if>
    <kul:notes />
    <kul:adHocRecipients />
    <kul:routeLog />
    <kul:panelFooter />
    <sys:documentControls transactionalDocument="true" extraButtons="${extraButtons}" />
</kul:documentPage>