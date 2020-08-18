package gov.va.salesforceTests.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import gov.va.salesforceTests.logger.Logger;

/**
 * Class TestProperties
 *       Class to read the configuration file of the framework
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class TestProperties {

    private Properties testProperties;

    /**
     * Method constructor to load config file
     * @throws IOException type exception
     */
    public TestProperties() throws IOException {
        testProperties = new Properties();
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
            Logger.log(ex.getStackTrace().toString());
            Logger.log(ex.getMessage());
            throw new IOException("no configuration file found!");
        }
    }

    /**
     * Method getPropValue to load config file
     * @param  propName of property
     * @return value of property
     */
    public String getPropValue(String propName) {
        return testProperties.getProperty(propName);
    }
}
