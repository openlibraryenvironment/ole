/*
 * Copyright 2010 The Regents of the University of California.
 */
package org.kuali.ole.sys.web.dddumper;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kns.web.struts.form.KualiForm;

public class DataDictionaryDumperDetailForm extends KualiForm {
	
	private List<DataDictionaryDumperSection> sections = new ArrayList<DataDictionaryDumperSection>();
	protected String title;

	public List<DataDictionaryDumperSection> getSections() {
		return sections;
	}

	public void setSections(List<DataDictionaryDumperSection> sections) {
		this.sections = sections;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
