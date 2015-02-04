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
//package org.kuali.ole.web;
//
//import org.kuali.ole.utility.HttpUtil;
//import org.kuali.ole.docstore.util.PropertyUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class BulkOptimizeServlet extends HttpServlet {
//	private static final Logger LOG         = LoggerFactory.getLogger(BulkOptimizeServlet.class);
//	private static final String RESULTS_JSP = "/bulkOptimizeResult.jsp";
//
//	@Override
//	protected void doPost(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		optimizeAllRecords(request, response);
//		LOG.info("Optimization of both Auth and bib is done");
//
//	}
//
//	/**
//	 * @throws Exception
//	 */
//	public void optimizeAllRecords(HttpServletRequest request,
//			HttpServletResponse response) {
//		RequestDispatcher rd = getServletContext().getRequestDispatcher(
//				RESULTS_JSP);
//		request.setAttribute("result",
//				"Optimization started. Please check logs for further details");
//		LOG.info("Ready for optimizing Auth and Bib Records");
//		try {
//			optimizeAuthRecords();
//			optimizeBibRecords();
//			rd.forward(request, response);
//		} catch (Exception e) {
//			LOG.error(
//					"Problem optimizing records! Please refer application logs for details",
//					e);
//		}
//	}
//
//	/**
//	 * @throws Exception
//	 */
//	public void optimizeBibRecords() throws Exception {
//		String docSearchURL = PropertyUtil.getPropertyUtil().getProperty(
//				"docSearchURL");
//		String indexCategory = "bib";
//		StringBuffer optimizeBibURL = new StringBuffer("");
//		optimizeBibURL.append(docSearchURL);
//		optimizeBibURL.append(indexCategory);
//		optimizeBibURL.append("/update/");
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("optimizeBibURL " + optimizeBibURL);
//		}
//		String parametersForOptimizeBibUrl = "optimize=true";
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("updateUrl for bib-->"
//					+ parametersForOptimizeBibUrl);
//		}
//		HttpUtil.postData(optimizeBibURL.toString(),
//                parametersForOptimizeBibUrl);
//		LOG.info("Optimization of bib records is done");
//	}
//
//	/**
//	 * @throws Exception
//	 */
//	public void optimizeAuthRecords() throws Exception {
//		String docSearchURL = PropertyUtil.getPropertyUtil().getProperty(
//				"docSearchURL");
//		String indexCategory = "auth";
//		StringBuffer optimizeAuthURL = new StringBuffer("");
//		optimizeAuthURL.append(docSearchURL);
//		optimizeAuthURL.append(indexCategory);
//		optimizeAuthURL.append("/update/");
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("optimizeAuthURL " + optimizeAuthURL);
//		}
//		String parametersForOptimizeAuthUrl = "optimize=true";
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("parametersForOptimizeAuthUrl-->"
//					+ parametersForOptimizeAuthUrl);
//		}
//		HttpUtil.postData(optimizeAuthURL.toString(),
//				parametersForOptimizeAuthUrl);
//		LOG.info("Optimization of auth reocrds is done");
//	}
//
//}
