package com.api.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

  private static final Properties properties = new Properties();

  static {
    try (InputStream input =
        ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        throw new RuntimeException("Unable to find config.properties");
      }
      properties.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load config.properties", e);
    }
  }

  public static String get(@NotNull String key) {
    // Check environment variable (Docker friendly)
    final String envVar = System.getenv(key.toUpperCase().replace(".", "_"));
    if (envVar != null) {
      return envVar;
    }
    // Check system property (-Dkey=value)
    final String sysProp = System.getProperty(key);
    if (sysProp != null) {
      return sysProp;
    }
    // Fallback to properties file
    return properties.getProperty(key);
  }
}
