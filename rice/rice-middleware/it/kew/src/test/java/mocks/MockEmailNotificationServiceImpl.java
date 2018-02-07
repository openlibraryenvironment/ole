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
package mocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.Mailer;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.mail.DailyEmailJob;
import org.kuali.rice.kew.mail.WeeklyEmailJob;
import org.kuali.rice.kew.mail.service.EmailContentService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.mail.service.impl.CustomizableActionListEmailServiceImpl;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;




public class MockEmailNotificationServiceImpl extends CustomizableActionListEmailServiceImpl implements MockEmailNotificationService {
    private static final Logger LOG = Logger.getLogger(MockEmailNotificationServiceImpl.class);

    private static Map<String,List> immediateReminders = new HashMap<String,List>();
    private static Map<String,Integer> aggregateReminderCount = new HashMap<String,Integer>();
    private boolean sendDailyReminderCalled = false;
    private boolean sendWeeklyReminderCalled = false;
    
    private static final String DAILY_TRIGGER_NAME = "Daily Email Trigger";
    private static final String DAILY_JOB_NAME = "Daily Email";
    private static final String WEEKLY_TRIGGER_NAME = "Weekly Email Trigger";
    private static final String WEEKLY_JOB_NAME = "Weekly Email";

    private EmailContentService contentService;
    private String deploymentEnvironment;

	private Mailer mailer;

    /**
     * Resets the reminder counts
     */
    public void resetReminderCounts() {
        aggregateReminderCount.clear();
        immediateReminders.clear();
    }

    /**
     * This overridden method will perform the standard operations from org.kuali.rice.kew.mail.ActionListEmailServiceImpl but will also keep track of action
     * items processed
     */
    @Override
    public void sendImmediateReminder(ActionItem actionItem, Boolean skipOnApprovals) {
        if (skipOnApprovals != null && skipOnApprovals.booleanValue()
                && actionItem.getActionRequestCd().equals(KewApiConstants.ACTION_REQUEST_APPROVE_REQ)) {
            LOG.debug("As requested, skipping immediate reminder notification on action item approval for " + actionItem.getPrincipalId());
            return;
        }
        List actionItemsSentUser = (List)immediateReminders.get(actionItem.getPrincipalId());
        Preferences preferences = getPreferencesService().getPreferences(actionItem.getPrincipalId());

        boolean shouldNotify = checkEmailNotificationPreferences(actionItem, preferences, KewApiConstants.EMAIL_RMNDR_IMMEDIATE);
        if(shouldNotify) {
            if (actionItemsSentUser == null) {
                actionItemsSentUser = new ArrayList();
                immediateReminders.put(actionItem.getPrincipalId(), actionItemsSentUser);
            }
            actionItemsSentUser.add(actionItem);
        }
    }

    /**
     * This overridden method returns a value of true always
     */
    //@Override
    protected boolean sendActionListEmailNotification() {

        return true;
    }

    @Override
	public void sendDailyReminder() {
        LOG.info("Sending daily reminder");
        try {
            getEmailContentGenerator().generateWeeklyReminder(null, null);
        }
        catch (NullPointerException npe) {}

        List<ActionItem> actionItems = new ArrayList<ActionItem>(1);
        actionItems.add(ActionItem.Builder.create("ai1", "ai2", "ai3", new DateTime(), "ai4", "ai5", "ai6", "ai7", "ai8").build());
        sendPeriodicReminder(null, actionItems, KewApiConstants.EMAIL_RMNDR_DAY_VAL);
        //super.sendDailyReminder();
        sendDailyReminderCalled = true;
    }

    @Override
    public boolean wasDailyReminderSent(){
        return this.sendDailyReminderCalled;
    }
    
    @Override
    public void sendWeeklyReminder() {
        LOG.info("Sending weekly reminder");
        try {
            getEmailContentGenerator().generateWeeklyReminder(null, null);
        }
        catch (NullPointerException npe) {}
        List<ActionItem> actionItems = new ArrayList<ActionItem>(1);
        actionItems.add(ActionItem.Builder.create("ai1", "ai2", "ai3", new DateTime(), "ai4", "ai5", "ai6", "ai7", "ai8").build());
        sendPeriodicReminder(null, actionItems, KewApiConstants.EMAIL_RMNDR_WEEK_VAL);
        //super.sendWeeklyReminder();
        sendWeeklyReminderCalled = true;
    }
    
    @Override
    public boolean wasWeeklyReminderSent(){
        return this.sendWeeklyReminderCalled;
    }

