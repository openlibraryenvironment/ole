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

<channel:portalChannelTop channelTitle="Year End Transactions" />
<div class="body">
    <br/>
    
    	<portal:olePortalLink grey="true" displayTitle="false" title="Financial Processing" url="" hiddenTitle="true"/><br/>
		<portal:olePortalLink grey="true" displayTitle="false" title="Year End Budget Adjustment" url="financialYearEndBudgetAdjustment.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEBA" hiddenTitle="true"/><br/>
		<portal:olePortalLink grey="true" displayTitle="false" title="Year End Distribution of Income and Expense" url="financialYearEndDistributionOfIncomeAndExpense.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEDI" hiddenTitle="true"/><br/>
		<portal:olePortalLink grey="true" displayTitle="false" title="Year End General Error Correction" url="financialYearEndGeneralErrorCorrection.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEGE" hiddenTitle="true"/><br/>
		<portal:olePortalLink grey="true" displayTitle="false" title="Year End Transfer of Funds" url="financialYearEndTransferOfFunds.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YETF" hiddenTitle="true"/><br/> <br/>
    </ul>
    <%--<strike><strong>Labor Distribution</strong></strike><br />
    
	  	<strike>
			<portal:olePortalLink displayTitle="true"
				title="Year End Benefit Expense Transfer"
				url="laborYearEndBenefitExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEBT" />
		</strike><br/>
		<strike>
			<portal:olePortalLink displayTitle="true"
				title="Year End Salary Expense Transfer"
				url="laborYearEndSalaryExpenseTransfer.do?methodToCall=docHandler&command=initiate&docTypeName=OLE_YEST" />
		</strike><br/>
    </ul> --%>
</div>
<channel:portalChannelBottom />
