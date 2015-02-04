package org.kuali.ole.docstore.indexer.solr;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 7/24/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentLocalId {

    public static int getDocumentId(String uuid) {
        if (hasPrefix(uuid)) {
            String[] prefixes = uuid.split("-");
            return Integer.valueOf(prefixes[1]).intValue();
        }
        return Integer.valueOf(uuid).intValue();
    }

    public static boolean hasPrefix(String uuid) {
        String[] prefixes = uuid.split("-");
        if (prefixes.length == 2) {
            return true;
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
