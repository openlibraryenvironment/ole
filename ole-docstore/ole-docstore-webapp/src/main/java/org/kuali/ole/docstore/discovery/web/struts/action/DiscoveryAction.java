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
package org.kuali.ole.docstore.discovery.web.struts.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.ole.docstore.discovery.service.DiscoveryService;
import org.kuali.ole.docstore.discovery.service.DiscoveryServiceImpl;
import org.kuali.ole.docstore.discovery.web.struts.form.DiscoveryForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;

public class DiscoveryAction extends Action {
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryAction.class);
    public static final String ACTION_BACK_TO_SEARCH = "backToSearch";

    public static final String SEARCH_PARAMS = "searchParams";
    public static final String DISCOVERY_SERVICE = "discoveryService";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        String facetValue = null;
        String facetFieldValue = null;
        String searchType = null;
        String linkValue = null;
        Map FacetTermsMap = null;
        long startTime = System.currentTimeMillis();
        DiscoveryForm discForm = (DiscoveryForm) form;
        SearchParams searchParams = getSearchParams(request);
        // Get the user input from the http request.
        searchType = request.getParameter("searchType");
        LOG.debug("searchType......... " + searchType);
        LOG.debug("sortByTerms......." + request.getParameter("advancedSearch[advancedSearch][sortByTerms]"));
        LOG.debug("sortingorder........" + request.getParameter("advancedSearch[advancedSearch][sortingorder]"));
        if (StringUtils.isEmpty(searchType)) {
            searchType = request.getParameter("advancedSearch[advancedSearch][searchType]");

        }
        LOG.debug("searchType " + searchType);
        if (StringUtils.isEmpty(searchType)) {
            // Do not initialize from request.
            LOG.debug("in if StringUtils.isEmpty ");
            String action = request.getParameter("action");
            if ("setPageSize".equals(action)) {
                LOG.debug("setPageSize..." + action);
                int resultFrmIdx = ((Integer.parseInt(request.getParameter("resultFromIndex"))) / (Integer.parseInt(request.getParameter("resultPageSize"))) + 1);
                discForm.setResultPageIndex(Integer.toString(resultFrmIdx));
                discForm.setResultPageSize(request.getParameter("resultPageSize"));
                String sortingTerms = request.getParameter("sortByTerms");
                sortTerms(sortingTerms, discForm);
            } else if ("goToPage".equals(action)) {
                LOG.debug("goToPage..." + action);
                String resultPgIdx = request.getParameter("resultPageIndex");
                int item_index = ((Integer.parseInt(resultPgIdx) - 1) * (Integer.parseInt(request.getParameter("resultPageSize"))));
                discForm.setResultFromIndex(Integer.toString(item_index));
                discForm.setResultPageIndex((resultPgIdx));
                String sortingTerms = request.getParameter("sortByTerms");
                sortTerms(sortingTerms, discForm);
            } else if ("sorting".equals(action)) {
                LOG.debug("sorting..." + action);
                String sortingTerms = request.getParameter("sortByTerms");
                discForm.setSortByTerms(sortingTerms);
                sortTerms(sortingTerms, discForm);
                discForm.setResultPageSize(request.getParameter("resultPageSize"));
            }

            LOG.debug("discForm.getSearchType()" + discForm.getSearchType());
            if ("moreFacets".equals(action)) {
                discForm.setSearchType(searchParams.getSearchType());
                discForm.setFacetPrefix("");
                discForm.setFacetPageSize("25");
                discForm.setFacetOffset("0");
                discForm.setFacetSort("lex");
                discForm.setSearchType(DiscoveryService.SEARCH_TYPE_MORE_FACET);
                discForm.setFacetField(request.getParameter("fieldName"));
                discForm.initSearchParams(searchParams);
                DiscoveryService discService = getDiscoveryService();
                String result = discService.search(searchParams);
                discForm.setSearchResult(result);
                discForm.setSearchQuery(searchParams.getSearchQuery());
                forward = mapping.findForward("moreFacets");
            } else if (!StringUtils.isEmpty(discForm.getDocCategory())) {
                if (!searchParams.getSearchType().equals(DiscoveryService.SEARCH_TYPE_MORE_FACET)) {
                    discForm.setSearchType(searchParams.getSearchType());
                }
                discForm.initSearchParams(searchParams);
                // Call the search service.
                DiscoveryService discService = getDiscoveryService();
                String result = discService.search(searchParams);
                discForm.setSearchResult(result);
                discForm.setSearchQuery(searchParams.getSearchQuery());
                forward = mapping.findForward("searchResults");
            } else {
                forward = mapping.findForward("search");
            }
        } else if (ACTION_BACK_TO_SEARCH.equals(searchType)) {
            forward = mapping.findForward("search");
        } else if (DiscoveryService.SEARCH_TYPE_NEW.equals(searchType)) {
            LOG.debug("in else if ACTION_NEW_SEARCH.equals ");
            searchParams.setFacetField(null);
            discForm.setFacetField(null);
            discForm.setFacetTermsMap(null);
            // Cleanup all search form.
            discForm.init();
            // Cleanup the search params bean.
            searchParams.init();
            forward = mapping.findForward("search");
        } else if (DiscoveryService.SEARCH_TYPE_QUICK.equals(searchType)
                || DiscoveryService.SEARCH_TYPE_ADVANCED.equals(searchType)) {
            discForm.initFromRequest(request);
            discForm.setResultPageIndex("1");
            discForm.setResultFromIndex("0");
            discForm.setFacetSort("count");
            String sortByTerms = request.getParameter("sortingorder");
            sortTerms(sortByTerms, discForm);
            discForm.setSortByTerms(sortByTerms);
            LOG.debug("SortField()........." + discForm.getSortField());
            LOG.debug("SortOrder()........." + discForm.getSortOrder());
            // Populate the search params bean.
            discForm.initSearchParams(searchParams);
            // Call the search service.
            DiscoveryService discService = getDiscoveryService();
            String result = discService.search(searchParams);
            LOG.debug("search terms...." + searchParams.getSearchTerms());
            if (searchParams.getSearchTerms() != null) {
                discForm.setSearchTerms(searchParams.getSearchTerms());
            }
            discForm.setSearchResult(result);
            discForm.setSearchQuery(searchParams.getSearchQuery());
            forward = mapping.findForward("searchResults");
        } else if (DiscoveryService.SEARCH_TYPE_FACET.equals(searchType)) {
            facetValue = request.getParameter("facetValue");
            facetFieldValue = request.getParameter("facetFieldValue");
            facetValue = URLEncoder.encode(facetValue, "UTF-8");
            discForm.updateFacetParams(facetFieldValue, facetValue);
            discForm.setResultPageIndex("1");
            discForm.setResultFromIndex("0");
            discForm.setFacetPrefix("");
            discForm.setFacetPageSize("25");
            discForm.setFacetSort("count");
            discForm.setFacetOffset("0");
            discForm.initSearchParams(searchParams);
            // Call the search service.
            DiscoveryService discService = getDiscoveryService();
            String result = discService.search(searchParams);
            LOG.debug("result " + result);
            discForm.setSearchResult(result);
            discForm.setSearchQuery(searchParams.getSearchQuery());
            forward = mapping.findForward("searchResults");
        } else if (DiscoveryService.SEARCH_TYPE_FACET_DELETE.equals(searchType)) {
            facetValue = request.getParameter("facetValue");
            facetFieldValue = request.getParameter("facetFieldValue");
            facetValue = URLEncoder.encode(facetValue, "UTF-8");
            discForm.removeFacet(facetFieldValue, facetValue);
            discForm.setResultPageIndex("1");
            discForm.setResultFromIndex("0");
            discForm.setFacetSort("count");
            discForm.initSearchParams(searchParams);
            // Call the search service.
            DiscoveryService discService = getDiscoveryService();
            String result = discService.search(searchParams);
            discForm.setSearchResult(result);
            discForm.setSearchQuery(searchParams.getSearchQuery());
            forward = mapping.findForward("searchResults");
        } else if (DiscoveryService.SEARCH_TYPE_LINK.equals(searchType)) {
            linkValue = request.getParameter("linkValue");
            LOG.debug("linkValue'..................." + linkValue);
            discForm.setResultPageIndex("1");
            discForm.setResultFromIndex("0");
            discForm.setSortField(searchParams.getSortField());
            discForm.setSortOrder(searchParams.getSortOrder());
            discForm.setLinkValue(linkValue);
            discForm.initSearchParams(searchParams);
            DiscoveryService discService = getDiscoveryService();
            String result = discService.search(searchParams);
            discForm.setSearchResult(result);
            discForm.setSearchQuery(searchParams.getSearchQuery());
            forward = mapping.findForward("searchResults");
        }

        // Added for Lis of Bibs
        else if (DiscoveryService.BATCH_UPLOAD_LINK_SEARCH.equals(searchType)) {
            linkValue = request.getParameter("linkValue");
            if (LOG.isDebugEnabled()) {
                LOG.debug("linkValue'..................." + linkValue);
            }
            discForm.setResultPageIndex("1");
            discForm.setResultFromIndex("0");
            discForm.setSortField(searchParams.getSortField());
            discForm.setSortOrder(searchParams.getSortOrder());
            discForm.setLinkValue(linkValue);
            discForm.initSearchParams(searchParams);
            DiscoveryService discService = getDiscoveryService();
            if (linkValue == null || "".equals(linkValue)) {
                String bibIds = request.getParameter("bibIds");
                searchParams.setLinkValue("(id:" + bibIds + ")");
            }
            searchParams.setSearchType(DiscoveryService.SEARCH_TYPE_LINK);
            String result = discService.search(searchParams);
            discForm.setSearchResult(result);
            if (LOG.isDebugEnabled()) {
                LOG.info("Result" + result);
            }
            discForm.setSearchQuery(searchParams.getSearchQuery());
            forward = mapping.findForward("searchResults");

        } else if (DiscoveryService.SEARCH_TYPE_MORE_FACET.equals(searchType)) {
            String facetPageSize = request.getParameter("facetPageSize");
            String facetPrefix = request.getParameter("facetPrefix");
            String facetSort = request.getParameter("facetSort");
            discForm.setResultPageIndex("1");
            discForm.setResultFromIndex("0");
            if (facetPrefix != null) {
                discForm.setFacetPrefix(facetPrefix);
            }
            if (facetPageSize != null) {
                discForm.setFacetPageSize(facetPageSize);
            } else {
                facetPageSize = discForm.getFacetPageSize();
            }
            if (facetSort != null) {
                discForm.setFacetSort(facetSort);
            }
            String facetPage = request.getParameter("facetPage");
            if (facetPage != null) {
                String offset = discForm.getFacetOffset();
                int current = 0;
                if (offset != null) {
                    current = Integer.parseInt(offset);
                }
                int limit = Integer.parseInt(facetPageSize);
                if (facetPage.equals("previous")) {
                    int previous = current - limit;
                    if (previous > 0) {
                        discForm.setFacetOffset(String.valueOf(previous));
                    } else {
                        discForm.setFacetOffset("0");
                    }
                } else if (facetPage.equals("next")) {
                    int next = current + limit;
                    discForm.setFacetOffset(String.valueOf(next));
                }
            } else {
                discForm.setFacetOffset("0");
            }
            discForm.initSearchParams(searchParams);
            // Call the search service.
            DiscoveryService discService = getDiscoveryService();
            String result = discService.search(searchParams);
            LOG.debug("result " + result);
            discForm.setSearchResult(result);
            discForm.setSearchQuery(searchParams.getSearchQuery());
            forward = mapping.findForward("moreFacets");
        }
        else {
            forward = mapping.findForward("search");
        }
        long endTime = System.currentTimeMillis();
        discForm.setSearchTime(String.valueOf(endTime - startTime));
        return forward;
    }


    private SearchParams getSearchParams(HttpServletRequest request) {
        SearchParams searchParams = (SearchParams) request.getSession()
                .getAttribute(SEARCH_PARAMS);
        if (null == searchParams) {
            searchParams = new SearchParams();
            request.getSession().setAttribute(SEARCH_PARAMS, searchParams);
        }
        return searchParams;
    }

    private DiscoveryService getDiscoveryService() {
        DiscoveryService discoveryService = DiscoveryServiceImpl.getInstance();
        return discoveryService;
    }

    public void sortTerms(String sortingTerms, DiscoveryForm discForm) {
        if ("authorasc".equals(sortingTerms)) {
            discForm.setSortField("Author_sort");
            discForm.setSortOrder("asc");
        } else if ("authordesc".equals(sortingTerms)) {
            discForm.setSortField("Author_sort");
            discForm.setSortOrder("desc");
        } else if ("titleasc".equals(sortingTerms)) {
            discForm.setSortField("Title_sort");
            discForm.setSortOrder("asc");
        } else if ("titledesc".equals(sortingTerms)) {
            discForm.setSortField("Title_sort");
            discForm.setSortOrder("desc");
        } else if ("pubdateasc".equals(sortingTerms)) {
            discForm.setSortField("PublicationDate_sort");
            discForm.setSortOrder("asc");
        } else if ("pubdatedesc".equals(sortingTerms)) {
            discForm.setSortField("PublicationDate_sort");
            discForm.setSortOrder("desc");
        } else if ("relevance".equals(sortingTerms)) {
            discForm.setSortField("score");
            discForm.setSortOrder("desc");
        }
    }
}