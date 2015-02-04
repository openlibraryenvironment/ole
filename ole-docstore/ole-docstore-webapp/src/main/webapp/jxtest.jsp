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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ page language="java" %>
<jsp:useBean id="documentStoreService" class="org.kuali.ole.DocumentStoreServiceImpl" scope="session"/>

<html>
<head>
    <title>Cotent Repository</title>

     <p>
        <input type="submit" value="submit"/>
    </p>

    <!-- include the jx delicious skin -->
    <link rel="stylesheet" href="jx/themes/delicious/jxtheme.css" type="text/css" media="screen" charset="utf-8">
    <!-- IE specific style sheets -->
    <!--[if LTE IE 6]>
    <link rel="stylesheet" href="jx/themes/delicious/ie6.css" type="text/css" media="screen" charset="utf-8">
    <![endif]-->
    <!--[if IE 7]>
    <link rel="stylesheet" href="jx/themes/delicious/ie7.css" type="text/css" media="screen" charset="utf-8">
    <![endif]-->

    <!-- this prevents scrollbars from appearing on the page -->
    <style type="text/css">
        body {
        overflow: hidden;
        }
    </style>

    <!-- include the jx library -->
    <script src="jx/jxlib.js" type="text/javascript" charset="utf-8"></script>

    <!-- our application code -->
    <script type='text/javascript'>
        window.addEvent('load', function() {
        // our custom javascript goes in here.
        var tree = new Jx.Tree({parent: 'treeArea'});

       // list of nodes from the service
        var list = '<%= documentStoreService.browse() %>';

        for (var j=1; j<4; j++) {
        var folder = new Jx.TreeFolder({label: 'Folder ' + j});
        tree.append(folder);
        for (var i=1; i<4; i++) {
        var item = new Jx.TreeItem({label: 'Item ' + i});
        folder.append(item);
        }
        }

        });
    </script>

</head>
<body>
<div id="treeArea"></div>
</body>
</html>
