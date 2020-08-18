package gov.va.salesforceTests.seleniumTests.Participants;

import com.google.gson.*;
import gov.va.salesforceTests.framework.Framework;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class TestSearchParticipantApiSensitive
 *       retrieves Participant data from a Salesforce instance via oData.
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class TestSearchParticipantApiSensitive extends Framework {

    private static String apiURL = "https://empwr-dev.dev8.bip.va.gov";
    private static String apiRetrieveURL = "/api/ms_ptc_search/retrieve";
    private static String partId = "001220052";
    private static String username = "salesforce_admin_mule";
    private static String password = "CggnYHJRTJJ3F3xuM6Bpv3mvVpc34dd4";

    public TestSearchParticipantApiSensitive() {}

    /**
     * Method doTest to perform Salesforce token auth procedures through
     * Salesforce connected apps.
     */
    public static void doTest() {

        String data = "{ \"SEARCH_NO\": " + "\"" + partId + "\" }";

        String response = "";
        try {
            response = retrieveParticipantData(data);
            log("resp length: " + response.length());
            log("response: " + response);
        } catch (IOException e) {
            e.getMessage();
            e.getStackTrace();
        }

        JsonElement jsonElement;
        JsonObject  jsonObject;
        JsonArray jsonArray;

        jsonObject = new JsonParser().parse(response).getAsJsonObject();
        jsonElement = jsonObject.get("ms_ptc_search");
        String resp = jsonElement.toString();
        jsonArray = new JsonParser().parse(resp).getAsJsonArray();

        JsonElement[] els;
        els = new JsonElement[jsonArray.size()];
        for(int i=0; i < jsonArray.size(); i++) {
            els[i] = jsonArray.get(i);
            System.out.println("els=" + i + " " + els[i].toString());
        }

        jsonObject = new JsonParser().parse(els[0].toString()).getAsJsonObject();

        jsonElement = jsonObject.get("LAST_NM");
        log("Last Name = " + jsonElement.toString());
        jsonElement = jsonObject.get("FIRST_NM");
        log("First Name = " + jsonElement.toString());
        jsonElement = jsonObject.get("TAX_ID_NO");
        log("Tax Id = " + jsonElement.toString());
        jsonElement = jsonObject.get("FILE_NO");
        log("File Number = " + jsonElement.toString());
        jsonElement = jsonObject.get("SEARCH_NO");
        log("Search Number = " + jsonElement.toString());

    }

    /**
     * Method retrieveParticipantData obtains the access token via post request of a connected App
     * in a Salesforce instance.
     *
     * @param data is the Json formated string with participant id and call id
     * @return json encoded String for the response of the post request
     */
    public static String retrieveParticipantData(String data) throws IOException {

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);



        String responseStr = "";
        int status = 0;

        try {
            String fullRetrieveURL = apiURL + apiRetrieveURL;
            log(data);

            URL url = new URL(fullRetrieveURL);
            outputUrlSettings(url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            byte[] inputData = data.getBytes(StandardCharsets.UTF_8);
            String lengthStr = Integer.toString(inputData.length);
            conn.setRequestProperty("Content-Length", lengthStr);
            conn.setFixedLengthStreamingMode(inputData.length);

            try {
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(inputData);
                } catch (IOException errGetOutputStream) {
                    log("IOException getOutputStream error Stack");
                    errGetOutputStream.printStackTrace();
                    log("getMessage: ");
                    errGetOutputStream.getMessage();
                }

            } catch (Exception errConnect) {
                errConnect.printStackTrace();
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
                    log("responseSTR: " + responseStr);
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
     * Method outputUrlSettings logs http query parameter key values.
     *
     * @param url for which to log query parameters.
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

}
