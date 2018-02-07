/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.config.at;

import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.kuali.maven.common.MvnContext;
import org.kuali.maven.common.MvnExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory
import org.codehaus.plexus.util.cli.WriterStreamConsumer
import org.apache.commons.lang.StringUtils;

class OutputAwareMvnExecutor extends MvnExecutor {

    private static final Logger log = LoggerFactory.getLogger(OutputAwareMvnExecutor.class);

    @Override
    protected void executePom(MvnContext context) throws Exception {
        File tempPom = null;
        try {
            StreamConsumer stdout = newOutStreamConsumer(context);
            StreamConsumer stderr = newErrStreamConsumer(context);
            Commandline cl = getCommandLine(context);
            showConfig(context, cl);
            tempPom = prepareFileSystem(context, cl);
            if (!context.isSilent() && !context.isQuiet()) {
                log.info(cl.toString());
            }
            int exitValue = CommandLineUtils.executeCommandLine(cl, stdout, stderr);
            validateExitValue(context, exitValue);
        } finally {
            if (context.isDeleteTempPom()) {
                deleteQuietly(tempPom);
            }
        }
    }

    @Override
    protected String getMvnExecutable(String executable) {
        if (!StringUtils.isBlank(executable)) {
            return executable;
        }
        String mavenHome = System.env["maven.home"];

        if (StringUtils.isBlank(mavenHome)) {
            mavenHome = System.env["m2.home"];
        }

        if (StringUtils.isBlank(mavenHome)) {
            mavenHome = System.env["MAVEN_HOME"];
        }

        if (StringUtils.isBlank(mavenHome)) {
            mavenHome = System.env["M2_HOME"];
        }

        if (StringUtils.isBlank(mavenHome)) {
            log.info("\${maven.home} or \${m2.home} or \${MAVEN_HOME} or \${M2_HOME} not set as env variable.  Using default executable '" + getActualExecutable() + "'");
            return getActualExecutable();
        } else {
            return mavenHome + File.separator + "bin" + File.separatorChar + getActualExecutable();
        }
    }

    StreamConsumer newOutStreamConsumer(MvnContext context) {
        if (context instanceof OutputAwareMvnContext) {
            return context.stdOutWriter != null ? new WriterStreamConsumer(context.stdOutWriter) : newSimpleConsumer()
        } else {
            return newSimpleConsumer()
        }
    }

    StreamConsumer newErrStreamConsumer(MvnContext context) {
        if (context instanceof OutputAwareMvnContext) {
            return context.stdErrWriter != null ? new WriterStreamConsumer(context.stdErrWriter) : newSimpleConsumer()
        } else {
            return newSimpleConsumer()
        }
    }

    StreamConsumer newSimpleConsumer() {
        return new DefaultConsumer();
    }
}