    @Override
    public void scheduleBatchEmailReminders() throws Exception {
        sendDailyReminderCalled = false;
        sendWeeklyReminderCalled = false;
        LOG.info("Scheduling Batch Email Reminders.");
        String emailBatchGroup = "Email Batch";
        String dailyCron = ConfigContext.getCurrentContextConfig()
                .getProperty(KewApiConstants.DAILY_EMAIL_CRON_EXPRESSION);
        if (!StringUtils.isBlank(dailyCron)) {
            LOG.info("Scheduling Daily Email batch with cron expression: " + dailyCron);
            CronTrigger dailyTrigger = new CronTrigger(DAILY_TRIGGER_NAME, emailBatchGroup, dailyCron);
            JobDetail dailyJobDetail = new JobDetail(DAILY_JOB_NAME, emailBatchGroup, DailyEmailJob.class);
            dailyTrigger.setJobName(dailyJobDetail.getName());
            dailyTrigger.setJobGroup(dailyJobDetail.getGroup());
            sendDailyReminderCalled = true;
        } else {
            LOG.warn("No " + KewApiConstants.DAILY_EMAIL_CRON_EXPRESSION
                    + " parameter was configured.  Daily Email batch was not scheduled!");
        }

        String weeklyCron = ConfigContext.getCurrentContextConfig().getProperty(
                KewApiConstants.WEEKLY_EMAIL_CRON_EXPRESSION);
        if (!StringUtils.isBlank(weeklyCron)) {
            LOG.info("Scheduling Weekly Email batch with cron expression: " + weeklyCron);
            CronTrigger weeklyTrigger = new CronTrigger(WEEKLY_TRIGGER_NAME, emailBatchGroup, weeklyCron);
            JobDetail weeklyJobDetail = new JobDetail(WEEKLY_JOB_NAME, emailBatchGroup, WeeklyEmailJob.class);
            weeklyTrigger.setJobName(weeklyJobDetail.getName());
            weeklyTrigger.setJobGroup(weeklyJobDetail.getGroup());
            sendWeeklyReminderCalled = true;
        } else {
            LOG.warn("No " + KewApiConstants.WEEKLY_EMAIL_CRON_EXPRESSION
                    + " parameter was configured.  Weekly Email batch was not scheduled!");
        }
    }

    //@Override
    protected void sendPeriodicReminder(Person user, Collection<ActionItem> actionItems, String emailSetting) {
        //super.sendPeriodicReminder(user, actionItems, emailSetting);
        if (!aggregateReminderCount.containsKey(emailSetting)) {
            aggregateReminderCount.put(emailSetting, actionItems.size());
        } else {
            aggregateReminderCount.put(emailSetting, aggregateReminderCount.get(emailSetting) + actionItems.size());
        }
    }

    public Integer getTotalPeriodicRemindersSent(String emailReminderConstant) {
        Integer returnVal = aggregateReminderCount.get(emailReminderConstant);
        if (returnVal == null) {
            returnVal = Integer.valueOf(0);
        }
        return returnVal;
    }

    public Integer getTotalPeriodicRemindersSent() {
        int total = 0;
        for (Map.Entry<String, Integer> mapEntry : aggregateReminderCount.entrySet()) {
            Integer value = mapEntry.getValue();
            total += (value == null) ? 0 : value.intValue();
        }
        return Integer.valueOf(total);
    }

    public boolean wasStyleServiceAccessed() {
        return getEmailContentGenerator().wasServiceAccessed();
    }

    private void resetStyleService() {
        getEmailContentGenerator().resetServiceAccessed();
    }

    public int immediateReminderEmailsSent(String networkId, String documentId, String actionRequestCd) {
        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(networkId);
        List actionItemsSentUser = immediateReminders.get(principal.getPrincipalId());
        if (actionItemsSentUser == null) {
            LOG.info("There are no immediate reminders for Principal " + networkId + " and Document ID " + documentId);
            return 0;
        }
        else {
            LOG.info("There are " + actionItemsSentUser.size() + " immediate reminders for Principal " + networkId + " and Document ID " + documentId);
        }
        int emailsSent = 0;
        for (Iterator iter = actionItemsSentUser.iterator(); iter.hasNext();) {
            ActionItem actionItem = (ActionItem) iter.next();
            if (actionItem.getDocumentId().equals(documentId) && actionItem.getActionRequestCd().equals(actionRequestCd)) {
                emailsSent++;
            }
        }
        LOG.info(emailsSent + "No immediate e-mails were sent to Principal " + networkId + " and Document ID " + documentId);
        return emailsSent;
    }

    public void setEmailContentGenerator(EmailContentService contentService) {
        this.contentService = contentService;
    }

    protected MockStyleableEmailContentService getEmailContentGenerator() {
        return (MockStyleableEmailContentService) contentService;
    }

	public void setMailer(Mailer mailer){
		this.mailer = mailer;
	}

	public void setDeploymentEnvironment(String deploymentEnvironment) {
		this.deploymentEnvironment = deploymentEnvironment;
	}

     private PreferencesService getPreferencesService() {
        return KewApiServiceLocator.getPreferencesService();
    }

}
