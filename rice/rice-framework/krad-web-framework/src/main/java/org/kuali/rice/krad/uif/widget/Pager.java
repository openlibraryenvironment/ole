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
package org.kuali.rice.krad.uif.widget;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.view.View;

/**
 * The Pager widget is used to display a list of links horizontally in a page selection user interface.  The user can
 * select a page to jump to, go to prev/next page, or go to the first or last page.  This widget needs to know
 * the numberOfPages total, and the currentPage the user is on currently, so this widget must be fed this information
 * from the code.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.krad.uif.layout.StackedLayoutManager
 */
public class Pager extends WidgetBase {
    private String linkScript;
    private int maxNumberedLinksShown;
    private int numberOfPages;
    private int currentPage;
    private boolean renderPrevNext;
    private boolean renderFirstLast;

    protected int pagesStart;
    protected int pagesEnd;

    public Pager() {
        super();
    }

    /**
     * performFinalize calculates the pagesStart and pagesEnd properties (using numberOfPages, currentPage, and
     * maxNumberedLinksShown - these must be set) which determines pages shown by the widget
     *
     * @param view the current view
     * @param model the current model
     * @param parent parent container
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // if no pages or 1 page, do not render
        if (numberOfPages == 0 || numberOfPages == 1) {
            this.setRender(false);
        }

        if (maxNumberedLinksShown >= numberOfPages) {
            // Show all pages if possible to do so
            pagesStart = 1;
            pagesEnd = numberOfPages;
        } else {
            // Determine how many pages max shown before an after the current page
            int beforeAfterShown = (int) Math.floor((double) maxNumberedLinksShown / 2.0);
            pagesStart = currentPage - beforeAfterShown;
            pagesEnd = currentPage + beforeAfterShown;

            // If maxNumberedLinksShown is even and cannot have an equal amount of pages showing before
            // and after the current page, so trim one off the end
            if (pagesEnd - pagesStart == maxNumberedLinksShown) {
                pagesEnd = pagesEnd - 1;
            }

            // The pagesEnd is within range of numberOfPages total, therefore show the last pages
            if (pagesEnd > numberOfPages) {
                pagesEnd = numberOfPages;
                pagesStart = numberOfPages - maxNumberedLinksShown + 1;
            }

            // The pageStart is within range, therefore show the first pages
            if (pagesStart < 1) {
                pagesStart = 1;
                if (maxNumberedLinksShown < numberOfPages) {
                    pagesEnd = maxNumberedLinksShown;
                }
            }
        }

        this.linkScript = "e.preventDefault();" + this.linkScript;
    }

    /**
     * The script to execute when a link is clicked (should probably use the "this" var in most cases, to determine
     * page number selected - see retrieveStackedPage(linkElement, collectionId) js function)
     *
     * @return the script to execute when a link is clicked
     */
    public String getLinkScript() {
        return linkScript;
    }

    /**
     * Set the link js script
     *
     * @param linkScript the link js script
     */
    public void setLinkScript(String linkScript) {
        this.linkScript = linkScript;
    }

    /**
     * The maximum number of NUMBERED links shown at once for pages, if number of pages that exist exceed this value,
     * the pager omits some pages before and/or after the current page (which are revealed during while
     * navigating using a carousel effect)
     *
     * @return the maximum number of NUMBERED links to show
     */
    public int getMaxNumberedLinksShown() {
        return maxNumberedLinksShown;
    }

    /**
     * Set the maximum number of NUMBERED links shown
     *
     * @param maxNumberedLinksShown
     */
    public void setMaxNumberedLinksShown(int maxNumberedLinksShown) {
        this.maxNumberedLinksShown = maxNumberedLinksShown;
    }

    /**
     * Number of pages TOTAL that make up the component being paged (this must be set by the framework based on some
     * list size)
     *
     * @return the number of pages used in this pager
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Set the TOTAL number of pages
     *
     * @param numberOfPages
     */
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    /**
     * The current page being shown by this pager widget (this must be set when the page is changed)
     *
     * @return the current page being shown
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Set the current page
     *
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Returns true if this pager widget is rendering the "First" and "Last" links
     *
     * @return true if rendering "First" and "Last" links
     */
    public boolean isRenderFirstLast() {
        return renderFirstLast;
    }

    /**
     * Set renderFirstLast
     *
     * @param renderFirstLast
     */
    public void setRenderFirstLast(boolean renderFirstLast) {
        this.renderFirstLast = renderFirstLast;
    }

    /**
     * Returns true if this pager widget is rendering the "Prev" and "Next" links
     *
     * @return true if rendering "First" and "Last" links
     */
    public boolean isRenderPrevNext() {
        return renderPrevNext;
    }

    /**
     * Set renderPrevNext
     *
     * @param renderPrevNext
     */
    public void setRenderPrevNext(boolean renderPrevNext) {
        this.renderPrevNext = renderPrevNext;
    }

    /**
     * The first page number to render; this is set by the framework
     *
     * @return
     */
    public int getPagesStart() {
        return pagesStart;
    }

    /**
     * The last page number to render; this is set by the framework
     *
     * @return
     */
    public int getPagesEnd() {
        return pagesEnd;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Pager pagerCopy = (Pager) component;
        pagerCopy.setLinkScript(this.getLinkScript());
        pagerCopy.setMaxNumberedLinksShown(this.getMaxNumberedLinksShown());
        pagerCopy.setNumberOfPages(this.getNumberOfPages());
        pagerCopy.setCurrentPage(this.getCurrentPage());
        pagerCopy.setRenderPrevNext(this.isRenderPrevNext());
        pagerCopy.setRenderFirstLast(this.isRenderFirstLast());
        pagerCopy.pagesStart = this.pagesStart;
        pagerCopy.pagesEnd = this.pagesEnd;
    }
}
