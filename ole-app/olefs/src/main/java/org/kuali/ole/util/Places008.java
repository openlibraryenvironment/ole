package org.kuali.ole.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:premkumarv
 * Date: 9/18/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Places008 {
    private List<Place> listOfPlaces = new ArrayList<Place>();
    private static Places008 placesObj = null;
    protected final Logger LOG = LoggerFactory.getLogger(Places008.class);

  public Places008() {
        try {
            URL url = this.getClass().getClassLoader().getResource("places008.xml");
            XStream xStream = new XStream();
            xStream.alias("places", Places008.class);
            xStream.alias("place", Place.class);
            xStream.addImplicitCollection(Places008.class, "listOfPlaces", Place.class);
            xStream.registerConverter(new PlaceConverter());
            Places008 places008 = (Places008) xStream.fromXML(IOUtils.toString((InputStream) url.getContent()));
            this.setListOfPlaces(Collections.unmodifiableList(places008.getListOfPlaces()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static Places008 getInstance() {
            if (placesObj == null) {
                placesObj = new Places008();
            }
            return placesObj;

        }


    public Place getPlace() {
        int indx = getListOfPlaces().indexOf(new Place());
        if (indx != -1) {
            return this.getListOfPlaces().get(indx);
        } else {
            return null;
        }
    }

    /**
     * Method to give language description. If not a valid language gives null.
     *
     * @param code
     * @return
     */
    public List<String> getPlaceDescription(String code) {
        List<String> pList = new ArrayList<>();
        for (Place place : listOfPlaces) {
            if (code.equals("*")) {
                pList.add(place.getValue());
            } else if (place.getValue().toLowerCase().indexOf(code)>0) {
                pList.add(place.getValue());
            }
            if(place.getCode().startsWith(code)){
                pList.add(place.getValue());
            }
        }
        return pList;
    }

    public List<Place> getListOfPlaces() {
        return listOfPlaces;
    }

    private void setListOfPlaces(List<Place> listOfPlaces) {
        this.listOfPlaces = listOfPlaces;
    }

    private void addLanguage(Place place) {
        this.listOfPlaces.add(place);
    }

    /**
     * Class Language.
     *
     * @author premkumarv
     */
    public class Place {
        public Place() {
        }

        public Place(String code) {
            this.code = code;
        }

        private String code = null;
        private String value = null;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Place) {
                if (((Place) obj).getCode().equals(code)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private class PlaceConverter
            implements Converter {
        @Override
        public void marshal(Object o, HierarchicalStreamWriter streamWriter, MarshallingContext marshallingContext) {
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader streamReader, UnmarshallingContext unmarshallingContext) {
            Place place = new Place();
            place.setCode(streamReader.getAttribute("code"));
            place.setValue(streamReader.getValue());
            return place;
        }

        @Override
        public boolean canConvert(Class aClass) {
            return aClass.equals(Place.class);
        }
    }

}
