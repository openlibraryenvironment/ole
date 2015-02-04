package org.kuali.ole.docstore.discovery.circulation.json;

import org.kuali.ole.docstore.common.document.content.instance.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 10/21/13
 * Time: 10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceRecord {
    protected String instanceIdentifier;
    protected List<String> resourceIdentifier;
    protected List<FormerIdentifier> formerResourceIdentifier;
    protected OleHoldings oleHoldings;
    protected SourceHoldings sourceHoldings;
    protected List<Item> items;
    protected Extension extension;

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public List<String> getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(List<String> resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    public List<FormerIdentifier> getFormerResourceIdentifier() {
        return formerResourceIdentifier;
    }

    public void setFormerResourceIdentifier(List<FormerIdentifier> formerResourceIdentifier) {
        this.formerResourceIdentifier = formerResourceIdentifier;
    }

    public OleHoldings getOleHoldings() {
        return oleHoldings;
    }

    public void setOleHoldings(OleHoldings oleHoldings) {
        this.oleHoldings = oleHoldings;
    }

    public SourceHoldings getSourceHoldings() {
        return sourceHoldings;
    }

    public void setSourceHoldings(SourceHoldings sourceHoldings) {
        this.sourceHoldings = sourceHoldings;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }
}
