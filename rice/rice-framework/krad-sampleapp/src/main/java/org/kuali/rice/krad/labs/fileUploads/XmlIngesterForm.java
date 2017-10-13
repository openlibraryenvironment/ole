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
package org.kuali.rice.krad.labs.fileUploads;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * This form backs the XmlIngester View
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class XmlIngesterForm extends UifFormBase {

	private static final long serialVersionUID = -6874672285597100759L;
	
	private List<MultipartFile> files;
	
	/**
	 * @return the files
	 */
	public List<MultipartFile> getFiles() {
		return this.files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public XmlIngesterForm(){
		files = new ArrayList<MultipartFile>();
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
		files.add(null);
	}

}
