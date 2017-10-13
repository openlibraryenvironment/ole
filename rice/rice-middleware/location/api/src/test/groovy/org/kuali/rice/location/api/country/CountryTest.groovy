/**
 * Copyright 2005-2014 The Kuali Foundation
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

package org.kuali.rice.location.api.country

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import junit.framework.Assert
import org.junit.Test

/**
 * Exercises the immutable Country class, including XML (un)marshalling
 */
class CountryTest {

  private static final String CODE = "US"
  private static final String ALT_CODE = "USA"
  private static final String NAME = "United States"

  private static final String XML = """
      <country xmlns="http://rice.kuali.org/location/v2_0">
        <code>${CODE}</code>
        <name>${NAME}</name>
        <alternateCode>${ALT_CODE}</alternateCode>
        <restricted>false</restricted>
        <active>true</active>
        <versionNumber>1</versionNumber>
    </country>
  """


  private final shouldFail = new GroovyTestCase().&shouldFail

  @Test
  void test_create_only_required_two_arg_create_factory_method() {
    Country.Builder.create(Country.Builder.create(CODE, NAME)).build();
  }

  @Test
  void test_create_only_required() {
    Country.Builder.create(Country.Builder.create(CODE, null, NAME, false, true)).build();
  }

  @Test
  public void testCountryBuilderPassedInParams() {
    //No assertions, just test whether the Builder gives us a Country object
    Country.Builder.create(CODE, null, NAME, false, true).build()
  }

  @Test
  public void testCountryBuilderPassedInCountryContract() {
    //No assertions, just test whether the Builder gives us a Country object
    Country country = Country.Builder.create(new CountryContract() {
      String getCode() { CountryTest.CODE }
      String getAlternateCode() { CountryTest.ALT_CODE }
      String getName() { CountryTest.NAME }
      boolean isActive() { true }
      boolean isRestricted() { false }
      Long getVersionNumber() { 1 }
    }).build()
  }

  @Test
  public void testCountryBuilderNullCountryCode() {
    shouldFail(IllegalArgumentException.class) {
      Country.Builder.create(null, null, NAME, false, true)
    }
  }

  @Test
  public void testCountryBuilderEmptyCountryCode() {
    shouldFail(IllegalArgumentException.class) {
      Country.Builder.create("  ", null, NAME, false, true)
    }
  }

  @Test
  public void testXmlMarshalingAndUnMarshalling() {
    JAXBContext jc = JAXBContext.newInstance(Country.class)
    Marshaller marshaller = jc.createMarshaller()
    StringWriter sw = new StringWriter()

	Country.Builder builder = Country.Builder.create(CODE, ALT_CODE, NAME, false, true)
	builder.setVersionNumber(1)
    Country country = builder.build()
    marshaller.marshal(country, sw)
    String xml = sw.toString()

    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Object actual = unmarshaller.unmarshal(new StringReader(xml))
    Object expected = unmarshaller.unmarshal(new StringReader(XML))
    Assert.assertEquals(expected, actual)
  }

  @Test
  public void testXmlUnmarshal() {
    JAXBContext jc = JAXBContext.newInstance(Country.class)
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Country country = (Country) unmarshaller.unmarshal(new StringReader(XML))
    Assert.assertEquals(CODE, country.code)
    Assert.assertEquals(ALT_CODE, country.alternateCode)
    Assert.assertEquals(NAME, country.name)
  }
}
