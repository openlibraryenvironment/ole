package org.kuali.ole.docstore.discovery.circulation.json;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/23/13
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CircInstance {
    private List<InstanceCollection> instanceCollection;

    public List<InstanceCollection> getInstanceCollection() {
        return instanceCollection;
    }

    public void setInstanceCollection(List<InstanceCollection> instanceCollection) {
        this.instanceCollection = instanceCollection;
    }
}
