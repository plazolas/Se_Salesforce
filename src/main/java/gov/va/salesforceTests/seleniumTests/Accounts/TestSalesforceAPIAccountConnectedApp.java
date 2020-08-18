package gov.va.salesforceTests.seleniumTests.Accounts;

import com.google.gson.*;
import gov.va.salesforceTests.classes.User;
import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.framework.HandShake;
import gov.va.salesforceTests.framework.BrowserUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class TestSalesforceAPIAccountConnectedApp
 *       retrieves Account data from a Salesforce instance via Connected App.
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class TestSalesforceAPIAccountConnectedApp extends Framework {

    private static HandShake handshake;
    public static JsonObject jsonObject;
    private BrowserUtils browser;

    public TestSalesforceAPIAccountConnectedApp() {}

    /**
     * method performTest to perform Salesforce token auth procedures through
     * Salesforce connected apps.
     */
    public static void performanceTest() {

        handshake = new HandShake();

        globals.setDriverMode("headless");
        globals.api = "true";

        getAuthCode(globals.getAuthTokenUrl);

        String response = "";
        try {
            response = postRequestAccessToken(globals.getAccessTokenUrl);
            log("resp length: " + response.length());
            //log("response: " + response);
        } catch (IOException e) {
            e.getMessage();
            e.getStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        AccessTokenResponseObj responseObj = gson.fromJson(response, AccessTokenResponseObj.class);
        globals.access_token = responseObj.get_access_token();
        log("************************************************");
        log("Access Token = " + globals.access_token);
        log("************************************************");

        try {
            response = getAccountData(globals.AccountId);
            log("resp length: " + response.length());
            //log("response: " + response);
        } catch (IOException e) {
            log(e.getMessage());
            log(e.getStackTrace().toString());
        }

        jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonElement jsonElement = jsonObject.get("Id");
        log("Id = " + jsonElement.toString());
        jsonElement = jsonObject.get("Name");
        log("Name = " + jsonElement.toString());
        jsonElement = jsonObject.get("Description");
        log("Description = " + jsonElement.toString());
        jsonElement = jsonObject.get("Phone");
        log("Phone = " + jsonElement.toString());
        jsonElement = jsonObject.get("ShippingCountry");
        log("ShippingCountry = " + jsonElement.toString());

    }

    /**
     * Method getAuthCode obtains the auth code via connected App
     * of a Salesforce instance.
     *
     * @param url of the Salesforce instance
     * @return Salesforce authorization code
     */
    public static String getAuthCode(String url) {
        String urlStr = url + "client_id=" + globals.client_id + "&response_type=" + globals.response_type +
                "&redirect_uri=" + globals.redirect_uri;

        User current_user = new User(globals.userFirstName, globals.username, globals.password, true);

        BrowserUtils browser = new BrowserUtils(0,0);

        try {
            log("urlStr: " + urlStr);
            long loginTime = handshake.doLogin(current_user, urlStr);
            if (globals.verify_code.contains("true")) {
                log("User needs to input verify code. Enter verify code:");
                handshake.enterVerifyCode();
            } else {
                log("No Verify code needed.");
            }
            if (loginTime > 0 && loginTime < 99999) {
                log("Login SUCCESS.");
            } else {
                log("UNABLE TO LOGIN.");
                System.exit(1);
            }
        } catch (Exception eLogin) {
            log(eLogin.getMessage());
            System.exit(1);
        }

        if (globals.auth_code == null) {
            log("Could not get authorization code");
            closeDriver();
            System.exit(1);
        }

        log("************************************************");
        log("*** code= " + globals.auth_code);
        log("************************************************");

        try {
            handshake.doLogout(globals.logoutUrl);
        } catch (Exception end) {
            log("XXXXX==> UNABLE TO LOGOUT!");
            log(end.getMessage());
            browser.check(globals.output_log);
        }
        if (!globals.status.contains("setup")) {
            Framework.closeDriver();
        }
        log("LOGOUT!");
        globals.setStatus("SUCCESS");
        return globals.auth_code;
    }

    /**
     * Method postRequestAccessToken obtains the access token via post request of a connected App
     * in a Salesforce instance.
     *
     * @param urlString base uri of the Salesforce connected App's access token retrieval service end point.
     * @return json encoded String for the response of the post request
     *         containing among other data, the access token.
     * @throws IOException type exception
     */
    public static String postRequestAccessToken(String urlString) throws IOException {

        String responseStr = "";
        int status = 0;

        try {
            String urlParams = urlString +
                    "&client_id=" + globals.client_id +
                    "&grant_type=" + globals.grant_type +
                    "&redirect_uri=" + globals.redirect_uri +
                    "&client_secret=" + globals.client_secret +
                    "&code=" + globals.auth_code;

            URL url = new URL(urlString);
            outputUrlSettings(url);

            log("************ urlParams BEGIN *************");
            log(urlParams);
            log("************ urlParams END *************");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Cookie", globals.cookie.toString());
            conn.setRequestProperty("Set-Cookie", "HttpOnly;Secure;SameSite=Strict");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            byte[] inputData = urlParams.getBytes(StandardCharsets.UTF_8);
            String lengthStr = Integer.toString(inputData.length);
            conn.setRequestProperty("Content-Length", lengthStr);
            conn.setFixedLengthStreamingMode(inputData.length);

            try {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(inputData, 0, inputData.length);
                } catch (IOException errGetOutputStream) {
                    log("IOException getOutputStream error Stack");
                    errGetOutputStream.printStackTrace();
                    log("getMessage: ");
                    errGetOutputStream.getMessage();
                }

            } catch (Exception errConnect) {
                log("errConnect LAST error Stack");
                errConnect.printStackTrace();
                log("getMessage: ");
                errConnect.getMessage();
            }

            try {
                status = conn.getResponseCode();
                log("**************************************");
                log("Response code: " + status);
                log("**************************************");

            } catch (Exception errConnect) {
                log("errConnect AFTER LAST error Stack");
                errConnect.printStackTrace();
                log("getMessage: ");
                errConnect.getMessage();
            }

            if (status == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    responseStr = response.toString();
                    log("responseStr: " + responseStr);
                } catch (IOException errgetInputStream) {
                    log("BufferedReader error Stack");
                    errgetInputStream.printStackTrace();
                    log("getMessage: ");
                    errgetInputStream.getMessage();
                }
            } else {
                log("Error stream: " + conn.getErrorStream());
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    responseStr = response.toString();
                } catch (IOException errgetInputStream) {
                    log("BufferedReader error Stack");
                    errgetInputStream.printStackTrace();
                    log("getMessage: ");
                    errgetInputStream.getMessage();
                }

            }

        } catch (IOException error) {
            error.getMessage();
            error.getStackTrace();
            throw new IOException();
        }
        return responseStr;
    }

    /**
     * Method getAccountData obtains the access token via post request of a connected App
     * in a Salesforce instance.
     *
     * @param account base uri of the connected App's data retrieval service end point.
     * @return json encoded String with account data or error message.
     * @throws IOException type exception
     */
    public static String getAccountData(String account) throws IOException {

        String urlString = globals.getAccountDataUrl + account;
        String responseStr = "";
        int status = 0;

        try {

            URL url = new URL(urlString);
            outputUrlSettings(url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-PrettyPrint", "1");
            conn.setRequestProperty("Authorization", "Bearer " + globals.access_token);
            conn.setDoInput(true);

            try {
                status = conn.getResponseCode();
                log("**************************************");
                log("Response code: " + status);
                log("**************************************");

            } catch (Exception errConnect) {
                log("errConnect AFTER LAST error Stack");
                errConnect.printStackTrace();
                log("getMessage: ");
                errConnect.getMessage();
            }

            if (status == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    responseStr = response.toString();
                    //log("responseStr: " + responseStr);
                } catch (IOException errgetInputStream) {
                    log("BufferedReader error Stack");
                    errgetInputStream.printStackTrace();
                    log("getMessage: ");
                    errgetInputStream.getMessage();
                }
            } else {
                log("Error stream: " + conn.getErrorStream());
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    responseStr = response.toString();
                } catch (IOException errgetInputStream) {
                    log("BufferedReader error Stack");
                    errgetInputStream.printStackTrace();
                    log("getMessage: " + errgetInputStream.getMessage());

                }

            }

        } catch (IOException error) {
            error.getMessage();
            error.getStackTrace();
            throw new IOException();
        }
        return responseStr;
    }

    /**
     * Method outputUrlSettings logs http query parameter key values.
     *
     * @param url for which to log query parameters.
     *
     */
    public static void outputUrlSettings(URL url) {
        log("*********** URL settings BEGIN ************");
        log("URL is " + url.toString());
        log("protocol is " + url.getProtocol());
        log("authority is " + url.getAuthority());
        log("file name is " + url.getFile());
        log("host is " + url.getHost());
        log("path is " + url.getPath());
        log("port is " + url.getPort());
        log("default port is " + url.getDefaultPort());
        log("query is " + url.getQuery());
        log("ref is " + url.getRef());
        log("************ URL settings END **************");
    }

    /**
     * Class for manipulating response obj for a Salesforce access token post request.
     */
    private class AccessTokenResponseObj {
        private String access_token;
        private String signature;
        private String scope;
        private String id_token;
        private String instance_url;
        private String id;
        private String token_type;
        private String issued_at;

        public AccessTokenResponseObj() {
        }

        public String get_access_token() {
            return this.access_token;
        }

        public String toString() {
            return "{  \"access_token\" : \"" + access_token +
                    "\", \"signature\" : \"" + signature +
                    "\", \"scope\": \"" + scope + "\"" +
                    "\", \"id_token\": " + id_token +
                    "\", \"instance_url\": " + instance_url +
                    "\", \"id\": " + id +
                    "\", \"issued_at\": " + issued_at +
                    "\" }";
        }
    }

}
