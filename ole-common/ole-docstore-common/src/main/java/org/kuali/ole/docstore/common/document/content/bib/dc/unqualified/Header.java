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
package org.kuali.ole.docstore.common.document.content.bib.dc.unqualified;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Header.
 *
 * @author Rajesh Chowdary K
 */
public class Header {

    private List<Tag> tags = new ArrayList<Tag>();

    /**
     * Method to add, set or update tag value.
     *
     * @param tagName
     * @param value
     */
    public void put(String tagName, String value) {
        tags.add(new Tag(tagName, value));
    }

    /**
     * Method to get Value of a tag.
     *
     * @param tagName
     * @return
     */
    public List<Tag> get(String tagName) {
        List<Tag> values = new ArrayList<Tag>();
        if (tagName != null) {
            for (Tag tag : tags) {
                if (tag.getName().equalsIgnoreCase(tagName)) {
                    values.add(tag);
                }
            }
        }
        return values;
    }

    /**
     * Method to return all tags
     *
     * @return
     */
    public List<Tag> getAllTags() {
        return tags;
    }

    @Override
    public String toString() {
        return tags.toString();
    }
}
