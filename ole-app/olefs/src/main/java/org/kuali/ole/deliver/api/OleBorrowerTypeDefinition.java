package org.kuali.ole.deliver.api;

import org.jdom.IllegalAddException;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/28/12
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = org.kuali.ole.deliver.api.OleBorrowerTypeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OleBorrowerTypeDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OleBorrowerTypeDefinition.Elements.ID,
        org.kuali.ole.deliver.api.OleBorrowerTypeDefinition.Elements.CODE,
        org.kuali.ole.deliver.api.OleBorrowerTypeDefinition.Elements.NAME,
        org.kuali.ole.deliver.api.OleBorrowerTypeDefinition.Elements.DESCRIPTION,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OleBorrowerTypeDefinition extends AbstractDataTransferObject implements org.kuali.ole.deliver.api.OleBorrowerTypeContract {
    @XmlElement(name = Elements.ID, required = false)
    private final String borrowerTypeId;

    @XmlElement(name = Elements.CODE, required = false)
    private final String borrowerTypeCode;

    @XmlElement(name = Elements.NAME, required = false)
    private final String borrowerTypeName;

    @XmlElement(name = Elements.NAME, required = false)
    private final String borrowerTypeDescription;


    /*@XmlElement(name = Elements., required = false)
    private final boolean active;
*/
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleBorrowerTypeDefinition() {
        borrowerTypeId = null;
        borrowerTypeCode = null;
        borrowerTypeName = null;
        borrowerTypeDescription = null;
        versionNumber = null;
    }

    public OleBorrowerTypeDefinition(Builder builder) {
        borrowerTypeId = builder.getBorrowerTypeId();
        borrowerTypeCode = builder.getBorrowerTypeCode();
        borrowerTypeName = builder.getBorrowerTypeName();
        borrowerTypeDescription = builder.getBorrowerTypeDescription();
        versionNumber = builder.getVersionNumber();
    }

    public static class Builder implements org.kuali.ole.deliver.api.OleBorrowerTypeContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = 1L;

        private String borrowerTypeId;
        private String borrowerTypeCode;
        private String borrowerTypeName;
        private String borrowerTypeDescription;
        private Long versionNumber;

        private Builder(String borrowerTypeId,
                        String borrowerTypeCode,
                        String borrowerTypeName,
                        String borrowerTypeDescription) {
            setBorrowerTypeId(borrowerTypeId);
            setBorrowerTypeCode(borrowerTypeCode);
            setBorrowerTypeName(borrowerTypeName);
            setBorrowerTypeDescription(borrowerTypeDescription);
        }

        public static Builder create(String borrowerTypeId,
                                     String borrowerTypeCode,
                                     String borrowerTypeName,
                                     String borrowerTypeDescription) {
            return new Builder(borrowerTypeId, borrowerTypeCode, borrowerTypeName, borrowerTypeDescription);
        }

        public static Builder create(OleBorrowerTypeContract borrowerType) {
            if (borrowerType == null) throw new IllegalAddException("BorrowerType may not be null");
            Builder builder = create(borrowerType.getBorrowerTypeId(),
                    borrowerType.getBorrowerTypeCode(), borrowerType.getBorrowerTypeName(),
                    borrowerType.getBorrowerTypeDescription());
            builder.setVersionNumber(borrowerType.getVersionNumber());
            return builder;
        }

        public void setBorrowerTypeId(String borrowerTypeId) {
            this.borrowerTypeId = borrowerTypeId;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setBorrowerTypeDescription(String borrowerTypeDescription) {
            this.borrowerTypeDescription = borrowerTypeDescription;
        }

        public void setBorrowerTypeName(String borrowerTypeName) {
            this.borrowerTypeName = borrowerTypeName;
        }

        public void setBorrowerTypeCode(String borrowerTypeCode) {
            this.borrowerTypeCode = borrowerTypeCode;
        }

        @Override
        public String getId() {
            return this.getBorrowerTypeId();
        }

        @Override
        public org.kuali.ole.deliver.api.OleBorrowerTypeDefinition build() {
            return new org.kuali.ole.deliver.api.OleBorrowerTypeDefinition(this);
        }

        @Override
        public String getBorrowerTypeId() {
            return this.borrowerTypeId;
        }

        @Override
        public String getBorrowerTypeCode() {
            return this.borrowerTypeCode;
        }

        @Override
        public String getBorrowerTypeDescription() {
            return this.borrowerTypeDescription;
        }

        @Override
        public String getBorrowerTypeName() {
            return this.borrowerTypeName;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }
    }

    @Override
    public String getBorrowerTypeId() {
        return this.borrowerTypeId;
    }

    @Override
    public String getBorrowerTypeCode() {
        return this.borrowerTypeCode;
    }

    @Override
    public String getBorrowerTypeDescription() {
        return this.borrowerTypeDescription;
    }

    @Override
    public String getBorrowerTypeName() {
        return this.borrowerTypeName;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getId() {
        return this.getBorrowerTypeId();
    }


    static class Constants {
        public static final String ROOT_ELEMENT_NAME = "OleBorrowerTypeDefinition";
        public static final String TYPE_NAME = "OleBorrowerTypeDefinitionType";
    }

    static class Elements {
        public static final String ID = "borrowerTypeId";
        public static final String CODE = "borrowerTypeCode";
        public static final String NAME = "borrowerTypeName";
        public static final String DESCRIPTION = "borrowerTypeDescription";
    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + OlePatronNoteTypeDefinition.Constants.TYPE_NAME;
    }
}
