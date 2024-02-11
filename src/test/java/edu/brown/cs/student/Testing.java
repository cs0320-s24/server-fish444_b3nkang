package edu.brown.cs.student;

import edu.brown.cs.student.main.MainHelper;
import edu.brown.cs.student.parser.creators.CreateListOfStringsFromRow;
import edu.brown.cs.student.parser.creators.CreatePersonFromRow;
import edu.brown.cs.student.parser.objects.Person;
import edu.brown.cs.student.parser.outputs.Parser;
import edu.brown.cs.student.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/** testing class */
public class Testing {

  /**
   * Basic test to see if Parser can read and parse a FileReader correctly
   *
   * @throws FileNotFoundException error thrown if the file cannot be found
   */
  @Test
  public void testParserWithFileReader() throws FileNotFoundException {
    FileReader csvToRead = new FileReader("data/census/dol_ri_earnings_disparity.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Assert.assertEquals("Multiracial", parsedStringCSV.get(6).get(1).trim());
  }

  /**
   * Basic test to see if Parser can read and parse a StringReader correctly
   *
   * @throws FileNotFoundException error thrown if the file cannot be found
   */
  @Test
  public void testParserWithStringReader() throws FileNotFoundException {
    StringReader stringToRead =
        new StringReader(
            "State,Data Type,Average Weekly Earnings,Number "
                + "of Workers,Earnings Disparity,Employed Percent\n"
                + "RI,White,\" $1,058.47 \",395773.6521, $1.00 ,75%\n"
                + "RI,Black, $770.26 ,30424.80376, $0.73 ,6%\n"
                + "RI,Native American/American Indian, $471.07 ,2315.505646, $0.45 ,0%\n"
                + "RI,Asian-Pacific Islander,\" $1,080.09 \",18956.71657, $1.02 ,4%\n"
                + "RI,Hispanic/Latino, $673.14 ,74596.18851, $0.64 ,14%\n"
                + "RI,Multiracial, $971.89 ,8883.049171, $0.92 ,2%");

    Parser<List<String>> stringParser =
        new Parser<>(stringToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Assert.assertEquals("Multiracial", parsedStringCSV.get(6).get(1).trim());
  }

  /**
   * Evaluate whether Parser properly deals with complicated Regex such as "$1,058.47"
   *
   * @throws FileNotFoundException error thrown if the file cannot be found
   */
  @Test
  public void testParserComplexRegex() throws FileNotFoundException {
    FileReader csvToRead = new FileReader("data/census/dol_ri_earnings_disparity.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Assert.assertEquals("$1,058.47", parsedStringCSV.get(1).get(2).trim());
  }

  /**
   * Basic test to see if Search can correctly determine rows in which a given term exists.
   * Specifically tests if a value isn't in a column (both with index and header) or in the entire
   * sheet
   *
   * @throws IOException error thrown if either Parser or Search return errors
   */
  @Test
  public void testSearchNotInSheet() throws IOException {
    FileReader csvToRead = new FileReader("data/census/dol_ri_earnings_disparity.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Search searchWithIndex = new Search(parsedStringCSV, "Hispanic/Latinos", 0, "int", true);
    Search searchWithHeaderName =
        new Search(parsedStringCSV, "Hispanic/Latinos", "State", "String", true);
    Search searchWithNoArgs =
        new Search(parsedStringCSV, "Hispanic/Latinos", "NoArgs", "NoArgsSupplied", true);

    List<Integer> rowsWithIndex = searchWithIndex.findRowsWithValueIndexArg();
    List<Integer> rowsWithHeaderName = searchWithHeaderName.findRowsWithValueStringArg();
    List<Integer> rowsWithNoArgs = searchWithNoArgs.findRowsWithValueNoArg();

    Assert.assertEquals("[]", rowsWithIndex.toString());
    Assert.assertEquals("[]", rowsWithHeaderName.toString());
    Assert.assertEquals("[]", rowsWithNoArgs.toString());
  }

  /**
   * Basic test to see if Search can correctly determine rows in which a given term exists.
   * Specifically tests if a value isn't in a column (both with index and header) BUT in the entire
   * sheet
   *
   * @throws IOException error thrown if either Parser or Search return errors
   */
  @Test
  public void testSearchNotInColButSheet() throws IOException {
    FileReader csvToRead = new FileReader("data/census/dol_ri_earnings_disparity.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Search searchWithIndex = new Search(parsedStringCSV, "Hispanic/Latinos", 0, "int", true);
    Search searchWithHeaderName =
        new Search(parsedStringCSV, "Hispanic/Latinos", "State", "String", true);
    Search searchWithNoArgs =
        new Search(parsedStringCSV, "Hispanic/Latino", "NoArgs", "NoArgsSupplied", true);

    List<Integer> rowsWithIndex = searchWithIndex.findRowsWithValueIndexArg();
    List<Integer> rowsWithHeaderName = searchWithHeaderName.findRowsWithValueStringArg();
    List<Integer> rowsWithNoArgs = searchWithNoArgs.findRowsWithValueNoArg();

    Assert.assertEquals("[]", rowsWithIndex.toString());
    Assert.assertEquals("[]", rowsWithHeaderName.toString());
    Assert.assertEquals("[5]", rowsWithNoArgs.toString());
  }

  /**
   * Basic test to see if Search can correctly determine rows in which a given term exists.
   * Specifically tests if a value IS in both a column (both with index and header) AND in the
   * entire sheet
   *
   * @throws IOException error thrown if either Parser or Search return errors
   */
  @Test
  public void testSearchInColAndSheet() throws IOException {
    FileReader csvToRead = new FileReader("data/census/dol_ri_earnings_disparity.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Search searchWithIndex = new Search(parsedStringCSV, "Hispanic/Latino", 1, "int", true);
    Search searchWithHeaderName =
        new Search(parsedStringCSV, "Hispanic/Latino", "Data Type", "String", true);
    Search searchWithNoArgs =
        new Search(parsedStringCSV, "Hispanic/Latino", "NoArgs", "NoArgsSupplied", true);

    List<Integer> rowsWithIndex = searchWithIndex.findRowsWithValueIndexArg();
    List<Integer> rowsWithHeaderName = searchWithHeaderName.findRowsWithValueStringArg();
    List<Integer> rowsWithNoArgs = searchWithNoArgs.findRowsWithValueNoArg();

    Assert.assertEquals("[5]", rowsWithIndex.toString());
    Assert.assertEquals("[5]", rowsWithHeaderName.toString());
    Assert.assertEquals("[5]", rowsWithNoArgs.toString());
  }

  /**
   * More complicated test to see if Search can correctly determine (multiple) rows in which a given
   * term exists. Specifically tests if a value isn't in a column (both with index and header) BUT
   * in the entire sheet
   *
   * @throws IOException error thrown if either Parser or Search return errors
   */
  @Test
  public void testSearchMultipleNotInColButSheet() throws IOException {
    FileReader csvToRead = new FileReader("data/census/income_by_race.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Search searchWithIndex = new Search(parsedStringCSV, "White", 2, "int", true);
    Search searchWithHeaderName = new Search(parsedStringCSV, "White", "ID Year", "String", true);
    Search searchWithNoArgs =
        new Search(parsedStringCSV, "White", "NoArgs", "NoArgsSupplied", true);

    List<Integer> rowsWithIndex = searchWithIndex.findRowsWithValueIndexArg();
    List<Integer> rowsWithHeaderName = searchWithHeaderName.findRowsWithValueStringArg();
    List<Integer> rowsWithNoArgs = searchWithNoArgs.findRowsWithValueNoArg();

    Assert.assertEquals("[]", rowsWithIndex.toString());
    Assert.assertEquals("[]", rowsWithHeaderName.toString());
    Assert.assertEquals(
        "[6, 7, 8, 9, 10, 46, 47, 48, 49, 50, 84, 85, 86, 87, 88, 122, 123, 124, 125, 126, 161, 162, 163, 164, 165, 202, 203, 204, 205, 206, 242, 243, 244, 245, 246, 286, 287, 288, 289, 290]",
        rowsWithNoArgs.toString());
  }

  /**
   * More complicated test to see if Search can correctly determine (multiple) rows in which a given
   * term exists. Specifically tests if a value IS in both a column (both with index and header) AND
   * in the entire sheet
   *
   * @throws IOException error thrown if either Parser or Search return errors
   */
  @Test
  public void testSearchMultipleInColAndSheet() throws IOException {
    FileReader csvToRead = new FileReader("data/census/income_by_race.csv");
    Parser<List<String>> stringParser = new Parser<>(csvToRead, new CreateListOfStringsFromRow());
    List<List<String>> parsedStringCSV = stringParser.readReader();
    Search searchWithIndex = new Search(parsedStringCSV, "White", 1, "int", true);
    Search searchWithHeaderName = new Search(parsedStringCSV, "White", "Race", "String", true);
    Search searchWithNoArgs =
        new Search(parsedStringCSV, "White", "NoArgs", "NoArgsSupplied", true);

    List<Integer> rowsWithIndex = searchWithIndex.findRowsWithValueIndexArg();
    List<Integer> rowsWithHeaderName = searchWithHeaderName.findRowsWithValueStringArg();
    List<Integer> rowsWithNoArgs = searchWithNoArgs.findRowsWithValueNoArg();

    Assert.assertEquals(
        "[6, 7, 8, 9, 10, 46, 47, 48, 49, 50, 84, 85, 86, 87, 88, 122, 123, 124, 125, 126, 161, 162, 163, 164, 165, 202, 203, 204, 205, 206, 242, 243, 244, 245, 246, 286, 287, 288, 289, 290]",
        rowsWithIndex.toString());
    Assert.assertEquals(
        "[6, 7, 8, 9, 10, 46, 47, 48, 49, 50, 84, 85, 86, 87, 88, 122, 123, 124, 125, 126, 161, 162, 163, 164, 165, 202, 203, 204, 205, 206, 242, 243, 244, 245, 246, 286, 287, 288, 289, 290]",
        rowsWithHeaderName.toString());
    Assert.assertEquals(
        "[6, 7, 8, 9, 10, 46, 47, 48, 49, 50, 84, 85, 86, 87, 88, 122, 123, 124, 125, 126, 161, 162, 163, 164, 165, 202, 203, 204, 205, 206, 242, 243, 244, 245, 246, 286, 287, 288, 289, 290]",
        rowsWithNoArgs.toString());
  }

  /**
   * Evaluates that Parser actually works with a generic with a custom object, in this case, from
   * CreatePersonFromRow with the Person object.
   */
  @Test
  public void testCreatorCreatePersonFromRow() {
    FileReader csvInput = null;
    try {
      csvInput = new FileReader("data/prod/people_to_test_custom_creator.csv");
    } catch (Exception e) {
      Assert.assertEquals(1, 2);
    }
    CreatePersonFromRow creator = new CreatePersonFromRow();
    Parser<Person> peopleParser = new Parser<>(csvInput, creator);
    List<Person> peopleList = peopleParser.readReader();
    for (Person person : peopleList) {
      String name = person.getName();
      int age = person.getAge();
      String state = person.getState();
    }
    String name = peopleList.get(0).getName();
    int age = peopleList.get(0).getAge();
    String state = peopleList.get(0).getState();

    Assert.assertEquals(name, "Peter");
    Assert.assertEquals(age, 50);
    Assert.assertEquals(state, "RI");
  }

  // and test MAIN

  /** Test of parse + search (with INDEX), run from mainHelper, simulating the command line */
  @Test
  public void testMainHelperIndex() throws FileNotFoundException {
    String[] args = new String[] {"dol_ri_earnings_disparity.csv", "$1,058.47", "true", "2"};
    MainHelper main = new MainHelper(args);
    String result = main.runHelper();
    //    System.out.println(result);
    String expected =
        "Searching for `$1,058.47` in the column with index `2` in `dol_ri_earnings_disparity.csv`, your search term was found in the following row(s): [1]";
    Assert.assertEquals(result, expected);
  }

  /** Test of parse + search (with HEADER), run from mainHelper, simulating the command line */
  @Test
  public void testMainHelperHeader() throws FileNotFoundException {
    String[] args =
        new String[] {
          "dol_ri_earnings_disparity.csv", "$1,058.47", "true", "Average Weekly Earnings"
        };
    MainHelper main = new MainHelper(args);
    String result = main.runHelper();
    String expected =
        "Searching for `$1,058.47` in the column with header `Average Weekly Earnings` in `dol_ri_earnings_disparity.csv`, your search term was found in the following row(s): [1]";
    Assert.assertEquals(result, expected);
  }

  /** Test of parse + search (with NO ARG), run from mainHelper, simulating the command line */
  @Test
  public void testMainHelperNoArg() throws FileNotFoundException {
    String[] args = new String[] {"dol_ri_earnings_disparity.csv", "$1,058.47", "true"};
    MainHelper main = new MainHelper(args);
    String result = main.runHelper();
    //    System.out.println(result);
    String expected =
        "Searching for `$1,058.47` in the whole of `dol_ri_earnings_disparity.csv`, your search term was found in the following row(s): [1]";
    Assert.assertEquals(result, expected);
  }

  //    test commands:
  //      ./run-2 "dol_ri_earnings_disparity.csv" "14%" "true" "Employed Percent"
  //      ./run "dol_ri_earnings_disparity.csv" "14%" "false" "Employed Percent"
  //      ./run "dol_ri_earnings_disparity.csv" "14s%" "true" "2"
  //      ./run "dol_ri_earnings_disparity.csv" "14%" "false" "69"
  //      ./run "dol_ri_earnings_disparity.csv" "14%" "true"
  //      ./run "dol_ri_earnings_disparity.csv" "14%" "false"
  //      ./run "dol_ri_earnings_disparity.csv" "RI" "true"
  //      ./run "dol_ri_earnings_disparity.csv" "RI" "false"
  //      ./run "dol_ri_earnings_disparity.csv" "4%" "true" "5"
  //      ./run "dol_ri_earnings_disparity.csv" "4%" "false" "5"
  //      ./run "dol_ri_earnings_disparity.csv" "4%" "true" "Employed Percent"
  //      ./run "dol_ri_earnings_disparity.csv" "$1.02" "true" "Earnings Disparity"
  //      ./run "dol_ri_earnings_disparity.csv" "\$1.02" "true"
  //      ./run "dol_ri_earnings_disparity.csv" "\$1.02" "true" "Earnings Disparity"
  //      ./run "dol_ri_earnings_disparity.csv" "\$1.02" "true" "4"
  //      ./run "dol_ri_earnings_disparity.csv" "\$1,058.47" "true" "2"
  //      ./run "malformed_signs.csv" "\$1,058.47" "true" "4"
  //      ./run "malformed_signs.csv" "" "true" "2"
  //      ./run "ten-star.csv" "" "true" "2"

  //       ["dol_ri_earnings_disparity.csv","\$1,058.47","true","2"]

}
