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
package org.kuali.ole.select.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.engine.service.index.solr.BibConstants;
import org.kuali.ole.docstore.engine.service.index.solr.ItemConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.DocInfoBean;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.*;

/**
 * This class is the implementation class for OleDocStoreSearchService
 * to integrate docstore search with database search.
 */
@NonTransactional
public class OleDocStoreSearchService extends PlatformAwareDaoBaseOjb implements org.kuali.ole.select.service.OleDocStoreSearchService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDocStoreSearchService.class);
    static HashMap<String, String> cache = new HashMap<String, String>();
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private DocstoreClientLocator docstoreClientLocator;

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }
    
/*    public boolean createCriteria(Object arg0, String arg1, String arg2, Object arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    
    public boolean createCriteria(Object arg0, String arg1, String arg2, boolean arg3, boolean arg4, Object arg5) {
        // TODO Auto-generated method stub
        return false;
    }
*/


    /**
     * This method returns list of docdata populated through docstore search
     *
     * @param arg1
     * @return
     * @throws Exception
     */
    public List<DocData> getDocResult(Map arg1) throws Exception {
        BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        // changed for jira 2144 starts
        List<DocInfoBean> docStoreResult = docStore.searchBibInfo(arg1);
        // changed for jira 2144 ends
        List<DocData> ress = new ArrayList<DocData>(0);
        ress = getDocResult(docStoreResult);
        return ress;
    }



    public List<DocData> getDocSearchResults(Map<String,String> criteria,String attr, List<Object> vals,boolean isMultiValuedSearch){
        int maxLimit = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_ORDERQUEUE_LIMIT_KEY));
        String title="";
        if (criteria != null) {
            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                if (entry.getKey().equals("title")) {
                    title = entry.getValue();
                }
            }
        }
        List<DocData> results=new ArrayList<DocData>();
        try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams searchParams = new org.kuali.ole.docstore.common.search.SearchParams();
            searchParams.setPageSize(maxLimit);
            SearchResponse searchResponse = null;
            HashMap titleIdMap = new HashMap();
            //searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Title_display", title), ""));
            if(isMultiValuedSearch){
                boolean isTitleExist=false;
                if (StringUtils.isNotBlank(title)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),BibConstants.TITLE_SEARCH, title), "AND"));
                    isTitleExist=true;
                }
                if(CollectionUtils.isNotEmpty(vals)){
                    int loop = 0;
                    for (Object iv : vals) {
                        String id = iv.toString();
                        boolean isIdExists = titleIdMap.containsValue(id);
                        if(isTitleExist){
                            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), ItemConstants.BIB_IDENTIFIER, id), "AND"));
                        } else {
                            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), ItemConstants.BIB_IDENTIFIER, id), "OR"));
                        }

                        loop++;
                        if (loop == maxLimit)
                            break;
                    }
                }

                //searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), BibConstants.TITLE_SEARCH, title), ""));
            } else {
                if (StringUtils.isNotBlank(title)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition("any", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),BibConstants.TITLE_SEARCH, title), ""));
                }
            }

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), ItemConstants.BIB_IDENTIFIER));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Bib.TITLE));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Bib.AUTHOR));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.PUBLICATIONDATE_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.PUBLISHER_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.ISBN_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.LOCALID_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.UNIQUE_ID));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ID));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.FORMAT_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.DOC_FORMAT));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.DOC_CATEGORY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), BibConstants.DOC_TYPE));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), "format"));


            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                DocData docData=new DocData();
                docData=buildDocInfoBean(docData,searchResult);
                results.add(docData);
            }
        } catch (Exception ex) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
            LOG.error(org.kuali.ole.OLEConstants.ITEM_EXIST + ex);
        }
        return results;
    }

    private DocData buildDocInfoBean(DocData data, SearchResult searchResult) {

        if (CollectionUtils.isNotEmpty(searchResult.getSearchResultFields())) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                if (StringUtils.isNotBlank(searchResultField.getFieldName())) {
                    if (searchResultField.getFieldName().equalsIgnoreCase(Bib.BIBIDENTIFIER)) {
                        data.setBibIdentifier(searchResultField.getFieldValue());
                        data.setUniqueId(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(Bib.TITLE)) {
                        data.setTitle(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(Bib.AUTHOR)) {
                        data.setAuthor(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.ISBN_DISPLAY)) {
                        data.setIsbn(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.LOCALID_DISPLAY)) {
                        data.setLocalIdentifier(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.UNIQUE_ID)) {
                        //data.setUniqueId(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.PUBLICATIONDATE_DISPLAY)) {
                        data.setPublicationDate(searchResultField.getFieldValue());
                    }else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.PUBLISHER_DISPLAY)) {
                        data.setPublisher(searchResultField.getFieldValue());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.FORMAT_DISPLAY)) {
                        data.setFormat(searchResultField.getFieldValue());
                    }else if (searchResultField.getFieldName().equalsIgnoreCase(BibConstants.DOC_FORMAT)) {
                        data.setFormat(searchResultField.getFieldValue());
                    }
                }
            }
        }
        return data;
    }

    /**
     * This method populates and returns a list of DocData from docstore search result
     *
     * @param docInfoBeanList
     * @return
     * @throws Exception
     */
    private List<DocData> getDocResult(List<DocInfoBean> docInfoBeanList) throws Exception {
        LOG.debug("Inside getDocResult of OleDocStoreSearchService");
        List<DocData> ress = new ArrayList<DocData>(0);
        DocData res = new DocData();
        for (DocInfoBean docInfoBean : docInfoBeanList) {
            res = new DocData(docInfoBean.getAuthor_display(), docInfoBean.getTitle_display(), docInfoBean.getIsbn_display(), docInfoBean.getLocalIdentifier_search(), docInfoBean.getPublisher_search(), docInfoBean.getPublicationPlace_search(), docInfoBean.getPublicationDate_search(), docInfoBean.getDocType(), docInfoBean.getPrice_f(), docInfoBean.getTitleId(), docInfoBean.getUniqueId(), docInfoBean.getBibIdentifier());
            ress.add(res);
        }
        LOG.debug("Leaving getDocResult of OleDocStoreSearchService");
        return ress;
    }

    /**
     * This method invokes search with bib fields.
     * @see org.kuali.rice.krad.dao.LookupDao#findCollectionBySearchHelper(java.lang.Class, java.util.Map, boolean, boolean)
     */
  /*  public Collection findCollectionBySearchHelper(Class arg0, Map arg1, boolean arg2, boolean arg3) {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Inside findCollectionBySearchHelper of OleDocStoreSearchService");
        }
        try{
            return getDocResult(arg1);
        }catch(Exception e){
            return null;
        }
    }

    *//**
     * This method invokes search with bib fields.
     * @see org.kuali.rice.krad.dao.LookupDao#findCollectionBySearchHelper(java.lang.Class, java.util.Map, boolean, boolean, java.lang.Object)
     *//*
    public Collection findCollectionBySearchHelper(Class arg0, Map arg1, boolean arg2, boolean arg3, Object arg4) {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Inside findCollectionBySearchHelper of OleDocStoreSearchService");
        }
        try{
            return getDocResult(arg1);
        }catch(Exception e){
            return null;
        }
    }*/

    
