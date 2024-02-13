package edu.brown.cs.student.main;

import java.io.*;
import java.util.*;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  //  private String fileName;
  //
  //  private String searchValue;
  //  private String columnHeaderIdentifier;
  //  private int columnIndexIdentifier;
  //  private boolean header;
  //  public String columnIdentifierType;

  /**
   * The public main which runs the private Main and does additional error handling
   *
   * @param args the command-line arguments made by the user
   */
  //  public static void main(String[] args) {
  //    try {
  //      new Main(args).run();
  //    } catch (FileNotFoundException e) {
  //      System.err.println("Error caught in main, unreachable file: " + e);
  //    } catch (IllegalArgumentException e) {
  //      System.err.println("Error caught in main, illegal args: " + e);
  //    } catch (Exception e) {
  //      System.err.println("Error caught in main, some other unidentified error: " + e);
  //    }
  //  }

  /**
   * The function which sets up the command-line argument structure and parses it
   *
   * @param args the command-line arguments
   * @throws IllegalArgumentException the error thrown if the args are incorrect
   */
  private Main(String[] args) throws IllegalArgumentException, FileNotFoundException {
    new MainHelper(args).runHelper();
    //    if (args.length < 2 || args.length > 4) {
    //      throw new IllegalArgumentException(
    //          "Error, missing or too many args: fileName.csv and stringToSearchFor required,
    // columnIdentifier and header optional.");
    //    } else {
    //      System.out.println("ARGS: " + Arrays.toString(args));
    //      this.fileName = args[0];
    //      this.searchValue = args[1];
    //      String headerString = args[2];
    //
    //      // check if "header" param is valid
    //      if (headerString.equals("true")) {
    //        this.header = true;
    //      } else if (headerString.equals("false")) {
    //        this.header = false;
    //      } else {
    //        throw new IllegalArgumentException(
    //            "Error, incorrect \"header\" argument passed: argument must either be \"true\" or
    // \"false\".");
    //      }
    //
    //      // check if "columnIdentifier" provided
    //      if (args.length == 4) {
    //        // figure out type columnIdentifier
    //        try {
    //          if (!this.header) {
    //            throw new IllegalArgumentException(
    //                "Error, columnIdentifier-header argument conflict: header was set to \"false\"
    // but a header-index-based \"columnIdentifier\" was specified.");
    //          } else {
    //            this.columnIndexIdentifier = Integer.parseInt(args[3]);
    //            this.columnIdentifierType = "int";
    //          }
    //        } catch (NumberFormatException e) {
    //          // check if header has been set to false despite a non-integer param being provided
    //          if (!this.header) {
    //            throw new IllegalArgumentException(
    //                "Error, columnIdentifier-header argument conflict: header was set to \"false\"
    // but a header-name-based \"columnIdentifier\" was specified.");
    //          } else {
    //            this.columnHeaderIdentifier = args[3];
    //            this.columnIdentifierType = "String";
    //          }
    //        }
    //      } else {
    //        this.columnHeaderIdentifier = "NoArgSupplied";
    //        this.columnIdentifierType = "NoArgSupplied";
    //      }
    //    }
  }

  /**
   * The function which instantiates the Parser and Search objects and prints the result to the user
   *
   * @throws FileNotFoundException the error thrown if command-line file arg is incorrect/missing
   */
  private void run() throws FileNotFoundException {
    //    // CSVs in Prod Directory
    //    String filepath = "data/prod/" + this.fileName;
    //    List<Integer> rowsWithVal = new ArrayList<>();
    //    try {
    //      FileReader csvToRead = new FileReader(filepath);
    //      Parser<List<String>> stringParser = new Parser<>(csvToRead, new
    // CreateListOfStringsFromRow());
    //      List<List<String>> parsedStringCSV = stringParser.readReader();
    //      System.out.println("PARSED LIST: " + parsedStringCSV);
    //      if (this.columnIdentifierType.equals("int")) {
    //        Search searchIndex =
    //            new Search(
    //                parsedStringCSV,
    //                this.searchValue,
    //                this.columnIndexIdentifier,
    //                this.columnIdentifierType,
    //                this.header);
    //        rowsWithVal = searchIndex.findRowsWithValueIndexArg();
    //        if (!rowsWithVal.isEmpty()) {
    //          System.out.println(
    //              "Searching for `"
    //                  + this.searchValue
    //                  + "` in the column with index `"
    //                  + this.columnIndexIdentifier
    //                  + "` in `"
    //                  + this.fileName
    //                  + ",");
    //          System.out.println("your search term was found in the following row(s): " +
    // rowsWithVal);
    //        } else {
    //          System.out.println(
    //              "Searching for `"
    //                  + this.searchValue
    //                  + "` in the column with index `"
    //                  + this.columnIndexIdentifier
    //                  + "` in `"
    //                  + this.fileName
    //                  + ",");
    //          System.out.println("your search term was found in the following row(s): none");
    //        }
    //        System.out.println(rowsWithVal);
    //      } else if (this.columnIdentifierType.equals("String")) {
    //        Search searchHeader =
    //            new Search(
    //                parsedStringCSV,
    //                this.searchValue,
    //                this.columnHeaderIdentifier,
    //                this.columnIdentifierType,
    //                this.header);
    //        rowsWithVal = searchHeader.findRowsWithValueStringArg();
    //        if (!rowsWithVal.isEmpty()) {
    //          System.out.println(
    //              "Searching for `"
    //                  + this.searchValue
    //                  + "` in the column with header `"
    //                  + this.columnHeaderIdentifier
    //                  + "` in `"
    //                  + this.fileName
    //                  + "`,");
    //          System.out.println("your search term was found in the following row(s): " +
    // rowsWithVal);
    //        } else {
    //          System.out.println(
    //              "Searching for `"
    //                  + this.searchValue
    //                  + "` in the column with header `"
    //                  + this.columnHeaderIdentifier
    //                  + "` in `"
    //                  + this.fileName
    //                  + "`,");
    //          System.out.println("your search term was found in the following row(s): none");
    //        }
    //        System.out.println(rowsWithVal);
    //      } else if (columnIdentifierType.equals("NoArgSupplied")) {
    //        Search searchNoArg =
    //            new Search(
    //                parsedStringCSV,
    //                this.searchValue,
    //                this.columnHeaderIdentifier,
    //                this.columnIdentifierType,
    //                this.header);
    //        rowsWithVal = searchNoArg.findRowsWithValueNoArg();
    //        if (!rowsWithVal.isEmpty()) {
    //          System.out.println(
    //              "Searching for `" + this.searchValue + "` in the whole of `" + this.fileName +
    // "`,");
    //          System.out.println("your search term was found in the following row(s): " +
    // rowsWithVal);
    //        } else {
    //          System.out.println(
    //              "Searching for `" + this.searchValue + "` in the whole of `" + this.fileName +
    // "`,");
    //          System.out.println("your search term was found in the following row(s): none");
    //        }
    //        System.out.println(rowsWithVal);
    //      } else {
    //        System.out.println("SOMETHING HAS GONE VERY WRONG.");
    //      }
    //    } catch (FileNotFoundException e) {
    //      System.err.println("Error: something went wrong with finding the file! Trace: " + e);
    //    }
  }
}
