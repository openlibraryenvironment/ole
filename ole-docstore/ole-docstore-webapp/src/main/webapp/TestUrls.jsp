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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <style type="text/css">
    #testurlsLinks td a:visited {
           color :#FF0000 !important;
    }
</style>
</head>
<body>

<table id="testurlsLinks" height="80%" width="100%">
	<tr>
		<td>
		<ol>
			<li><a
				href="bib/select/?q=*:*"
				target="_blank"><font style="font: bold 70%/100%"> Search
			for Bibliographic data</font></a></li>

			<ol>
				<li><a
					href="bib/select/?q=DocFormat:marc"
					target="_blank"><font style="font: bold 70%/100%">
				Search for Bib - marc data</font></a></li>
				<ol>
					<li>Hit highlighting
						<ol>
							<li>
						<a
						href="bib/select/?q=((DocFormat:marc) AND(Author_search:(s*)))&hl.fl=Author_search&hl=true&fl=Author_search"
						target="_blank"><font
						style="font: bold 70%/100%"> Search by Author.</font></a>

<%--						<a
						href="bib/select/?q=DocFormat:marc AND Author_search:s*&hl=true&hl.fl=Author_search"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
	--%>						</li>
							<li>
						<a
						href="bib/select/?q=((DocFormat:marc)AND (Title_search:(s*)))&hl.fl=Title_search&hl=true&fl=Title_search "
						target="_blank"><font
						style="font: bold 70%/100%"> Search by Title.</font></a>

<%--
						<a
						href="bib/select/?q=(DocFormat:marc AND Title_search:s*)&hl=true&hl.fl=Title_search"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
--%>
							</li>
						</ol>

					</li>
					<!--li><a
						href="bib/select/?q=DocFormat:marc AND MainEntryPersonalNameComposite:H*"
						target="_blank"><font
						style="font: bold 70%/100%"> Search by
					MainEntryPersonalNameComposite Field</font></a>
					<a
						href="bib/select/?q=DocFormat:marc AND MainEntryPersonalNameComposite:H*&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%">
					[HTML]</font></a></li--></li>
<%--
					<li><a
						href="bib/select/?q=DocFormat:marc AND Title_search:s*"
						target="_blank"><font
						style="font: bold 70%/100%"> Search by
					Title Field</font></a>
					<a
						href="bib/select/?q=DocFormat:marc AND Title_search:s*&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%">[HTML]
					</font></a>



					<li><a
						href="bib/select/?q=(DocFormat:marc)AND(Price_f:[10 TO 20])"
						target="_blank"><font
						style="font: bold 70%/100%"> Search by Price Range [10 TO 20]</font></a>
						<a
						href="bib/select/?q=(DocFormat:marc)AND(Price_f:[10 TO 20])&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>

						</li>
--%>
					<li><a
						href="bib/select/?q=(DocFormat:marc)AND (PublicationDate_display:[* TO NOW])"
						target="_blank"><font
						style="font: bold 70%/100%"> Search by Date Range Till Now </font></a>
<%--
						<a
						href="bib/select/?q=(DocFormat:marc)AND(PublicationDate_display:[* TO NOW])&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%">[HTML] </font></a>
--%>
						</li>

<%--
					<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&sort=Price_f desc"
						target="_blank"><font
						style="font: bold 70%/100%"> Sort By Price higher to lower</font></a>
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&sort=Price_f desc&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
						</li>
--%>
							<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&sort=PublicationDate_sort asc&fl=PublicationDate_search"
						target="_blank"><font
						style="font: bold 70%/100%"> Sort By Date in lower to higher </font></a>
<%--
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&sort=PublicationDate_sort asc&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
--%>
						</li>
						</li>
					  <li><a
						href="bib/select/?q=(*:*) AND (DocFormat:marc)&sort=Title_sort asc&fl=Title_search"
						target="_blank"> <font style="font: bold 70%/100%">
					Sort by Title in Alphabetical order[A-Z]</font></a>
<%--
					<a
						href="bib/select/?q=(*:*) AND (DocFormat:marc)&sort=Title_sort asc&wt=xslt&tr=response.xsl"
						target="_blank"> <font style="font: bold 70%/100%">[HTML]
					</font></a>
--%>
					</li>
						<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&sort=Title_sort desc&fl=Title_search"
						target="_blank"><font
						style="font: bold 70%/100%"> Sort By Title Alphabetical order[Z-A]</font></a>
