//package org.kuali.ole.docstore.model.xstream.security.patron.oleml;
//
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.QNameMap;
//import com.thoughtworks.xstream.io.xml.StaxDriver;
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.Patron;
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.PatronGroup;
//
//import javax.xml.namespace.QName;
//
///**
// * Created by IntelliJ IDEA.
// * User: Pranitha
// * Date: 3/16/12
// * Time: 12:22 PM
// * To change this template use File | Settings | File Templates.
// */
//public class SecurityPatronOlemlRecordProcessor {
//
//    public PatronGroup fromXML(String patronXML) {
//        System.out.println("patron xml in processor" + patronXML);
//        QNameMap nsm = new QNameMap();
//        nsm.registerMapping(new QName("uri", "ole"), Patron.class);
//        StaxDriver d = new StaxDriver(nsm);
//        XStream xs = new XStream(d);
//         System.out.println("xs" + xs);
//        xs.autodetectAnnotations(true);
//          System.out.println("autodetectAnnotations" );
//        xs.processAnnotations(PatronGroup.class);
//           System.out.println("after process annotation" );
//        Object patronObject = xs.fromXML(patronXML);
//         System.out.println("patronObject" + patronObject.toString() );
//        return (PatronGroup) patronObject;
//    }
//
//      public String toXML(PatronGroup patron) {
//        XStream xs = new XStream();
//        xs.autodetectAnnotations(true);
//        xs.processAnnotations(PatronGroup.class);
//        String xml = xs.toXML(patron);
//        return xml;
//    }
//}
