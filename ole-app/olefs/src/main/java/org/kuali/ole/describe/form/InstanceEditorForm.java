package org.kuali.ole.describe.form;

import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.describe.bo.InstanceRecordMetaData;
import org.kuali.ole.describe.bo.SourceEditorForUI;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * InstanceEditorForm is the form class for Instance Editor
 */
public class InstanceEditorForm extends UifFormBase {

    private List<Instance> oleInstanceList;

    private Instance instance;

    private InstanceRecordMetaData itemRecordMetaData = new InstanceRecordMetaData();

    private InstanceRecordMetaData holdingRecordMetaData = new InstanceRecordMetaData();

    private String uuid;

    private Tree<String, String> instanceHierarchy = new Tree<String, String>();

    private String existing = "false";

    private String message;

    private String selectedItemId;

    private String treeData;

    private String hdnUuid;

    private int hdnIndex = 0;

    private OleHoldings selectedHolding;

    private SourceEditorForUI selectedSourceHolding;

    private Item selectedItem;

    private String hdnAddItemUUID;

    private int instanceIndex;

    private String selectedDocType;

    private String deleteItemId;

    public String getDeleteItemId() {
        return deleteItemId;
    }

    public void setDeleteItemId(String deleteItemId) {
        this.deleteItemId = deleteItemId;
    }

    public InstanceEditorForm() {
        getSelectedHolding().getUri().add(new Uri());
        getSelectedHolding().getNote().add(new Note());
        getSelectedHolding().getExtentOfOwnership().add(new ExtentOfOwnership());
        getSelectedHolding().getExtentOfOwnership().get(0).getNote().add(new Note());
    }

    public String getTreeData() {
        return treeData;
    }

    public void setTreeData(String treeData) {
        this.treeData = treeData;
    }

    public String getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(String selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public InstanceRecordMetaData getItemRecordMetaData() {
        return itemRecordMetaData;
    }

    public void setItemRecordMetaData(InstanceRecordMetaData itemRecordMetaData) {
        this.itemRecordMetaData = itemRecordMetaData;
    }

    public InstanceRecordMetaData getHoldingRecordMetaData() {
        return holdingRecordMetaData;
    }

    public void setHoldingRecordMetaData(InstanceRecordMetaData holdingRecordMetaData) {
        this.holdingRecordMetaData = holdingRecordMetaData;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Tree<String, String> getInstanceHierarchy() {
        return instanceHierarchy;
    }

    public void setInstanceHierarchy(Tree<String, String> instanceHierarchy) {
        this.instanceHierarchy = instanceHierarchy;
    }

    public String getExisting() {
        return existing;
    }

    public void setExisting(String existing) {
        this.existing = existing;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHdnUuid() {
        return hdnUuid;
    }

    public void setHdnUuid(String hdnUuid) {
        this.hdnUuid = hdnUuid;
    }

    public int getHdnIndex() {
        return hdnIndex;
    }

    public void setHdnIndex(int hdnIndex) {
        this.hdnIndex = hdnIndex;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<Instance> getOleInstanceList() {
        return oleInstanceList;
    }

    public void setOleInstanceList(List<Instance> oleInstanceList) {
        this.oleInstanceList = oleInstanceList;
    }

    public String getHdnAddItemUUID() {
        return hdnAddItemUUID;
    }

    public void setHdnAddItemUUID(String hdnAddItemUUID) {
        this.hdnAddItemUUID = hdnAddItemUUID;
    }

    public int getInstanceIndex() {
        return instanceIndex;
    }

    public void setInstanceIndex(int instanceIndex) {
        this.instanceIndex = instanceIndex;
    }

    public OleHoldings getSelectedHolding() {
        if (null == selectedHolding) {
            selectedHolding = new OleHoldings();
        }
        return selectedHolding;
    }

    public void setSelectedHolding(OleHoldings selectedHolding) {
        this.selectedHolding = selectedHolding;
    }

    public String getSelectedDocType() {
        return selectedDocType;
    }

    public void setSelectedDocType(String selectedDocType) {
        this.selectedDocType = selectedDocType;
    }

    public SourceEditorForUI getSelectedSourceHolding() {
        return selectedSourceHolding;
    }

    public void setSelectedSourceHolding(SourceEditorForUI selectedSourceHolding) {
        this.selectedSourceHolding = selectedSourceHolding;
    }
}
