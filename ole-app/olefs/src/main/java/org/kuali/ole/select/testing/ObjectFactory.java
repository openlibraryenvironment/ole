
package org.kuali.ole.select.testing;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.kuali.ole.select.testing package.
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

    private final static QName _CreatePreOrderForForm_QNAME = new QName("http://service.select.ole.kuali.org/", "createPreOrderForForm");
    private final static QName _CreatePreOrderForFormResponse_QNAME = new QName("http://service.select.ole.kuali.org/", "createPreOrderForFormResponse");
    private final static QName _CreatePreOrderForCitation_QNAME = new QName("http://service.select.ole.kuali.org/", "createPreOrderForCitation");
    private final static QName _CreatePreOrderForCitationResponse_QNAME = new QName("http://service.select.ole.kuali.org/", "createPreOrderForCitationResponse");
    private final static QName _CreatePreOrderForOpenURL_QNAME = new QName("http://service.select.ole.kuali.org/", "createPreOrderForOpenURL");
    private final static QName _CreatePreOrderForOpenURLResponse_QNAME = new QName("http://service.select.ole.kuali.org/", "createPreOrderForOpenURLResponse");
    private final static QName _Exception_QNAME = new QName("http://service.select.ole.kuali.org/", "WSException");


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.kuali.ole.select.testing
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreatePreOrderForForm }
     */
    public CreatePreOrderForForm createCreatePreOrderForForm() {
        return new CreatePreOrderForForm();
    }

    /**
     * Create an instance of {@link CreatePreOrderForFormResponse }
     */
    public CreatePreOrderForFormResponse createCreatePreOrderForFormResponse() {
        return new CreatePreOrderForFormResponse();
    }

    /**
     * Create an instance of {@link CreatePreOrderForCitation }
     */
    public CreatePreOrderForCitation createCreatePreOrderForCitation() {
        return new CreatePreOrderForCitation();
    }

    /**
     * Create an instance of {@link CreatePreOrderForCitationResponse }
     */
    public CreatePreOrderForCitationResponse createCreatePreOrderForCitationResponse() {
        return new CreatePreOrderForCitationResponse();
    }

    /**
     * Create an instance of {@link CreatePreOrderForOpenURLResponse }
     */
    public CreatePreOrderForOpenURLResponse createCreatePreOrderForOpenURLResponse() {
        return new CreatePreOrderForOpenURLResponse();
    }

    /**
     * Create an instance of {@link CreatePreOrderForOpenURL }
     */
    public CreatePreOrderForOpenURL createCreatePreOrderForOpenURL() {
        return new CreatePreOrderForOpenURL();
    }

    /**
     * Create an instance of {@link WSException }
     */
    public WSException createException() {
        return new WSException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePreOrderForForm }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "createPreOrderForForm")
    public JAXBElement<CreatePreOrderForForm> createCreatePreOrderForForm(CreatePreOrderForForm value) {
        return new JAXBElement<CreatePreOrderForForm>(_CreatePreOrderForForm_QNAME, CreatePreOrderForForm.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePreOrderForFormResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "createPreOrderForFormResponse")
    public JAXBElement<CreatePreOrderForFormResponse> createCreatePreOrderForFormResponse(CreatePreOrderForFormResponse value) {
        return new JAXBElement<CreatePreOrderForFormResponse>(_CreatePreOrderForFormResponse_QNAME, CreatePreOrderForFormResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePreOrderForCitation }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "createPreOrderForCitation")
    public JAXBElement<CreatePreOrderForCitation> createCreatePreOrderForCitation(CreatePreOrderForCitation value) {
        return new JAXBElement<CreatePreOrderForCitation>(_CreatePreOrderForCitation_QNAME, CreatePreOrderForCitation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePreOrderForCitationResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "createPreOrderForCitationResponse")
    public JAXBElement<CreatePreOrderForCitationResponse> createCreatePreOrderForCitationResponse(CreatePreOrderForCitationResponse value) {
        return new JAXBElement<CreatePreOrderForCitationResponse>(_CreatePreOrderForCitationResponse_QNAME, CreatePreOrderForCitationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePreOrderForOpenURL }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "createPreOrderForOpenURL")
    public JAXBElement<CreatePreOrderForOpenURL> createCreatePreOrderForOpenURL(CreatePreOrderForOpenURL value) {
        return new JAXBElement<CreatePreOrderForOpenURL>(_CreatePreOrderForOpenURL_QNAME, CreatePreOrderForOpenURL.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreatePreOrderForOpenURLResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "createPreOrderForOpenURLResponse")
    public JAXBElement<CreatePreOrderForOpenURLResponse> createCreatePreOrderForOpenURLResponse(CreatePreOrderForOpenURLResponse value) {
        return new JAXBElement<CreatePreOrderForOpenURLResponse>(_CreatePreOrderForOpenURLResponse_QNAME, CreatePreOrderForOpenURLResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WSException }{@code >}}
     */
    @XmlElementDecl(namespace = "http://service.select.ole.kuali.org/", name = "WSException")
    public JAXBElement<WSException> createException(WSException value) {
        return new JAXBElement<WSException>(_Exception_QNAME, WSException.class, null, value);
    }


}
