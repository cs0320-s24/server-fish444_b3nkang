package edu.brown.cs.student.parser.creators;

import edu.brown.cs.student.parser.excsandinterfaces.CreatorFromRow;
import java.util.*;

public class CreateListOfStringsFromRow implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row) {
    return row;
  }
}
