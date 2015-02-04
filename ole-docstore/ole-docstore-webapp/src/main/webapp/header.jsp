<%--
   - Copyright 2011 The Kuali Foundation.
   - 
   - Licensed un der the Educational Community License, Version 2.0 (the "License");
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
<!-- Header -->
<%@ page import="org.kuali.ole.docstore.discovery.config.*"%>
<%@ page import="java.util.*"%>
<%
	DocSearchConfigImpl docSearchConfigImpl = DocSearchConfigImpl
			.getDocSearchConfigImplInstance();
	List<DocCategoryDTO> docCategoryList = docSearchConfigImpl.getDocCategories();
	List<DocTypeDTO> fieldataVal = null;
	List<DocFieldDTO> fieldVAttributes = null;
	List<DocFieldDTO> facetFieldVAttributes = null;
	String docName = null;
	String fieldName = null;
	String facetFieldName = null;
%>
    <div id="header">

        <!-- Logotyp -->
        <span id="logo"><strong>OLE Doc Store Discovery </strong><br/>
        &nbsp;  <div id="copyright">POC by  HTC Global Services, Inc.</div>
        </span>
        
        <!-- Search -->
        <div id="search" class="noprint">                                   
                    <form name="search" method="post" action="./results.jsp">
                     <fieldset  value="OK">
                      <legend>Search</legend>                     
                    <label> 
                    
                    <center>
                    <span id="category">Select Category:<SELECT NAME="documentCategory">
                    
						<%
							String docCategory = request.getParameter("documentCategory");
							String selected = "";
							for (int i = 0; i < docCategoryList.size(); i++) {
								if (docCategoryList.get(i).getId().equalsIgnoreCase(docCategory)) {
									selected = "selected";
								} else {
									selected = "";
								}
						%>
						<Option value="<%=docCategoryList.get(i).getId()%>" <%=selected%>><%=docCategoryList.get(i).getName()%></option>
						<%
							}
						%>
						</SELECT>
					</span>
					</center>
					
                    <span id="search-input-out">
                    <input type="text" name="searchterm" align="right" id="search-input" size="30" onkeypress="return KeyPress('QuickSearch',event)"/>
                    
                    <a href="./sidebar.jsp" target="">
                   
                    <div id="help" align="center">Advanced Search</div></a>
                    
                    </span></label>
                    <a href="#" onclick="validateQuickSearch();" id="search-submit">
                    <img  src="images/search_submit.gif" id="search-submit" border="0"  />
                    </a>
                    <input type="hidden" name="searchtype" value="quick"></input>                   
                    </fieldset>
               </form>
        </div> 
        
        <!-- /search -->

    </div> <!-- /header -->
