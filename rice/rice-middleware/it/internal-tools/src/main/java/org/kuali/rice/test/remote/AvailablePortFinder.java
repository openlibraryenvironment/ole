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
package org.kuali.rice.test.remote;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * <p>Finds currently available server ports.</p>
 *
 * <p>This class is a verbatim copy of AvailablePortFinder from the camel-test component of the Apache Camel project.
 * </p>
 *
 * @see <a href="http://svn.apache.org/viewvc/camel/trunk/components/camel-test/src/main/java/org/apache/camel/test/AvailablePortFinder.java?revision=1137595&view=co">IANA.org</a>
 */
public final class AvailablePortFinder {
    /**
     * The minimum server currentMinPort number. Set at 1024 to avoid returning privileged
     * currentMinPort numbers.
     */
    public static final int MIN_PORT_NUMBER = 1024;

    /**
     * The maximum server currentMinPort number.
     */
    public static final int MAX_PORT_NUMBER = 49151;


    /**
     * Incremented to the next lowest available port when getNextAvailable() is called.
     */
    private static AtomicInteger currentMinPort = new AtomicInteger(MIN_PORT_NUMBER);

    /**
     * Creates a new instance.
     */
    private AvailablePortFinder() {
        // Do nothing
    }

    /**
     * Gets the next available currentMinPort starting at the lowest currentMinPort number. This is the preferred
     * method to use. The port return is immediately marked in use and doesn't rely on the caller actually opening
     * the port.
     *
     * @throws NoSuchElementException if there are no ports available
     */
    public static synchronized int getNextAvailable() {
        int next = getNextAvailable(currentMinPort.get());
        currentMinPort.set(next + 1);
        return next;
    }

    /**
     * Gets the next available currentMinPort starting at a currentMinPort.
     *
     * @param fromPort the currentMinPort to scan for availability
     * @throws NoSuchElementException if there are no ports available
     */
    public static synchronized int getNextAvailable(int fromPort) {
        if (fromPort < currentMinPort.get() || fromPort > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start currentMinPort: " + fromPort);
        }

        for (int i = fromPort; i <= MAX_PORT_NUMBER; i++) {
            if (available(i)) {
                return i;
            }
        }

        throw new NoSuchElementException("Could not find an available currentMinPort above " + fromPort);
    }

    /**
     * Checks to see if a specific currentMinPort is available.
     *
     * @param port the currentMinPort to check for availability
     */
    public static boolean available(int port) {
        if (port < currentMinPort.get() || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start currentMinPort: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            // Do nothing
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
