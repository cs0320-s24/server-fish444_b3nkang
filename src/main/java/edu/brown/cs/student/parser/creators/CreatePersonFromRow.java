package edu.brown.cs.student.parser.creators;

import edu.brown.cs.student.parser.excsandinterfaces.CreatorFromRow;
import edu.brown.cs.student.parser.excsandinterfaces.FactoryFailureException;
import edu.brown.cs.student.parser.objects.Person;
import java.util.*;

public class CreatePersonFromRow implements CreatorFromRow<Person> {
  public Person create(List<String> row) throws FactoryFailureException {
    if (row.size() != 3) {
      throw new FactoryFailureException("Error: incorrect column-row CSV data format", row);
    }
    String name = row.get(0);
    String ageString = row.get(1);
    String state = row.get(2);
    Integer age;
    try {
      age = Integer.parseInt(ageString);
    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Error: age column value is not valid integer.", row);
    }
    return new Person(name, age, state);
  }
}
