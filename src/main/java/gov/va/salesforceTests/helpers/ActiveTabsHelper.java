package gov.va.salesforceTests.helpers;

import gov.va.salesforceTests.framework.Framework;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.*;

/**
 * Class ActiveTabsHelper
 *       helper class to close all active tabs in Salesforce.
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class ActiveTabsHelper extends Framework {

    public ActiveTabsHelper(){}

    public void close() {

        WebElement e;
        List<WebElement> els;
        String str;
        int i, j = 0;
        int max_buttons = 4;

        log("********** CLOSING TABS *************");
        try {
            els = driver.findElements(By.tagName("button"));
            log("Total buttons: " + els.size());
            if (els.size() > 0) {
                for (i = 0; i < els.size() && j < max_buttons; i++) {
                    e = els.get(i);
                    if (e.isDisplayed() && e.isEnabled()) {
                        str = e.getAttribute("innerHTML");
                        if (str.contains("Close")) {
                            j++;
                            try {
                                Thread.sleep(500);
                                e.click();
                            } catch (Exception er) {
                                er.getMessage();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log("NO more Closing buttons found");
        }
        log("********** CLOSED  TABS ****************");
    }
    
}
