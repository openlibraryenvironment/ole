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
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OleProxyPatronDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleProxyPatronDefinition.Constants.TYPE_NAME, propOrder = {
        OleProxyPatronDefinition.Elements.OLE_PROXY_PATRON_DOC_ID,
        OleProxyPatronDefinition.Elements.OLE_PROXY_PATRON_ID,
        OleProxyPatronDefinition.Elements.OLE_PATRON_ID,
        OleProxyPatronDefinition.Elements.OLE_PROXY_PATRON_EXP_DT,
        OleProxyPatronDefinition.Elements.OLE_PROXY_PATRON_ACT_DT,
        //OleProxyPatronDefinition.Elements.OLE_PATRON_DOC,
        OleProxyPatronDefinition.Elements.ACTIVE_INDICATOR,

        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OleProxyPatronDefinition extends AbstractDataTransferObject implements OleProxyPatronContract {

    private static final long serialVersionUID = 1L;


    @XmlElement(name = Elements.OLE_PROXY_PATRON_DOC_ID, required = false)
    private final String oleProxyPatronDocumentId;

    @XmlElement(name = Elements.OLE_PROXY_PATRON_ID, required = false)
    private final String proxyPatronId;

    @XmlElement(name = Elements.OLE_PATRON_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.ACTIVE_INDICATOR, required = false)
    private final boolean active;

    @XmlElement(name = Elements.OLE_PROXY_PATRON_EXP_DT, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date proxyPatronExpirationDate;

    @XmlElement(name = Elements.OLE_PROXY_PATRON_ACT_DT, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date proxyPatronActivationDate;
/*

    @XmlElement(name = Elements.OLE_PATRON_DOC, required = false)
    private final OlePatronDefinition olePatronDocument;
*/

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleProxyPatronDefinition() {
        this.olePatronId = null;
        this.oleProxyPatronDocumentId = null;
        this.proxyPatronId = null;
        this.active = false;
        this.proxyPatronExpirationDate = null;
        this.proxyPatronActivationDate = null;
        //  this.olePatronDocument = null;
        this.versionNumber = null;
        //this.objectId = null;
    }


    private OleProxyPatronDefinition(Builder builder) {
        this.oleProxyPatronDocumentId = builder.getOleProxyPatronDocumentId();
        this.proxyPatronId = builder.getProxyPatronId();
        this.olePatronId = builder.getOlePatronId();
        this.proxyPatronExpirationDate = builder.getProxyPatronExpirationDate();
        this.proxyPatronActivationDate = builder.getProxyPatronActivationDate();
        this.active = builder.isActive();

        // this.olePatronDocument = builder.getOlePatronDocument().build();

        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }

    @Override
    public String getOlePatronId() {
        return this.olePatronId;
    }

    @Override
    public String getOleProxyPatronDocumentId() {
        return this.oleProxyPatronDocumentId;
    }

    ;

    @Override
    public String getProxyPatronId() {
        return this.proxyPatronId;
    }

    ;

    /* @Override
     public OlePatronDefinition getOlePatronDocument(){
         return this.olePatronDocument;
     };*/
    @Override
    public Date getProxyPatronExpirationDate() {
        return this.proxyPatronExpirationDate;
    }

    ;

    @Override
    public Date getProxyPatronActivationDate() {
        return this.proxyPatronActivationDate;
    }

    ;

    @Override
    public boolean isActive() {
        return this.active;
    }

    ;


    @Override
    public String getId() {
        return this.oleProxyPatronDocumentId;
    }


    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }


    //@Override
    /*public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }*/

    public static class Builder
            implements Serializable, ModelBuilder, OleProxyPatronContract {
        private String oleProxyPatronDocumentId;
        private String proxyPatronId;
        private String olePatronId;
        private Date proxyPatronExpirationDate;
        private Date proxyPatronActivationDate;
        private boolean active;
        //private OlePatronDocument proxyPatron;
        //private OlePatronDefinition.Builder olePatronDocument;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleProxyPatronContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }
            if (contract.getOleProxyPatronDocumentId() != null) {
                builder.setOleProxyPatronDocumentId(contract.getOleProxyPatronDocumentId());
            }
            if (contract.getProxyPatronId() != null) {
                builder.setProxyPatronId(contract.getProxyPatronId());
            }
            if (contract.isActive()) {
                builder.setActive(contract.isActive());
            }
            if (contract.getProxyPatronExpirationDate() != null) {
                builder.setProxyPatronExpirationDate(contract.getProxyPatronExpirationDate());
            }

            if (contract.getProxyPatronActivationDate() != null) {
                builder.setProxyPatronActivationDate(contract.getProxyPatronActivationDate());
            }

           /* if (contract.getOlePatronDocument() != null) {
                builder.setOlePatronDocument(OlePatronDefinition.Builder.create(contract.getOlePatronDocument()));
            }
*/
            builder.setVersionNumber(contract.getVersionNumber());
            /*builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());*/
            builder.setId(contract.getId());
            return builder;
        }


        public OleProxyPatronDefinition build() {
            return new OleProxyPatronDefinition(this);
        }

        public String getOleProxyPatronDocumentId() {
            return oleProxyPatronDocumentId;
        }

        public void setOleProxyPatronDocumentId(String oleProxyPatronDocumentId) {
            this.oleProxyPatronDocumentId = oleProxyPatronDocumentId;
        }

        public String getProxyPatronId() {
            return proxyPatronId;
        }

        public void setProxyPatronId(String proxyPatronId) {
            this.proxyPatronId = proxyPatronId;
        }

        public String getOlePatronId() {
            return olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        public Date getProxyPatronExpirationDate() {
            return proxyPatronExpirationDate;
        }

        public void setProxyPatronExpirationDate(Date proxyPatronExpirationDate) {
            this.proxyPatronExpirationDate = proxyPatronExpirationDate;
        }

        public Date getProxyPatronActivationDate() {
            return proxyPatronActivationDate;
        }

        public void setProxyPatronActivationDate(Date proxyPatronActivationDate) {
            this.proxyPatronActivationDate = proxyPatronActivationDate;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

       /* public OlePatronDefinition.Builder getOlePatronDocument() {
            return olePatronDocument;
        }

        public void setOlePatronDocument(OlePatronDefinition.Builder olePatronDocument) {
            this.olePatronDocument = olePatronDocument;
        }*/

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        /*@Override
        public String getObjectId() {
            return objectId;
        }*/

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        @Override
        public String getId() {
            return this.oleProxyPatronDocumentId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.oleProxyPatronDocumentId = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleProxyPatron";
        final static String TYPE_NAME = "OleProxyPatronType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_PROXY_PATRON_DOC_ID = "oleProxyPatronDocumentId";
        final static String OLE_PROXY_PATRON_ID = "proxyPatronId";
        final static String OLE_PATRON_ID = "olePatronId";
        final static String OLE_PROXY_PATRON_EXP_DT = "proxyPatronExpirationDate";
        final static String OLE_PROXY_PATRON_ACT_DT = "proxyPatronActivationDate";
        //final static String OLE_PATRON_DOC = "olePatronDocument";
        final static String ACTIVE_INDICATOR = "active";
        //final static String ENTITY_ID = "entityId";


    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
