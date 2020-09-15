package gov.va.salesforceTests.utils;

import gov.va.salesforceTests.framework.Framework;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Class GenTestId
 *    Class to get unique test ids
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class GenTestId extends Framework {

    private String testid = "";

    /**
     * Method to generate a unique id
     * @throws NoSuchAlgorithmException type exception
     */
    public GenTestId() throws NoSuchAlgorithmException {

        String uniqueTestId = "";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        int number = 0;
        for (int i = 0; i < 20; i++) {
            number = random.nextInt(21);
        }
        byte[] secureRandom = random.getSeed(number);
        long milliSeconds = System.currentTimeMillis();
        String timeStampLong = Long.toString(milliSeconds);

        try {
            uniqueTestId = "".concat("" + secureRandom.hashCode()).concat("_" + timeStampLong);
            if (uniqueTestId.length() == 0) {
                throw new NoSuchAlgorithmException("No Test Id");
            }
        } catch (NoSuchAlgorithmException ex) {
            log(ex.getMessage());
        }
        testid = uniqueTestId.replaceAll("-", "");
    }

    /**
     * Method to get the unique id
     * @return the testid string.
     */
    public String getTestid() {
        return testid;
    }
}
