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

import java.util.ArrayList;
import java.util.List;

/** utility class for copying controls. */
final class ControlCopy {

    private ControlCopy() {
        throw new IllegalArgumentException("do not call.");
    }

    public static RemotableAbstractControl.Builder toBuilder(RemotableControlContract c) {
        if (c == null) {
            throw new IllegalArgumentException("c is null");
        }
        if (c instanceof RemotableCheckbox || c instanceof RemotableCheckbox.Builder) return RemotableCheckbox.Builder.create();
        if (c instanceof RemotableCheckboxGroup || c instanceof RemotableCheckboxGroup.Builder) return RemotableCheckboxGroup.Builder.create(((KeyLabeled)c).getKeyLabels());
        if (c instanceof RemotableHiddenInput || c instanceof RemotableHiddenInput.Builder) return RemotableHiddenInput.Builder.create();
        if (c instanceof RemotablePasswordInput || c instanceof RemotablePasswordInput.Builder){
            RemotablePasswordInput.Builder b = RemotablePasswordInput.Builder.create();
            b.setSize(((Sized) c).getSize());
            return b;
        }
        if (c instanceof RemotableRadioButtonGroup || c instanceof RemotableRadioButtonGroup.Builder) return RemotableRadioButtonGroup.Builder.create(((KeyLabeled)c).getKeyLabels());
        if (c instanceof RemotableSelect) {
            RemotableSelect sc = (RemotableSelect) c;
            RemotableSelect.Builder b = RemotableSelect.Builder.create(sc.getKeyLabels());

            final List<RemotableSelectGroup.Builder> temp = new ArrayList<RemotableSelectGroup.Builder>();
            if (sc.getGroups() != null) {
                for (RemotableSelectGroup attr : sc.getGroups()) {
                    temp.add(RemotableSelectGroup.Builder.create(attr.getKeyLabels(), attr.getLabel()));
                }
            }

            b.setGroups(temp);
            b.setSize(sc.getSize());
            b.setMultiple(sc.isMultiple());
            return b;
        }
        if (c instanceof RemotableSelect.Builder) {
            RemotableSelect.Builder sc = (RemotableSelect.Builder) c;
            RemotableSelect.Builder b = RemotableSelect.Builder.create(sc.getKeyLabels());
            b.setGroups(sc.getGroups());
            b.setSize(sc.getSize());
            b.setMultiple(sc.isMultiple());
            return b;
        }
        if(c instanceof RemotableTextarea || c instanceof RemotableTextarea.Builder) {
            RemotableTextarea.Builder b = RemotableTextarea.Builder.create();
            b.setWatermark(((Watermarked) c).getWatermark());
            b.setCols(((RowsCols) c).getCols());
            b.setRows(((RowsCols) c).getRows());
            return b;
        }
        if(c instanceof RemotableTextInput || c instanceof RemotableTextInput.Builder) {
            RemotableTextInput.Builder b = RemotableTextInput.Builder.create();
            b.setWatermark(((Watermarked) c).getWatermark());
            b.setSize(((Sized) c).getSize());
            return b;
        }
        throw new UnsupportedOperationException(c.getClass().getName() + " not supported");
    }
}
