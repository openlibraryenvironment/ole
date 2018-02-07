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
package org.kuali.rice.krad.web.form;

import org.kuali.rice.krad.uif.util.BreadcrumbItem;
import org.kuali.rice.krad.uif.util.UrlInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * HistoryFlow represents the a flow of urls.  When the flow is continued, the flow inherits the urls/breadcrumbs from
 * a previous flow.  Using a flow key it is possible to jump back to previous flows.
 */
public class HistoryFlow implements Serializable {
    protected Stack<UrlInfo> flowUrls = new Stack<UrlInfo>();
    protected String flowReturnPoint;
    protected String flowStartPoint;
    protected String flowKey;

    protected BreadcrumbItem currentViewItem;
    protected List<BreadcrumbItem> pastItems;

    /**
     * Initialize a new HistoryFlow with a key
     *
     * @param flowKey the flowKey to use
     */
    public HistoryFlow (String flowKey) {
        this.flowKey = flowKey;
    }

    /**
     * Get all urls in the HistoryFlow stack
     *
     * @return the stack of HistoryFlow urls
     */
    public Stack<UrlInfo> getFlowUrls() {
        return flowUrls;
    }

    /**
     * Set the flowUrls for this HistoryFlow
     *
     * @param flowUrls
     */
    public void setFlowUrls(Stack<UrlInfo> flowUrls) {
        this.flowUrls = flowUrls;
    }

    /**
     * The returnPoint used to jump back to a previous flow
     *
     * @return the flowReturnPoint
     */
    public String getFlowReturnPoint() {
        return flowReturnPoint;
    }

    /**
     * Set the returnPoint for use to jump back to the previous flow
     *
     * @param flowReturnPoint
     */
    public void setFlowReturnPoint(String flowReturnPoint) {
        this.flowReturnPoint = flowReturnPoint;
    }

    public String getFlowStartPoint() {
        return flowStartPoint;
    }

    public void setFlowStartPoint(String flowStartPoint) {
        this.flowStartPoint = flowStartPoint;
    }

    /**
     * The flowKey used to identify the HistoryFlow
     *
     * @return
     */
    public String getFlowKey() {
        return flowKey;
    }

    /**
     * Set the flowKey for this HistoryFlow
     *
     * @param flowKey
     */
    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }

    /**
     * Push the url onto the history stack
     *
     * @param url the url to push
     */
    public void push(String url){
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setHref(url);
        flowUrls.push(urlInfo);
    }

    /**
     * Push the url onto the history stack
     *
     * @param urlInfo the urlInfo object to push
     */
    public void push(UrlInfo urlInfo){
        flowUrls.push(urlInfo);
    }

    /**
     * Update the last url on the history stack with the new value
     *
     * @param url the url to update to
     */
    public void update(String url){
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setHref(url);

        if(flowUrls.empty()){
            flowUrls.push(urlInfo);
        }
        else{
            flowUrls.pop();
            flowUrls.push(urlInfo);
        }
    }

    /**
     * Update the last url on the history stack with the new value
     *
     * @param urlInfo the UrlInfo object to update to
     */
    public void update(UrlInfo urlInfo){
        if(flowUrls.empty()){
            flowUrls.push(urlInfo);
        }
        else{
            flowUrls.pop();
            flowUrls.push(urlInfo);
        }
    }

    /**
     * Get the last url on the history stack
     *
     * @return the last url on the history stack
     */
    public String getCurrentLocation(){
        if(flowUrls != null && !flowUrls.isEmpty()){
            return flowUrls.peek().getHref();
        }

        return null;
    }

    /**
     * Clear all urls on the history stack and accumulated breadcrumbs
     */
    public void clear(){
        flowUrls.clear();
        pastItems.clear();
        currentViewItem = null;
        flowReturnPoint = null;
    }

    /**
     * Continue a new flow from a previous flow.  This will copy the prevFlow's flow urls to flowUrls,
     * pastItems and currentViewItem to the new flow's pastItems, and set the flowReturnPoint to the currentLocation
     * of the prevFlow.
     *
     * @param prevFlow
     */
    public void continueFlow(HistoryFlow prevFlow){
        if(prevFlow != null){
            flowReturnPoint = prevFlow.getCurrentLocation();
            this.setFlowUrls(prevFlow.getFlowUrls());

            if(this.getFlowUrls() != null && !this.getFlowUrls().isEmpty()){
                flowStartPoint = this.getFlowUrls().firstElement().getHref();
            }

            pastItems = new ArrayList<BreadcrumbItem>();

            if (prevFlow.getPastItems() != null){
                pastItems.addAll(prevFlow.getPastItems());
            }

            if (prevFlow.getCurrentViewItem() != null){
                pastItems.add(prevFlow.getCurrentViewItem());
            }
        }
    }

    /**
     * Get the item which represents the most current BreadcrumbItem for the View for this flow
     *
     * @return the View BreadcrumbItem
     */
    public BreadcrumbItem getCurrentViewItem() {
        return currentViewItem;
    }

    /**
     * Set the current BreadcrumbItem
     *
     * @param currentViewItem
     */
    public void setCurrentViewItem(BreadcrumbItem currentViewItem) {
        this.currentViewItem = currentViewItem;
    }

    /**
     * Get all the past BreadcrumbItems (these represents the path to the current item)
     *
     * @return the past BreadcrumbItems
     */
    public List<BreadcrumbItem> getPastItems() {
        return pastItems;
    }

    /**
     * Set the past BreadcrumbItems
     *
     * @param pastItems
     */
    public void setPastItems(List<BreadcrumbItem> pastItems) {
        this.pastItems = pastItems;
    }
}
