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

/**
 * Test the actual handlers.
 *
 * <p>On Sprint 2, you'll need to deserialize API responses. Although this demo doesn't need to do
 * that, these _tests_ do.
 *
 * <p>https://junit.org/junit5/docs/current/user-guide/
 *
 * <p>Because these tests exercise more than one "unit" of code, they're not "unit tests"...
 *
 * <p>If the backend were "the system", we might call these system tests. But I prefer "integration
 * test" since, locally, we're testing how the Soup functionality connects to the handler. These
 * distinctions are sometimes fuzzy and always debatable; the important thing is that these ARE NOT
 * the usual sort of unit tests.
 *
 * <p>Note: if we were doing this for real, we might want to test encoding formats other than UTF-8
 * (StandardCharsets.UTF_8).
 */


/**
 * this is where a data source would be nice, because we would have a method that would get the state codes
 * and broadband codes instead of just the handler, so it makes testing harder
 */
public class BroadbandHandlerTest {

    @BeforeAll
    public static void setup_before_everything() {
        // Set the Spark port number. This can only be done once, and has to
        // happen before any route maps are added. Hence using @BeforeClass.
        // Setting port 0 will cause Spark to use an arbitrary available port.
        Spark.port(0);
        // Don't try to remember it. Spark won't actually give Spark.port() back
        // until route mapping has started. Just get the port number later. We're using
        // a random _free_ port to remove the chances that something is already using a
        // specific port on the system used for testing.

        // Remove the logging spam during tests
        //   This is surprisingly difficult. (Notes to self omitted to avoid complicating things.)

        // SLF4J doesn't let us change the logging level directly (which makes sense,
        //   given that different logging frameworks have different level labels etc.)
        // Changing the JDK *ROOT* logger's level (not global) will block messages
        //   (assuming using JDK, not Log4J)
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    /**
     * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
     * need to replace the reference itself. We clear this state out after every test runs.
     */
    //final List<Soup> menu = new ArrayList<>();

    @BeforeEach
    public void setup() {
        // Re-initialize state, etc. for _every_ test method run
        //this.menu.clear();

        // In fact, restart the entire Spark server for every test!
        Spark.get("loadcsv", new LoadHandler());
        Spark.get("viewcsv", new ViewHandler());
        Spark.get("searchcsv", new SearchHandler());
        Spark.get("broadband", new BroadbandHandler());
        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening
    }

    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on both endpoints after each test
        Spark.unmap("/loadcsv");
        //Spark.unmap("viewcsv");
        Spark.unmap("/searchcsv");
        //Spark.unmap("broadband");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    /**
     * Helper to start a connection to a specific API endpoint/params
     *
     * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
     *     structure!)
     * @return the connection for the given URL, just after connecting
     * @throws IOException if the connection fails for some reason
     */
    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        // The default method is "GET", which is what we're using here.
        // If we were using "POST", we'd need to say so.
        clientConnection.connect();

        return clientConnection;
    }

    @Test
    // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
    // checker
    public void testAPINoRecipes() throws IOException {
        HttpURLConnection clientConnection = tryRequest("broadband");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(200, clientConnection.getResponseCode());

        // Now we need to see whether we've got the expected Json response.
        // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
        Moshi moshi = new Moshi.Builder().build();
        // We'll use okio's Buffer class here
        OrderHandler.SoupNoRecipesFailureResponse response =
                moshi
                        .adapter(OrderHandler.SoupNoRecipesFailureResponse.class)
                        .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));


        BroadbandHandler.
                System.out.println(response);
        // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception, but
        // a real Json reply.

        clientConnection.disconnect();
    }

    @Test
    // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
    // checker
    public void testAPIOneRecipe() throws IOException {
        menu.add(Soup.buildNoExceptions("Carrot", Arrays.asList("carrot", "onion", "celery",
                "garlic", "ginger", "vegetable broth")));

        HttpURLConnection clientConnection = tryRequest("order");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(200, clientConnection.getResponseCode());

        // Now we need to see whether we've got the expected Json response.
        // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
        // NOTE:   (How could we reduce the code repetition?)
        Moshi moshi = new Moshi.Builder().build();

        // We'll use okio's Buffer class here
        System.out.println(clientConnection.getInputStream());
        OrderHandler.SoupSuccessResponse response =
                moshi.adapter(OrderHandler.SoupSuccessResponse.class).fromJson(new
                        Buffer().readFrom(clientConnection.getInputStream()));

        Soup carrot = new Soup("Carrot", Arrays.asList("carrot", "onion", "celery",
                "garlic" , "ginger", "vegetable broth"), false);
        Map<String, Object> result = (Map<String, Object>) response.responseMap().get("Carrot");
        System.out.println(result.get("ingredients"));
        assertEquals(carrot.getIngredients(), result.get("ingredients"));
        clientConnection.disconnect();
    }
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

        System.out.println("State: " + stateParam);
        System.out.println("County: " + countyParam);

        if (stateParam != null && countyParam != null) {
            System.out.println("is passed through");
            // code below taken from gearup (not bothering to change var names)
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


            Assert.assertEquals(2, 2);

        }
    }
}
