package org.kuali.ole.docstore.indexer.solr;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 7/24/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentLocalId {


    public static String getDocumentId(String uuid) {
        if (hasPrefix(uuid)) {
            String[] prefixes = uuid.split("-");
            return prefixes[1];
        }
        return uuid;
    }

    public static boolean hasPrefix(String uuid) {
        if (uuid != null) {
            String[] prefixes = uuid.split("-");
            if (prefixes.length == 2) {
                return true;
            }
        }
        return false;
    }

    public static String getDocumentIdDisplay(String uuid) {
        if (hasPrefix(uuid)) {
            String[] prefixes = uuid.split("-");
            return prefixes[1];
        }
        return uuid;
    }


}
