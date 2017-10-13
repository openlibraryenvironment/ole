/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.scripts

import org.jsoup.Jsoup
import org.jsoup.nodes.Document;

/**
 * Handles the parsing of jsp and tag files
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JspParserUtils {

    /**
     * Takes a filePath and nodelist returns from the jsp file
     * method wraps the results in a jsp root element and replaces comments and source code with kul:comment and kul:source
     *
     * @param fileName
     * @return
     */
    public static def parseJspFile(filePath) {
        // load file
        def file = new File(filePath)
        return parseJspText(file.text)
    }

    /**
     * cleans up jsp text and parsers into nodelist
     *
     * @param fileText
     * @return
     */
    public static def parseJspText(fileText) {
        // perform cleanup of tags
        // TODO: need a way to extract this information without breaking parser
        fileText = cleanupCTags(fileText)
        fileText = cleanupCommentTags(fileText)
        fileText = cleanupAttrTags(fileText)
        fileText = cleanupReformatTags(fileText)
        return parseJstlText(fileText)
    }

    /**
     * removes conditional c:tags to help the parsing of the jsp/tag file
     *
     * @param fileText
     * @return
     */
    static def cleanupCTags(fileText) {
        fileText = fileText.replaceAll(/(?ms)<c:out\s+(value=.*?)\/>/, '{kul:out}')
        fileText = fileText.replaceAll(/(?ms)<c:if(.*?)>.*?<\/c:if>/, "{kul:if}")
        fileText = fileText.replaceAll(/(?ms)<c:url.*?\/c:url>/, "{kul:url}")
        fileText = fileText.replaceAll(/(?ms)<c:url.*?\/>/, "{kul:url}")
        fileText = fileText.replaceAll(/&&/, "&amp;&amp;")
        return fileText
    }

    /**
     * reformats jsp comment tags as kul:comment tags
     *
     * @param fileText
     * @return
     */
    static def cleanupCommentTags(fileText) {
        fileText = fileText.replaceAll(/<%--/, "<kul:comment>")
        fileText = fileText.replaceAll(/--%>/, "</kul:comment>")

        return fileText
    }

    /**
     * replaces attribute tags as kul:source tags
     *
     * @param fileText
     * @return
     */
    static def cleanupAttrTags(fileText) {
        fileText = fileText.replaceAll(/<%@/, "<kul:source>")
        fileText = fileText.replaceAll(/%>/, "</kul:source>")

        return fileText
    }

    /**
     * closes tags, handles special characters, etc...
     *
     * @param fileText
     * @return
     */
    static def cleanupReformatTags(fileText) {
        // jsoup parser is used as it handles invalid html formatting well
        Document doc = Jsoup.parse(fileText);
        return doc.outerHtml()
    }

    /**
     * TODO: Throw exception and handle if unparsable
     *
     * @param fileText
     * @return
     */
    static def parseJstlText(fileText) {
        // loads sax parser, ignore namespaces and sets all elements to lowercase
        def saxParser = new org.cyberneko.html.parsers.SAXParser()
        saxParser.setFeature('http://xml.org/sax/features/namespaces', false)
        saxParser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower")
        def slurper = new XmlSlurper(saxParser)
        def jstlRoot = slurper.parseText(fileText)
        return jstlRoot
    }

}