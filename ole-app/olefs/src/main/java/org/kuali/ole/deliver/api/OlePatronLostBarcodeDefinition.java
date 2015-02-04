package org.kuali.ole.deliver.api;

import org.apache.commons.lang.StringUtils;
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
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OlePatronLostBarcodeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OlePatronLostBarcodeDefinition.Constants.TYPE_NAME, propOrder = {
        OlePatronLostBarcodeDefinition.Elements.OLE_PTRN_LOST_BAR_ID,
        OlePatronLostBarcodeDefinition.Elements.OLE_PTRN_ID,
        OlePatronLostBarcodeDefinition.Elements.OLE_PTRN_LOST_BAR_EFF_DT,
        OlePatronLostBarcodeDefinition.Elements.OLE_PTRN_LOST_BAR_NUM,
        //OlePatronDefinition.Elements.OLE_BORROWER_TYPE,
        //OlePatronDefinition.Elements.ENTITY,

        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OlePatronLostBarcodeDefinition extends AbstractDataTransferObject implements OlePatronLostBarcodeContract {

    private static final long serialVersionUID = 1L;


    @XmlElement(name = Elements.OLE_PTRN_LOST_BAR_ID, required = false)
    private final String olePatronLostBarcodeId;

    @XmlElement(name = Elements.OLE_PTRN_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.OLE_PTRN_LOST_BAR_EFF_DT, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date invalidOrLostBarcodeEffDate;

    @XmlElement(name = Elements.OLE_PTRN_LOST_BAR_NUM, required = false)
    private final String invalidOrLostBarcodeNumber;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;


    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;


    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronLostBarcodeDefinition() {
        this.olePatronLostBarcodeId = null;
        this.olePatronId = null;
        this.invalidOrLostBarcodeNumber = null;
        this.invalidOrLostBarcodeEffDate = null;
        this.versionNumber = null;

        this.objectId = null;
    }


    private OlePatronLostBarcodeDefinition(Builder builder) {
        this.olePatronLostBarcodeId = builder.getOlePatronLostBarcodeId();
        this.olePatronId = builder.getOlePatronId();
        this.invalidOrLostBarcodeEffDate = builder.getInvalidOrLostBarcodeEffDate();
        this.invalidOrLostBarcodeNumber = builder.getInvalidOrLostBarcodeNumber();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    public String getOlePatronLostBarcodeId() {
        return this.olePatronLostBarcodeId;
    }


    public String getOlePatronId() {
        return this.olePatronId;
    }


    public Date getInvalidOrLostBarcodeEffDate() {
        return this.invalidOrLostBarcodeEffDate;
    }

    public String getInvalidOrLostBarcodeNumber() {
        return this.invalidOrLostBarcodeNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getId() {
        return this.olePatronLostBarcodeId;
    }


    //@Override
    /*public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }*/

    public static class Builder
            implements Serializable, ModelBuilder, OlePatronLostBarcodeContract {
        private String olePatronLostBarcodeId;
        private String olePatronId;
        private Date invalidOrLostBarcodeEffDate;
        private String invalidOrLostBarcodeNumber;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePatronLostBarcodeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOlePatronLostBarcodeId() != null) {
                builder.setOlePatronLostBarcodeId(contract.getOlePatronLostBarcodeId());
            }
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }
            if (contract.getInvalidOrLostBarcodeNumber() != null) {
                builder.setInvalidOrLostBarcodeNumber(contract.getInvalidOrLostBarcodeNumber());
            }
            if (contract.getInvalidOrLostBarcodeEffDate() != null) {
                builder.setInvalidOrLostBarcodeEffDate(new Date(contract.getInvalidOrLostBarcodeEffDate().getTime()));
            }

            builder.setObjectId(contract.getObjectId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setId(contract.getId());
            return builder;
        }


        public OlePatronLostBarcodeDefinition build() {
            return new OlePatronLostBarcodeDefinition(this);
        }

        public String getOlePatronLostBarcodeId() {
            return this.olePatronLostBarcodeId;
        }

        public void setOlePatronLostBarcodeId(String olePatronLostBarcodeId) {
            this.olePatronLostBarcodeId = olePatronLostBarcodeId;
        }

        public String getOlePatronId() {
            return this.olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        public Date getInvalidOrLostBarcodeEffDate() {
            return this.invalidOrLostBarcodeEffDate;
        }

        public void setInvalidOrLostBarcodeEffDate(Date invalidOrLostBarcodeEffDate) {
            this.invalidOrLostBarcodeEffDate = invalidOrLostBarcodeEffDate;
        }

        public String getInvalidOrLostBarcodeNumber() {
            return this.invalidOrLostBarcodeNumber;
        }

        public void setInvalidOrLostBarcodeNumber(String invalidOrLostBarcodeNumber) {
            this.invalidOrLostBarcodeNumber = invalidOrLostBarcodeNumber;
        }

        @Override
        public String getId() {
            return this.olePatronLostBarcodeId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.olePatronLostBarcodeId = id;
        }

        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "olePatronLostBarcode";
        final static String TYPE_NAME = "OlePatronLostBarcodeType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_PTRN_LOST_BAR_ID = "olePatronLostBarcodeId";
        final static String OLE_PTRN_ID = "olePatronId";
        final static String OLE_PTRN_LOST_BAR_EFF_DT = "invalidOrLostBarcodeEffDate";
        final static String OLE_PTRN_LOST_BAR_NUM = "invalidOrLostBarcodeNumber";


    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
