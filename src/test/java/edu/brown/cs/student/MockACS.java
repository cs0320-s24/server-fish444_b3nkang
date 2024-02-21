package edu.brown.cs.student;

import edu.brown.cs.student.Proxys.BroadbandProxy;
import java.util.ArrayList;
import java.util.List;

public class MockACS {

  List<List<String>> stateCodes;

  List<List<String>> broadband;

  public MockACS() {
    this.stateCodes = new ArrayList<>();
    this.broadband = new ArrayList<>();
  }

  public MockACS(List<List<String>> stateCodes, List<List<String>> broadband) {
    this.stateCodes = stateCodes;
    this.broadband = broadband;
  }

  public MockACS(List<List<String>> stateCodes) {
    this.stateCodes = stateCodes;
  }

  public List<List<String>> getStateCodes(List<List<String>> stateCodes) {
    BroadbandProxy proxy = new BroadbandProxy(stateCodes);
    return proxy;
  }

  public List<List<String>> getBroadband(List<List<String>> broadband) {
    BroadbandProxy proxy = new BroadbandProxy(broadband);
    return proxy;
  }
}
