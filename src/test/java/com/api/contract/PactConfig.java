package com.api.contract;

/**
 * Central config for Pact tests.
 * Keeps naming consistent across all contract tests.
 */
public class PactConfig {

    // The system making the request (your test project)
    public static final String CONSUMER = "qa-items-consumer";

    // The API being called
    public static final String PROVIDER = "qa-items-provider";
}