package org.kuali.ole.deliver.bo;


import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = OlePatronLoanDocuments.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "PatronLoan", propOrder = {
        OlePatronLoanDocuments.Elements.OLE_PATRON_LOAN_ITM
})
public class OlePatronLoanDocuments extends AbstractDataTransferObject implements OlePatronLoanDocumentsContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_PATRON_LOAN_ITM, required = false)
    private final List<OlePatronLoanDocument> olePatronLoanItem;


    public OlePatronLoanDocuments() {
        this.olePatronLoanItem = null;
    }

    public OlePatronLoanDocuments(Builder builder) {

        this.olePatronLoanItem = new ArrayList<OlePatronLoanDocument>();
        if (!CollectionUtils.isEmpty(builder.getOlePatronLoanDocuments())) {
            for (OlePatronLoanDocument.Builder olePatronLoanDocument : builder.getOlePatronLoanDocuments()) {
                this.olePatronLoanItem.add(olePatronLoanDocument.build());
            }
        }
    }

    /**
     * This method converts the PersistableBusinessObjectBase OlePatronDocument into immutable object OlePatronDefinition
     *
     * @param bo
     * @return OlePatronDefinition
     */
    public static OlePatronLoanDocument to(OleRenewalLoanDocument bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronLoanDocument.Builder.create(bo).build();
    }


    @Override
    public String getId() {
        return null;
    }

    @Override
    public Long getVersionNumber() {
        return null;
    }


    @Override
    public List<? extends OlePatronLoanDocumentContract> getOlePatronLoanDocuments() {
        return olePatronLoanItem;
    }

    public static class Builder
            implements Serializable, ModelBuilder, OlePatronLoanDocumentsContract {
        private List<OlePatronLoanDocument.Builder> olePatronLoanDocuments;
        private Long versionNumber;
        private String objectId;

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePatronLoanDocumentsContract contract) {

            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();

            builder.olePatronLoanDocuments = new ArrayList<OlePatronLoanDocument.Builder>();
            if (!CollectionUtils.isEmpty(contract.getOlePatronLoanDocuments())) {
                for (OlePatronLoanDocumentContract olePatronLoanDocumentContract : contract.getOlePatronLoanDocuments()) {
                    builder.olePatronLoanDocuments.add(OlePatronLoanDocument.Builder.create(olePatronLoanDocumentContract));
                }
            }

            builder.setVersionNumber(contract.getVersionNumber());

            //builder.setId(contract.getId());

            return builder;
        }


        public OlePatronLoanDocuments build() {
            return new OlePatronLoanDocuments(this);
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public Long getVersionNumber() {
            return null;
        }

        @Override
        public List<OlePatronLoanDocument.Builder> getOlePatronLoanDocuments() {
            return olePatronLoanDocuments;
        }

        public void setOlePatronLoanDocuments(List<OlePatronLoanDocumentContract> OlePatronLoanDocumentContract) {
            this.olePatronLoanDocuments = olePatronLoanDocuments;
        }


    }


    static class Constants {

        final static String ROOT_ELEMENT_NAME = "olePatronLoanItems";
    }

    static class Elements {
        //final static String OLE_PATRON_LOAN_ITMS = "olePatronLoanItems";
        final static String OLE_PATRON_LOAN_ITM = "olePatronLoanItem";

    }

}
