var loopCount = 5;
var viewId = "${viewId}";
var page = "${pageId}"
var pageLoadedText = "Jump To Top Demo";
var baseUrl = "${baseurl}";
var testUrl = baseUrl + "portal.do?channelTitle=Uif%20Components%20%28Kitchen%20Sink%29&channelUrl="+baseUrl+"kr-krad/uicomponents?viewId="+viewId+"&methodToCall=start&readOnlyFields=field91"+page;
var timeoutSeconds = 360;
var logoutAtEnd = false;
var username = "loadtester" + test.getUserNum();
//var username = "${user}";

/**
 * Test is the main control interface for Neustar Web Performance Management scripts. It is
 * exposed here as the global variable 'test'. This object is effectively the "starting
 * point", as it serves as the interface for interacting with web sites (via
 * test.openBrowser() and test.openHttpClient() as well as the mechanism for controlling
 * transactions and steps (via test.beginTransaction() and test.beginStep(String)).
 *
 * For full documentation, see: http://docs.wpm.neustar.biz/testscript-api/
 **/

// First: Decide if you want to use a real web browser (Real Browser Script)
var webDriver = test.openBrowser();
webDriver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
    
// ... or if you want a simple HTTP traffic test script (Basic Script)
var c = test.openHttpClient();

// By default SSL Certificates are not validated.
// If you turn SSL Certificates Validation on, trying to connect
// to a Server that returns an invalid SSL Certificate will throw an exception.
// To turn SSL Certificate validation ON, uncomment the following line:
// c.setCheckValidSSL(true);

// Blacklist requests made to sites like Google Analytics and DoubleClick.  See the
// HttpClient.blacklistCommonUrls() documentation for a list of URLs currently blocked by
// this function.
c.blacklistCommonUrls();

// You can optionally enable Bandwidth limitation for your script.
// Uncomment the following lines to simulate a business DSL line.
/*
test.setBandwidthLimitMode("on");
test.setDownstreamKbps(1000);           // 1 Mbps (max 6500 Kbps RBU, 1000 Kbps VU)
test.setUpstreamKbps(384);              // 384 Kbps (max 6500 Kbps RBU, 1000 Kbps VU)
test.setLatency(50);                    // 50 ms latency
*/

// For load testing, you can get the unique user number (0 -> max users)...
//var userNum = test.getUserNum();
// ... or the transaction count for that specific user
//var txCount = test.getTxCount();
// This can be useful in load testing scripts for applying different actions for different
// users or after a certain number of transactions.

// Start a new transaction.  This is needed to start recording HTTP traffic and timings.
test.beginTransaction();

// Transactions are grouped into "steps".  You can do work outside of a step, but it won't
// be recorded in the reports and charts. To record timings, start a step.
test.beginStep("Step 1 - " + baseUrl + "portal.do");
// Steps optionally also take a timeout.  The following will timeout if the step takes
// more than 30 seconds.
// test.beginStep("Description of Step 1", 30000);

// At this point you can either control the browser using the webDriver API...

// Navigate the browser to the given URL
webDriver.get(baseUrl + "portal.do");

// Here is an example on how to check for text present on the body of the page
/*
var bodyText = webDriver.findElement(By.tagName("body")).getText();
if (!bodyText.contains("not available for registration")) {
    // thrown exceptions will also cause the monitor to record an error
    throw "Expected content not found!";
}
*/

/* Here is how you use the HTTP client if you're creating a Basic Script
var resp = c.get("http://example.com", "not available for registration");
if (!resp.isContentMatched()) {
    // Couldn't find 'not available for registration'
    throw "Expected content not found!";
}
c.post("http://example.com/login", "Could not log in", {
    username: 'bob',
    password: 'mypassw0rd'
});
if (resp.isContentMatched()) {
    throw "Unable to log in!";
}
*/

var bodyText = webDriver.findElement(By.tagName("body")).getText();
if (!bodyText.contains("Login") || !bodyText.contains("Username") ) {
    throw "Expected content Login or Username not found!";
}
// End the step. You can begin additional steps after this call if you'd like.
test.endStep();

test.beginStep("Step 2 - __login_user");
webDriver.findElement(By.name("__login_user")).sendKeys(username);
test.endStep();

test.beginStep("Step 3 - input[type=\"submit\"]");
webDriver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
webDriver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
webDriver.findElement(By.tagName("body")).getText(); // wait for portal to load
test.endStep();

for (var i = 0; i < loopCount; i++) {
    test.beginStep("Step 4 - " + viewId + " " + page, timeoutSeconds * 1000);
    var start = java.util.Calendar.getInstance().getTimeInMillis();
    webDriver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
    webDriver.get(testUrl);
    webDriver.findElement(By.tagName("body")).getText(); // wait for portal to load
    webDriver.switchTo().defaultContent();
    var containerFrame;
    if(webDriver.findElements(By.xpath("//iframe")).size() > 0) {
        containerFrame = webDriver.findElement(By.xpath("//iframe"));
        webDriver.switchTo().frame(containerFrame);
    }
    if(webDriver.findElements(By.xpath("//iframe")).size() > 0) {
        contentFrame = webDriver.findElement(By.xpath("//iframe"));
        webDriver.switchTo().frame(contentFrame);
    }
    bodyText = webDriver.findElement(By.tagName("body")).getText();
    if (!bodyText.contains(pageLoadedText)) {
        throw "Expected content " + pageLoadedText + " not found!";
    }
    var time = java.util.Calendar.getInstance().getTimeInMillis() - start;
    test.log(viewId + " " + page + " " + time + "ms.");
    test.endStep();
}

if (logoutAtEnd) {
    test.beginStep("Step 5 - Logout");
    webDriver.switchTo().defaultContent();
    webDriver.findElement(By.xpath("//input[@name='imageField' and @value='Logout']")).click();
    bodyText = webDriver.findElement(By.tagName("body")).getText();
    if (!bodyText.contains("Login") || !bodyText.contains("Username") ) {
        throw "Expected content Login or Username not found!";
    }
    test.endStep();
}
// Finally, end the transaction.  Saving the transaction, it's steps and the total time the
// transaction was executing.
test.endTransaction();
