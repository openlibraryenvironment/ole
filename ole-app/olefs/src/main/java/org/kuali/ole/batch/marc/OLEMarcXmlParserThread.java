package org.kuali.ole.batch.marc;

import org.marc4j.*;
import org.xml.sax.InputSource;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMarcXmlParserThread extends Thread {

    private RecordStack queue;
    private InputSource input;
    private ErrorHandler errors;

    public OLEMarcXmlParserThread(RecordStack queue, InputSource input, ErrorHandler errors) {
        this.queue = queue;
        this.input = input;
        this.errors = errors;
    }

    public void run() {
        try {
            MarcXmlHandler handler = new OLEMarcXmlHandler(queue, errors);
            MarcXmlParser parser = new MarcXmlParser(handler);
            parser.parse(input);
        } catch (MarcException me) {
            queue.passException(me);
        } finally {
            queue.end();
        }
    }

}