/*    public Long findCountByMap(Object arg0, Map arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Object findObjectByMap(Object arg0, Map arg1) {
        // TODO Auto-generated method stub
        return null;
    }*/

    /**
     * This method invokes docstore search query with multiple document uuids
     *
     * @param cl
     * @param attr
     * @param vals
     * @return
     * @throws Exception
     */
    public List getResult(Class cl, String attr, List<Object> vals) throws Exception {
        LOG.debug("Inside getResult of OleDocStoreSearchService");
        List result = new ArrayList(0);
        int maxLimit = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_ORDERQUEUE_LIMIT_KEY));

        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);

        StringBuilder query = new StringBuilder("q=");
        query.append("(DocType:bibliographic AND (");
        HashMap titleIdMap = new HashMap();
        int loop = 0;
        for (Object iv : vals) {
            String id = iv.toString();
            boolean isIdExists = titleIdMap.containsValue(id);
            titleIdMap.put(id, id);
            if (!isIdExists) {
                if (loop != 0) query.append(" OR ");
                query.append("id:" + id + " ");
                loop++;
            }
            if (loop == maxLimit)
                break;
        }
        query.append("))");
        // Changes to include userId in docstore URl.
        if (GlobalVariables.getUserSession() != null) {
            query.append("&userId=" + GlobalVariables.getUserSession().getPerson().getPrincipalName());
        }
        query.append("&rows=" + loop);
        if (LOG.isDebugEnabled())
            LOG.debug("Doc Store Query :" + query.toString());

        if (loop > 0) {
            docInfoBeanList = getResponse(query.toString());
            result = getDocResult(docInfoBeanList);
        }
        LOG.debug("Leaving getResult of OleDocStoreSearchService");
        return result;
    }

    /**
     * This method populates bib related values based on the document uuids using docstore search.
     *
     * @see org.kuali.ole.select.service.OleDocStoreSearchService#getResult(java.lang.Class, java.util.Map)
     */
    public List getResult(Class cl, Map<String, List<Object>> val) {
        LOG.debug("Inside getResult of OleDocStoreSearchService");
        List resul = new ArrayList(0);
        for (String key : val.keySet()) {
            List ll;
            try {
                ll = getResult(cl, key, val.get(key));
                if (ll != null && ll.size() > 0)
                    resul.addAll(ll);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        LOG.debug("Leaving getResult of OleDocStoreSearchService");
        return resul;

    }

    /**
     * This method invokes getResult when there is no search based on bib
     * and invokes getDocResult when we have any bib related fields in search.
     *
     * @see org.kuali.ole.select.service.OleDocStoreSearchService#getResult(java.lang.Class, java.lang.String, java.util.List, java.util.Map)
     */
    public List getResult(Class cl, String attr, List<Object> val, Map vas) {
        LOG.debug("Inside getResult of OleDocStoreSearchService");
        try {
            if (vas == null || vas.size() < 1) {
                return getResult(cl, attr, val);
            } else {
                return getDocResult(vas);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method invokes docstore call for multiple document uuids
     *
     * @param query
     * @return
     */
    private List<DocInfoBean> getResponse(String query) {
        LOG.debug("Inside getResponse of OleDocStoreSearchService");
        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);
        BuildDocInfoBean buildDocInfoBean = new BuildDocInfoBean();
        docInfoBeanList = buildDocInfoBean.getDocInfoBeanList(query);
        LOG.debug("Leaving getResponse of OleDocStoreSearchService");
        return docInfoBeanList;
    }

    public String getBibUUID(String itemTitleId) {
        String uuid = null;
        if (itemTitleId != null) {
            HashMap<String, String> queryMap = new HashMap<String, String>();
            queryMap.put(OleSelectConstant.DocStoreDetails.ITEMLINKS_KEY, itemTitleId);
            List<DocInfoBean> docStoreResult;
            try {
                docStoreResult = getBibInfoWrapperService().searchBibInfo(queryMap);
                Iterator bibIdIterator = docStoreResult.iterator();
                if (bibIdIterator.hasNext()) {
                    DocInfoBean docInfoBean = (DocInfoBean) bibIdIterator.next();
                    if (docInfoBean.getBibIdentifier() == null) {
                        uuid = docInfoBean.getUniqueId();
                        return uuid;

                    } else {
                        uuid = docInfoBean.getBibIdentifier();
                        return uuid;
                    }
                }
            } catch (Exception ex) {
                // TODO Auto-generated catch block
                throw new RuntimeException();
            }
        }
        return uuid;
    }


}
