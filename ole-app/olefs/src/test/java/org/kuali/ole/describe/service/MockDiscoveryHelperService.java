package org.kuali.ole.describe.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockDiscoveryHelperService {

    List<String> isbnList;
    List<String> issnList;
    List<String> oclcList;
    List<String> locationList;

    public MockDiscoveryHelperService(){
        isbnList = new ArrayList<String>();
        oclcList = new ArrayList<String>();
        issnList = new ArrayList<String>();
        locationList = new ArrayList<String>();


        isbnList.add("9600000000000");
        isbnList.add("9700000000000");
        isbnList.add("9800000000000");
        isbnList.add("9780348436357");

        issnList.add("0371-8419");
        issnList.add("0372-8418");
        issnList.add("0373-8417");
        issnList.add("0374-8416");

        oclcList.add("(YBP)100552240");
        oclcList.add("(YBP)100552241");
        oclcList.add("(YBP)100552242");
        oclcList.add("(YBP)100552243");

        locationList.add("Bloomington");
        locationList.add("Indiana");
        locationList.add("Fortwayne");
    }

    public boolean isIsbnExists(String isbn){
        if(isbn!=null && isbnList.contains(isbn.toString())){
            return true;
        }
        return false;
    }

    public boolean isIssnExists(String issn){
        if(issn!=null && issnList.contains(issn.toString())){
            return true;
        }
        return false;
    }

    public boolean isOclcExists(String oclc){
        if(oclc!=null && oclcList.contains(oclc.toString())){
            return true;
        }
        return false;
    }

    public boolean isLocationExists(String location){
        if(location!=null && locationList.contains(location.toString())){
            return true;
        }
        return false;
    }

}
