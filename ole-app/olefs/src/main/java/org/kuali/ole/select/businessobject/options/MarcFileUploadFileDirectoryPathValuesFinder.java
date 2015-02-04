/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject.options;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.batch.BatchFileUtils;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MarcFileUploadFileDirectoryPathValuesFinder extends KeyValuesBase {

    protected OlePurapService olePurapService;

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    @Override
    public List<KeyValue> getKeyValues() {
        List<File> rootDirectories = BatchFileUtils.retrieveBatchFileLookupRootDirectories();
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        for (File rootDirectory : rootDirectories) {
            SubDirectoryWalker walker = new SubDirectoryWalker(keyValues);
            try {
                walker.addKeyValues(rootDirectory);
            } catch (IOException e) {
                throw new RuntimeException("IOException caught.", e);
            }
        }
        return keyValues;
    }


    protected class SubDirectoryWalker extends DirectoryWalker {
        private List<KeyValue> keyValues;
        private int recursiveDepth;
        private File rootDirectory;

        public SubDirectoryWalker(List<KeyValue> keyValues) {
            super(DirectoryFileFilter.DIRECTORY, -1);
            this.keyValues = keyValues;
            this.recursiveDepth = 0;
        }

        public void addKeyValues(File startDirectory) throws IOException {
            rootDirectory = startDirectory;
            walk(startDirectory, null);
        }

        /**
         * @see org.apache.commons.io.DirectoryWalker#handleDirectoryStart(java.io.File, int, java.util.Collection)
         */
        @Override
        protected void handleDirectoryStart(File directory, int depth, Collection results) throws IOException {
            String sourcePath = getOlePurapService().getParameter(OLEConstants.PARENT_FOLDER).trim();
            super.handleDirectoryStart(directory, depth, results);
            if (directory.getName() != null && !directory.getName().equalsIgnoreCase(sourcePath) && directory.getPath().contains(sourcePath)) {
                ConcreteKeyValue entry = new ConcreteKeyValue();
                entry.setKey(BatchFileUtils.pathRelativeToRootDirectory(directory.getAbsolutePath()));
                StringBuilder indent = new StringBuilder();
                indent.append(StringUtils.repeat("-", 4 * recursiveDepth - 8));
                indent.append(directory.getName());
                entry.setValue(indent.toString());
                keyValues.add(entry);
            }
            this.recursiveDepth++;
        }


        /**
         * @see org.apache.commons.io.DirectoryWalker#handleDirectoryEnd(java.io.File, int, java.util.Collection)
         */
        @Override
        protected void handleDirectoryEnd(File directory, int depth, Collection results) throws IOException {
            super.handleDirectoryEnd(directory, depth, results);
            this.recursiveDepth--;
        }
    }
}
