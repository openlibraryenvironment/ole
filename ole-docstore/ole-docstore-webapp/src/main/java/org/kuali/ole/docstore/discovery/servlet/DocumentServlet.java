///*
// * Copyright 2011 The Kuali Foundation.
// *
// * Licensed under the Educational Community License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.opensource.org/licenses/ecl2.php
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.kuali.ole.docstore.discovery.servlet;
//
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.lang.StringUtils;
//import org.kuali.ole.docstore.discovery.service.IndexerService;
//import org.kuali.ole.docstore.discovery.service.IndexerServiceImpl;
//import org.kuali.ole.docstore.discovery.util.PropertyUtil;
//import org.slf4j.LoggerFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.namespace.QName;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.events.Attribute;
//import javax.xml.stream.events.StartElement;
//import javax.xml.stream.events.XMLEvent;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
//import javax.xml.xpath.XPathFactory;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.*;
//
//public class DocumentServlet extends HttpServlet {
//
//	private static final long serialVersionUID = -3717561557966540651L;
//    org.slf4j.Logger LOG = LoggerFactory.getLogger(DocumentServlet.class);
//	String dataImportDirectory = null;
//
//	/**
//	 * @see HttpServlet#HttpServlet()
//	 */
//
//	public DocumentServlet() {
//		super();
//		dataImportDirectory = getDataDir("bib", "marc");
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doPost(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		List<FileItem> fileItems = getMultiPartFileItems(request, response);
//		String docAction = getParameter("docAction", fileItems, request);
//		LOG.debug("docAction-->" + docAction);
//		if ("add".equals(docAction)) {
//			LOG.info(" Requesting for Add a Document ");
//			addDocument(fileItems, request, response);
//		} else if ("delete".equals(docAction)) {
//			LOG.info(" Requesting for Delete  a Document ");
//			deleteDocuments(request, response);
//		} else if ("getIDs".equals(docAction)) {
//			LOG.info(" Requesting for IDs  ");
//			getIDs(request, response);
//        } else if ("index".equals(docAction)) {
//            LOG.info(" Requesting for IDs  ");
//            indexDocuments(request, response);
//		} else {
//			// Send "Invalid request" as response.
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
//					"Invalid docAction.");
//		}
//	}
//
//    protected void indexDocuments(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String docCategory = request.getParameter("docCategory");
//        String docType = request.getParameter("docType");
//        String docFormat = request.getParameter("docFormat");
//        String docContent = request.getParameter("docContent");
//        String result = null;
//        try {
//            IndexerService indexerService = getIndexerService();
//            if (null != docContent) {
//                // TODO: Need to use indexerService.indexDocumentsFromFileBySolrDoc()
//                //result = indexerService.indexDocuments(docCategory, docType, docFormat, docContent);
//            }
//            else {
//                // TODO: Need to use indexerService.indexDocumentsFromFileBySolrDoc()
//                //result = indexerService.indexDocuments(docCategory, docType, docFormat);
//            }
//            String responseMsg = buildResponseMessage(result, "");
//            sendResponse(response, responseMsg);
//        }
//        catch (Exception e) {
//            LOG.error("Exception while indexing documents:", e);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Indexing of documents has failed.");
//        }
//    }
//
//    protected String buildResponseMessage(String status, String message) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<ole-discovery>\n");
//        sb.append("  <response>\n");
//        sb.append("    <status>").append(status).append("</status>\n");
//        sb.append("    <message>").append(message).append("</message>\n");
//        sb.append("  </response>");
//        sb.append("</ole-discovery>");
//        return sb.toString();
//    }
//
//    protected void sendResponse(HttpServletResponse response, String message) throws Exception {
//        PrintWriter out = null;
//        response.setContentType("text/xml");
//        response.setCharacterEncoding("UTF-8");
//        out = response.getWriter();
//        out.print(message);
//        out.close();
//    }
//
//    protected void deleteDocuments(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String docCategory = request.getParameter("docCategory");
//        String uuids = request.getParameter("id");
//        String result = null;
//        try {
//            IndexerService indexerService = getIndexerService();
//            if ((null != uuids) && (uuids.length() > 0)) {
//                String[] uuidArr = StringUtils.split(uuids, ",");
//                List<String> uuidList = Arrays.asList(uuidArr);
//                result = indexerService.deleteDocuments(docCategory, uuidList);
//                 String responseMsg = buildResponseMessage(result, "");
//                sendResponse(response, responseMsg);
//            }
//            else {
//                result = IndexerService.FAILURE;
//                String message = "No uuids to delete.";
//                String responseMsg = buildResponseMessage(result, "");
//                sendResponse(response, responseMsg);
//            }
//        }
//        catch (Exception e) {
//            LOG.error("Exception while deleting documents:", e);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Deletion of documents failed.");
//        }
//    }
//
//    private IndexerService getIndexerService() {
//        IndexerService indexerService = IndexerServiceImpl.getInstance();
//        return indexerService;
//    }
//
//
//	protected void getIDs(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String docCategory = request.getParameter("docCategoryGetId");
//		String operator = request.getParameter("operator");
//		Map parameterMap = request.getParameterMap();
//		String[] fieldNames = null;
//		String[] fieldValues = null;
//		StringBuffer query = new StringBuffer("");
//		String finalQuery = "";
//		fieldNames = (String[]) parameterMap.get("field");
//		fieldValues = (String[]) parameterMap.get("fvalue");
//		for (int i = 0; i < fieldNames.length; i++) {
//			if (!fieldNames[i].equalsIgnoreCase("select field")) {
//				if (query.length() != 0) {
//					query.append(operator);
//				}
//				query.append("(");
//				query.append(fieldNames[i]);
//				query.append(":");
//				query.append(fieldValues[i]);
//				query.append(")");
//				finalQuery = query.toString();
//			}
//		}
//		if (query.length() == 0) {
//			finalQuery = "*:*";
//		}
//		LOG.info("query=" + query);
//		LOG.info("finalQuery=" + finalQuery);
//		String docSearchURL = PropertyUtil.getPropertyUtil().getProperty(
//				"docSearchURL");
//		String finalQueryEncoded = URLEncoder.encode(finalQuery, "UTF8");
//		String searchURL = docSearchURL + docCategory + "/select?q="
//				+ finalQueryEncoded + "&fl=id";
//		LOG.info("searchURL-->" + searchURL);
//		URL url = new URL(searchURL);
//		HttpURLConnection searchConnection = (HttpURLConnection) url
//				.openConnection();
//		BufferedInputStream bis = new BufferedInputStream(
//				searchConnection.getInputStream());
//		BufferedOutputStream bos = new BufferedOutputStream(
//				response.getOutputStream());
//		int readBytes = 0;
//		while ((readBytes = bis.read()) != -1) {
//			bos.write(readBytes);
//		}
//       		bis.close();
//		bos.close();
//	}
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String docAction = request.getParameter("docAction");
//		LOG.info("DocumentServlet.doGet()....." + docAction);
//		if ("cleanIndex".equals(docAction)) {
//			LOG.info("Requesting for cleaning Solr Index");
//			cleanSolrIndex(request, response);
//		} else if ("getDocument".equals(docAction)) {
//			LOG.info(" Requesting for getting a Document by its ID ");
//			documentById(request, response);
//		} else {
//			// Send "Invalid request" as response.
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
//					"Invalid docAction.");
//		}
//
//	}
//
//	protected void addDocument(List<FileItem> fileItems,
//			HttpServletRequest request, HttpServletResponse response)
//			throws ServletException {
//		LOG.info(" Add Document request is in process");
//		boolean fileAvailable = false;
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			response.setContentType("text/plain");
//			String docCategory = null;
//			String docType = null;
//			if (fileItems != null) {
//				Random rand = new Random();
//				int randomFileNumber = rand.nextInt();
//				String destFile = dataImportDirectory + "/tempUploadFile"
//						+ randomFileNumber + ".xml";
//				Iterator<FileItem> itr = fileItems.iterator();
//				File uploadedFile = null;
//				while (itr.hasNext()) {
//					FileItem item = (FileItem) itr.next();
//					if (item.isFormField()) {
//						String name = item.getFieldName();
//						String value = item.getString();
//						if ("docCategory".equals(name)) {
//							docCategory = value;
//						} else if ("docType".equals(name)) {
//							docType = value;
//						}
//					} else {
//						uploadedFile = new File(destFile);
//						LOG.debug("File Saved to:" + destFile);
//						item.write(uploadedFile);
//						fileAvailable = true;
//					}
//				}
//				if (fileAvailable) {
//					String status = invokeDataImportHandler(docCategory,
//							docType, response);
//					uploadedFile.delete();
//				} else {
//					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
//							"Input data not available.");
//				}
//			}
//			LOG.info("Adding Document Request is completed");
//		} catch (Exception exception) {
//			out.println("<HTML>\n" + "<HEAD></HEAD>\n" + "<BODY>\n"
//					+ "Import failed. Please contact support team.");
//			out.println("<!--");
//			StringWriter sWriter = new StringWriter();
//			PrintWriter pWriter = new PrintWriter(sWriter);
//			exception.printStackTrace(pWriter);
//			out.println(sWriter);
//			out.println("-->");
//			out.println("</BODY></HTML>");
//			pWriter.close();
//			LOG.error("Error in Adding the Document:", exception);
//		}
//	}
//
//	protected String invokeDataImportHandler(String docCategory,
//			String docType, HttpServletResponse response) throws Exception {
//		LOG.info("Invoking the DataImportHandler");
//		PrintWriter out = response.getWriter();
//		String docSearchURL = PropertyUtil.getPropertyUtil().getProperty(
//				"docSearchURL");
//		String commandPrefix = docSearchURL + docCategory + "/" + docType
//				+ "DataImport?command=";
//		String dataImportCommand = commandPrefix + "full-import&clean=false";
//		LOG.debug("dataImportCommand-->" + dataImportCommand);
//		URL url = new URL(dataImportCommand);
//		HttpURLConnection dataImportConnection = (HttpURLConnection) url
//				.openConnection();
//		String status = getStatusValue(dataImportConnection);
//
//		String statusCommand = commandPrefix + "status";
//		LOG.debug("statusCommand-->" + statusCommand);
//		URL statusUrl = new URL(statusCommand);
//		HttpURLConnection statusConn = (HttpURLConnection) statusUrl
//				.openConnection();
//		status = getStatusValue(statusConn);
//		LOG.debug("Status of Import-->" + status);
//		while (!status.equalsIgnoreCase("idle")) {
//			LOG.info("DocumentServlet.invokeDataImportHandler()-->Inside while loop");
//			HttpURLConnection statusConnTest = (HttpURLConnection) statusUrl
//					.openConnection();
//
//			status = getStatusValue(statusConnTest);
//			LOG.debug("DocumentServlet.invokeDataImportHandler()-->Status in while loop-->"
//					+ status);
//			Thread.sleep(1000);
//		}
//		HttpURLConnection statusConnDoc = (HttpURLConnection) statusUrl
//				.openConnection();
//		String docsProcessed = getIndexingStatus(statusConnDoc);
//		LOG.debug("DocumentServlet.invokeDataImportHandler()-->docsProcessed-->"
//				+ docsProcessed);
//		out.println("Indexing completed." + docsProcessed
//				+ " documents have been processed");
//		return status;
//	}
//
//	/**
//	 * @param httpConnection
//	 * @return status value (Idle or Busy)
//	 */
//	public String getStatusValue(HttpURLConnection httpConnection)
//			throws Exception {
//		XMLEventReader eventReader = null;
//		XMLInputFactory inputFactory;
//		String responseCount = null;
//		String status = null;
//		inputFactory = XMLInputFactory.newInstance();
//		eventReader = inputFactory.createXMLEventReader(httpConnection
//				.getInputStream());
//		while (eventReader.hasNext()) {//
//			XMLEvent event = eventReader.nextEvent();
//			if (event.isStartElement()) {
//				StartElement element = (StartElement) event;
//				if (element.getName().toString().equalsIgnoreCase(("str"))) {
//					Iterator iterator = element.getAttributes();
//					while (iterator.hasNext()) {
//						Attribute attribute = (Attribute) iterator.next();
//						QName name = attribute.getName();
//						if (name.toString().equalsIgnoreCase(("name"))) {
//							responseCount = attribute.getValue();
//							if (responseCount.equalsIgnoreCase("status")) {
//								if (event.asStartElement().getName()
//										.getLocalPart().equals("str")) {
//									event = eventReader.nextEvent();
//									status = event.asCharacters().getData();
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return status;
//	}
//
//	/**
//	 * @param category
//	 * @param docType
//	 * @return destDir
//	 */
//	protected String getDataDir(String category, String docType) {
//		String solrHome = System.getProperty("solr.solr.home");
//		LOG.debug("solrHome-->" + solrHome);
//		String configFile = solrHome + "/" + category + "/conf/" + docType
//				+ "-data-config.xml";
//		LOG.info("configFile-->" + configFile);
//		String bibBaseDir = null;
//		try {
//			DocumentBuilderFactory domFactory = DocumentBuilderFactory
//					.newInstance();
//			domFactory.setNamespaceAware(true);
//			DocumentBuilder builder = domFactory.newDocumentBuilder();
//			Document configDoc = builder.parse(configFile);
//			XPathFactory factory = XPathFactory.newInstance();
//			XPath xpath = factory.newXPath();
//			XPathExpression categoryExpr = xpath.compile("//entity/@baseDir");
//			LOG.debug("categoryExpr-->" + categoryExpr);
//			Object baseDirResult = categoryExpr.evaluate(configDoc,
//					XPathConstants.NODESET);
//			NodeList baseDirNodes = (NodeList) baseDirResult;
//			LOG.debug("length-->" + baseDirNodes.getLength());
//			for (int i = 0; i < baseDirNodes.getLength(); i++) {
//				bibBaseDir = baseDirNodes.item(i).getNodeValue();
//				LOG.debug("Node val-->" + bibBaseDir);
//			}
//		} catch (Exception e) {
//			LOG.error("Error in reading the Data Directory:", e);
//
//		}
//		return bibBaseDir;
//	}
//
//	/**
//	 * @param request
//	 * @param response
//	 * @return docAction (add or delete)
//	 * @throws ServletException
//	 */
//	protected List<FileItem> getMultiPartFileItems(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException {
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		String docAction = null;
//		List<FileItem> items = null;
//		try {
//			if (isMultipart) {
//				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
//				ServletFileUpload uploadHandler = new ServletFileUpload(
//						fileItemFactory);
//				items = uploadHandler.parseRequest(request);
//			}
//		} catch (Exception ex) {
//			LOG.error("Error in getting the Reading the File:", ex);
//		}
//		return items;
//	}
//
//	/**
//	 * @param action
//	 * @param items
//	 * @param request
//	 * @return docAction (delete)
//	 */
//	protected String getParameter(String action, List<FileItem> items,
//			HttpServletRequest request) {
//		String docAction = null;
//		if (items != null && items.size() > 0) {
//			Iterator<FileItem> itr = items.iterator();
//			while (itr.hasNext()) {
//				FileItem item = (FileItem) itr.next();
//				if (item.isFormField()) {
//					String name = item.getFieldName();
//					String value = item.getString();
//					if ("docAction".equals(name)) {
//						docAction = value;
//					}
//				}
//			}
//		} else {
//			docAction = request.getParameter("docAction");
//		}
//		return docAction;
//	}
//
//	/**
//	 * @param httpConnection
//	 * @return docCount (Number of documents indexed)
//	 * @throws Exception
//	 */
//	public String getIndexingStatus(HttpURLConnection httpConnection)
//			throws Exception {
//		XMLEventReader eventReader = null;
//		XMLInputFactory inputFactory;
//		String responseCount = null;
//		String docCount = null;
//		inputFactory = XMLInputFactory.newInstance();
//		eventReader = inputFactory.createXMLEventReader(httpConnection
//				.getInputStream());
//		while (eventReader.hasNext()) {
//			XMLEvent event = eventReader.nextEvent();
//			if (event.isStartElement()) {
//				StartElement element = (StartElement) event;
//				if (element.getName().toString().equalsIgnoreCase(("str"))) {
//					Iterator iterator = element.getAttributes();
//					while (iterator.hasNext()) {
//						Attribute attribute = (Attribute) iterator.next();
//						QName name = attribute.getName();
//						if (name.toString().equalsIgnoreCase(("name"))) {
//							responseCount = attribute.getValue();
//							if (responseCount
//									.equalsIgnoreCase("Total Documents Processed")) {
//								if (event.asStartElement().getName()
//										.getLocalPart().equals("str")) {
//									event = eventReader.nextEvent();
//									docCount = event.asCharacters().getData();
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return docCount;
//	}
//
//	/**
//	 * @param request
//	 * @param response
//	 * @throws ServletException
//	 */
//	protected void cleanSolrIndex(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException {
//		LOG.info(" Clear solr index request is in process");
//		String docSearchURL = PropertyUtil.getPropertyUtil().getProperty(
//				"docSearchURL");
//		String docCore = request.getParameter("core");
//		LOG.debug("docCore-->" + docCore);
//		String cleanURL = null;
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			if (docCore != null && !"".equalsIgnoreCase(docCore)) {
//				cleanURL = docSearchURL
//						+ docCore
//						+ "/update/?stream.body=<delete><query>*:*</query></delete>&commit=true";
//				LOG.debug("cleanURL-->" + cleanURL);
//				URL cleanSolrURL = new URL(cleanURL);
//				HttpURLConnection cleanConnection = (HttpURLConnection) cleanSolrURL
//						.openConnection();
//				cleanConnection.setDoOutput(true);
//				cleanConnection.connect();
//				LOG.debug("clean Solr URL ......" + cleanConnection);
//				OutputStreamWriter streamWriter = new OutputStreamWriter(
//						cleanConnection.getOutputStream());
//				streamWriter.write("Request data....");
//				streamWriter.flush();
//				// Get the response from clean Solr Index URL
//				BufferedReader bufferReader = new BufferedReader(
//						new InputStreamReader(cleanConnection.getInputStream()));
//				streamWriter.close();
//				bufferReader.close();
//				out.print("Cleaning Solr Index for " + docCore
//						+ " has been completed successfully");
//			} else {
//				out.print("Invalid core. Cleaning Solr Index cannot be done");
//			}
//			LOG.info("Cleaning Solr Index Request is completed");
//		} catch (Exception ex) {
//			LOG.error("Error in Cleaning Solr Index:", ex);
//			out.println("<HTML>\n"
//					+ "<HEAD></HEAD>\n"
//					+ "<BODY>\n"
//					+ "Cleaning Solr Index is failed. Please contact support team.");
//			out.println("<!--");
//			StringWriter sWriter = new StringWriter();
//			PrintWriter pWriter = new PrintWriter(sWriter);
//			ex.printStackTrace(pWriter);
//			out.println(sWriter);
//			out.println("-->");
//			out.println("</BODY></HTML>");
//			pWriter.close();
//		}
//	}
//
//	protected void documentById(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException {
//		String docId = request.getParameter("id");
//		String docCategory = "bib";
//		String docSearchURL = PropertyUtil.getPropertyUtil().getProperty(
//				"docSearchURL");
//		if ((docId != null) && !"".equals(docId)) {
//			String searchURL = docSearchURL + docCategory + "/select?q=id:"
//					+ docId;
//			try {
//				URL url = new URL(searchURL);
//				HttpURLConnection searchConnection = (HttpURLConnection) url
//						.openConnection();
//				BufferedInputStream bis = new BufferedInputStream(
//						searchConnection.getInputStream());
//				BufferedOutputStream bos = new BufferedOutputStream(
//						response.getOutputStream());
//				int readBytes = 0;
//				while ((readBytes = bis.read()) != -1) {
//					bos.write(readBytes);
//				}
//				bos.close();
//				bis.close();
//			} catch (Exception e) {
//				LOG.error("Error in getting document By Id:", e);
//			}
//		}
//	}
//}
