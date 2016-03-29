
package org.kuali.ole.gobi.datobjects.marc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.kuali.ole.gobi.datobjects.marc package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Record_QNAME = new QName("", "record");
    private final static QName _Collection_QNAME = new QName("", "collection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.kuali.ole.gobi.datobjects.marc
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RecordType }
     */
    public RecordType createRecordType() {
        return new RecordType();
    }

    /**
     * Create an instance of {@link CollectionType }
     */
    public CollectionType createCollectionType() {
        return new CollectionType();
    }

    /**
     * Create an instance of {@link SubfieldatafieldType }
     */
    public SubfieldatafieldType createSubfieldatafieldType() {
        return new SubfieldatafieldType();
    }

    /**
     * Create an instance of {@link DataFieldType }
     */
    public DataFieldType createDataFieldType() {
        return new DataFieldType();
    }

    /**
     * Create an instance of {@link LeaderFieldType }
     */
    public LeaderFieldType createLeaderFieldType() {
        return new LeaderFieldType();
    }

    /**
     * Create an instance of {@link ControlFieldType }
     */
    public ControlFieldType createControlFieldType() {
        return new ControlFieldType();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link RecordType }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "record")
    public JAXBElement<RecordType> createRecord(RecordType value) {
        return new JAXBElement<RecordType>(_Record_QNAME, RecordType.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link CollectionType }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "collection")
    public JAXBElement<CollectionType> createCollection(CollectionType value) {
        return new JAXBElement<CollectionType>(_Collection_QNAME, CollectionType.class, null, value);
    }

}
