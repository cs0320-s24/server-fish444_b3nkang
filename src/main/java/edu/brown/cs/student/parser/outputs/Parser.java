package edu.brown.cs.student.parser.outputs;

import edu.brown.cs.student.parser.excsandinterfaces.CreatorFromRow;
import edu.brown.cs.student.parser.excsandinterfaces.FactoryFailureException;
import java.io.*;
import java.util.*;

/**
 * A class that takes a CSV input and parses it into a List<String> for a Creator to use
 *
 * @param <T> denoting the use of a generic
 */
public class Parser<T> {

  private Reader csvInput;
  private CreatorFromRow<T> creator;

  /**
   * The constructor for the Parser.
   *
   * @param csvInput the CSV input of type Reader
   * @param creator the 'attachment' for additional parsing of the data of type CreatorFromRow<T>
   */
  public Parser(Reader csvInput, CreatorFromRow<T> creator) {
    this.csvInput = csvInput;
    this.creator = creator;
  }

  // postprocess javadoc and method below taken from CSV assignment handout link
  /**
   * Eliminate a single instance of leading or trailing double-quote, and replace pairs of double
   * quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   */
  public static String postprocess(String arg) {
    return arg
        // Remove extra spaces at beginning and end of the line
        .trim()
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"");
  }

  /**
   * The function to read the CSV input, split it, clean it, and call the Creator.
   *
   * @return an ArrayList<T> of row objects, of the type specified in the Creator.
   */
  public List<T> readReader() {
    ArrayList<T> rowObjList = new ArrayList<>();
    BufferedReader buffer = new BufferedReader(this.csvInput);

    try {
      String row;
      while ((row = buffer.readLine()) != null) {
        String[] splitRow =
            row.split(
                ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))"); // Regex taken from livecode
        // handout
        String[] processedRow = new String[splitRow.length];
        for (int i = 0; i < splitRow.length; i++) {
          processedRow[i] = postprocess(splitRow[i]);
        }
        List<String> splitRowTypeList = Arrays.asList(processedRow);
        rowObjList.add(creator.create(splitRowTypeList));
      }
    } catch (IOException e) {
      System.err.println("Something with the data input went wrong: " + e);
    } catch (FactoryFailureException e) {
      throw new RuntimeException("Something with the Creator went wrong: " + e);
    }

    return rowObjList;
  }
}
