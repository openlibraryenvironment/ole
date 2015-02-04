package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml package.
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

    private final static QName _InstanceCollection_QNAME = new QName("http://ole.kuali.org/standards/ole-eInstance", "instanceCollection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection }
     */
    public InstanceCollection createInstanceCollection() {
        return new InstanceCollection();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.PerpetualAccess }
     */
    public PerpetualAccess createPerpetualAccess() {
        return new PerpetualAccess();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Platform }
     */
    public Platform createPlatform() {
        return new Platform();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.PerpetualAccesses }
     */
    public PerpetualAccesses createPerpetualAccesses() {
        return new PerpetualAccesses();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.FormerIdentifier }
     */
    public FormerIdentifier createFormerIdentifier() {
        return new FormerIdentifier();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.LocationLevel }
     */
    public LocationLevel createLocationLevel() {
        return new LocationLevel();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Location }
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Invoice }
     */
    public Invoice createInvoice() {
        return new Invoice();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EHoldings }
     */
    public EHoldings createEHoldings() {
        return new EHoldings();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Coverages }
     */
    public Coverages createCoverages() {
        return new Coverages();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Link }
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumberType }
     */
    public CallNumberType createCallNumberType() {
        return new CallNumberType();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ShelvingOrder }
     */
    public ShelvingOrder createShelvingOrder() {
        return new ShelvingOrder();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.AccessInformation }
     */
    public AccessInformation createAccessInformation() {
        return new AccessInformation();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.ExtentOfOwnership }
     */
    public ExtentOfOwnership createExtentOfOwnership() {
        return new ExtentOfOwnership();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Coverage }
     */
    public Coverage createCoverage() {
        return new Coverage();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.CallNumber }
     */
    public CallNumber createCallNumber() {
        return new CallNumber();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.TypeOrSource }
     */
    public TypeOrSource createTypeOrSource() {
        return new TypeOrSource();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance }
     */
    public EInstance createEInstance() {
        return new EInstance();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Identifier }
     */
    public Identifier createIdentifier() {
        return new Identifier();
    }

    /**
     * Create an instance of {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.Note }
     */
    public Note createNote() {
        return new Note();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.InstanceCollection }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ole.kuali.org/standards/ole-eInstance", name = "instanceCollection")
    public JAXBElement<InstanceCollection> createInstanceCollection(InstanceCollection value) {
        return new JAXBElement<InstanceCollection>(_InstanceCollection_QNAME, InstanceCollection.class, null, value);
    }

}
