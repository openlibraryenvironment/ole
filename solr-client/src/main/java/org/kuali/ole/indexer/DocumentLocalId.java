package org.kuali.ole.indexer;

/**
 * Created by sheiks on 27/10/16.
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
