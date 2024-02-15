package edu.brown.cs.student.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import spark.*;

public class BroadbandHandler implements Route {

  private String statecode;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    String stateParam = request.queryParams("state");
    String countyParam = request.queryParams("county");

    if (stateParam != null && countyParam != null) {
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
      Type listOfListOfStrings = Types.newParameterizedType(List.class, Types.newParameterizedType(List.class, String.class));
      JsonAdapter<List<List<String>>> adapter2 = moshi2.adapter(listOfListOfStrings);
      List<List<String>> responseLoS = adapter2.fromJson(sentBoredApiResponse.body());
      try {
        for (List<String> row : responseLoS) {
          if (row.get(0).equals(stateParam)) {
            this.statecode = row.get(1);
            break;
          } else {
            this.statecode = "error_not_found";
          }
        }
      }
      catch (NullPointerException e) {
        this.statecode = "error_bad_request";
      }
    }
    return null;
  }
}
