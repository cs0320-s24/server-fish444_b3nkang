package edu.brown.cs.student.handlers;

import com.squareup.moshi.*;
import edu.brown.cs.student.main.Server;
import edu.brown.cs.student.parser.creators.CreateListOfStringsFromRow;
import edu.brown.cs.student.parser.outputs.Parser;
import edu.brown.cs.student.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import spark.*;

// NOTE YOU MUST USE `+` FOR SPACES IN URL

/**
 * A class to search a loaded CSV file for a certain term, with flexibility for searching within a
 * specific row.
 */
public class SearchHandler implements Route {
  // copied from Ben's CSV
  private String columnHeaderIdentifier;
  private int columnIndexIdentifier;
  private boolean header;
  public String columnIdentifierType;
  private List<Integer> rowsWithVal;

  /**
   * A method to handle the request made when the endpoint is hit and to return an appropriate
   * JSON response.
   *
   * @return a JSON-like string
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String searchvalueParam = request.queryParams("searchvalue");
    String headerString = request.queryParams("header");
    String columnidentifierParam = request.queryParams("columnidentifier");
    String error_type = null;
    String error_message = null;

    //    System.out.println(searchvalueParam);
    //    System.out.println(headerString);
    //    System.out.println(columnidentifierParam);

    //    System.out.println("PARSED COLUMN: " + columnidentifierParam);

    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    //    System.out.println("1");

    // validate that csv has been loaded
    if (!Server.loaded) {
      responseMap.put("result", "error_datasource");
      responseMap.put("data", "none returned; csv must be loaded before viewing");
      return adapter.toJson(responseMap);
    }

    // validation for params
    List<String> expectedParams = Arrays.asList("searchvalue", "header", "columnidentifier");
    List<String> mandatoryParams = Arrays.asList("searchvalue", "header");
    Map<String, String> providedParams = new HashMap<>();
    for (String param : request.queryParams()) {
      providedParams.put(param, request.queryParams(param));
    }
    for (String key : providedParams.keySet()) {
      if (!expectedParams.contains(key)) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("data", "none returned; unknown/malformed parameter in call");
        return adapter.toJson(responseMap);
      }
    }
    for (String param : mandatoryParams) {
      if (!providedParams.containsKey(param) || providedParams.get(param).isEmpty()) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("data", "none returned; missing, empty, or malformed parameter(s)");
        return adapter.toJson(responseMap);
      }
    }

    try {
      // just to test that everything works
      FileReader csvToRead = new FileReader(Server.filepath);
      Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
      List<List<String>> parsedStringCSVTest = stringParser.readReader();
    } catch (FileNotFoundException e) {
      System.out.println("UNREACHABLE TRIGGERED: SearchHandler PARSING");
      responseMap.put("result", "error_datasource");
      responseMap.put("data", "none returned; csv filepath invalid/could not be found");
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      System.out.println("UNREACHABLE TRIGGERED: SearchHandler PARSING");
      responseMap.put("result", "error_bad_json");
      responseMap.put("data", "none returned; bad json");
      return adapter.toJson(responseMap);
    }

    FileReader csvToRead = new FileReader(Server.filepath);
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();

    //    System.out.println("2");

    // copied from Ben's CSV
    if (headerString.equals("true")) {
      header = true;
    } else if (headerString.equals("false")) {
      header = false;
    } else {
      error_type = "error_bad_request";
      error_message =
          "Error, incorrect 'header' argument passed: argument must either be 'true' or 'false'.";
      //      throw new IllegalArgumentException(
      //          "Error, incorrect 'header' argument passed: argument must either be 'true' or
      // 'false'.");
    }
    //    System.out.println("3");

    // check if "columnIdentifier" provided
    if (columnidentifierParam != null) {
      // figure out type columnIdentifier
      try {
        if (!header) {
          error_type = "error_bad_request";
          error_message =
              "Error, columnIdentifier-header argument conflict: header was set to 'false' but a header-index-based 'columnIdentifier' was specified.";
          //          throw new IllegalArgumentException(
          //              "Error, columnIdentifier-header argument conflict: header was set to
          // 'false' but a header-index-based 'columnIdentifier' was specified.");
        } else {
          columnIndexIdentifier = Integer.parseInt(columnidentifierParam);
          columnIdentifierType = "int";
        }
      } catch (NumberFormatException e) {
        // check if header has been set to false despite a non-integer param being provided
        if (!header) {
          error_type = "error_bad_request";
          error_message =
              "Error, columnIdentifier-header argument conflict: header was set to 'false' but a header-name-based 'columnIdentifier' was specified.";
          //          throw new IllegalArgumentException(
          //              "Error, columnIdentifier-header argument conflict: header was set to
          // 'false' but a header-name-based 'columnIdentifier' was specified.");
        } else {
          columnHeaderIdentifier = columnidentifierParam;
          columnIdentifierType = "String";
        }
      }
    } else {
      columnHeaderIdentifier = "NoArgSupplied";
      columnIdentifierType = "NoArgSupplied";
    }
    // end of arg verification
    //    System.out.println("4");

    // logic to call search
    if (columnIdentifierType.equals("int")) {
      System.out.println("INT SEARCH");
      Search searchIndex =
          new Search(
              parsedStringCSV,
              searchvalueParam,
              columnIndexIdentifier,
              columnIdentifierType,
              header);
      rowsWithVal = searchIndex.findRowsWithValueIndexArg();
      if (!rowsWithVal.isEmpty()) {
        System.out.println(
            "Searching for `"
                + searchvalueParam
                + "` in the column with index `"
                + columnIndexIdentifier
                + "` in `"
                + Server.filepath
                + ",");
        System.out.println("your search term was found in the following row(s): " + rowsWithVal);
      } else {
        System.out.println(
            "Searching for `"
                + searchvalueParam
                + "` in the column with index `"
                + columnIndexIdentifier
                + "` in `"
                + Server.filepath
                + ",");
        System.out.println("your search term was found in the following row(s): none");
      }
      //        System.out.println(rowsWithVal);
    } else if (columnIdentifierType.equals("String")) {
      System.out.println("STRING SEARCH");
      System.out.println(
          "PARAMS: "
              + parsedStringCSV
              + " "
              + searchvalueParam
              + " "
              + columnHeaderIdentifier
              + " "
              + columnIdentifierType
              + " "
              + header);

      Search searchHeader =
          new Search(
              parsedStringCSV,
              searchvalueParam,
              columnHeaderIdentifier,
              columnIdentifierType,
              header);
      rowsWithVal = searchHeader.findRowsWithValueStringArg();
      if (!rowsWithVal.isEmpty()) {
        System.out.println(
            "Searching for `"
                + searchvalueParam
                + "` in the column with header `"
                + columnHeaderIdentifier
                + "` in `"
                + Server.filepath
                + "`,");
        System.out.println("your search term was found in the following row(s): " + rowsWithVal);
      } else {
        System.out.println(
            "Searching for `"
                + searchvalueParam
                + "` in the column with header `"
                + columnHeaderIdentifier
                + "` in `"
                + Server.filepath
                + "`,");
        System.out.println("your search term was found in the following row(s): none");
      }
      //        System.out.println(rowsWithVal);
    } else if (columnIdentifierType.equals("NoArgSupplied")) {
      System.out.println("ARGLESS SEARCH");
      //      System.out.println(
      //          "PARAMS: "
      //              + parsedStringCSV
      //              + " "
      //              + searchvalueParam
      //              + " "
      //              + columnHeaderIdentifier
      //              + " "
      //              + columnIdentifierType
      //              + " "
      //              + header);
      Search searchNoArg =
          new Search(
              parsedStringCSV,
              searchvalueParam,
              columnHeaderIdentifier,
              columnIdentifierType,
              header);
      rowsWithVal = searchNoArg.findRowsWithValueNoArg();
      if (!rowsWithVal.isEmpty()) {
        System.out.println(
            "Searching for `" + searchvalueParam + "` in the whole of `" + Server.filepath + "`,");
        System.out.println("your search term was found in the following row(s): " + rowsWithVal);
      }
      //        System.out.println(rowsWithVal);
    } else {
      System.out.println("SOMETHING HAS GONE VERY WRONG.");
    }
    //
    //    System.out.println("5");

    ArrayList<String> params = new ArrayList<>();
    params.add(searchvalueParam);
    params.add(headerString);
    params.add(columnidentifierParam);

    if (error_type != null) {
      responseMap.put("result", error_type);
      responseMap.put("error_details", error_message);
      responseMap.put("data", "none returned; bad request params");
      responseMap.put("parameters", params);
      responseMap.put("rows_with_params", "none returned; bad request params");
    } else {
      responseMap.put("result", "success");
      responseMap.put("data", parsedStringCSV);
      responseMap.put("parameters", params);
      responseMap.put("rows_with_params", rowsWithVal);
    }
    return adapter.toJson(responseMap);
  }
}
