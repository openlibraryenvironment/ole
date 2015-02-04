package org.kuali.ole.docstore.discovery.solr.work.bib;


import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.indexer.solr.DocumentIndexerManagerFactory;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 2/3/12
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocBuilder {
    private String publicationDateRegex = "[0-9]{4}";

    public String extractPublicationDateWithRegex(String publicationDate) {
        Pattern pattern = Pattern.compile(publicationDateRegex);
        Matcher matcher = pattern.matcher(publicationDate);
        if (matcher.find()) {
            if (matcher.group(0).equalsIgnoreCase("0000")) {
                return "";
            }
            return matcher.group(0);
        } else {
            return "";
        }


    }

    /**
     * @param publicationDate
     * @param publicationEndDate
     * @return
     */
    public Object buildPublicationDateFacetValue(String publicationDate, String publicationEndDate) {
        int pubDat = 0;
        List<String> pubList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        if (publicationDate != null && publicationDate.length() == 4 && Integer.parseInt(publicationDate) <= year) {
            int pubStartDate = Integer.parseInt(publicationDate);
            if (publicationEndDate != null && publicationEndDate.length() == 4 && pubStartDate < Integer
                    .parseInt(publicationEndDate)) {
                if (Integer.parseInt(publicationEndDate) > year) {
                    publicationEndDate = String.valueOf(year);
                }
                int pubEndDate = Integer.parseInt(publicationEndDate);
                while (pubStartDate < pubEndDate) {
                    pubStartDate = (pubStartDate / 10) * 10;
                    if (pubStartDate == 0) {
                        pubList.add("Date could not be determined");
                    } else {
                        pubList.add(String.valueOf(pubStartDate) + "s");
                    }
                    pubStartDate = pubStartDate + 10;
                }
                pubStartDate = Integer.parseInt(publicationDate);
                pubEndDate = Integer.parseInt(publicationEndDate);
                while (pubStartDate < pubEndDate) {
                    pubStartDate = (pubStartDate) / 100;
                    pubDat = (pubStartDate) + 1;
                    pubCentury(pubDat, pubList);
                    pubStartDate = pubStartDate * 100 + 100;
                }
            } else {
                pubDat = (pubStartDate / 10) * 10;
                int pubCen = ((pubStartDate) / 100) + 1;
                if (pubDat == 0) {
                    pubList.add("Date could not be determined");
                } else {
                    pubList.add(String.valueOf(pubDat) + "s");
                    pubCentury(pubCen, pubList);
                }
            }
        } else {
            pubList.add("Date could not be determined");
        }
        return pubList;
    }


    /**
     * @param pubCen
     * @param pubList
     */
    private void pubCentury(int pubCen, List<String> pubList) {
        String pubCentury = String.valueOf(pubCen);
        if (pubCentury.endsWith("1")) {
            if (pubCentury.equalsIgnoreCase("11")) {
                pubList.add(pubCentury + "th Century");
            } else {
                pubList.add(pubCentury + "st Century");
            }
        } else if (pubCentury.endsWith("2")) {
            if (pubCentury.equalsIgnoreCase("12")) {
                pubList.add(pubCentury + "th Century");
            } else {
                pubList.add(pubCentury + "nd Century");
            }
        } else if (pubCentury.endsWith("3")) {
            if (pubCentury.equalsIgnoreCase("13")) {
                pubList.add(pubCentury + "th Century");
            } else {
                pubList.add(pubCentury + "rd Century");
            }
        } else {
            pubList.add(pubCentury + "th Century");
        }

    }

    /**
     * Builds the facet values for the given publication dates.
     *
     * @param publicationDates
     * @return
     */
    public List<String> buildPublicationDateFacetValues(List<String> publicationDates) {
        List<String> valueList = null;
        if (!CollectionUtils.isEmpty(publicationDates)) {
            valueList = new ArrayList<String>(publicationDates.size());
            for (int i = 0; i < publicationDates.size(); i++) {
                String pubDate = publicationDates.get(i);
                Object pubDt = buildPublicationDateFacetValue(pubDate, "");
                if (pubDt instanceof String) {
                    valueList.add((String) pubDt);
                } else if (pubDt instanceof List) {
                    List<String> pubDateList = (List<String>) pubDt;
                    for (String pubDtVal : pubDateList) {
                        valueList.add(pubDtVal);
                    }
                }
            }
        }
        return valueList;
    }


    public String getSortString(String str) {
        String ret = "";
        StringBuffer sortString = new StringBuffer();
        ret = str.toLowerCase();
        ret = ret.replaceAll("[\\-\\/]", " ");
        ret = ret.replace("&lt;", "");
        ret = ret.replace("&gt;", "");
        ret = ret.replaceAll("[\\.\\,\\;\\:\\(\\)\\{\\}\\'\\!\\?\\\"\\<\\>\\[\\]]", "");
        ret = Normalizer.normalize(ret, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        ret = ret.replaceAll("\\s+", " ");
        sortString.append(ret);
        sortString.append(" /r/n!@#$");
        sortString.append(str);
        return sortString.toString();
    }

    public List<String> getSortString(List<String> list) {
        List<String> sortStringList = new ArrayList<String>();
        for (String str : list) {
            sortStringList.add(getSortString(str));
        }
        return sortStringList;
    }

    public IndexerService getIndexerService(RequestDocument requestDocument) {
        IndexerService indexerService = DocumentIndexerManagerFactory.getInstance().getDocumentIndexManager(requestDocument.getCategory(), requestDocument.getType(), requestDocument.getFormat());
        return indexerService;
    }

}
