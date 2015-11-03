
package newpackage;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the newpackage package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _HelloResponse_QNAME = new QName("http://learning/", "helloResponse");
    private final static QName _OperationResponse_QNAME = new QName("http://learning/", "operationResponse");
    private final static QName _Operation_QNAME = new QName("http://learning/", "operation");
    private final static QName _Hello_QNAME = new QName("http://learning/", "hello");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: newpackage
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Hello }
     * 
     */
    public Hello createHello() {
        return new Hello();
    }

    /**
     * Create an instance of {@link Operation }
     * 
     */
    public Operation createOperation() {
        return new Operation();
    }

    /**
     * Create an instance of {@link HelloResponse }
     * 
     */
    public HelloResponse createHelloResponse() {
        return new HelloResponse();
    }

    /**
     * Create an instance of {@link OperationResponse }
     * 
     */
    public OperationResponse createOperationResponse() {
        return new OperationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://learning/", name = "helloResponse")
    public JAXBElement<HelloResponse> createHelloResponse(HelloResponse value) {
        return new JAXBElement<HelloResponse>(_HelloResponse_QNAME, HelloResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://learning/", name = "operationResponse")
    public JAXBElement<OperationResponse> createOperationResponse(OperationResponse value) {
        return new JAXBElement<OperationResponse>(_OperationResponse_QNAME, OperationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Operation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://learning/", name = "operation")
    public JAXBElement<Operation> createOperation(Operation value) {
        return new JAXBElement<Operation>(_Operation_QNAME, Operation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Hello }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://learning/", name = "hello")
    public JAXBElement<Hello> createHello(Hello value) {
        return new JAXBElement<Hello>(_Hello_QNAME, Hello.class, null, value);
    }

}
