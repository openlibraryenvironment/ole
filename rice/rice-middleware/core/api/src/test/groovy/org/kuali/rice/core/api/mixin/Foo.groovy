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



package org.kuali.rice.core.api.mixin

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

@XmlRootElement(name = Foo.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
@XmlType(name = Foo.Constants.TYPE_NAME, propOrder = [
    Foo.Elements.BAR,
])
//@RemoteableModelObject
class Foo extends Bleh {

    def String bar = "bar"

    private Foo(String s) { super(s) }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "fooKey";
		final static String TYPE_NAME = "FooType";
	}

	/**
	 * A private class which exposes constants which define the XML element names to use
	 * when this object is marshalled to XML.
	 */
	static class Elements {
		final static String BAR = "bar";
	}

    public static void main(String...s) {
        Foo.getDeclaredFields().each {
            if (it.getName().equals("_futureElements")) {
                println(it)
                println(it.annotations)
            }
        }

        Foo.getDeclaredAnnotations().each {
            if (it.annotationType().equals(XmlType.class)) {
                println(it)
            }
        }

        Foo.getDeclaredConstructors().each {
            if (it.getParameterTypes().length == 0) {
                println(it)
            }
        }
        //does it work?
        println(new Foo())
    }
}

