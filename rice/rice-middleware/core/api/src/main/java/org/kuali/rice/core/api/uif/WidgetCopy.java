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
package org.kuali.rice.core.api.uif;

/** utility class for copying widgets. */
final class WidgetCopy {

    private WidgetCopy() {
        throw new IllegalArgumentException("do not call.");
    }

    public static RemotableAbstractWidget.Builder toBuilder(RemotableWidgetContract w) {
        if (w == null) {
            throw new IllegalArgumentException("w is null");
        }

        if (w instanceof RemotableDatepicker || w instanceof RemotableDatepicker.Builder) return RemotableDatepicker.Builder.create();
        if (w instanceof RemotableTextExpand || w instanceof RemotableTextExpand.Builder) return RemotableTextExpand.Builder.create();

        if (w instanceof RemotableQuickFinder) {
            RemotableQuickFinder.Builder b = RemotableQuickFinder.Builder.create(((RemotableQuickFinder) w).getBaseLookupUrl(),  ((RemotableQuickFinder) w).getDataObjectClass());
            b.setFieldConversions(((RemotableQuickFinder) w).getFieldConversions());
            b.setLookupParameters(((RemotableQuickFinder) w).getLookupParameters());
            return b;
        } else if (w instanceof RemotableQuickFinder.Builder) {
            RemotableQuickFinder.Builder b = RemotableQuickFinder.Builder.create(((RemotableQuickFinder.Builder) w).getBaseLookupUrl(),  ((RemotableQuickFinder.Builder) w).getDataObjectClass());
            b.setFieldConversions(((RemotableQuickFinder.Builder) w).getFieldConversions());
            b.setLookupParameters(((RemotableQuickFinder.Builder) w).getLookupParameters());
            return b;
        }
        throw new UnsupportedOperationException(w.getClass().getName() + " not supported");
    }
}
