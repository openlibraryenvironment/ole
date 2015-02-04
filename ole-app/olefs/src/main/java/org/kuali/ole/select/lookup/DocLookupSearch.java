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
package org.kuali.ole.select.lookup;

import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.businessobject.DocInfoBean;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.impl.BibInfoWrapperServiceImpl;
import org.kuali.ole.select.service.impl.BuildDocInfoBean;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.util.*;


@NonTransactional
public class DocLookupSearch extends PlatformAwareDaoBaseOjb implements IDocLookupSearch {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocLookupSearch.class);
    static HashMap<String, String> cache = new HashMap<String, String>();
    private DocstoreClientLocator docstoreClientLocator;

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
    }*/

    private List<DocData> getAllData() {
        List<DocData> ress = new ArrayList<DocData>(0);
        Iterator<String> iter = cache.values().iterator();
        for (String st : cache.values()) {
            String[] vals = st.split("\\^");
            DocData ss = new DocData(vals[0], vals[1], vals[2], vals[3], vals[4], vals[5], vals[6], vals[7], vals[8], vals[9], vals[10], vals[11]);
            ress.add(ss);
        }
        return ress;
    }

    private void setCache(Map arg1) throws Exception {
        BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        List<BibInfoBean> bibInfoBeanList = docStore.searchBibInfo(arg1);
        for (BibInfoBean bean : bibInfoBeanList) {
            String bibString = bean.getAuthor() + "^" + bean.getTitle() + "^" + bean.getStandardNumber() + "^" + bean.getPublisher() + "^" + bean.getLocalIdentifier() + "^" + bean.getPlaceOfPublication() + "^" + bean.getYearOfPublication() + "^" + bean.getFormat() + "^" + bean.getPrice() + "^" + bean.getTitleId();
            cache.put(bean.getTitleId(), bibString);
        }
    }

    /*
        private List<DocData> getDocResult(Map arg1) throws Exception {

        BibInfoBean bibInfoBean = new BibInfoBean();
        bibInfoBean.setTitleId((String) arg1.get("titleId"));
        BibInfoWrapperService docStore = SpringContext.getBean(BibInfoWrapperServiceImpl.class);
        List<DocInfoBean> docStoreResult = docStore.searchBibInfo(bibInfoBean);
        List<DocData> ress = new ArrayList<DocData>(0);
        ress = getDocResult(docStoreResult);
        return ress;
        }

        private List<DocData> getDocResult(Bib bib) throws Exception {
           List<DocData> ress = new ArrayList<DocData>(0);
            DocData res = new DocData();
            for (DocInfoBean docInfoBean : docInfoBeanList) {
                res = new DocData(docInfoBean.getAuthor_display(), docInfoBean.getTitle_display().trim(), docInfoBean.getIsbn_display(), docInfoBean.getLocalIdentifier_search(), docInfoBean.getPublisher_display(), docInfoBean.getPublicationPlace_search(), docInfoBean.getDateOfPublication(), docInfoBean.getDocType(), docInfoBean.getPrice_f(), docInfoBean.getTitleId(), docInfoBean.getUniqueId(), docInfoBean.getBibIdentifier());
                ress.add(res);
            }
            return ress;
        }
        */
    private List<DocData> getDocResult(Map arg1) throws Exception {
        BibInfoBean bibInfoBean = new BibInfoBean();
        List<Bib> bibs=new ArrayList<>();
        bibInfoBean.setTitleId((String) arg1.get("titleId"));
        Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib((String) arg1.get("titleId"));
        List<DocData> ress = new ArrayList<DocData>(0);
        bibs.add(bib);
        ress = getDocResult(bibs);
        return ress;
    }

    private List<DocData> getDocResult(List<Bib> bibs) throws Exception {
        List<DocData> ress = new ArrayList<DocData>(0);
        for(Bib bib:bibs){
        DocData res = new DocData(bib.getAuthor(), bib.getTitle(), bib.getIsbn(), bib.getLocalId(), bib.getPublisher(), "", bib.getPublicationDate(), bib.getType(), "", bib.getId(), bib.getId(), bib.getId());
        ress.add(res);
        }
        return ress;
    }
/*    public Collection findCollectionBySearchHelper(Class arg0, Map arg1, boolean arg2, boolean arg3) {
        try{
            return getDocResult(arg1);
        }catch(Exception e){
            return null;
        }
    }

    
    public Collection findCollectionBySearchHelper(Class arg0, Map arg1, boolean arg2, boolean arg3, Object arg4) {
        try{
            return getDocResult(arg1);
        }catch(Exception e){
            return null;
        }
    }

    
    public Long findCountByMap(Object arg0, Map arg1) {
        // TODO Auto-generated method stub
        return null;
    }*/


    public Object findObjectByMap(Object arg0, Map arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    public List getResult(Class cl, String attr, List<Object> vals) throws Exception {
        List result = new ArrayList(0);
        int maxLimit = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_ORDERQUEUE_LIMIT_KEY));
        HashMap titleIdMap = new HashMap();
        List<String> bibIds=new ArrayList<>();
        List<Bib> bibs=new ArrayList<>();
        String bibId = null;
        for (Object iv : vals) {
            String id = iv.toString();
            boolean isIdExists = titleIdMap.containsValue(id);
            titleIdMap.put(id, id);
            if (!isIdExists) {
               bibIds.add(id);
            }
        }
        if(bibIds.size()>0){
            for(String id:bibIds){
                Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(id);
                bibs.add(bib);
            }
        }
        result = getDocResult(bibs);
        return result;
       /* List result = new ArrayList(0);
        int maxLimit = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_ORDERQUEUE_LIMIT_KEY));

        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);

        StringBuilder query = new StringBuilder("q=");
        query.append("(");
        HashMap titleIdMap = new HashMap();
        int loop = 0;
        for (Object iv : vals) {
            String id = iv.toString();
            boolean isIdExists = titleIdMap.containsValue(id);
            titleIdMap.put(id, id);
            if (!isIdExists) {
                if (loop != 0) query.append("OR");
                query.append("(id:" + id + ")");
                loop++;
            }
            if (loop == maxLimit)
                break;
        }
        query.append(")");
        query.append("&fl=Title,id,NameOfPublisher,MainEntryPersonalName");
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
        return result;*/
    }

    public List getResult(Class cl, Map<String, List<Object>> val) {
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
        return resul;

    }

    public List getResult(Class cl, String attr, List<Object> val, Map vas) {
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

    private List<DocInfoBean> getResponse(String query) {
        List<DocInfoBean> docInfoBeanList = new ArrayList<DocInfoBean>(0);
        BuildDocInfoBean buildDocInfoBean = new BuildDocInfoBean();
        docInfoBeanList = buildDocInfoBean.getDocInfoBeanList(query);
        return docInfoBeanList;
    }
}
