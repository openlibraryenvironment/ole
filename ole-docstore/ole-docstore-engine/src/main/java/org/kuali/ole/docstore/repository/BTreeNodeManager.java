//package org.kuali.ole.docstore.repository;
//
//import org.kuali.ole.docstore.OleDocStoreException;
//import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
//
//import javax.jcr.Node;
//import javax.jcr.RepositoryException;
//import javax.jcr.Session;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
///**
// * Implements the use of BTree like structure when creating nodes.
// *
// * @author tirumalesh.b
// * @version %I%, %G%
// *          Date: 28/8/12 Time: 6:10 PM
// */
//public class BTreeNodeManager
//        implements NodeManager {
//    private static BTreeNodeManager ourInstance = new BTreeNodeManager();
//
//    public static BTreeNodeManager getInstance() {
//        return ourInstance;
//    }
//
//    private BTreeNodeManager() {
//    }
//
//    @Override
//    public void linkNodes(Node node, Node linkedDocumentNode, Session session) throws OleDocStoreException {
//    }
//
//    @Override
//    public Node getParentNode(RequestDocument requestDocument, Session session) throws OleDocStoreException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Node createFileNode(RequestDocument document, String name, Node parentNode, Session session)
//            throws OleDocStoreException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Node createContentNode(Node fileNode, RequestDocument document, Node parentNode, Session session)
//            throws OleDocStoreException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void enableVersioning(Node node) throws OleDocStoreException {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Node getNodeByUUID(Session session, String uuid) throws OleDocStoreException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public String getData(Node nodeByUUID) throws OleDocStoreException, RepositoryException, FileNotFoundException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public byte[] getBinaryData(Node nodeByUUID) throws RepositoryException, IOException {
//        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void ingestItemRecForInstance(RequestDocument reqDoc, Session session) throws OleDocStoreException {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public String getInstanceData(Node instanceNode)
//            throws RepositoryException, OleDocStoreException, FileNotFoundException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//}
