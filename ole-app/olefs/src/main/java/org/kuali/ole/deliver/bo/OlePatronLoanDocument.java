package org.kuali.ole.deliver.bo;


import org.kuali.api.jaxb.DateAdapter;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;


@XmlRootElement(name = OlePatronLoanDocument.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "PatronLoanDocument", propOrder = {
        OlePatronLoanDocument.Elements.ITEM_BAR_CODE,
        OlePatronLoanDocument.Elements.TITLE,
        OlePatronLoanDocument.Elements.AUTHOR,
        OlePatronLoanDocument.Elements.LOCATION,
        OlePatronLoanDocument.Elements.CALL_NUMBER,
        OlePatronLoanDocument.Elements.DUE_DATE,
        OlePatronLoanDocument.Elements.MESSAGE_INFO


})
public class OlePatronLoanDocument extends AbstractDataTransferObject implements OlePatronLoanDocumentContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.ITEM_BAR_CODE, required = false)
    private final String itemBarcode;

    @XmlElement(name = Elements.TITLE, required = false)
    private final String title;

    @XmlElement(name = Elements.AUTHOR, required = false)
    private final String author;

    @XmlElement(name = Elements.DUE_DATE, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date dueDate;

    @XmlElement(name = Elements.LOCATION, required = false)
    private final String location;

    @XmlElement(name = Elements.CALL_NUMBER, required = false)
    private final String callNumber;

    @XmlElement(name = Elements.MESSAGE_INFO, required = false)
    private final String messageInfo;


    public OlePatronLoanDocument() {
        this.itemBarcode = null;
        this.title = null;
        this.author = null;
        this.dueDate = null;
        this.location = null;
        this.callNumber = null;
        this.messageInfo = null;

    }


    private OlePatronLoanDocument(Builder builder) {
        this.itemBarcode = builder.getItemBarcode();
        this.title = builder.getTitle();
        this.author = builder.getAuthor();
        this.dueDate = builder.getDueDate();
        this.location = builder.getLocation();
        this.callNumber = builder.getCallNumber();
        this.messageInfo = builder.getMessageInfo();

    }


    @Override
    public String getMessageInfo() {
        return messageInfo;
    }

    @Override
    public String getItemBarcode() {
        return itemBarcode;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getCallNumber() {
        return callNumber;
    }

    @Override
    public Long getVersionNumber() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }


    public static class Builder
            implements Serializable, ModelBuilder, OlePatronLoanDocumentContract {
        private String itemBarcode;
        private String title;
        private String author;
        private Date dueDate;
        private String location;
        private String callNumber;
        private String id;
        private Long versionNumber;
        private String messageInfo;
        private String patronBarcode;


        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePatronLoanDocumentContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getItemBarcode() != null) {
                builder.setItemBarcode(contract.getItemBarcode());
            }
            if (contract.getAuthor() != null) {
                builder.setAuthor(contract.getAuthor());
            }
            if (contract.getTitle() != null) {
                builder.setTitle(contract.getTitle());
            }
            if (contract.getCallNumber() != null) {
                builder.setCallNumber(contract.getCallNumber());
            }
            if (contract.getLocation() != null) {
                builder.setLocation(contract.getLocation());
            }
            if (contract.getDueDate() != null) {
                builder.setDueDate(contract.getDueDate());
            }
            if (contract.getMessageInfo() != null) {
                builder.setMessageInfo(contract.getMessageInfo());
            }
            return builder;
        }


        public void setPatronBarcode(String patronBarcode) {
            this.patronBarcode = patronBarcode;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public OlePatronLoanDocument build() {
            return new OlePatronLoanDocument(this);
        }

        @Override
        public String getItemBarcode() {
            return itemBarcode;
        }

        public void setItemBarcode(String itemBarcode) {
            this.itemBarcode = itemBarcode;
        }

        @Override
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        @Override
        public Date getDueDate() {
            return dueDate;
        }

        public void setDueDate(Date dueDate) {
            this.dueDate = dueDate;
        }

        @Override
        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String getCallNumber() {
            return callNumber;
        }

        @Override
        public String getMessageInfo() {
            return messageInfo;
        }

        public void setMessageInfo(String messageInfo) {
            this.messageInfo = messageInfo;
        }

        public void setCallNumber(String callNumber) {
            this.callNumber = callNumber;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "olePatronLoanItem";
    }

    static class Elements {
        final static String ITEM_BAR_CODE = "itemBarcode";
        final static String TITLE = "title";
        final static String AUTHOR = "author";
        final static String DUE_DATE = "dueDate";
        final static String LOCATION = "location";
        final static String CALL_NUMBER = "callNumber";
        final static String MESSAGE_INFO = "messageInfo";

    }

}
