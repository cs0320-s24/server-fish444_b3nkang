package edu.brown.cs.student.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;
import spark.*;

public class BroadbandHandler implements Route {

  private String stateCode;
  private String broadband;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // all in one method, no helpers or abstractions, for funsies
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
      JsonAdapter<List<List<String>>> adapter2 = moshi2.adapter(Types.newParameterizedType(List.class, Types.newParameterizedType(List.class, String.class)));
      List<List<String>> responseLoS = adapter2.fromJson(sentBoredApiResponse.body());
      try {
        for (List<String> row : responseLoS) {
          if (row.get(0).equals(stateParam)) {
            this.stateCode = row.get(1);
            break;
          } else {
            this.stateCode = "error_bad_request";
            responseMap.put("result", "error_bad_request");
            responseMap.put("error_details", "state param(s) malformed and not found");
            return responseMap;
          }
        }
      }
      catch (NullPointerException e) {
        this.stateCode = "error_not_found";
        responseMap.put("result", "error_datasource");
        responseMap.put("error_details", "internal Census API error");
        return responseMap;
      }

      if (!this.stateCode.equals("error_bad_request") && !this.stateCode.equals("error_not_found")){
        // finding the county code
        HttpRequest buildBoredApiRequest2 =
            HttpRequest.newBuilder()
                .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:"+this.stateCode))
                .GET()
                .build();
        HttpResponse<String> sentBoredApiResponse2 =
            HttpClient.newBuilder()
                .build()
                .send(buildBoredApiRequest2, HttpResponse.BodyHandlers.ofString());
        // build the type format, this time for county
        Moshi moshi3 = new Moshi.Builder().build();
        JsonAdapter<List<List<String>>> adapter3 = moshi3.adapter(Types.newParameterizedType(List.class, Types.newParameterizedType(List.class, String.class)));
        List<List<String>> responseLoS2 = adapter3.fromJson(sentBoredApiResponse2.body());
        try {
          for (List<String> row : responseLoS2) {
            if (row.contains(countyParam+" County, "+stateParam)) {
              this.broadband = row.get(1);
              break;
            } else {
              responseMap.put("result", "error_bad_request");
              responseMap.put("error_details", "state param(s) malformed and not found");
              return responseMap;
            }
          }
        }
        catch (NullPointerException e) {
          responseMap.put("result", "error_datasource");
          responseMap.put("error_details", "internal Census API error");
          return responseMap;
        }
      } else {
        responseMap.put("result", "error_bad_request");
        responseMap.put("error_details", "state param(s) malformed and not found");
        return responseMap;
      }
    } else {
      responseMap.put("result", "error_bad_request");
      responseMap.put("error_details", "state/county param(s) malformed and not found");
      return responseMap;
    }

    // build the successful output if we reach this point
    LocalDateTime currentTime = LocalDateTime.now();
    responseMap.put("result", "success");
    responseMap.put("state", stateParam);
    responseMap.put("county", countyParam);
    responseMap.put("broadband_access_percent", this.broadband);
    responseMap.put("date_and_time", currentTime);

    return responseMap;
  }
}
