package com.saucelabs.appium;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Simple test which demonstrates how to run an <a href="https://github.com/appium/appium">Appium</a>
 * using <a href="http://saucelabs.com">Sauce Labs</a>.
 *
 * This test also includes the <a href="https://github.com/saucelabs/sauce-java/tree/master/junit">Sauce JUnit</a> helper classes, which will use the Sauce REST API to mark the Sauce Job as passed/failed.
 *
 * In order to use the {@link SauceOnDemandTestWatcher}, the test must implement the {@link SauceOnDemandSessionIdProvider} interface.
 *
 * <p/>
 * The test relies on SAUCE_USER_NAME and SAUCE_ACCESS_KEY environment variables being set which reference
 * the Sauce username/access key.
 *
 * @author Ross Rowe
 */
public class CreditKarmaTest implements SauceOnDemandSessionIdProvider {

    private AppiumDriver<WebElement> driver;

    private List<Integer> values;

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 10;

    private String sessionId;

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    public @Rule
    SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    /**
     * Sets up appium.  You will need to either explictly set the sauce username/access key variables, or set
     * SAUCE_USERNAME and SAUCE_ACCESS_KEY environment variables to reference your Sauce account details.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        String sauceUserName = authentication.getUsername();
        String sauceAccessKey = authentication.getAccessKey();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "9.3");
        capabilities.setCapability("deviceName", "iPhone Simulator");
        capabilities.setCapability("automationName", "XCUITest");
        //capabilities.setCapability("appiumVersion", "1.6.5");
        capabilities.setCapability("autoAcceptAlerts", "true");
        capabilities.setCapability("name", "Credit Karma Test");
        capabilities.setCapability("app", "sauce-storage:CreditKarma.app.zip");

        driver = new IOSDriver<WebElement>(new URL(MessageFormat.format("https://{0}:{1}@ondemand.saucelabs.com/wd/hub", sauceUserName, sauceAccessKey)),
                capabilities);
        this.sessionId = driver.getSessionId().toString();
        values = new ArrayList<Integer>();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    @Test
    public void checkApp() throws Exception {

        String elemText = driver.findElementsByClassName("UIAStaticText").get(0).getText();
        assertNotNull(elemText);
    }

    public String getSessionId() {
        return sessionId;
    }
}