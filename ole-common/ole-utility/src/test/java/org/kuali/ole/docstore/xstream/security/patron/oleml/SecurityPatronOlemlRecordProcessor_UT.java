//package org.kuali.ole.docstore.xstream.security.patron.oleml;
//
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.Patron;
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.PatronGroup;
//import org.kuali.ole.docstore.model.xstream.security.patron.oleml.SecurityPatronOlemlRecordProcessor;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URL;
//
///**
// * Created by IntelliJ IDEA.
// * User: Pranitha
// * Date: 3/16/12
// * Time: 5:56 PM
// * To change this template use File | Settings | File Templates.
// */
//public class SecurityPatronOlemlRecordProcessor_UT  {
//
//
//    public void patronProcessor() throws Exception {
//        try{
//        SecurityPatronOlemlRecordProcessor securityPatronOlemlRecordProcessor = new SecurityPatronOlemlRecordProcessor();
//        URL   resource = getClass().getResource("/org/kuali/ole/patron.xml");
//            File file = new File(resource.toURI());
////           String content = readFile(file).replace("op:","");
//        PatronGroup patronGroup = securityPatronOlemlRecordProcessor.fromXML(readFile(file));
//            System.out.println("ptaron" + patronGroup.getPatron().size());
//            for(Patron patron : patronGroup.getPatron()){
//                System.out.println(patron.getRecordNumber());
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//    private String readFile(File file) throws IOException {
//           BufferedReader reader = new BufferedReader(new FileReader(file));
//           String line = null;
//           StringBuilder stringBuilder = new StringBuilder();
//           String ls = System.getProperty("line.separator");
//           while ((line = reader.readLine()) != null) {
//               stringBuilder.append(line);
//               stringBuilder.append(ls);
//           }
//           return stringBuilder.toString();
//       }
//
//}
