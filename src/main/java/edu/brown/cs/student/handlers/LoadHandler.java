package edu.brown.cs.student.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Server;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import spark.*;

public class LoadHandler implements Route {
  public LoadHandler() {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String filepathParam = request.queryParams("filepath");
    if (filepathParam == null || filepathParam.isEmpty()) {
      response.status(400);
      return throwErrorResponse("filepath is incorrect/invalid");
    } else if (!filepathParam.contains("data/prod/")) {
      response.status(400);
      return throwErrorResponse("filepath is missing 'data/prod/...' root");
    }

    // code in next four lines is copied/adapted in parts from the gearup code and class livecode
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    try {
      Server.filepath = filepathParam;
      // just to make sure we can find it
      FileReader csvToRead = new FileReader(filepathParam);
      responseMap.put("result", "success");
      responseMap.put("filepath", filepathParam);
    } catch (FileNotFoundException e) {
      throwErrorResponse("file passed initial validation but was not found.");
    }
    return adapter.toJson(responseMap);
  }

  private Map<String, Object> throwErrorResponse(String message) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("result", "error");
    errorResponse.put("message", message);
    return errorResponse;
  }
}
