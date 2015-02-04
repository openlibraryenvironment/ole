package org.kuali.ole.docstore.document.jcr;


import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import javax.jcr.Session;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements the DocumentManager interface for [Work-Bib-MARC] documents.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 1:10 PM
 */
public class JcrWorkBibMarcDocumentManager
        extends JcrWorkBibDocumentManager {
    private static JcrWorkBibMarcDocumentManager ourInstance = null;

    public static JcrWorkBibMarcDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new JcrWorkBibMarcDocumentManager();
        }
        return ourInstance;
    }

    protected JcrWorkBibMarcDocumentManager() {
        super();
    }

    @Override
    protected void modifyDocumentContent(RequestDocument doc, String nodeIdentifier, String parentNodeIdentifier) {
        String content = doc.getContent().getContent();
        if (content != null && content != "" && content.length() > 0) {
            Pattern pattern = Pattern.compile("tag=\"001\">.*?</controlfield");
            Pattern pattern2 = Pattern.compile("<controlfield.*?tag=\"001\"/>");
            Matcher matcher = pattern.matcher(content);
            Matcher matcher2 = pattern2.matcher(content);
            if (matcher.find()) {
                doc.getContent().setContent(matcher.replaceAll("tag=\"001\">" + nodeIdentifier + "</controlfield"));
            } else if (matcher2.find()) {
                doc.getContent()
                        .setContent(matcher2.replaceAll("<controlfield tag=\"001\">" + nodeIdentifier + "</controlfield>"));
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
                sb.append(nodeIdentifier);
                sb.append("</controlfield>");
                sb.append(content.substring(ind + 1));
                doc.getContent().setContent(sb.toString());
            }
        }
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        Session session = (Session) object;
        for (RequestDocument linkDoc : requestDocument.getLinkedRequestDocuments()) {
            JcrWorkInstanceDocumentManager.getInstance().validateInput(linkDoc, session, valuesList);
        }
    }
}
