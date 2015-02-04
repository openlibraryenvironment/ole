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
package org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class OaiDcDoc.
 *
 * @author Rajesh Chowdary K
 */
public class OaiDcDoc {

    public static final String TITLE = "dc:title";
    public static final String CREATOR = "dc:creator";
    public static final String SUBJECT = "dc:subject";
    public static final String DESCRIPTION = "dc:description";
    public static final String PUBLISHER = "dc:publisher";
    public static final String DATE = "dc:date";
    public static final String IDENTIFIER = "dc:identifier";
    public static final String SOURCE = "dc:source";
    public static final String LANGUAGE = "dc:language";
    public static final String TYPE = "dc:type";
    public static final String RIGHTS = "dc:rights";
    public static final String FORMAT = "dc:format";
    public static final String CONTRIBUTOR = "dc:contributor";
    public static final String COVERAGE = "dc:coverage";
    public static final String RELATION = "dc:relation";

    private static final Set<String> repeatableTags = new HashSet<String>();
    private static final Set<String> nonRepeatableTags = new HashSet<String>();

    static {
        nonRepeatableTags.add(DATE);
        nonRepeatableTags.add(DESCRIPTION);
        nonRepeatableTags.add(FORMAT);
        nonRepeatableTags.add(LANGUAGE);
        nonRepeatableTags.add(PUBLISHER);
        nonRepeatableTags.add(RIGHTS);
        nonRepeatableTags.add(SOURCE);
        nonRepeatableTags.add(TITLE);
        nonRepeatableTags.add(TYPE);
        nonRepeatableTags.add(CONTRIBUTOR);
        nonRepeatableTags.add(COVERAGE);
        nonRepeatableTags.add(RELATION);
        repeatableTags.add(CREATOR);
        repeatableTags.add(IDENTIFIER);
        repeatableTags.add(SUBJECT);
    }

    private List<Tag> tags = new ArrayList<Tag>();

    /**
     * Method to add, set or update tag value.
     *
     * @param tagName
     * @param value
     */
    public void put(String tagName, String value) {
        Tag input = new Tag(tagName, value);
        int idx = tags.indexOf(input);
        if (tagName != null && nonRepeatableTags.contains(tagName.toLowerCase())) {
            if (idx > -1) {
                tags.set(idx, input);
            } else {
                tags.add(input);
            }
        } else if (tagName != null && repeatableTags.contains(tagName.toLowerCase())) {
            tags.add(input);
        } else {
            throw new RuntimeException("Unknown Tag: " + tagName);
        }
    }

    /**
     * Method to get Value of a tag.
     *
     * @param tagName
     * @return
     */
    public List<Tag> get(String tagName) {
        List<Tag> values = new ArrayList<Tag>();
        if (tagName != null && nonRepeatableTags.contains(tagName.toLowerCase())) {
            int idx = tags.indexOf(new Tag(tagName));
            if (idx >= 0) {
                values.add(tags.get(idx));
            }

        } else if (tagName != null && repeatableTags.contains(tagName.toLowerCase())) {
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
        return "[OaiDcDoc: " + tags + "]";
    }
}
