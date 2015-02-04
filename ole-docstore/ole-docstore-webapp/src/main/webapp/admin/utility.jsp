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
    <!--
<link rel="stylesheet" type="text/css" href="./css/ole.css" />
-->
<style type="text/css">
    #utilityLinks td a:visited {
           color : red !important;
    }
</style>
</head>

<body>

<table id="utilityLinks" style="background: #E8F0FF;" height="40%" width="100%" border="1">
	<tr>
		<td style="font:bold 100%/100%;verdana:sans-serif;">Name</td>
		<td style="font:bold 100%/100%;verdana:sans-serif;">Description</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/index.jsp"
			target="_blank">Admin home</a></td>
		<td>Admin Home page</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/stats.jsp"
			target="_blank">Statistics</a></td>
		<td>The Statistics administration page provides a variety of
		useful statistics related to Solr performance. Statistics include:
		<ol>
			<li>Information about when the index was loaded and how many
			documents are in it.</li>
			<li>Usage information on the SolrRequestHandlers used to service
			queries.</li>
			<li>Data covering the indexing process, including the number of
			additions, deletions, commits, etc.</li>
			<li>Cache implementation and hit information.</li>
		</ol>
		</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/registry.jsp"
			target="_blank">Info</a></td>
		<td>Details the version of Solr that is running and the classes
		used in the current implementation for queries, updates, and caching.
		Also includes information about where the files are located in the
		Solr subversion repository and brief descriptions of the functionality
		of the file.</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/distributiondump.jsp"
			target="_blank">Distribution</a></td>
		<td>Displays information about index distribution and
		replication.</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/get-properties.jsp"
			target="_blank">Java properties</a>
		</td>
		<td>Displays all of the Java system properties in use by the
		current system. Solr supports system property substitution through the
		the command line. See the solrconfig.xml file for information about
		implementing this feature.</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/schema.jsp"
			target="_blank">schema</a>
		</td>
		<td>Window for viewing details of the Solr server's schema, which
		defines fields, dynamic fields, and field types used for indexing.</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/dataimport.jsp"
			target="_blank">dataImport</a>
		</td>
		<td>Displays the handlers available. On selecting the handler, we
		can do fullImport, full import with cleaning, get the status, and get
		the document count.</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/raw-schema.jsp"
			target="_blank">Raw schema</a>
		</td>
		<td>Displays the response as schema.xml with all indexed fields</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/analysis.jsp"
			target="_blank">Analysis</a>
		</td>
		<td>It is helpful to see how the current configuration of Solr
		indexes a sample text and processes a sample query.</td>
	</tr>
	<tr>
		<td><a
			href="bib/admin/threaddump.jsp"
			target="_blank">Thread dump</a>
		</td>
		<td>The thread dump option displays stack trace information for
		all the threads running in the JVM.</td>
	</tr>
	<tr>
		<td><a href="bib/admin/ping.jsp"
			target="_blank">Ping</a> (Not Working)</td>
		<td>Issues a ping request to the server, consisting of the query
		specified in the admin section of the solrconfig.xml file.</td>
	</tr>
</table>
</body>
</html>
