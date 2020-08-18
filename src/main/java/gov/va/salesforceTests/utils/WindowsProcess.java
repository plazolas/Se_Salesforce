package gov.va.salesforceTests.utils;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gov.va.salesforceTests.logger.Logger;

/**
 * @author Oswaldo Plazola
 * @version 1.0
 * Class WindowsProcess for windows OS process manipulation
 */
public class WindowsProcess {
    private static String processName;

    /**
     * Constructor
     * Method to load process name.
     * @param processName is the name of the process.
     */
    public WindowsProcess(String processName) {
        this.processName = processName;
    }

    /**
     * Method kill terminates current process name
     * @throws Exception type exception
     */
    public static void kill() throws Exception {
        if (isRunning()) {
            getRuntime().exec("taskkill /F /IM " + processName);
        }
    }

    /**
     * Method isRunning check if process is running
     * @return true if process is running
     */
    public static boolean isRunning() {
        try {
            Process listTasksProcess = getRuntime().exec("tasklist");
            BufferedReader tasksListReader = new BufferedReader(
                    new InputStreamReader(listTasksProcess.getInputStream()));
            String tasksLine;

            try {
                while ((tasksLine = tasksListReader.readLine()) != null) {
                    if (tasksLine.contains(processName)) {
                        return true;
                    }
                }
            } catch (IIOException iioe) {
                Logger.log(iioe.getStackTrace().toString());
                Logger.log(iioe.getMessage());
                return false;
            }
        } catch (IOException ioe) {
            Logger.log(ioe.getStackTrace().toString());
            Logger.log(ioe.getMessage());
            return false;
        }

        return false;
    }

    /**
     * Method getRuntime gets the runtime of the process
     * @return Runtime of the process
     */
    private static Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    /**
     * Method getDriverExecFromBrowser gets the exec file name of the browser currently executing.
     * @param browser type of browser string
     * @return Name of the file of the browser that is currently executing
     */
    public static String getDriverExecFromBrowser(String browser) {
        String browserExe = "";
        switch (browser) {
            case "chrome":
                browserExe = "chromedriver.exe";
                break;
            case "firefox":
                browserExe = "geckodriver.exe";
                break;
            case "ie":
                browserExe = "iedriver.exe";
                break;
            default:
                browserExe = null;
        }
        return browserExe;
    }
}