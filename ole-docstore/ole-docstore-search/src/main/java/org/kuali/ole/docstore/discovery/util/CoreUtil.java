//package org.kuali.ole.docstore.discovery.util;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created by IntelliJ IDEA.
// * User: ND6967
// * Date: 2/3/12
// * Time: 4:02 PM
// * To change this template use File | Settings | File Templates.
// */
//public class CoreUtil {
//     static String publicationDateRegex="[0-9]{4}";
//    public static String validatePublicationDateWithRegex(String publicationDate){
//
//        Pattern pattern= Pattern.compile(publicationDateRegex);
//        Matcher matcher=pattern.matcher(publicationDate);
//        if(matcher.find()){
//            return matcher.group(0);
//        }
//        else
//            return "";
//
//
//    }
//
//}
