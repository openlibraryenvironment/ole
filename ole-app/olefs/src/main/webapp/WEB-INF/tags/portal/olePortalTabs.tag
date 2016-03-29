<%--
 Copyright 2005 The Kuali Foundation
 
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

<%@ attribute name="selectedTab" required="true"%>
<%@ attribute name="environment" required="false"%>
<div id="tabs" class="tabposition">
	<ul>
		<%-- Deliver Tab --%>
		<c:if test='${selectedTab == "oleDeliver"}'>
			<li class="red">
				<a class="red" href="portal.do?selectedTab=oleDeliver"
					title="Deliver">Deliver</a>
			</li>
		</c:if>
		<c:if test='${selectedTab != "oleDeliver"}'>
			<c:if test="${empty selectedTab}">
				<li class="red">
					<a class="red" href="portal.do?selectedTab=oleDeliver"
						title="Deliver">Deliver</a>
				</li>
			</c:if>
			<c:if test="${!empty selectedTab}">
				<li class="green">
					<a class="green" href="portal.do?selectedTab=oleDeliver"
						title="Deliver">Deliver</a>
				</li>
			</c:if>
		</c:if>


            <%-- Describe Tab --%>
		<c:if test='${selectedTab == "oleDescribe"}'>
			<li class="red">
				<a class="red" href="portal.do?selectedTab=oleDescribe"
					title="Describe">Describe</a>
			</li>
		</c:if>
		<c:if test='${selectedTab != "oleDescribe"}'>
			<li class="green">
				<a class="green"
					href="portal.do?selectedTab=oleDescribe"
					title="Describe">Describe</a>
			</li>
		</c:if>

            <%-- Select & Acquire Tab --%>
		<c:if test='${selectedTab == "oleSelectAcquire"}'>
			<li class="red">
				<a class="red" href="portal.do?selectedTab=oleSelectAcquire"
					title="Select/Acquire">Select/Acquire</a>
			</li>
		</c:if>
		<c:if test='${selectedTab != "oleSelectAcquire"}'>
			<li class="green">
				<a class="green"
					href="portal.do?selectedTab=oleSelectAcquire"
					title="Select/Acquire">Select/Acquire</a>
			</li>
		</c:if>

            <%-- Maintenece Tab --%>
            <c:if test='${selectedTab == "oleMaintenance"}'>
                <li class="red">
                    <a class="red" href="portal.do?selectedTab=oleMaintenance"
                       title="Maintenance">Maintenance</a>
                </li>
            </c:if>
            <c:if test='${selectedTab != "oleMaintenance"}'>
                <li class="green">
                    <a class="green"
                       href="portal.do?selectedTab=oleMaintenance"
                       title="Maintenance">Maintenance</a>
                </li>
            </c:if>
            <%-- Admin Tab --%>
            <c:if test='${selectedTab == "oleAdmin"}'>
                <li class="red">
                    <a class="red" href="portal.do?selectedTab=oleAdmin"
                       title="Admin">Admin</a>
                </li>
            </c:if>
            <c:if test='${selectedTab != "oleAdmin"}'>
                <li class="green">
                    <a class="green"
                       href="portal.do?selectedTab=oleAdmin"
                       title="Admin">Admin</a>
                </li>
            </c:if>

            <%-- New Batch Tab --%>
            <c:if test='${selectedTab == "oleNewBatch"}'>
                <li class="red">
                    <a class="red" href="portal.do?selectedTab=oleNewBatch"
                       title="New Batch">New Batch</a>
                </li>
            </c:if>
            <c:if test='${selectedTab != "oleNewBatch"}'>
                <li class="green">
                    <a class="green"
                       href="portal.do?selectedTab=oleNewBatch"
                       title="New Batch">New Batch</a>
                </li>
            </c:if>

            <c:if test='${selectedTab == "Reports"}'>
                <li class="red">
                    <a class="red" href="${reportsUrl}"
                       title="Reports">Reports</a>
                </li>
            </c:if>
            <c:if test='${selectedTab != "Reports"}'>
                <li class="green">
                    <a class="green"
                       href="${reportsUrl}"
                       title="Reports">Reports</a>
                </li>
            </c:if>


	</ul>
</div>