<%--
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&sort=Title_sort asc&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
--%>
						</li>

				<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=Author_facet&facet.query=Author_facet:Author_facet:[A TO C]&facet.query=Author_facet:[D TO F]&facet.query=Author_facet:[G TO I]&facet.query=Author_facet:[J TO L]&facet.query=Author_facet:[M TO O]&facet.query=Author_facet:[P TO R]&facet.query=Author_facet:[S TO U]&facet.query=Author_facet:[V TO Z]"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets by Author field</font></a>
<%--
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=Price_f&facet.query=Price_f:[1 TO 50]&facet.query=Price_f:[51 TO 100]&facet.query=Price_f:[101 TO 200]&facet.query=Price_f:[201 TO 500]&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
--%>
						</li>
						<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=Format_facet&fl=Format_facet"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets by Format field</font></a>
<%--
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=r_name_facetLetter&facet.query=r_name_facetLetter:[A TO C]&facet.query=r_name_facetLetter:[D TO F]&facet.query=r_name_facetLetter:[G TO I]&facet.query=r_name_facetLetter:[J TO L]&facet.query=r_name_facetLetter:[M TO O]&facet.query=r_name_facetLetter:[P TO R]&facet.query=r_name_facetLetter:[S TO U]&facet.query=r_name_facetLetter:[V TO Z]&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
--%>
						</li>
						<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=PublicationDate_facet&facet.query=PublicationDate_facet:[1900 TO 1950]&facet.query=PublicationDate_facet:[1951 TO 2000]&facet.query=PublicationDate_facet:[2000 TO 2011]"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets by Year of Publication</font></a>
<%--
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=PublicationDate_facet&facet.query=PublicationDate_facet:[1900 TO 1950]&facet.query=PublicationDate_facet:[1951 TO 2000]&facet.query=PublicationDate_facet:[2000 TO 2011]&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>
--%>

						</li>
						<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=Subject_facet&facet.query=Subject_facet:[A TO C]&facet.query=Subject_facet:[D TO F]&facet.query=Subject_facet:[G TO I]&facet.query=Subject_facet:[J TO L]&facet.query=Subject_facet:[M TO O]&facet.query=Subject_facet:[P TO R]&facet.query=Subject_facet:[S TO U]&facet.query=Subject_facet:[V TO Z]"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets by Name of Subject</font></a>
<%--
						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=r_NameOfPublisher_facetLetter&facet.query=r_NameOfPublisher_facetLetter:[A TO C]&facet.query=r_NameOfPublisher_facetLetter:[D TO F]&facet.query=r_NameOfPublisher_facetLetter:[G TO I]&facet.query=r_NameOfPublisher_facetLetter:[J TO L]&facet.query=r_NameOfPublisher_facetLetter:[M TO O]&facet.query=r_NameOfPublisher_facetLetter:[P TO R]&facet.query=r_NameOfPublisher_facetLetter:[S TO U]&facet.query=r_NameOfPublisher_facetLetter:[V TO Z]&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%"> [HTML]</font></a>

						</li>
						<li><a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=Author_facet&facet.query=Author_facet:[A TO C]&facet.query=Author_facet:[D TO F]&facet.query=Author_facet:[G TO I]&facet.query=Author_facet:[J TO L]&facet.query=Author_facet:[M TO O]&facet.query=Author_facet:[P TO R]&facet.query=Author_facet:[S TO U]&facet.query=Author_facet:[V TO Z]"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets by PersonalName</font></a>

						<a
						href="bib/select/?q=(*:*)AND(DocFormat:marc)&facet=true&facet.field=r_AddedEntryPersonalName_facetLetter&facet.query=r_AddedEntryPersonalName_facetLetter:[A TO C]&facet.query=r_AddedEntryPersonalName_facetLetter:[D TO F]&facet.query=r_AddedEntryPersonalName_facetLetter:[G TO I]&facet.query=r_AddedEntryPersonalName_facetLetter:[J TO L]&facet.query=r_AddedEntryPersonalName_facetLetter:[M TO O]&facet.query=r_AddedEntryPersonalName_facetLetter:[P TO R]&facet.query=r_AddedEntryPersonalName_facetLetter:[S TO U]&facet.query=r_AddedEntryPersonalName_facetLetter:[V TO Z]&wt=xslt&tr=response.xsl"
						target="_blank"><font
						style="font: bold 70%/100%">[HTML]</font></a>
