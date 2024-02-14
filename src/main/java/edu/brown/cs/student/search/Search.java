package edu.brown.cs.student.search;

import java.io.*;
import java.util.*;

/** A class that searches to find rows containing a certain cell value. */
public class Search {

  private List<List<String>> parsedData;
  private String searchValue;
  private int columnIndexIdentifier;
  private String columnHeaderIdentifier;
  public String columnIdentifierType;
  private boolean header;



  /**
   * Constructor for Search which takes a String columnIdentifier
   *
   * @param parsedData the parsed CSV data of form List<List<String>>
   * @param searchValue the String to search for
   * @param columnIndexIdentifier the int column index within which to search
   * @param columnIdentifierType a String of the type of columnIdentifier
   * @param header a boolean of whether the CSV file has a header or not
   */
  public Search(
      List<List<String>> parsedData,
      String searchValue,
      int columnIndexIdentifier,
      String columnIdentifierType,
      boolean header) {
    this.parsedData = parsedData;
    this.searchValue = searchValue;
    this.columnIndexIdentifier = columnIndexIdentifier;
    this.columnIdentifierType = columnIdentifierType;
    this.header = header;
  }

  /**
   * Constructor for Search which takes a String columnIdentifier
   *
   * @param parsedData the parsed CSV data of form List<List<String>>
   * @param searchValue the String to search for
   * @param columnHeaderIdentifier the column (based on header name) within which to search
   * @param columnIdentifierType a String of the type of columnIdentifier
   * @param header a boolean of whether the CSV file has a header or not
   */
  public Search(
      List<List<String>> parsedData,
      String searchValue,
      String columnHeaderIdentifier,
      String columnIdentifierType,
      boolean header) {
    this.parsedData = parsedData;
    this.searchValue = searchValue;
    this.columnHeaderIdentifier = columnHeaderIdentifier;
    this.columnIdentifierType = columnIdentifierType;
    this.header = header;
  }

  /**
   * Finds all rows with this.searchValue at a certain numerical column index
   *
   * @return an array of row indices, starting from 0, which contain this.searchValue
   */
  public List<Integer> findRowsWithValueIndexArg() {
    ArrayList<Integer> rowsWithValueInCol = new ArrayList<>();

    // NOTE THAT WE DO NOT SEARCH THE HEADER FOR TERMS
    int rowCount = 0;
    int colIndex = this.columnIndexIdentifier;
    int maxColIndex = this.parsedData.get(0).size() - 1;

    // if out of bounds just return empty list
    if (this.columnIndexIdentifier < 0 || this.columnIndexIdentifier > maxColIndex) {
      return rowsWithValueInCol;
    }

    for (List<String> row : this.parsedData) {
      if (!this.header || rowCount > 0) {
        String colIndexStringCleaned = row.get(colIndex).trim();
        if (colIndexStringCleaned.equals(this.searchValue.trim())) {
          rowsWithValueInCol.add(rowCount);
        }
      }
      rowCount++;
    }
    return rowsWithValueInCol;
  }
  /**
   * Finds all rows with this.searchValue at a certain column index based on the header name
   *
   * @return an array of row indices, starting from 0, which contain this.searchValue
   */
  public List<Integer> findRowsWithValueStringArg() {
    ArrayList<Integer> rowsWithValueInCol = new ArrayList<>();
    int rowCount = 0;
    int colIndex = -1;

    for (String value : this.parsedData.get(0)) {
      if (value.equals(this.columnHeaderIdentifier)) {
        colIndex = rowCount;
        break;
      }
      rowCount++;
    }

    // validation for if columnidentifier does not exist
    if (colIndex == -1) {
      return rowsWithValueInCol;
    }
    rowCount = 0;
    for (List<String> row : this.parsedData) {
      if (header) {
        String colValue = row.get(colIndex).trim();
        if (colValue.equals(this.searchValue.trim())) {
          rowsWithValueInCol.add(rowCount);
        }
      } else if (!(rowCount == 0)) {
        if (row.size() > colIndex) {
          String colValue = row.get(colIndex).trim();
          if (colValue.equals(this.searchValue.trim())) {
            rowsWithValueInCol.add(rowCount);
          }
        }
      }
      rowCount++;
    }
    return rowsWithValueInCol;
  }

  /**
   * Finds all rows that contain this.searchValue within the entire CSV file
   *
   * @return an array of row indices, starting from 0, which contain this.searchValue
   */
  public List<Integer> findRowsWithValueNoArg() {
    ArrayList<Integer> rowsWithValue = new ArrayList<>();
    int rowCount = 0;
    for (List<String> row : this.parsedData) {
      if (!this.header || rowCount > 0) {
        for (String value : row) {
          if (value.trim().equals(this.searchValue.trim())) {
            rowsWithValue.add(rowCount);
            break;
          }
        }
      }
      rowCount++;
    }
    return rowsWithValue;
  }
}
