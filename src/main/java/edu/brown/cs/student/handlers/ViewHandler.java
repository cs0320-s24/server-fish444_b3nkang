package edu.brown.cs.student.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.Server;
import edu.brown.cs.student.parser.creators.CreateListOfStringsFromRow;
import edu.brown.cs.student.parser.outputs.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.*;

/** A class to view a loaded CSV's data. */
public class ViewHandler implements Route {

  /**
   * A method to handle the request made when the endpoint is hit and to return an appropriate JSON
   * response.
   *
   * @return a JSON-like string
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    if (!Server.loaded) {
      responseMap.put("result", "error_datasource");
      responseMap.put("data", "none returned; csv must be loaded before viewing");
      return adapter.toJson(responseMap);
    } else {
      try {
        FileReader csvToRead = new FileReader(Server.filepath);
        Parser<List<String>> stringParser =
            new Parser<>(csvToRead, new CreateListOfStringsFromRow());
        List<List<String>> parsedStringCSV = stringParser.readReader();
        responseMap.put("result", "success");
        responseMap.put("data", parsedStringCSV);
      } catch (FileNotFoundException e) {
        responseMap.put("result", "error_datasource");
        responseMap.put("data", "none returned; datasource error");
      } catch (Exception e) {
        responseMap.put("result", "error_bad_json");
        responseMap.put("data", "none returned; bad json");
      }
      return adapter.toJson(responseMap);
    }
  }
}
