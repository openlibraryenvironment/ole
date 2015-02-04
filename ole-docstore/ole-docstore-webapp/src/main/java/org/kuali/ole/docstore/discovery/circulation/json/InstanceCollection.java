package org.kuali.ole.docstore.discovery.circulation.json;


import org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/23/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceCollection {
    protected EInstance eInstance;
    protected InstanceRecord instance;


    public EInstance geteInstance() {
        return eInstance;
    }

    public void seteInstance(EInstance eInstance) {
        this.eInstance = eInstance;
    }

    public InstanceRecord getInstance() {
        return instance;
    }

    public void setInstance(InstanceRecord instance) {
        this.instance = instance;
    }
}
