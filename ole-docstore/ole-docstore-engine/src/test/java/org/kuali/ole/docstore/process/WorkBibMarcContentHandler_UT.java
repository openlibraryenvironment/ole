package org.kuali.ole.docstore.process;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.IngestDocumentHandler;
import org.kuali.ole.documenthandler.WorkBibMarcContentHandler;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: nd6967
 * Date: 9/12/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcContentHandler_UT {
    IngestDocumentHandler ingestDocumentHandler = new IngestDocumentHandler();

    @Test
    public void testdoPreIngestContentManipulations() throws Exception {
        URL resource = getClass().getResource("/org/kuali/ole/repository/Sample-Input.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);
        ArrayList<RequestDocument> ingestDocs = new ArrayList<RequestDocument>();
        int uuid = 1;
        for (int i = 0; i < 1000; i++) {
            ingestDocs.add(ingestDocumentHandler.toObject(fileContent));
        }
        WorkBibMarcContentHandler wbch = new WorkBibMarcContentHandler();
        RequestDocument reqDoc = ingestDocs.get(0);
        System.out.println("First Request Document content before modifying " + reqDoc.getContent().getContent());
        long start = System.currentTimeMillis();
        get("start");
        for (RequestDocument rq : ingestDocs) {
            wbch.doPreIngestContentManipulations(rq, Integer.toString(uuid));
//            if (uuid <= 3) {
//                System.out.println(" Request Document content after modifying " + rq.getContent().getContent());
//            }
            uuid++;
        }
        get("end");
        System.out.println("total " + (System.currentTimeMillis() - start));


    }

    @Test
    public void testdoPreIngestContentManipulationsForTesting() throws Exception {
        URL resource = getClass().getResource("/org/kuali/ole/repository/Sample-Input.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);
        ArrayList<RequestDocument> ingestDocs = new ArrayList<RequestDocument>();
        int uuid = 1;
        for (int i = 0; i < 1000; i++) {
            ingestDocs.add(ingestDocumentHandler.toObject(fileContent));
        }
        WorkBibMarcContentHandler wbch = new WorkBibMarcContentHandler();
        RequestDocument reqDoc = ingestDocs.get(0);
        System.out.println("First Request Document content before modifying " + reqDoc.getContent().getContent());
        long start = System.currentTimeMillis();
        get("start");
        for (RequestDocument rq : ingestDocs) {
            System.out.println("uuid " + uuid);
            wbch.doPreIngestContentManipulationsForTesting(rq, Integer.toString(uuid));
//            if (uuid <= 3) {
//                System.out.println(" Request Document content after modifying " + rq.getContent().getContent());
//            }
            uuid++;
        }
        get("end");

        System.out.println("total " + (System.currentTimeMillis() - start));
    }

    public void get(String str) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        System.out.println("Timeeeeee " + " " + str + " " + now + " = " + formatter.format(calendar.getTime()));
    }

    public void display(RequestDocument rq) {
        System.out.println("getCategory " + rq.getCategory());
        System.out.println("getDocumentName " + rq.getDocumentName());
        System.out.println("getContent " + rq.getContent().getContent());
        System.out.println("getFormat " + rq.getFormat());
        System.out.println("getType " + rq.getType());

    }

}
