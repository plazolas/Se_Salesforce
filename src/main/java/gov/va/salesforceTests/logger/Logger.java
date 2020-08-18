package gov.va.salesforceTests.logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class Logger
 *       Simple logger class to log messages to log file or output depending on config.
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class Logger {

    private static String level = null;
    private static Properties testProperties;

    public static void log(String msg) {
        try {
            testProperties = new Properties();
        } catch (Exception err) {
            log(err.getStackTrace().toString());
            log(err.getMessage());
            System.exit(1);
        }
        if(level == null ) {
            try {
                String config_file = "";
                String os = System.getenv("OS");
                if (os.toLowerCase().contains("windows")) {
                    config_file = System.getenv("PWD") + "\\local.properties";
                } else {
                    config_file = System.getenv("PWD") + "/local.properties";
                }
                InputStream input = new FileInputStream(config_file);
                testProperties.load(input);
            } catch (IOException ex) {
                log(ex.getStackTrace().toString());
                log(ex.getMessage());
                System.exit(1);
            }
            level = testProperties.getProperty("logging.level.root");
        }
        if (level != null && level.contains("DEBUG")) System.out.println(getMethodName() + msg);
    }

    /**
     * Method getMethodName
     *       Gets current method's name in the stack trace.
     * @return current method name.
     */
    private static String getMethodName() {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        return   traces[4].getMethodName() + ":" + traces[3].getMethodName() + ":";
    }

    /**
     * Method echoTrace
     *       Gets current Stack Trace.
     * @param depth depth of call stack to log.
     * @return current execution stack trace.
     */
    public static String echoTrace(int depth) {
        String str = "";
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        for (int i = 0; i < traces.length && i < depth;i++) {
            str += traces[i] + "\n";
        }
        return str;
    }
}
