package org.kuali.ole.deliver.api;

import org.apache.commons.lang.StringUtils;
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
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OlePatronNoteTypeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OlePatronNoteTypeDefinition.Constants.TYPE_NAME, propOrder = {
        OlePatronNoteTypeDefinition.Elements.ID,
        OlePatronNoteTypeDefinition.Elements.CODE,
        OlePatronNoteTypeDefinition.Elements.NAME,
        CoreConstants.CommonElements.ACTIVE,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OlePatronNoteTypeDefinition extends AbstractDataTransferObject implements OlePatronNoteTypeContract {

    private static final long serialVersionUID = 1L;
    @XmlElement(name = Elements.ID, required = false)
    private final String patronNoteTypeId;

    @XmlElement(name = Elements.CODE, required = false)
    private final String patronNoteTypeCode;

    @XmlElement(name = Elements.NAME, required = false)
    private final String patronNoteTypeName;

    /*@XmlElement(name = Elements., required = false)
    private final boolean active;
*/

    @XmlElement(name = CoreConstants.CommonElements.ACTIVE, required = false)
    private boolean active;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronNoteTypeDefinition() {
        patronNoteTypeId = null;
        patronNoteTypeCode = null;
        patronNoteTypeName = null;
    }

    public OlePatronNoteTypeDefinition(Builder builder) {

        patronNoteTypeId = builder.getPatronNoteTypeId();
        patronNoteTypeCode = builder.getPatronNoteTypeCode();
        patronNoteTypeName = builder.getPatronNoteTypeName();
    }

    public static class Builder implements OlePatronNoteTypeContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = 1L;

        private String patronNoteTypeId;
        private String patronNoteTypeCode;
        private String patronNoteTypeName;
        private Long versionNumber;
        private boolean active;

        private Builder(String patronNoteTypeId, String patronNoteTypeCode,
                        String patronNoteTypeName) {
            setPatronNoteTypeId(patronNoteTypeId);
            setPatronNoteTypeCode(patronNoteTypeCode);
            setPatronNoteTypeName(patronNoteTypeName);
        }

        public static Builder create(String patronNoteTypeId, String patronNoteTypeCode,
                                     String patronNoteTypeName) {
            return new Builder(patronNoteTypeId, patronNoteTypeCode, patronNoteTypeName);

        }

        public static Builder create(OlePatronNoteTypeContract patronNoteType) {
            if (patronNoteType == null) throw new IllegalAddException("PatronNoteType may not be null");
            Builder builder = create(patronNoteType.getPatronNoteTypeId(), patronNoteType.getPatronNoteTypeCode(), patronNoteType.getPatronNoteTypeName());
            builder.setActive(patronNoteType.isActive());
            return builder;
        }

        @Override
        public String getId() {
            return this.patronNoteTypeId;
        }

        public void setPatronNoteTypeId(String patronNoteTypeId) {
            if (patronNoteTypeId != null && StringUtils.isBlank(patronNoteTypeId)) {
                throw new IllegalArgumentException("id must contain non-whitespace chars");
            }
            this.patronNoteTypeId = patronNoteTypeId;
        }


        public void setPatronNoteTypeCode(String patronNoteTypeCode) {
            this.patronNoteTypeCode = patronNoteTypeCode;
        }


        public void setPatronNoteTypeName(String patronNoteTypeName) {
            this.patronNoteTypeName = patronNoteTypeName;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        @Override
        public String getPatronNoteTypeId() {
            return patronNoteTypeId;
        }

        @Override
        public String getPatronNoteTypeCode() {
            return patronNoteTypeCode;
        }

        @Override
        public String getPatronNoteTypeName() {
            return patronNoteTypeName;
        }

        @Override
        public OlePatronNoteTypeDefinition build() {
            return new OlePatronNoteTypeDefinition(this);
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    @Override
    public String getPatronNoteTypeId() {
        return this.patronNoteTypeId;
    }

    @Override
    public String getPatronNoteTypeCode() {
        return this.patronNoteTypeCode;
    }

    @Override
    public String getPatronNoteTypeName() {
        return this.patronNoteTypeName;
    }

    @Override
    public String getId() {
        return this.patronNoteTypeId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    static class Constants {
        public static final String ROOT_ELEMENT_NAME = "olePatronNoteTypeDefinition";
        public static final String TYPE_NAME = "olePatronNoteTypeDefinitionType";
    }

    static class Elements {
        public static final String ID = "patronNoteTypeId";
        public static final String CODE = "patronNoteTypeCode";
        public static final String NAME = "patronNoteTypeName";
    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
