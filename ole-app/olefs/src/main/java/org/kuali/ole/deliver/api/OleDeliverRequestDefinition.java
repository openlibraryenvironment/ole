package org.kuali.ole.deliver.api;

import org.kuali.api.jaxb.DateAdapter;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OleDeliverRequestDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleDeliverRequestDefinition.Constants.TYPE_NAME, propOrder = {
        OleDeliverRequestDefinition.Elements.AUTHOR,
        OleDeliverRequestDefinition.Elements.CALL_NUMBER,
        OleDeliverRequestDefinition.Elements.COPY_NUMBER,
        OleDeliverRequestDefinition.Elements.ITEM_STATUS,
        OleDeliverRequestDefinition.Elements.ITEM_TYPE,
        OleDeliverRequestDefinition.Elements.PATRON_NAME,
        OleDeliverRequestDefinition.Elements.VOLUME_NUMBER,
        OleDeliverRequestDefinition.Elements.SHELVING_LOCATION,
        OleDeliverRequestDefinition.Elements.TITLE,
        OleDeliverRequestDefinition.Elements.ITEM_ID,
        OleDeliverRequestDefinition.Elements.REQUEST_ID,
        OleDeliverRequestDefinition.Elements.BORR_QUEUE_POS,
        OleDeliverRequestDefinition.Elements.CREATE_DATE,
        OleDeliverRequestDefinition.Elements.OLE_DLVR_REQ_TYPE,

        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OleDeliverRequestDefinition extends AbstractDataTransferObject implements OleDeliverRequestContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.TITLE, required = false)
    private final String title;

    @XmlElement(name = Elements.AUTHOR, required = false)
    private final String author;

    @XmlElement(name = Elements.CALL_NUMBER, required = false)
    private final String callNumber;

    @XmlElement(name = Elements.COPY_NUMBER, required = false)
    private final String copyNumber;

    @XmlElement(name = Elements.VOLUME_NUMBER, required = false)
    private final String volumeNumber;

    @XmlElement(name = Elements.ITEM_STATUS, required = false)
    private final String itemStatus;

    @XmlElement(name = Elements.ITEM_ID, required = false)
    private final String itemId;

    @XmlElement(name = Elements.REQUEST_ID, required = false)
    private final String requestId;

    @XmlElement(name = Elements.ITEM_TYPE, required = false)
    private final String itemType;

    @XmlElement(name = Elements.PATRON_NAME, required = false)
    private final String patronName;

    @XmlElement(name = Elements.CREATE_DATE, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Timestamp.class)
    private final Timestamp createDate;

    @XmlElement(name = Elements.BORR_QUEUE_POS, required = false)
    private final Integer borrowerQueuePosition;

    @XmlElement(name = Elements.SHELVING_LOCATION, required = false)
    private final String shelvingLocation;

    @XmlElement(name = Elements.OLE_DLVR_REQ_TYPE, required = false)
    private final OleDeliverRequestTypeDefinition oleDeliverRequestType;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleDeliverRequestDefinition() {
        this.title = null;
        this.author = null;
        this.itemStatus = null;
        this.callNumber = null;
        this.copyNumber = null;
        this.volumeNumber = null;
        this.shelvingLocation = null;
        this.itemType = null;
        this.patronName = null;
        this.itemId = null;
        this.requestId = null;
        this.createDate = null;
        this.borrowerQueuePosition = null;
        this.oleDeliverRequestType = null;
        this.versionNumber = null;

        //this.objectId = null;
    }


    private OleDeliverRequestDefinition(Builder builder) {
        this.title = builder.getTitle();
        this.author = builder.getAuthor();
        this.callNumber = builder.getCallNumber();
        this.copyNumber = builder.getCopyNumber();
        this.itemType = builder.getItemType();
        this.itemStatus = builder.getItemStatus();
        this.shelvingLocation = builder.getShelvingLocation();
        this.volumeNumber = builder.getVolumeNumber();
        this.patronName = builder.getPatronName();
        this.itemId = builder.getItemId();
        this.requestId = builder.getRequestId();
        this.borrowerQueuePosition = builder.getBorrowerQueuePosition();
        this.createDate = builder.getCreateDate();
        this.oleDeliverRequestType = builder.getOleDeliverRequestType().build();
        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }


    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getId() {
        return this.requestId;
    }

    @Override
    public String getItemId() {
        return this.itemId;
    }

    @Override
    public String getRequestId() {
        return this.requestId;
    }

    @Override
    public String getItemStatus() {
        return this.itemStatus;
    }

    @Override
    public String getItemType() {
        return this.itemType;
    }

    @Override
    public Integer getBorrowerQueuePosition() {
        return this.borrowerQueuePosition;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public String getShelvingLocation() {
        return this.shelvingLocation;
    }

    @Override
    public String getCallNumber() {
        return this.callNumber;
    }

    @Override
    public String getCopyNumber() {
        return this.copyNumber;
    }

    @Override
    public String getPatronName() {
        return this.patronName;
    }

    @Override
    public Timestamp getCreateDate() {
        return this.createDate;
    }


    @Override
    public String getVolumeNumber() {
        return this.volumeNumber;
    }

    @Override
    public OleDeliverRequestTypeDefinition getOleDeliverRequestType() {
        return this.oleDeliverRequestType;
    }


    public static class Builder
            implements Serializable, ModelBuilder, OleDeliverRequestContract {
        private String title;
        private String author;
        private String shelvingLocation;
        private String callNumber;
        private String copyNumber;
        private String volumeNumber;
        private String patronName;
        private String itemType;
        private String itemStatus;
        private String itemId;
        private String requestId;
        private Integer borrowerQueuePosition;
        private Timestamp createDate;
        private OleDeliverRequestTypeDefinition.Builder oleDeliverRequestType;


        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleDeliverRequestContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getTitle() != null) {
                builder.setTitle(contract.getTitle());
            }
            if (contract.getAuthor() != null) {
                builder.setAuthor(contract.getAuthor());
            }
            if (contract.getCallNumber() != null) {
                builder.setCallNumber(contract.getCallNumber());
            }
            if (contract.getCopyNumber() != null) {
                builder.setCopyNumber(contract.getCopyNumber());
            }
            if (contract.getItemStatus() != null) {
                builder.setItemStatus(contract.getItemStatus());
            }
            if (contract.getItemType() != null) {
                builder.setItemType(contract.getItemType());
            }
            if (contract.getShelvingLocation() != null) {
                builder.setShelvingLocation(contract.getShelvingLocation());
            }
            if (contract.getVolumeNumber() != null) {
                builder.setVolumeNumber(contract.getVolumeNumber());
            }
            if (contract.getItemId() != null) {
                builder.setItemId(contract.getItemId());
            }
            if (contract.getRequestId() != null) {
                builder.setRequestId(contract.getRequestId());
            }
            if (contract.getRequestId() != null) {
                builder.setRequestId(contract.getRequestId());
            }
            if (contract.getBorrowerQueuePosition() != null) {
                builder.setBorrowerQueuePosition(contract.getBorrowerQueuePosition());
            }
            if (contract.getCreateDate() != null) {
                builder.setCreateDate(contract.getCreateDate());
            }
            if (contract.getOleDeliverRequestType() != null) {
                builder.setOleDeliverRequestType(OleDeliverRequestTypeDefinition.Builder.create(contract.getOleDeliverRequestType()));
            }

            builder.setId(contract.getId());
            return builder;
        }


        public OleDeliverRequestDefinition build() {
            return new OleDeliverRequestDefinition(this);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getShelvingLocation() {
            return shelvingLocation;
        }

        public void setShelvingLocation(String shelvingLocation) {
            this.shelvingLocation = shelvingLocation;
        }

        public String getCallNumber() {
            return callNumber;
        }

        public void setCallNumber(String callNumber) {
            this.callNumber = callNumber;
        }

        public String getCopyNumber() {
            return copyNumber;
        }

        public void setCopyNumber(String copyNumber) {
            this.copyNumber = copyNumber;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getVolumeNumber() {
            return volumeNumber;
        }

        public void setBorrowerQueuePosition(Integer borrowerQueuePosition) {
            this.borrowerQueuePosition = borrowerQueuePosition;
        }

        public void setCreateDate(Timestamp createDate) {
            this.createDate = createDate;
        }

        public OleDeliverRequestTypeDefinition.Builder getOleDeliverRequestType() {
            return oleDeliverRequestType;
        }

        public void setOleDeliverRequestType(OleDeliverRequestTypeDefinition.Builder oleDeliverRequestType) {
            this.oleDeliverRequestType = oleDeliverRequestType;
        }

        public void setVolumeNumber(String volumeNumber) {
            this.volumeNumber = volumeNumber;
        }

        public String getPatronName() {
            return patronName;
        }

        @Override
        public Timestamp getCreateDate() {
            return this.createDate;
        }


        public void setPatronName(String patronName) {
            this.patronName = patronName;
        }

        public String getItemType() {
            return itemType;
        }

        @Override
        public Integer getBorrowerQueuePosition() {
            return this.borrowerQueuePosition;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getItemStatus() {
            return itemStatus;
        }

        public void setItemStatus(String itemStatus) {
            this.itemStatus = itemStatus;
        }

        @Override
        public String getId() {
            return null;
        }

        public void setId(String id) {
        }

        @Override
        public Long getVersionNumber() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleDeliverRequest";
        final static String TYPE_NAME = "OleDeliverRequestType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String ITEM_ID = "itemId";
        final static String REQUEST_ID = "requestId";
        final static String TITLE = "title";
        final static String ITEM_STATUS = "itemStatus";
        final static String AUTHOR = "author";
        final static String ITEM_TYPE = "itemType";
        final static String SHELVING_LOCATION = "shelvingLocation";
        final static String CALL_NUMBER = "callNumber";
        final static String COPY_NUMBER = "copyNumber";
        final static String VOLUME_NUMBER = "volumeNumber";
        final static String PATRON_NAME = "patronName";
        final static String CREATE_DATE = "createDate";
        final static String BORR_QUEUE_POS = "borrowerQueuePosition";
        final static String OLE_DLVR_REQ_TYPE = "oleDeliverRequestType";


    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
