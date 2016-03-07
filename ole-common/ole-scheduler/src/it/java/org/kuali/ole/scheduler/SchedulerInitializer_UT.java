package org.kuali.ole.scheduler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

public class SchedulerInitializer_UT {
    private SchedulerInitializer schedulerInitializer;

    @Before
    public void setUp() {
        schedulerInitializer = new SchedulerInitializer();
        schedulerInitializer.setUrlBase("http://localhost:8080/olefs");
    }

    @Test
    public void init() throws ServletException {
        schedulerInitializer.init();
        Assert.assertTrue(true);
    }
}
