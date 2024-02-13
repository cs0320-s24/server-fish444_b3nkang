package edu.brown.cs.student.handlers;

import com.squareup.moshi.*;
import edu.brown.cs.student.main.Server;
import edu.brown.cs.student.search.Search;
import java.util.*;
import spark.*;

// NOTE YOU MUST USE `+` FOR SPACES IN URL
public class SearchHandler implements Route {
  // copied from Ben's CSV
  private String columnHeaderIdentifier;
  private int columnIndexIdentifier;
  private boolean header;
  public String columnIdentifierType;
  private List<Integer> rowsWithVal;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String searchvalueParam = request.queryParams("searchvalue");
    String headerString = request.queryParams("header");
    String columnidentifierParam = request.queryParams("columnidentifier");
    String error_type = null;
    String error_message = null;
    System.out.println("PARSED COLUMN: " + columnidentifierParam);

    // copied from Ben's CSV
    if (headerString.equals("true")) {
      header = true;
    } else if (headerString.equals("false")) {
      header = false;
    } else {
      error_type = "error_bad_request";
      error_message =
          "Error, incorrect \"header\" argument passed: argument must either be \"true\" or \"false\".";
      //      throw new IllegalArgumentException(
      //          "Error, incorrect \"header\" argument passed: argument must either be \"true\" or
      // \"false\".");
    }

    // check if "columnIdentifier" provided
    if (columnidentifierParam != null) {
      // figure out type columnIdentifier
      try {
        if (!header) {
          error_type = "error_bad_request";
          error_message =
              "Error, columnIdentifier-header argument conflict: header was set to \"false\" but a header-index-based \"columnIdentifier\" was specified.";
          //          throw new IllegalArgumentException(
          //              "Error, columnIdentifier-header argument conflict: header was set to
          // \"false\" but a header-index-based \"columnIdentifier\" was specified.");
        } else {
          columnIndexIdentifier = Integer.parseInt(columnidentifierParam);
          columnIdentifierType = "int";
        }
      } catch (NumberFormatException e) {
        // check if header has been set to false despite a non-integer param being provided
        if (!header) {
          error_type = "error_bad_request";
          error_message =
              "Error, columnIdentifier-header argument conflict: header was set to \"false\" but a header-name-based \"columnIdentifier\" was specified.";
          //          throw new IllegalArgumentException(
          //              "Error, columnIdentifier-header argument conflict: header was set to
          // \"false\" but a header-name-based \"columnIdentifier\" was specified.");
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

    // logic to call search
    if (columnIdentifierType.equals("int")) {
      Search searchIndex =
          new Search(
              Server.parsedStringCSV,
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
      Search searchHeader =
          new Search(
              Server.parsedStringCSV,
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
      Search searchNoArg =
          new Search(
              Server.parsedStringCSV,
              searchvalueParam,
              columnHeaderIdentifier,
              columnIdentifierType,
              header);
      rowsWithVal = searchNoArg.findRowsWithValueNoArg();
      if (!rowsWithVal.isEmpty()) {
        System.out.println(
            "Searching for `" + searchvalueParam + "` in the whole of `" + Server.filepath + "`,");
        System.out.println("your search term was found in the following row(s): " + rowsWithVal);
      } else {
        System.out.println(
            "Searching for `" + searchvalueParam + "` in the whole of `" + Server.filepath + "`,");
        System.out.println("your search term was found in the following row(s): none");
      }
      //        System.out.println(rowsWithVal);
    } else {
      System.out.println("SOMETHING HAS GONE VERY WRONG.");
    }
    //
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

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
      responseMap.put("data", Server.parsedStringCSV);
      responseMap.put("parameters", params);
      responseMap.put("rows_with_params", rowsWithVal);
    }
    return adapter.toJson(responseMap);
  }
}
