package edu.brown.cs.student.handlers;

import com.squareup.moshi.*;
import edu.brown.cs.student.main.Server;
import java.io.*;
import java.util.*;
import spark.*;

public class LoadHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // code in next four lines is copied/adapted in parts from the gearup code and class livecode
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    String filepathParam = request.queryParams("filepath");
    if (filepathParam == null || filepathParam.isEmpty()) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("error_details", "endpoint request may be misspelled");
      responseMap.put("filepath", filepathParam);
      return adapter.toJson(responseMap);
    } else if (!filepathParam.contains("data/prod/")) {
      responseMap.put("result", "error_bad_json");
      responseMap.put(
          "error_details", "filepath field is ill-formed, missing data/prod at a minimum");
      responseMap.put("filepath", filepathParam);
      return adapter.toJson(responseMap);
    }

    try {
      Server.filepath = filepathParam;
      // just to make sure we can find it
      FileReader csvToRead = new FileReader(filepathParam);
      responseMap.put("result", "success");
      responseMap.put("filepath", filepathParam);
      Server.loaded = true;
    } catch (FileNotFoundException e) {
      responseMap.put("result", "error_datasource");
      responseMap.put("filepath", filepathParam);
      Server.loaded = false;
    } catch (Exception e) {
      responseMap.put("result", "error_bad_json");
      responseMap.put("filepath", filepathParam);
      Server.loaded = false;
    }
    return adapter.toJson(responseMap);
  }

  private Map<String, Object> throwErrorResponse(String message) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("result", "error_bad_request");
    errorResponse.put("message", message);
    return errorResponse;
  }
}
