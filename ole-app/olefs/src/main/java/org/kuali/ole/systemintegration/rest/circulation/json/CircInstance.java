package org.kuali.ole.systemintegration.rest.circulation.json;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 7:59 PM
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
