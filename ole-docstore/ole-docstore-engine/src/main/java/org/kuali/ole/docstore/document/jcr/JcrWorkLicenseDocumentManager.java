package org.kuali.ole.docstore.document.jcr;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.repository.WorkLicenseNodeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Implements the DocumentManager interface for [Work-License-*] documents.
 *
 * @version %I%, %G%
 * @author: tirumalesh.b
 * Date: 31/8/12 Time: 7:04 PM
 */

public class JcrWorkLicenseDocumentManager
        extends JcrAbstractDocumentManager {
    private static JcrWorkLicenseDocumentManager ourInstance = null;
    private static final Logger LOG = LoggerFactory
            .getLogger(JcrWorkLicenseDocumentManager.class);

    public static JcrWorkLicenseDocumentManager getInstance() {
        if (null == ourInstance) {
            ourInstance = new JcrWorkLicenseDocumentManager();
        }
        return ourInstance;
    }

    protected JcrWorkLicenseDocumentManager() {
        super();
        this.nodeManager = WorkLicenseNodeManager.getInstance();
    }

    @Override
    public boolean isVersioningEnabled() {
        return true;
    }

    @Override
    protected void modifyAdditionalAttributes(RequestDocument requestDocument, Node node) {
        if (!DocFormat.ONIXPL.isEqualTo(requestDocument.getFormat())) {
            AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
            String docName = new File(requestDocument.getDocumentName()).getName();
            additionalAttributes.setAttribute("dateEntered", Calendar.getInstance().toString());
            additionalAttributes.setAttribute("fileName", docName);
            additionalAttributes.setAttribute("owner", requestDocument.getUser());
        }
    }

    @Override
    protected byte[] convertContentToBytes(RequestDocument reqDoc) throws OleDocStoreException {
        String charset = "UTF-8";
        byte[] documentBytes = new byte[0];
        try {
            if (reqDoc.getDocumentName() != null && reqDoc.getDocumentName().trim().length() != 0) {
                documentBytes = FileUtils.readFileToByteArray(new File(reqDoc.getDocumentName()));
            } else if (reqDoc.getContent().getContent() != null) {
                documentBytes = reqDoc.getContent().getContent().getBytes(charset);
            }
        } catch (Exception e) {
            LOG.info("Failed to convert input string to byte[] with charset " + e);
            throw new OleDocStoreException(e.getMessage());
        }
        return documentBytes;
    }

    @Override
    protected String checkOutContent(Node nodeByUUID, String format, String user) throws OleDocStoreException {
        String outputFilePath = null;
        if (!user.equalsIgnoreCase("RestWebUser") && (format.equals(DocFormat.PDF.getCode()) || format
                .equals(DocFormat.DOC.getCode()))) {
            try {
                FileOutputStream fos = null;
                File output = File.createTempFile("checkout.", ".output");
                FileUtils.deleteQuietly(output);
                output.mkdirs();

                String fileNameAndExtension = null;
                fileNameAndExtension = nodeByUUID.getIdentifier() + format;
                outputFilePath = output.getAbsolutePath() + File.separator + fileNameAndExtension;
                byte[] binaryContent = nodeManager.getBinaryData(nodeByUUID);
                fos = new FileOutputStream(outputFilePath);
                fos.write(binaryContent);
                fos.close();
                LOG.info("path-->" + output.getAbsolutePath());
            } catch (Exception e) {
                LOG.info("Checking out file from JCR" + e.getMessage(), e);
                throw new OleDocStoreException(e);
            }
        } else {
            try {
                outputFilePath = nodeManager.getData(nodeByUUID);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new OleDocStoreException(e.getMessage(), e);
            }
        }

        return outputFilePath;
    }

    @Override
    public ResponseDocument delete(RequestDocument requestDocument, Object object) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validateInput(RequestDocument requestDocument, Object object, List<String> valuesList) throws OleDocStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ResponseDocument> deleteVerify(List<RequestDocument> requestDocument, Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseDocument deleteVerify(RequestDocument requestDocument, Object object) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
