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
<html>
<head>
</head>
<body>

    <%
String pageTitle="Ingest documents";
%>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">

<table align="center" border="0" width="994px" height="85%" cellpadding="0" cellspacing="0">
    <tr height="98%" valign="top">
        <td>
            <form action="injest" method="POST" enctype="multipart/form-data">
                <table>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            Category (e.x. Bibliographic, Authority etc.)
                        </td>
                        <td>
                            <input type="text" name="category"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Format (e.x. Marc, Eac etc.)
                        </td>
                        <td>
                            <input type="text" name="format"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input type="file" name="fileName"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" value="submit"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>

</table>

<br/>
</div>
</body>
</html>
