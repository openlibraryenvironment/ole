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
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Languages008 {
    private List<Language> listOfLanguages = new ArrayList<Language>();
    private static Languages008 languagesObj = null;
    protected final Logger LOG = LoggerFactory.getLogger(Places008.class);

    public Languages008() {
        try {
            URL url = this.getClass().getClassLoader().getResource("008languages.xml");
            XStream xStream = new XStream();
            xStream.alias("languages", Languages008.class);
            xStream.alias("language", Language.class);
            xStream.addImplicitCollection(Languages008.class, "listOfLanguages", Language.class);
            xStream.registerConverter(new LanguageConverter());
            Languages008 languages008 = (Languages008) xStream.fromXML(IOUtils.toString((InputStream) url.getContent()));
            this.setListOfLanguages(Collections.unmodifiableList(languages008.getListOfLanguages()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static Languages008 getInstance() {
        if (languagesObj == null) {
            languagesObj = new Languages008();
        }
        return languagesObj;

    }

    /**
     * Method to give language description. If not a valid language gives null.
     *
     * @param code
     * @return
     */
    public List<String> getLanguageDescription(String code) {
        List<String> lList = new ArrayList<>();
        for (Language language : listOfLanguages) {
            if (code.equals("*")) {
                lList.add(language.getValue());
            } else if (language.getValue().toLowerCase().indexOf(code)>0) {
                lList.add(language.getValue());
            }

            if(language.getCode().startsWith(code)){
                lList.add(language.getValue());
            }
        }
        return lList;
    }

    public List<Language> getListOfLanguages() {
        return listOfLanguages;
    }

    private void setListOfLanguages(List<Language> listOfLanguages) {
        this.listOfLanguages = listOfLanguages;
    }

     /**
     * Class Language.
     *
     * @author premkumarv
     */
    public class Language {
        public Language() {
        }

        public Language(String code) {
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
            if (obj instanceof Language) {
                if (((Language) obj).getCode().equals(code)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private class LanguageConverter
            implements Converter {
        @Override
        public void marshal(Object o, HierarchicalStreamWriter streamWriter, MarshallingContext marshallingContext) {
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader streamReader, UnmarshallingContext unmarshallingContext) {
            Language language = new Language();
            language.setCode(streamReader.getAttribute("code"));
            language.setValue(streamReader.getValue());
            return language;
        }

        @Override
        public boolean canConvert(Class aClass) {
            return aClass.equals(Language.class);
        }
    }

}
