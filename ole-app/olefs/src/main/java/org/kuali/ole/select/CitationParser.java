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
package org.kuali.ole.select;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class CitationParser {

    private static final String CITATION_PARSER_URL = "http://freecite.library.brown.edu/citations/create";
    private URL citationParserURL;
    private Writer outputStreamWriter;
    private Reader reader;
    private Logger LOG = org.apache.log4j.Logger.getLogger(CitationParser.class);

    public boolean isConnectionAlive() {
        try {
            URL citationParserURL = getURLObject();
            HttpURLConnection urlConnection = (HttpURLConnection) citationParserURL.openConnection();
            urlConnection.setConnectTimeout(1000);
            if (urlConnection != null) {
                urlConnection.getInputStream();
            }
            return urlConnection != null;

        } catch (IOException e) {
            return false;
        }
    }

    public String parse(String object) {
        String parsedCitation = "";

        HttpURLConnection urlConnection = null;
        try {
            URL citationParserURL = getURLObject();
            urlConnection = (HttpURLConnection) citationParserURL.openConnection();
            setUrlConnectionProperties(urlConnection);
            readCitationParmameters(object, urlConnection);
            parsedCitation = readResultsFromCitationParser(urlConnection);
        } catch (FileNotFoundException e) {
            LOG.error("Invalid Citation Parser URL (" + citationParserURL + " ): " + e);
            throw new RuntimeException("Invalid Citation Parser URL (" + citationParserURL + " ): " + e, e);
        } catch (UnknownHostException e) {
            LOG.error("Invalid Citation Parser URL (" + citationParserURL + " ): " + e);
            throw new RuntimeException("Invalid Citation Parser URL (" + citationParserURL + " ): " + e, e);
        } catch (IOException e) {
            if (e.getMessage().indexOf("504") != -1) {
                LOG.error("Connection Timeout: (" + citationParserURL + " ): " + e);
                throw new RuntimeException("Connection Timeout: (" + citationParserURL + " ): " + e, e);
            } else {
                LOG.error("Internal Server Error or Service Unavailable: (" + citationParserURL + " ): " + e);
                throw new RuntimeException("Internal Server Error or Service Unavailable (" + citationParserURL + " ): " + e, e);
            }
        } catch (Exception ex) {
            LOG.error("Connection error:unable to connect " + citationParserURL + " : " + ex);
            throw new RuntimeException("Connection error:unable to connect " + citationParserURL + " : " + ex, ex);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return parsedCitation;
    }

    private URL getURLObject() throws MalformedURLException {
        if (null == citationParserURL) {
            citationParserURL = new URL(CITATION_PARSER_URL);
        }
        return citationParserURL;
    }

    public void setCitationParserURL(URL citationParserURL) {
        this.citationParserURL = citationParserURL;
    }

    private String readResultsFromCitationParser(HttpURLConnection urlConnection) throws Exception {
        InputStream inputStream = urlConnection.getInputStream();
        StringWriter stringWriter = new StringWriter();
        String parsedCitation = "";
        try {
            reader = getInputStreamReader(inputStream);
            pipe(reader, stringWriter);
            if (LOG.isDebugEnabled()) {
                LOG.debug("response--------->" + stringWriter.toString());
            }
            parsedCitation = stringWriter.toString();
            reader.close();
        } catch (IOException e) {
            throw new Exception("IOException while reading response", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return parsedCitation;
    }

    private Reader getInputStreamReader(InputStream inputStream) {
        //if (null == reader) { //Removed if condition because for the first time the object will be set here for the next time object will be in not null state wont set the object, but it will be in closed state, so each and every time this object needs to be initialized here.
        reader = new InputStreamReader(inputStream);
        //}
        return reader;
    }

    private void readCitationParmameters(Object object, HttpURLConnection urlConnection) throws Exception {
        Reader stringReader = new StringReader(object.toString());
        OutputStream outputStream = urlConnection.getOutputStream();
        try {
            outputStreamWriter = getOutputStreamWriter(outputStream);
            pipe(stringReader, outputStreamWriter);
            outputStreamWriter.close();
        } catch (IOException e) {
            throw new Exception("IOException while posting stringReader", e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public void setOutputStreamWriter(Writer outputStreamWriter) {
        this.outputStreamWriter = outputStreamWriter;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    private Writer getOutputStreamWriter(OutputStream outputStream) throws UnsupportedEncodingException {
        //if (null == outputStreamWriter) { //Removed if condition because for the first time the object will be set here for the next time object will be in not null state wont set the object, but it will be in closed state, so each and every time this object needs to be initialized here.
        outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        //}
        return outputStreamWriter;
    }

    private void setUrlConnectionProperties(HttpURLConnection urlConnection) {
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Accept", "text/xml");
        // give it 5 seconds to connect normally - should never take that long.
        urlConnection.setConnectTimeout(5000);
    }

    private void pipe(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();
    }

}
