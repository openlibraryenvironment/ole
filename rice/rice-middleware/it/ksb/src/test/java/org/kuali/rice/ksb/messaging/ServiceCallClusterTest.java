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
package org.kuali.rice.ksb.messaging;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.messaging.bam.BAMTargetEntry;
import org.kuali.rice.ksb.messaging.remotedservices.GenericTestService;
import org.kuali.rice.ksb.messaging.remotedservices.SOAPService;
import org.kuali.rice.ksb.messaging.remotedservices.TestServiceInterface;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;


/**
 * Verify services in a cluster are being both being called
 * Verify a locally deployed service is always called instead of a remote service in a cluster
 * Verify that a service in a cluster fails over when one of the services in the cluster goes down.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceCallClusterTest extends KSBTestCase {

	private final static int SERVICE_CALLS = 15;
	
	public boolean startClient1() {
		return true;
	}
	
	public boolean startClient2() {
		return true;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		KsbApiServiceLocator.getServiceBus().synchronize();
	}

	@Test public void testSOAPClustering() throws Exception {
		QName serviceName = new QName("testNameSpace", "soap-cluster-test");
		int i = 0;
		List<SOAPService> services = new ArrayList<SOAPService>();
		while (i < SERVICE_CALLS) {
			i++;
			services.add((SOAPService)GlobalResourceLoader.getService(serviceName));
		}
		
		for (SOAPService service : services) {
			service.doTheThing("testing one two three");
		}
		
		String server1Name = "TestClient1";
		String server2Name = "TestClient2";
		boolean server1Called = false;
		boolean server2Called = false;
		//verify clustering is happening through bam
		List<BAMTargetEntry> bams = KSBServiceLocator.getBAMService().getCallsForService(serviceName);
		for (BAMTargetEntry bam : bams) {
			System.out.println("Found bam service URL of " + bam.getServiceURL());
			if (bam.getServiceURL().contains(server1Name)) {
				server1Called = true;
			} else if (bam.getServiceURL().contains(server2Name)) {
				server2Called = true;
			}
		}
		
		assertTrue(server1Called);
		assertTrue(server2Called);
	}
	
	@Test public void testClustering() throws Exception {
		QName serviceName = new QName("KEW", "testServiceFailover");
		List<TestServiceInterface> services = new ArrayList<TestServiceInterface>();
		int i = 0;
		while (i < SERVICE_CALLS) {
			i++;
			services.add((TestServiceInterface) GlobalResourceLoader.getService(serviceName));
		}
		
		for (TestServiceInterface service : services) {
			service.invoke();
		}
		
		String server1Name = "TestClient1";
		String server2Name = "TestClient2";
		boolean server1Called = false;
		boolean server2Called = false;
		//verify clustering is happening
		List<BAMTargetEntry> bams = KSBServiceLocator.getBAMService().getCallsForService(serviceName);
		for (BAMTargetEntry bam : bams) {
			if (bam.getServiceURL().contains(server1Name)) {
				server1Called = true;
			} else if (bam.getServiceURL().contains(server2Name)) {
				server2Called = true;
			}
		}
		
		assertTrue(server1Called);
		assertTrue(server2Called);
	}

    /**
     *  The services published to the service def table when ServiceCallClusterTest runs used to have
     *  have two different application IDs, TestCl1 and TestCl2.  A fix was contributed from IU so that when a
     *  service fails, it fails over to services only using the same application ID.   This is correct, but it made
     *  the tests no longer work.  The tests now bring up services with the same application ID, TestCl1.
     */
	@Test public void testServiceFailOver() throws Exception {
		QName serviceName = new QName("KEW", "testServiceFailover");
		List<TestServiceInterface> services = new ArrayList<TestServiceInterface>();
		int i = 0;
		while (i < SERVICE_CALLS) {
			i++;
			services.add((TestServiceInterface)GlobalResourceLoader.getService(serviceName));
		}
		
		String server1Name = "TestClient1";
		String server2Name = "TestClient2";
		boolean server1Called = false;
		boolean server2Called = false;
		//stop the server 1 and verify all calls going to server 2
		try {
			getTestClient1().stop();	
		} catch (Throwable t) {
			// this is okay
		}
		
		
		for (TestServiceInterface service : services) {
			service.invoke();
		}
		
		List<BAMTargetEntry> bams = KSBServiceLocator.getBAMService().getCallsForService(serviceName);
		for (BAMTargetEntry bam : bams) {
			if (bam.getServiceURL().contains(server1Name) && bam.getServerInvocation()) {
				server1Called = true;
			} else if (bam.getServiceURL().contains(server2Name)) {
				server2Called = true;
			}
		}
		
		assertFalse("server1 should not have been called", server1Called);
		assertTrue("server2 should have been called", server2Called);
	}

	
	@Test public void testSOAPFailOver() throws Exception {
		QName serviceName = new QName("testNameSpace", "soap-cluster-test");
		int i = 0;
		List<SOAPService> services = new ArrayList<SOAPService>();
		while (i < SERVICE_CALLS) {
			i++;
			services.add((SOAPService)GlobalResourceLoader.getService(serviceName));
		}
		
//		stop the server 1 and verify all calls going to server 2
		try {
			getTestClient1().stop();	
		} catch (Throwable t) {
		    // this is okay
		}
		
		for (SOAPService service : services) {
			service.doTheThing("testing one two three");
		}
		
		String server1Name = "TestClient1";
		String server2Name = "TestClient2";
		boolean server1Called = false;
		boolean server2Called = false;
		//verify clustering is happening through bam
		List<BAMTargetEntry> bams = KSBServiceLocator.getBAMService().getCallsForService(serviceName);
		for (BAMTargetEntry bam : bams) {
			if (bam.getServerInvocation() && bam.getServiceURL().contains(server1Name)) {
				server1Called = true;
			} else if (bam.getServerInvocation() && bam.getServiceURL().contains(server2Name)) {
				server2Called = true;
			}
		}
		
		assertFalse(server1Called);
		assertTrue(server2Called);
	}

	@Test public void testDefaultToLocalService() throws Exception {
		QName serviceName = new QName("KEW", "testLocalServiceFavoriteCall");
		List<TestServiceInterface> services = new ArrayList<TestServiceInterface>();
		int i = 0;
		while (i < SERVICE_CALLS) {
			i++;
			services.add((TestServiceInterface)GlobalResourceLoader.getService(serviceName));
		}
		
		for (TestServiceInterface service : services) {
			service.invoke();
		}
		
		String testHarness = "en-test";
		String server1Name = "TestClient1";
		boolean localCalled = false;
		boolean server1Called = false;
		//verify clustering is happening
		List<BAMTargetEntry> bams = KSBServiceLocator.getBAMService().getCallsForService(serviceName);
		for (BAMTargetEntry bam : bams) {
			if (bam.getServiceURL().contains(server1Name)) {
				server1Called = true;
			} else if (bam.getServiceURL().contains(testHarness)) {
				localCalled = true;
			}
		}
		
		assertFalse("BAM should not have recorded locally called services", localCalled);
		assertFalse("Remotely deployed service should have never been called in favor of remote service", server1Called);
		
		assertTrue("Service should have been called locally", GenericTestService.NUM_CALLS > 0);
	}
	
}
