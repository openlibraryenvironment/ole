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
package org.kuali.rice.core.web.cache;

import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Collection;

public final class CacheAdminForm extends UifFormBase {
    private Tree<String, String> cacheTree = new Tree<String, String>();

    //it would be nice if this were a cacheTree of selected nodes so it doesn't have to be parsed
    private Collection<String> flush = new ArrayList<String>();

    public void setCacheTree(Tree<String, String> cacheTree) {
        this.cacheTree = cacheTree;
    }

    public Tree<String, String> getCacheTree() {
        return cacheTree;
    }

    public Collection<String> getFlush() {
        return flush;
    }

    public void setFlush(Collection<String> flush) {
        this.flush = flush;
    }
}