--%>
						</li>


				</ol>
				<ol></ol>
				<li><a
					href="bib/select/?q=DocFormat:dublin*"
					target="_blank"><font
					style="font: bold 70%/100%"> Search for Dublin  data</font></a></li>
				<ol>
					<li>
						<a href="bib/select/?q=(DocFormat:dublin*)&sort=Title_sort asc&fl=Title_search,id&rows=300" target="_blank">
							<font style="font: bold 70%/100%"> Sort by Title</font>
						</a>
					</li>
					<li><a
						href="bib/select?q=(DocFormat:dublin)&facet=true&facet.field=Language_facet&facet.field=Subject_facet&facet.field=Author_facet"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets for Dublin Qualified data</font></a></li>
						<ol>
							<li>
								<a href="bib/select?q=(DocFormat:dublin)&fl=Author_facet" target="_blank">
									<font style="font: bold 70%/100%">Author </font>
								</a>
							</li>
							<li>
								<a href="bib/select?q=(DocFormat:dublin)&facet=true&fl=Language_facet" target="_blank">
									<font style="font: bold 70%/100%">Language </font>
								</a>
							</li>
							<li>
								<a href="bib/select?q=(DocFormat:dublin)&fl=PublicationDate_facet" target="_blank">
									<font style="font: bold 70%/100%">PublicationDate</font>
								</a>
							</li>
                            <li>
                                <a href="bib/select?q=(PublicationDate_facet:19*)&(DocFormat:dublin)" target="_blank">
                                     <font style="font: bold 70%/100%">With Sample Data</font>
                                </a>
                            </li>

						</ol>
                    <li><a
                        href="bib/select?q=(DocFormat:dublinunq)&facet=true&facet.field=Language_facet&facet.field=Subject_facet&facet.field=PublicationDate_facet"
                        target="_blank"><font
                        style="font: bold 70%/100%"> Facets for Dublin Unqualified data</font></a>
                    </li>

                    <ol>
                            <li>
                                <a href="bib/select?q=(DocFormat:dublinunq)&facet=true&facet.field=Language_facet&fl=Language_facet" target="_blank">
                                    <font style="font: bold 70%/100%">Language </font>
                                </a>
                           </li>
                            <li>
                                <a href="bib/select?q=((DocFormat:dublinunq))&facet=true&facet.field=Author_facet&fl=Author_facet" target="_blank">
                                    <font style="font: bold 70%/100%">Author</font>
                                </a>
                            </li>
                            <li>
                                <a href="bib/select?q=((DocFormat:dublinunq))&facet=true&facet.field=PublicationDate_facet&fl=PublicationDate_facet" target="_blank">
                                    <font style="font: bold 70%/100%">PublicationDate</font>
                                </a>
                          </li>
                        <li>
                                <a href="bib/select?q=((DocFormat:dublinunq))&facet=true&facet.field=PublicationDate_facet" target="_blank">
                                <font style="font: bold 70%/100%">With Sample Data</font>
                                </a>

                        </li>

				</ol>


			</ol>
				<ol>

<%--
					<li><a
						href="bib/select?q=(DocFormat:dublinunq)&facet=true&facet.field=Language_facet&facet.field=Subject_facet&facet.field=PublicationDate_facet"
						target="_blank"><font
						style="font: bold 70%/100%"> Facets for Dublin Unqualified</font></a>
					</li>

					<ol>
							<li>
								<a href="bib/select?q=(DocFormat:dublinunq)&facet=true&facet.field=Language_facet&fl=Language_facet" target="_blank">
									<font style="font: bold 70%/100%">Language </font>
								</a>
								<ol>
									<a href="bib/select?q=((DocFormat:dublinunq) AND Language_facet:English)&facet=true" target="_blank">
									<font style="font: bold 70%/100%">With Sample Data</font>
									</a>
								</ol>
							</li>
							<li>
								<a href="bib/select?q=((DocFormat:dublinunq))&facet=true&facet.field=Author_facet&fl=Author_facet" target="_blank">
									<font style="font: bold 70%/100%">Author</font>
								</a>
								<ol>
									<a href="bib/select?q=((DocFormat:dublinunq) AND Author_facet:s*)&facet=true&facet.field=Author_facet" target="_blank">
									<font style="font: bold 70%/100%">With Sample Data</font>
									</a>
								</ol>
							</li>
							<li>
								<a href="bib/select?q=((DocFormat:dublinunq))&facet=true&facet.field=PublicationDate_facet&fl=PublicationDate_facet" target="_blank">
									<font style="font: bold 70%/100%">PublicationDate</font>
								</a>
								<ol>
									<a href="bib/select?q=((DocFormat:dublinunq))&facet=true&facet.field=PublicationDate_facet" target="_blank">
									<font style="font: bold 70%/100%">With Sample Data</font>
									</a>
								</ol>
							</li>
--%>
						</ol>
				</ol>
		</ol>
		</font> </a></td>
	</tr>
</table>

</body>
</html>
