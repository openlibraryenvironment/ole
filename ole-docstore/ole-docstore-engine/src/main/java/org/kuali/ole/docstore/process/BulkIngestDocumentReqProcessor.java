package org.kuali.ole.docstore.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class BulkIngestDocumentReqProcessor.
 *
 * @author Rajesh Chowdary K
 * @version 0.8
 * @created Aug 3, 2012
 */
public class BulkIngestDocumentReqProcessor
        implements Processor {

    private String user;
    private String action;
    private String category;
    private String type;
    private String format;
    private String target;
    private String preend = "";
    private String postend = "";

    public BulkIngestDocumentReqProcessor(String user, String action, String category, String type, String format,
                                          String target) {
        this.user = user;
        this.action = action;
        this.category = category;
        this.type = type;
        this.format = format;
        this.target = target;
        if (DocCategory.WORK.isEqualTo(category) && DocType.BIB.isEqualTo(type) && DocFormat.MARC.isEqualTo(format)) {
            this.preend = "<collection xmlns=\"http://www.loc.gov/MARC21/slim\">\n\t";
            this.postend = "\n</collection>\n";
        } else if (DocCategory.WORK.isEqualTo(category) && DocType.BIB.isEqualTo(type) && DocFormat.DUBLIN_UNQUALIFIED
                .isEqualTo(format)) {
            this.preend =
                    "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                            + "xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">\n"
                            + "\t<responseDate>" + new Date() + "</responseDate>\n" + "\t<request verb=\"ListRecords\" from=\""
                            + new Date() + "\" metadataPrefix=\"oai_dc\" set=\"hathitrust\">"
                            + "http://quod.lib.umich.edu/cgi/o/oai/oai</request>\n\t<ListRecords>\n";
            this.postend = "\n\t</ListRecords>\n" + "</OAI-PMH>\n";
        }

    }

    public void process(Exchange exchange) throws Exception {
        Integer ind = (Integer) exchange.getProperty("CamelSplitIndex");
        RandomAccessFile target = null;
        try {
            File reqFile = new File(this.target + File.separator + new File(
                    ((GenericFile) exchange.getProperty("CamelFileExchangeFile")).getFileNameOnly()).getName()
                    + ".tmp");
            if (!reqFile.exists()) {
                reqFile.createNewFile();
            }
            target = new RandomAccessFile(reqFile, "rw");
            target.seek(target.length());
            if (ind.equals(0)) {
                target.writeBytes(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<request>\n" + "\t<user>" + user + "</user>\n"
                                + "\t<operation>" + action + "</operation>\n" + "\t<requestDocuments>\n" + "");
            }
            if (exchange.getIn().getBody() != null) {
                String body = exchange.getIn().getBody().toString().trim();
                if (body.length() != 0) {
                    target.write(("\t\t<ingestDocument id=\"" + (ind + 1)).getBytes());
                    target.write(("\" category=\"" + category + "\" type=\"" + type + "\" format=\"" + format
                            + "\" > \n\t\t\t<content>").getBytes());
                    target.write("<![CDATA[ \n".getBytes());
                    target.write(preend.getBytes(Charset.forName("UTF-8")));
                    target.write(body.getBytes(Charset.forName("UTF-8")));
                    target.write(postend.getBytes(Charset.forName("UTF-8")));
                    target.write("\n ]]> \n\t\t\t</content>\n\t\t\t<additionalAttributes />\n".getBytes());
                    target.write("\t\t</ingestDocument>\n".getBytes());
                }
            }
            if (exchange.getProperty("CamelSplitComplete") != null && Boolean.TRUE
                    .equals(exchange.getProperty("CamelSplitComplete"))) {
                target.writeBytes("\t</requestDocuments>\n" + "</request>\n");
                target.close();
                // Build a new file name with timestamp.
                String newFileName = reqFile.getAbsolutePath()
                        .substring(0, reqFile.getAbsolutePath().lastIndexOf(".tmp"));
                String fnSuffix = "-Req" + new SimpleDateFormat("(yyyy.MM.dd-HH.mm.ss)").format(new Date());
                if (newFileName.endsWith(".xml")) {
                    newFileName = newFileName.substring(0, newFileName.lastIndexOf(".xml")) + fnSuffix;
                } else {
                    newFileName = newFileName + fnSuffix;
                }
                newFileName = newFileName + ".xml";
                reqFile.renameTo(new File(newFileName));
                //                reqFile.renameTo(new File(reqFile.getAbsolutePath().substring(0, reqFile.getAbsolutePath().length() - 8) + "-Req"
                //                        + new SimpleDateFormat("(yyyy.MM.dd-HH.mm.ss-z)").format(new Date()) + ".xml"));
            }
        } catch (Exception e) {
            throw new Exception(
                    "Unable to Process Record below from File : " + exchange.getProperty("CamelFileExchangeFile"), e);
        } finally {
            if (target != null) {
                target.close();
            }
        }
    }
}
