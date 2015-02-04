/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.docstore.discovery.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;


/**
 * Utility class for simulating an HTTP POST request.
 */
public class HttpPostUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpPostUtil.class);

    /**
     * Submits an HTTP POST request to the given target with the given request
     * parameters.
     *
     * @param target  url
     * @param content of request parameters
     * @return the response
     * @throws Exception Usage:
     *                   String target = "http://localhost:8080/OLE-DocSearch/select/";
     *                   String content = "q=(ModifyingAgency:iul)&facet=true&facet.field=Price_f&facet.query=Price_f:[1 TO 50]&facet.query=Price_f:[51 TO 100]&facet.query=Price_f:[101 TO 200]&facet.query=Price_f:[201 TO 500]&facet.field=r_name_facetLetter&facet.query=r_name_facetLetter:[A TO C]&facet.query=r_name_facetLetter:[D TO F]&facet.query=r_name_facetLetter:[G TO I]&facet.query=r_name_facetLetter:[J TO L]&facet.query=r_name_facetLetter:[M TO O]&facet.query=r_name_facetLetter:[P TO R]&facet.query=r_name_facetLetter:[S TO U]&facet.query=r_name_facetLetter:[V TO Z]&facet.field=YearOfPublication&facet.query=YearOfPublication:[1900 TO 1950]&facet.query=YearOfPublication:[1951 TO 2000]&facet.query=YearOfPublication:[2000 TO 2011]&facet.field=r_NameOfPublisher_facetLetter&facet.query=r_NameOfPublisher_facetLetter:[A TO C]&facet.query=r_NameOfPublisher_facetLetter:[D TO F]&facet.query=r_NameOfPublisher_facetLetter:[G TO I]&facet.query=r_NameOfPublisher_facetLetter:[J TO L]&facet.query=r_NameOfPublisher_facetLetter:[M TO O]&facet.query=r_NameOfPublisher_facetLetter:[P TO R]&facet.query=r_NameOfPublisher_facetLetter:[S TO U]&facet.query=r_NameOfPublisher_facetLetter:[V TO Z]&facet.field=r_AddedEntryPersonalName_facetLetter&facet.query=r_AddedEntryPersonalName_facetLetter:[A TO C]&facet.query=r_AddedEntryPersonalName_facetLetter:[D TO F]&facet.query=r_AddedEntryPersonalName_facetLetter:[G TO I]&facet.query=r_AddedEntryPersonalName_facetLetter:[J TO L]&facet.query=r_AddedEntryPersonalName_facetLetter:[M TO O]&facet.query=r_AddedEntryPersonalName_facetLetter:[P TO R]&facet.query=r_AddedEntryPersonalName_facetLetter:[S TO U]&facet.query=r_AddedEntryPersonalName_facetLetter:[V TO Z]&wt=xslt&tr=response.xsl&hl.fl=ModifyingAgency,MainEntryPersonalName,FullerFormOfName,DatesAssociatedWithName,Title,RemainderOfTitle,StatementOfResponsibility,PlaceOfPublication,NameOfPublisher,DateOfPublication,Extent,Dimentions,GeneralNote,TopicalTermorgeographicnameElement,GeneralSubdivision,CorporateNameJurisdictionNameEntryElement,SubordinateUnit,AddedEntryPersonalName,999_i,999_p,999_u,ISBN,ISSN&&hl=true";
     *                   String solrResponse = HttpPostUtil.postData(target, content);
     */
    public static String postData(String target, String content) throws Exception {
        LOG.debug("About Post URL:" + target + "\nCONTENT length:" + content.length());
        String response = "";
        URL url = new URL(target);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Writer w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        w.write(content);
        w.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String temp;
        while ((temp = in.readLine()) != null) {
            response += temp + "\n";
        }
        in.close();
        LOG.debug("Server response: " + response);
        return response;
    }

}
