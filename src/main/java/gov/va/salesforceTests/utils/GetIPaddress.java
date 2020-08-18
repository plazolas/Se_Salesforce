package gov.va.salesforceTests.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Class GetIPaddress
 *    Class to get unique test ids
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class GetIPaddress {

    private URL myIP;
    private static String ip = "";

    /**
     * Method to get the current IP address for the machine currently running
     */
    public GetIPaddress() {
        try {
            myIP = new URL("http://icanhazip.com/");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(myIP.openStream())
            );
            ip = in.readLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                myIP = new URL("http://myip.dnsomatic.com/");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(myIP.openStream())
                );
                ip = in.readLine();
                System.out.println(ip);
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
                try {
                    myIP = new URL("http://api.externalip.net/ip/");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(myIP.openStream())
                    );
                    ip = in.readLine();
                    System.out.println(ip);
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                    e2.printStackTrace();
                }
            }
        }
    }
    /**
     * Method to get the IP address
     * @return current's process ip address
     */
    public String getIP () {
        return ip;
    }
}
