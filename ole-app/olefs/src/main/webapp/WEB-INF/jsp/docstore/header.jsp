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
<!-- Header -->
   

       <table>
       <tr>
       <td>
                    <label> <b><span class="noscreen">Quick Find:</span></b>
                    <span id="search-input-out">
                    <input type="text" name="searchterm" id="search-input" onkeypress="return KeyPress('QuickSearch',event)" size="30" />
                    
                   <a href="#" onclick="validateQuickSearch();" id="search-submit">
                   <img src="kr/static/images/buttonsmall_search.gif" />
                    </a>
                                         </span></label>
                    </td>
                    <%
                    String req = (String)request.getParameter("requires");
                    if(req == null || !req.equals("yes")){
                    %>
                    
                    <td>
                    
                         <a href="docStore.do" >
                   
                    <img src="static/images/buttonsmall_back.gif" /></a>        
                    </td>
                    <%} %>
                    <tr>
                  </table> 

     <!-- /header -->
