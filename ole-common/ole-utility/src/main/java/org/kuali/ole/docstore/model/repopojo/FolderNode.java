package org.kuali.ole.docstore.model.repopojo;


import java.util.List;

/**
 * Class for representing folder nodes in docstore
 * User: Pranitha
 * Date: 2/28/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderNode
        extends DocStoreNode {

    private List<DocStoreNode> children;

    public List<DocStoreNode> getChildren() {
        return children;
    }

    public void setChildren(List<DocStoreNode> children) {
        this.children = children;
    }
}
