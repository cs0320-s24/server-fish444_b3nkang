package edu.brown.cs.student.parser.objects;

import java.util.*;

public class Person {
  private String name;
  private int age;
  private String state;

  public Person(String name, int age, String state) {
    this.name = name;
    this.age = age;
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getState() {
    return state;
  }
}
