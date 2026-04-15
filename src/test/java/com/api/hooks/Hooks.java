package com.api.hooks;

import com.api.service.ItemService;
import com.api.utils.ApiTestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

  private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

  private final ApiTestContext context;
  private final ItemService itemService;

  public Hooks(ApiTestContext context, ItemService itemService) {
    this.context = context;
    this.itemService = itemService;
  }

  @Before
  public void beforeScenario(Scenario scenario) {
    context.clear();
    logger.info("=== Running scenario: {} ===", scenario.getName());
  }

  @After
  public void afterScenario(Scenario scenario) {

    // Log the last response if scenario failed
    if (scenario.isFailed() && context.getResponse() != null) {
      logger.error("Scenario failed. Last response:");
      logger.error(context.getResponse().asPrettyString());
    }

    // Cleanup test data if an object was created
    final String id = context.getObjectId();
    if (id != null) {
      itemService.deleteItemIfExists(id); // safe cleanup
    }

    // Clear context for next scenario
    context.clear();

   final  String status = scenario.isFailed() ? "FAILED" : "PASSED";
    logger.info("=== Finished scenario: {} | Status: {} ===", scenario.getName(), status);
  }
}
