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
package org.kuali.rice.edl.framework.workflow;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.DeleteEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * PostProcessor responsible for posting events to a url defined in the EDL doc definition.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDocLitePostProcessor extends DefaultPostProcessor {
    private static final Logger LOG = Logger.getLogger(EDocLitePostProcessor.class);
    private static final Timer TIMER = new Timer();
    public static final int SUBMIT_URL_MILLISECONDS_WAIT = 60000;
    public static final String EVENT_TYPE_ACTION_TAKEN = "actionTaken";
    public static final String EVENT_TYPE_DELETE_ROUTE_HEADER = "deleteRouteHeader";
    public static final String EVENT_TYPE_ROUTE_LEVEL_CHANGE = "routeLevelChange";
    public static final String EVENT_TYPE_ROUTE_STATUS_CHANGE = "statusChange";

    private static String getURL(Document edlDoc) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        return (String) xpath.evaluate("//edlContent/edl/eventNotificationURL", edlDoc, XPathConstants.STRING);
    }

    /**
     * @param urlstring
     * @param eventDoc
     */
    private static void submitURL(String urlstring, Document eventDoc) throws IOException {
        String content;
        try {
            content = XmlJotter.jotNode(eventDoc, true);
        } catch (XmlException te) {
            LOG.error("Error writing serializing event doc: " + eventDoc);
            throw te;
        }
        byte[] contentBytes = content.getBytes("UTF-8");

        LOG.debug("submitURL: " + urlstring);
        URL url = new URL(urlstring);

        String message = "POST " + url.getFile() + " HTTP/1.0\r\n" +
                         "Content-Length: " + contentBytes.length + "\r\n" +
                         "Cache-Control: no-cache\r\n" +
                         "Pragma: no-cache\r\n" +
                         "User-Agent: Java/1.4.2; EDocLitePostProcessor\r\n" +
                         "Host: " + url.getHost() + "\r\n" +
                         "Connection: close\r\n" +
                         "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                         content;

        byte[] buf = message.getBytes("UTF-8");
        Socket s = new Socket(url.getHost(), url.getPort());

        /*URLConnection con = url.openConnection();
        LOG.debug("got connection: " + con);
        con.setDoOutput(true);
        con.setDoInput(true);
        LOG.debug("setDoOutput(true)");

        con.setRequestProperty("Connection", "close");
        con.setRequestProperty("Content-Length", String.valueOf(buf.length));*/

        OutputStream os = s.getOutputStream();
        try {
            try {
                os.write(buf, 0, buf.length);
                os.flush();
            } catch (InterruptedIOException ioe) {
                LOG.error("IO was interrupted while posting event to url " + urlstring + ": " + ioe.getMessage());
            } catch (IOException ioe) {
                LOG.error("Error posting EDocLite content to url " + urlstring + ioe.getMessage());
            } finally {
                try {
                    LOG.debug("Shutting down output stream");
                    s.shutdownOutput();
                } catch (IOException ioe) {
                    LOG.error("Error shutting down output stream for url " + urlstring + ": " + ioe.getMessage());
                }
            }

            InputStream is = s.getInputStream();
            try {

                buf = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // this is what actually forces the write on the URLConnection!
                int read = is.read(buf);
                if (read != -1) {
                    baos.write(buf, 0, read);
                }
                LOG.debug("EDocLite post processor response:\n" + new String(baos.toByteArray()));
            } catch (InterruptedIOException ioe) {
                LOG.error("IO was interrupted while reading response from url " + urlstring + ": " + ioe.getMessage());
            } catch (IOException ioe) {
                LOG.error("Error reading response from EDocLite handler url " + urlstring + ioe.getMessage());
            } finally {
                try {
                    LOG.debug("Shutting down input stream");
                    s.shutdownInput();
                } catch (IOException ioe) {
                    LOG.error("Error shutting down input stream for url " + urlstring + ": " + ioe.getMessage());
                }
            }
        } finally {
            try {
                s.close();
            } catch (IOException ioe) {
                LOG.error("Error closing socket", ioe);
            }
        }
    }

    protected static void postEvent(String docId, Object event, String eventName) {
    	try {
    		Document doc = getEDLContent(docId);
    		if(LOG.isDebugEnabled()){
    			LOG.debug("Submitting doc: " + XmlJotter.jotNode(doc));
    		}

    		String urlstring = getURL(doc);
    		if (org.apache.commons.lang.StringUtils.isEmpty(urlstring)) {
    			LOG.warn("No eventNotificationURL defined in EDLContent");
    			return;
    		}

    		Document eventDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    		Element eventE = eventDoc.createElement("event");
    		eventE.setAttribute("type", eventName);
    		eventDoc.appendChild(eventE);

    		Element infoE = (Element) eventDoc.importNode(propertiesToXml(event, "info"), true);
    		Element docIdE = eventDoc.createElement("docId");
    		docIdE.appendChild(eventDoc.createTextNode(String.valueOf(docId)));
    		infoE.appendChild(docIdE);

    		eventE.appendChild(infoE);
    		eventE.appendChild(eventDoc.importNode(doc.getDocumentElement(), true));

    		String query = "docId=" + docId;
    		if (urlstring.indexOf('?') != -1) {
    			urlstring += "&" + query;
    		} else {
    			urlstring += "?" + query;
    		}

    		final String _urlstring = urlstring;
    		final Document _eventDoc = eventDoc;
    		// a super cheesy way to enforce asynchronicity/timeout follows:
    		final Thread t = new Thread(new Runnable() {
    			public void run() {
    				try {
    					LOG.debug("Post Event calling url: " + _urlstring);
    					submitURL(_urlstring, _eventDoc);
    					LOG.debug("Post Event done calling url: " + _urlstring);
    				} catch (Exception e) {
    					LOG.error(e);
    				}
    			}
    		});
    		t.setDaemon(true);
    		t.start();

    		// kill the submission thread if it hasn't completed after 1 minute
    		TIMER.schedule(new TimerTask() {
    			public void run() {
    				t.interrupt();
    			}
    		}, SUBMIT_URL_MILLISECONDS_WAIT);
    	} catch (Exception e) {
    		if (e instanceof RuntimeException) {
    			throw (RuntimeException)e;
    		}
    		throw new RuntimeException(e);
    	}
    }

    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange event) throws RemoteException {
        LOG.debug("doRouteStatusChange: " + event);
        postEvent(event.getDocumentId(), event, EVENT_TYPE_ROUTE_STATUS_CHANGE);
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport doActionTaken(ActionTakenEvent event) throws RemoteException {
        LOG.debug("doActionTaken: " + event);
        postEvent(event.getDocumentId(), event, EVENT_TYPE_ACTION_TAKEN);
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws RemoteException {
        LOG.debug("doDeleteRouteHeader: " + event);
        postEvent(event.getDocumentId(), event, EVENT_TYPE_DELETE_ROUTE_HEADER);
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange event) throws RemoteException {
        LOG.debug("doRouteLevelChange: " + event);
        postEvent(event.getDocumentId(), event, EVENT_TYPE_ROUTE_LEVEL_CHANGE);
        return new ProcessDocReport(true, "");
    }

    public static Document getEDLContent(String documentId) {
    	try {
    		DocumentContent documentContent = KewApiServiceLocator.getWorkflowDocumentService().getDocumentContent(documentId);
    		String content = documentContent.getFullContent();
    		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(content)));
    		return doc;
    	} catch (Exception e) {
    		if (e instanceof RuntimeException) {
    			throw (RuntimeException)e;
    		}
    		throw new RuntimeException(e);
    	}
    }

    public static DocumentBuilder getDocumentBuilder() throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    private static String lowerCaseFirstChar(String s) {
        if (s.length() == 0 || Character.isLowerCase(s.charAt(0)))
            return s;
        StringBuffer sb = new StringBuffer(s.length());
        sb.append(Character.toLowerCase(s.charAt(0)));
        if (s.length() > 1) {
            sb.append(s.substring(1));
        }
        return sb.toString();
    }

    public static Element propertiesToXml(Object o, String elementName) throws Exception {
        Class c = o.getClass();
        Document doc = getDocumentBuilder().newDocument();
        Element wrapper = doc.createElement(elementName);
        Method[] methods = c.getMethods();
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if ("getClass".equals(name))
                continue;
            if (!name.startsWith("get") || methods[i].getParameterTypes().length > 0)
                continue;
            name = name.substring("get".length());
            name = lowerCaseFirstChar(name);
            String value = null;
            try {
                Object result = methods[i].invoke(o, null);
                if (result == null) {
                    LOG.debug("value of " + name + " method on object " + o.getClass() + " is null");
                    value = "";
                } else {
                    value = result.toString();
                }
                Element fieldE = doc.createElement(name);
                fieldE.appendChild(doc.createTextNode(value));
                wrapper.appendChild(fieldE);
            } catch (RuntimeException e) {
                LOG.error("Error accessing method '" + methods[i].getName() + " of instance of " + c);
                throw e;
            } catch (Exception e) {
                LOG.error("Error accessing method '" + methods[i].getName() + " of instance of " + c);
            }
        }
        return wrapper;
    }
}
