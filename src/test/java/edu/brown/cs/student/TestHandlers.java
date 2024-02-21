package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.Proxys.BroadbandProxy;
import edu.brown.cs.student.handlers.BroadbandHandler;
import edu.brown.cs.student.handlers.LoadHandler;
import edu.brown.cs.student.handlers.SearchHandler;
import edu.brown.cs.student.handlers.ViewHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import spark.Request;
import spark.Response;
import spark.Spark;


public class TestHandlers {

    MockACS ACS = new MockACS();


    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        // The default method is "GET", which is what we're using here.
        // If we were using "POST", we'd need to say so.
        clientConnection.connect();

        return clientConnection;
    }

    /**
     * method to test, getting the map of codes and getting the code
     * @param request
     * @param response
     * @param state
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Test
    public void testAPIStateCode (Request request, Response response, String state) throws IOException, InterruptedException, URISyntaxException {
        HttpURLConnection clientConnection = tryRequest("state");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        //assertEquals(200, clientConnection.getResponseCode());

        // Now we need to see whether we've got the expected Json response.
        // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
        // NOTE:   (How could we reduce the code repetition?)
        Moshi moshi = new Moshi.Builder().build();

        JsonAdapter<Map<String, Object>> adapter =
                moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));


        String stateParam = request.queryParams("state");
        String countyParam = request.queryParams("county");


        if (stateParam != null && countyParam != null) {
            System.out.println("is passed through");
            HttpRequest buildBoredApiRequest =
                    HttpRequest.newBuilder()
                            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
                            .GET()
                            .build();
            HttpResponse<String> sentBoredApiResponse =
                    HttpClient.newBuilder()
                            .build()
                            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

            // build the type format
            Moshi moshi2 = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter2 =
                    moshi2.adapter(
                            Types.newParameterizedType(
                                    List.class, Types.newParameterizedType(List.class, String.class)));


            BroadbandProxy proxyResponseLoS = new BroadbandProxy(adapter2.fromJson(sentBoredApiResponse.body()));

            ACS.stateCodes = List.of(List.of("Alabama", "01"), List.of("Alaska", "02")); // keep going
            Assert.assertEquals(ACS.stateCodes, proxyResponseLoS);

            Assert.assertEquals(ACS.stateCodes.get(1), proxyResponseLoS.get(1));

        }


    }
    @Test
    public void testAPIBroadband (Request request, Response response, List<List<String>> stateCode) throws URISyntaxException, IOException, InterruptedException {
        if (!stateCode.equals("error_bad_request")
                && !stateCode.equals("error_not_found")) {
            // finding the county code
            HttpRequest buildBoredApiRequest2 =
                    HttpRequest.newBuilder()
                            .uri(
                                    new URI(
                                            "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:"
                                                    + stateCode))
                            .GET()
                            .build();
            HttpResponse<String> sentBoredApiResponse2 =
                    HttpClient.newBuilder()
                            .build()
                            .send(buildBoredApiRequest2, HttpResponse.BodyHandlers.ofString());
            // build the type format, this time for county
            Moshi moshi3 = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter3 =
                    moshi3.adapter(
                            Types.newParameterizedType(
                                    List.class, Types.newParameterizedType(List.class, String.class)));
            BroadbandProxy proxyResponseLoS2 = new BroadbandProxy(adapter3.fromJson(sentBoredApiResponse2.body()));

            this.ACS.broadband = List.of(List.of("Kings County, California", "83.5", "06", "031"), List.of(
                    "Los Angles County, California", "89.9", "06", "037"
            )); // add more

            Assert.assertEquals(this.ACS.broadband, proxyResponseLoS2);
            Assert.assertEquals(this.ACS.broadband.get(1), proxyResponseLoS2.get(1));

        }
    }
}


