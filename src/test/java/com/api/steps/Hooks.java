package com.api.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

  @Before
  public void beforeScenario(Scenario scenario) {
    System.out.println("\n=== Running scenario: " + scenario.getName() + " ===");
  }

  @After
  public void afterScenario(Scenario scenario) {
    String status = scenario.isFailed() ? "FAILED" : "PASSED";
    System.out.println(
        "=== Finished scenario: " + scenario.getName() + " | Status: " + status + " ===\n");
  }
}
