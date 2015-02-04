package org.kuali.ole.docstore.common.exception;

import com.thoughtworks.xstream.XStream;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 1/23/14
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreExceptionProcessor {

    private static XStream xStream = getXstream();

    private static XStream getXstream() {
        XStream xStream = new XStream();
        xStream.alias("exception", DocstoreException.class);
        return xStream;
    }

    public static DocstoreException fromXML(String fileContent) {
        return (DocstoreException) xStream.fromXML(fileContent);
    }

    public static String toXml(DocstoreException docstoreException) {
        return xStream.toXML(docstoreException);

    }


}
