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
package org.kuali.rice.core.api.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class RiceUtilities {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RiceUtilities.class);

	private static String instanceIpAddress;
	private static String instanceHostName;
	
	private RiceUtilities() {
		throw new UnsupportedOperationException("do not call");
	}

    public static synchronized String getIpNumber() {
        if ( instanceIpAddress == null ) {
            // protect from running upon startup
            if ( ConfigContext.getCurrentContextConfig() != null ) {
                // attempt to load from environment
                String ip = System.getProperty("host.ip");
                if ( StringUtils.isBlank(ip) ) {
                    ip = ConfigContext.getCurrentContextConfig().getProperty("host.ip");
                }
                // if not set at all just return
                if ( StringUtils.isBlank(ip) ) {                    
                    return getCurrentEnvironmentNetworkIp();
                } else { 
                    // ok - it was set in configuration or by this method, set it permanently for this instance
                    instanceIpAddress = ip;
                }
            } else {
                // prior to startup, just return it
                return getCurrentEnvironmentNetworkIp();
            }
        }
        return instanceIpAddress;
    }

    /** * @return the current environment's IP address, taking into account the Internet connection to any of the available
	 *         machine's Network interfaces. Examples of the outputs can be in octatos or in IPV6 format.
	 *
	 *         fec0:0:0:9:213:e8ff:fef1:b717%4 siteLocal: true isLoopback: false isIPV6: true
	 *         ============================================ 130.212.150.216 <<<<<<<<<<<------------- This is the one we
	 *         want to grab so that we can. siteLocal: false address the DSP on the network. isLoopback: false isIPV6:
	 *         false ==> lo ============================================ 0:0:0:0:0:0:0:1%1 siteLocal: false isLoopback:
	 *         true isIPV6: true ============================================ 127.0.0.1 siteLocal: false isLoopback:
	 *         true isIPV6: false
	 */
	private static String getCurrentEnvironmentNetworkIp() {
	     Enumeration<NetworkInterface> netInterfaces = null;
	     try {
	          netInterfaces = NetworkInterface.getNetworkInterfaces();
	     } catch (SocketException e) {
	          LOG.error("Somehow we have a socket error...",e);
	          return "127.0.0.1";
	     }

	     while (netInterfaces.hasMoreElements()) {
	          NetworkInterface ni = netInterfaces.nextElement();
	          Enumeration<InetAddress> address = ni.getInetAddresses();
	          while (address.hasMoreElements()) {
	               InetAddress addr = address.nextElement();
	               if (!addr.isLoopbackAddress() && !addr.isSiteLocalAddress()
	                         && !(addr.getHostAddress().indexOf(":") > -1)) {
	                    return addr.getHostAddress();
	               }
	          }
	     }
	     try {
	          return InetAddress.getLocalHost().getHostAddress();
	     } catch (UnknownHostException e) {
	          return "127.0.0.1";
	     }
	}
	
	
	public static synchronized String getHostName() {
        if ( instanceHostName == null ) {
            try {
                // protect from running upon startup
                if ( ConfigContext.getCurrentContextConfig() != null ) {
                    String host = System.getProperty("host.name");
                    if ( StringUtils.isBlank(host) ) {
                        host = ConfigContext.getCurrentContextConfig().getProperty("host.name");
                    }
                    // if not set at all just return
                    if ( StringUtils.isBlank(host) ) {
                        return InetAddress.getByName( getCurrentEnvironmentNetworkIp() ).getHostName();
                    } else { 
                        // ok - it was set in configuration or by this method, set it permanently for this instance
                        instanceHostName = host;
                    }
                } else {
                    // prior to startup, just return it
                    return InetAddress.getByName( getCurrentEnvironmentNetworkIp() ).getHostName();
                }
            } catch ( Exception ex ) {
                return "localhost";
            }
        }
        return instanceHostName;
	}

	/**
	 * The standard Spring FileSystemResourceLoader does not support normal absolute file paths
	 * for historical backwards-compatibility reasons.  This class simply circumvents that behavior
	 * to allow proper interpretation of absolute paths (i.e. not stripping a leading slash)  
	 */
	private static class AbsoluteFileSystemResourceLoader extends FileSystemResourceLoader {
        @Override
        protected Resource getResourceByPath(String path) {
            return new FileSystemResource(path);
        }
	}

	/**
	 * Attempts to retrieve the resource stream.
	 * 
	 * @param resourceLoc resource location; syntax supported by {@link DefaultResourceLoader} 
	 * @return the resource stream or null if the resource could not be obtained
	 * @throws MalformedURLException
	 * @throws IOException
	 * @see DefaultResourceLoader
	 */
	public static InputStream getResourceAsStream(String resourceLoc) throws MalformedURLException, IOException {
	    Resource resource = getResource(resourceLoc);
	    if (resource.exists()) {
	        return resource.getInputStream();
	    }
	    return null;
    }
	
	/**
	 * Returns a handle to the requested Resource.
	 * 
	 * @param resourceLoc resource location; syntax supported by {@link DefaultResourceLoader}
	 * @return a handle to the Resource
	 */
	private static Resource getResource(String resourceLoc) {
		AbsoluteFileSystemResourceLoader resourceLoader = new AbsoluteFileSystemResourceLoader();
		resourceLoader.setClassLoader(ClassLoaderUtils.getDefaultClassLoader());
	    return resourceLoader.getResource(resourceLoc);
	}

	/**
     * This method searches for an exception of the specified type in the stack trace of the given
     * exception.
     * @param topLevelException the exception whose stack to traverse
     * @param exceptionClass the exception class to look for
     * @return the first instance of an exception of the specified class if found, or null otherwise
     */
    public static <T extends Throwable> T findExceptionInStack(Throwable topLevelException, Class<T> exceptionClass) {
        Throwable t = topLevelException;
        while (t != null) {
            if (exceptionClass.isAssignableFrom(t.getClass())) return (T) t;
            t = t.getCause();
        }
        return null;
    }
    
    /**
     * Converts a given file size in bytes to a unit of bytes, kilobyte,
     * megabyte, or gigabyte
     * 
     * @param fileSize
     *            - size in bytes
     * @return String with format 'size units'
     */
    public static String getFileSizeUnits(Long fileSize) {
        Long newFileSize = fileSize;
        String fileSizeUnits = "bytes";

        if (fileSize > 1024) {
            Long kiloSize = fileSize / 1024;

            if (kiloSize < 1024) {
                newFileSize = kiloSize;
                fileSizeUnits = "KB";
            } else {
                Long megaSize = kiloSize / 1024;

                if (megaSize < 1024) {
                    newFileSize = megaSize;
                    fileSizeUnits = "MB";
                } else {
                    Long gigaSize = megaSize / 1024;

                    newFileSize = gigaSize;
                    fileSizeUnits = "GB";
                }
            }
        }

        return newFileSize + fileSizeUnits;
    }
}
