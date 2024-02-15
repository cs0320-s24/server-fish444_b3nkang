package edu.brown.cs.student.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
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

  //  @Override
  //  public Object handle(Request request, Response response) throws Exception {
  //    Map<String, Object> responseMap = new HashMap<>();
  //    Moshi moshi = new Moshi.Builder().build();
  //    JsonAdapter<Map<String, Object>> adapter =
  //        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
  //
  //    String stateParam = request.queryParams("state");
  //    String countyParam = request.queryParams("county");
  //
  //    System.out.println("State: " + stateParam);
  //    System.out.println("County: " + countyParam);
  //    responseMap.put("state", stateParam);
  //    responseMap.put("county", countyParam);
  //    return adapter.toJson(responseMap);
  //  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // all in one method, no helpers or abstractions, for funsies
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    String stateParam = request.queryParams("state");
    String countyParam = request.queryParams("county");

    System.out.println("State: " + stateParam);
    System.out.println("County: " + countyParam);

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
      JsonAdapter<List<List<String>>> adapter2 =
          moshi2.adapter(
              Types.newParameterizedType(
                  List.class, Types.newParameterizedType(List.class, String.class)));
      List<List<String>> responseLoS = adapter2.fromJson(sentBoredApiResponse.body());
      //      System.out.println("REPOLOS 1: " + responseLoS);
      try {
        for (List<String> row : responseLoS) {
          //          System.out.println("STATE: " + stateParam + "ROW: " + row);
          if (row.contains(stateParam)) {
            this.stateCode = row.get(1);
            //            System.out.println("GOT STATECODE: " + this.stateCode);
            break;
          } else {
            this.stateCode = "error_bad_request";
          }
        }
      } catch (NullPointerException e) {
        this.stateCode = "error_not_found";
        responseMap.put("result", "error_datasource");
        responseMap.put("error_details", "internal Census API error");
        return adapter.toJson(responseMap);
      }
      System.out.println("STATECODE: " + this.stateCode);
      if (!this.stateCode.equals("error_bad_request")
          && !this.stateCode.equals("error_not_found")) {
        // finding the county code
        HttpRequest buildBoredApiRequest2 =
            HttpRequest.newBuilder()
                .uri(
                    new URI(
                        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:"
                            + this.stateCode))
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
        List<List<String>> responseLoS2 = adapter3.fromJson(sentBoredApiResponse2.body());
        //        System.out.println("REPOLOS 2: " + responseLoS2);
        try {
          for (List<String> row : responseLoS2) {
            //            System.out.println("COUNTY: " + countyParam + "ROW: " + row);
            if (row.contains(countyParam + " County, " + stateParam)) {
              this.broadband = row.get(1);
              //              System.out.println("GOT BROADBAND: " + this.broadband);
              break;
            } else {
              this.broadband = "error_bad_request";
            }
          }
        } catch (NullPointerException e) {
          responseMap.put("result", "error_datasource");
          responseMap.put("error_details", "internal Census API error");
          return adapter.toJson(responseMap);
        }
      } else {
        responseMap.put("result", "error_bad_request");
        responseMap.put("error_details", "state FIELD (e.g. California) malformed and not found");
        return adapter.toJson(responseMap);
      }
    } else {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "error_details", "state and/or county PARAM(S) (i.e. `state` and/or `county`) malformed");
      return adapter.toJson(responseMap);
    }
    System.out.println("BROADBAND: " + this.broadband);
    if (this.broadband.equals("error_bad_request")) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("error_details", "county FIELD (e.g. Napa) malformed and not found");
      return adapter.toJson(responseMap);
    }
    // build the successful output if we reach this point
    LocalDateTime currentTime = LocalDateTime.now();
    responseMap.put("result", "success");
    responseMap.put("state", stateParam);
    responseMap.put("county", countyParam);
    responseMap.put("broadband_access_percent", this.broadband);
    responseMap.put("date_and_time", currentTime.toString());
    //    System.out.println("before print");
    //    System.out.println(adapter.toJson(responseMap));
    //    System.out.println("after print");
    return adapter.toJson(responseMap);
  }
}