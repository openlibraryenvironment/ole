package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.kuali.ole.docstore.model.xmlpojo.newInst package.
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

    private final static QName _InstanceCollection_QNAME = new QName("http://ole.kuali.org/standards/ole-instance", "instanceCollection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.kuali.ole.docstore.model.xmlpojo.newInst
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Item }
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link LocationLevel }
     */
    public LocationLevel createLocationLevel() {
        return new LocationLevel();
    }

    /**
     * Create an instance of {@link Instance }
     */
    public Instance createInstance() {
        return new Instance();
    }

    /**
     * Create an instance of {@link Items }
     */
    public Items createItems() {
        return new Items();
    }

    /**
     * Create an instance of {@link StatisticalSearchingCode }
     */
    public StatisticalSearchingCode createStatisticalSearchingCode() {
        return new StatisticalSearchingCode();
    }

    /**
     * Create an instance of {@link InstanceCollection }
     */
    public InstanceCollection createInstanceCollection() {
        return new InstanceCollection();
    }

    /**
     * Create an instance of {@link Location }
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link FormerIdentifier }
     */
    public FormerIdentifier createFormerIdentifier() {
        return new FormerIdentifier();
    }

    /**
     * Create an instance of {@link ExtentOfOwnership }
     */
    public ExtentOfOwnership createExtentOfOwnership() {
        return new ExtentOfOwnership();
    }

    /**
     * Create an instance of {@link AccessInformation }
     */
    public AccessInformation createAccessInformation() {
        return new AccessInformation();
    }

    /**
     * Create an instance of {@link SourceHoldings }
     */
    public SourceHoldings createSourceHoldings() {
        return new SourceHoldings();
    }

    /**
     * Create an instance of {@link ShelvingScheme }
     */
    public ShelvingScheme createShelvingScheme() {
        return new ShelvingScheme();
    }

    /**
     * Create an instance of {@link ShelvingOrder }
     */
    public ShelvingOrder createShelvingOrder() {
        return new ShelvingOrder();
    }

    /**
     * Create an instance of {@link OleHoldings }
     */
    public OleHoldings createOleHoldings() {
        return new OleHoldings();
    }

    /**
     * Create an instance of {@link Uri }
     */
    public Uri createUri() {
        return new Uri();
    }

    /**
     * Create an instance of {@link TypeOrSource }
     */
    public TypeOrSource createTypeOrSource() {
        return new TypeOrSource();
    }

    /**
     * Create an instance of {@link Identifier }
     */
    public Identifier createIdentifier() {
        return new Identifier();
    }

    /**
     * Create an instance of {@link CallNumber }
     */
    public CallNumber createCallNumber() {
        return new CallNumber();
    }

    /**
     * Create an instance of {@link ItemType }
     */
    public ItemType createItemType() {
        return new ItemType();
    }

    /**
     * Create an instance of {@link Extension }
     */
    public Extension createExtension() {
        return new Extension();
    }

    /**
     * Create an instance of {@link Note }
     */
    public Note createNote() {
        return new Note();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstanceCollection }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ole.kuali.org/standards/ole-instance", name = "instanceCollection")
    public JAXBElement<InstanceCollection> createInstanceCollection(InstanceCollection value) {
        return new JAXBElement<InstanceCollection>(_InstanceCollection_QNAME, InstanceCollection.class, null, value);
    }

}
