/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.documenthandler;

import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class WorkBibMarcContentHandler to handle content of Marc Files.
 *
 * @author Rajesh Chowdary K
 * @created Mar 21, 2012
 */
public class WorkBibMarcContentHandler {

    public void doPreIngestContentManipulations(RequestDocument doc, final String fileNodeUUID) {
        String content = doc.getContent().getContent();
        if (content != null && content != "" && content.length() > 0) {
            Pattern pattern = Pattern.compile("tag=\"001\">.*?</controlfield");
            Pattern pattern2 = Pattern.compile("<controlfield.*?tag=\"001\"/>");
            Matcher matcher = pattern.matcher(content);
            Matcher matcher2 = pattern2.matcher(content);
            if (matcher.find()) {
                doc.getContent().setContent(matcher.replaceAll("tag=\"001\">" + fileNodeUUID + "</controlfield"));
            } else if (matcher2.find()) {
                doc.getContent()
                        .setContent(matcher2.replaceAll("<controlfield tag=\"001\">" + fileNodeUUID + "</controlfield>"));
            } else {
                int ind = content.indexOf("</leader>") + 9;
                if (ind == 8) {
                    ind = content.indexOf("<leader/>") + 9;
                    if (ind == 8) {
                        ind = content.indexOf("record>") + 7;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(content.substring(0, ind));
                sb.append("<controlfield tag=\"001\">");
                sb.append(fileNodeUUID);
                sb.append("</controlfield>");
                sb.append(content.substring(ind + 1));
                doc.getContent().setContent(sb.toString());
            }
        }
    }


    public void doPreIngestContentManipulationsForTesting(RequestDocument doc, final String fileNodeUUID) {
        String content = doc.getContent().getContent();
        int ind = content.indexOf("</leader>") + 9;
        if (ind == 8) {
            ind = content.indexOf("<leader/>") + 9;
            if (ind == 8) {
                ind = content.indexOf("record>") + 7;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(content.substring(0, ind));
        sb.append("<controlfield tag=\"001\">");
        sb.append(fileNodeUUID);
        sb.append("</controlfield>");
        sb.append(content.substring(ind + 1));
        doc.getContent().setContent(sb.toString());
    }
}
