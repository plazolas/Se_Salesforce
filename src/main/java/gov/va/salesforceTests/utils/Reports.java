package gov.va.salesforceTests.utils;

import gov.va.salesforceTests.framework.Framework;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class Reports creates performance reports files
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class Reports extends Framework {

    /**
     * Method create creates performance reports files
     *
     * @param content contains report's content
     * @param prefix contains report's file name prefix
     * @param reportsPath contains report's file path
     * @param appName contains the app name that is being tested
     * @param testid contains a unique id for the report name
     * @return true is able to create report
     * @throws IOException type exception
     */
    public static Boolean create(String content, String prefix, String reportsPath, String appName, String testid) throws IOException {

        String filename = "";
        appName = (appName.contains("eMPWR-VA")) ? "eMPWR-VA" : appName;
        if (reportsPath.contains("C")) {
            // Generate reports for windows local run.
            filename = reportsPath + appName + "/" + prefix + "_" + testid + ".csv";
        } else {
            // Generate reports for linux or kubernetes run.
            filename = reportsPath + prefix + "_" + testid + ".csv";
        }
        log("*********************************************************");
        log("Created PERFORMANCE report: " + filename);
        log("*********************************************************");

        File file = new File(filename);
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));

        try {
            out.write(content);
            out.close();
            return true;
        } catch (IOException ex) {
            log(ex.getMessage());
            return false;
        }
    }
    
}
