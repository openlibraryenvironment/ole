package org.kuali.ole.deliver.api;

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
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OlePatronNotesDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OlePatronNotesDefinition.Constants.TYPE_NAME, propOrder = {
        OlePatronNotesDefinition.Elements.NOTE_ID,
        OlePatronNotesDefinition.Elements.PATRON_ID,
        // OlePatronNotesDefinition.Elements.NOTE_TYPE_ID,
        OlePatronNotesDefinition.Elements.NOTE_TEXT,
        OlePatronNotesDefinition.Elements.NOTE_TYPE,
        OlePatronNotesDefinition.Elements.ACTIVE,
        //OlePatronNotesDefinition.Elements.PATRON,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OlePatronNotesDefinition extends AbstractDataTransferObject implements OlePatronNotesContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.NOTE_ID, required = false)
    private final String patronNoteId;
    @XmlElement(name = Elements.PATRON_ID, required = false)
    private final String olePatronId;
    //@XmlElement(name = Elements.NOTE_TYPE_ID, required = false)
    private final String patronNoteTypeId;
    @XmlElement(name = Elements.NOTE_TEXT, required = false)
    private final String patronNoteText;
    @XmlElement(name = Elements.NOTE_TYPE, required = false)
    private final OlePatronNoteTypeDefinition olePatronNoteType;
    //@XmlElement(name = Elements.PATRON, required = false)
    //private final OlePatronDefinition olePatron;

    @XmlElement(name = CoreConstants.CommonElements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;


    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronNotesDefinition() {
        patronNoteId = null;
        olePatronId = null;
        patronNoteTypeId = null;
        patronNoteText = null;
        olePatronNoteType = null;
        //olePatron = null;
        versionNumber = null;
        active = false;
        objectId = null;
    }

    public OlePatronNotesDefinition(Builder builder) {
        patronNoteId = builder.getPatronNoteId();
        olePatronId = builder.getOlePatronId();
        patronNoteTypeId = builder.getOlePatronNoteType().getPatronNoteTypeId();
        patronNoteText = builder.getPatronNoteText();
        olePatronNoteType = builder.getOlePatronNoteType().build();
        //olePatron = builder.getOlePatron().build();
        versionNumber = builder.getVersionNumber();
        active = builder.isActive();
        objectId = builder.getObjectId();
    }

    public static class Builder implements OlePatronNotesContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = 1L;
        public String patronNoteId;
        public String olePatronId;
        public String patronNoteTypeId;
        public String patronNoteText;
        public OlePatronNoteTypeDefinition.Builder olePatronNoteType;
        public OlePatronDefinition.Builder olePatron;
        public Long versionNumber;
        public boolean active;
        public String objectId;

        private Builder(String patronNoteId, String olePatronId,
                        String patronNoteText,
                        OlePatronNoteTypeDefinition.Builder olePatronNoteType) {
            //OlePatronDefinition.Builder olePatron) {

            setOlePatronId(olePatronId);
            setPatronNoteText(patronNoteText);
            setOlePatronNoteType(olePatronNoteType);
            setPatronNoteTypeId(olePatronNoteType.getId());
            setPatronNoteId(patronNoteId);
//            setOlePatron(olePatron);
//            setOlePatron(olePatron);

        }

        public static Builder create(String patronNoteId, String olePatronId,
                                     String patronNoteText,
                                     OlePatronNoteTypeDefinition.Builder olePatronNoteType) {
            //OlePatronDefinition.Builder olePatron) {
            return new Builder(patronNoteId, olePatronId, patronNoteText, olePatronNoteType);
        }

        public static Builder create(OlePatronNotesContract patronNotesDefinition) {
            Builder builder = Builder.create(patronNotesDefinition.getPatronNoteId(),
                    patronNotesDefinition.getOlePatronId(),
                    patronNotesDefinition.getPatronNoteText(),
                    OlePatronNoteTypeDefinition.Builder.create(patronNotesDefinition.getOlePatronNoteType()));
            //OlePatronDefinition.Builder.create(patronNotesDefinition.getOlePatron()));
            builder.setVersionNumber(patronNotesDefinition.getVersionNumber());
            builder.setActive(patronNotesDefinition.isActive());
            builder.setObjectId(patronNotesDefinition.getObjectId());
            return builder;

        }

        @Override
        public String getPatronNoteId() {
            return this.patronNoteId;
        }

        @Override
        public String getOlePatronId() {
            return this.olePatronId;
        }

        @Override
        public String getPatronNoteText() {
            return this.patronNoteText;
        }

        @Override
        public OlePatronNoteTypeDefinition.Builder getOlePatronNoteType() {
            return this.olePatronNoteType;
        }


        public OlePatronDefinition.Builder getOlePatron() {
            return this.olePatron;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getId() {
            return this.patronNoteId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setPatronNoteId(String patronNoteId) {
            this.patronNoteId = patronNoteId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        public void setPatronNoteTypeId(String patronNoteTypeId) {
            this.patronNoteTypeId = patronNoteTypeId;
        }

        public void setPatronNoteText(String patronNoteText) {
            this.patronNoteText = patronNoteText;
        }

        public void setOlePatronNoteType(OlePatronNoteTypeDefinition.Builder olePatronNoteType) {
            this.olePatronNoteType = olePatronNoteType;
        }

        public void setOlePatron(OlePatronDefinition.Builder olePatron) {
            this.olePatron = olePatron;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public OlePatronNotesDefinition build() {
            return new OlePatronNotesDefinition(this);
        }
    }

    @Override
    public String getPatronNoteId() {
        return this.patronNoteId;
    }

    @Override
    public String getOlePatronId() {
        return this.olePatronId;
    }

    @Override
    public String getPatronNoteText() {
        return this.patronNoteText;
    }

    @Override
    public OlePatronNoteTypeDefinition getOlePatronNoteType() {
        return this.olePatronNoteType;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public String getId() {
        return this.patronNoteId;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    /*public OlePatronDefinition getOlePatron() {
        return this.olePatron;
    }*/
    static class Constants {
        public static final String ROOT_ELEMENT_NAME = "olePatronNotesDefinition";
        public static final String TYPE_NAME = "olePatronNotesDefinitionType";
    }

    static class Elements {
        public static final String NOTE_ID = "patronNoteId";
        public static final String PATRON_ID = "olePatronId";
        public static final String NOTE_TYPE_ID = "patronNoteTypeId";
        public static final String NOTE_TEXT = "patronNoteText";
        public static final String NOTE_TYPE = "olePatronNoteType";
        public static final String PATRON = "olePatron";
        public static final String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
