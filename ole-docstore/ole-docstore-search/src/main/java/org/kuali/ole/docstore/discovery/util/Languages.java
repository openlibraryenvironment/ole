/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.docstore.discovery.util;

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

public class Languages {

    private List<Language> languages = new ArrayList<Language>();
    private static Languages soLanguagesISO639_1_cc = null;
    private static Languages soLanguagesISO639_3 = null;
    protected final Logger LOG = LoggerFactory.getLogger(Languages.class);
    public static final String ISO_639_1_CC = "ISO_639_1_CC";
    public static final String ISO_639_3 = "ISO_639_3";

    private Languages(String encoding) {
        try {
            String file = null;
            if (ISO_639_1_CC.equals(encoding)) {
                file = "languages-iso-639-1-cc.xml";
            } else if (ISO_639_3.equals(encoding)) {
                file = "languages-iso-639-3.xml";
            }
            URL url = this.getClass().getClassLoader().getResource(file);
            XStream xStream = new XStream();
            xStream.alias("languages", Languages.class);
            xStream.alias("lang", Language.class);
            xStream.addImplicitCollection(Languages.class, "languages", Language.class);
            xStream.registerConverter(new LanguageConverter());
            Languages langs = (Languages) xStream.fromXML(IOUtils.toString((InputStream) url.getContent()));
            this.setLanguages(Collections.unmodifiableList(langs.getLanguages()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Method to give out the Single Instance of Languages.
     *
     * @param encoding - Specify the encoding type of the languages expected.
     * @return
     */
    public static Languages getInstance(String encoding) {
        if (ISO_639_1_CC.equals(encoding)) {
            if (soLanguagesISO639_1_cc == null) {
                soLanguagesISO639_1_cc = new Languages(encoding);
            }
            return soLanguagesISO639_1_cc;
        } else if (ISO_639_3.equals(encoding)) {
            if (soLanguagesISO639_3 == null) {
                soLanguagesISO639_3 = new Languages(encoding);
            }
            return soLanguagesISO639_3;
        } else {
            return null;
        }
    }

    /**
     * Method to give language. If not a valid language gives null.
     *
     * @param code
     * @return
     */
    public Language getLanguage(String code) {
        int indx = getLanguages().indexOf(new Language(code));
        if (indx != -1) {
            return this.getLanguages().get(indx);
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
    public String getLanguageDescription(String code) {
        int indx = getLanguages().indexOf(new Language(code));
        if (indx != -1) {
            return this.getLanguages().get(indx).getValue();
        } else {
            return null;
        }
    }

    public List<Language> getLanguages() {
        return languages;
    }

    private void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    private void addLanguage(Language language) {
        this.languages.add(language);
    }

    /**
     * Class Language.
     *
     * @author Rajesh Chowdary K
     */
    class Language {
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
