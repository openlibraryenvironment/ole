package org.kuali.ole.docstore.repository;

import org.kuali.ole.docstore.OleDocStoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface for dealing with nodes in the repository.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 5:40 PM
 */
public interface NodeManager {

    public Node getParentNode(RequestDocument requestDocument, Session session) throws OleDocStoreException;

    //    public Node createNode(RequestDocument requestDocument, Session session) throws OleDocStoreException;

    public Node createFileNode(RequestDocument document, String name, Node parentNode, Session session)
            throws OleDocStoreException;

    public Node createContentNode(Node fileNode, RequestDocument document, Node parentNode, Session session)
            throws OleDocStoreException;

    public void enableVersioning(Node node) throws OleDocStoreException;

    //    public Node createNode(Node node, RequestDocument requestDocument, RequestDocument linkedRequestDocument, Session session) throws OleDocStoreException;
    public void linkNodes(Node node, Node linkedDocumentNode, Session session) throws OleDocStoreException;

    //    public boolean isVersioningEnabled();

    public Node getNodeByUUID(Session session, String uuid) throws OleDocStoreException;

    public String getData(Node nodeByUUID) throws OleDocStoreException, RepositoryException, FileNotFoundException;

    public byte[] getBinaryData(Node nodeByUUID) throws RepositoryException, IOException;

    public void ingestItemRecForInstance(RequestDocument reqDoc, String id, Session session) throws OleDocStoreException;

    public String getInstanceData(Node instanceNode) throws RepositoryException, OleDocStoreException, FileNotFoundException;

}
