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
package org.kuali.rice.core.api.impex.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream-based XML doc.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StreamXmlDoc extends BaseXmlDoc {
	private byte[] bytes;
	private InputStream stream;

	public StreamXmlDoc(InputStream stream, XmlDocCollection collection) {
		super(collection);
		this.stream = stream;
	}

	public synchronized InputStream getStream() throws IOException {
		// this sucks, but does it suck less than copying the file?
		// or dealing with concurrency issues on a surveilling input stream
		if (bytes == null) {
			bytes = StreamXmlDoc.toByteArray(stream);
		}
		return new ByteArrayInputStream(bytes);
	}

	public String getName() {
		return stream.toString();
	}

	/**
	 * The following code copied from commons-io IOUtils to avoid a core-api dependency on that library.
	 */
	
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	private static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	/**
	 * End code copied from common-io IOUtils
	 */

}
