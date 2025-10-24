package com.api.utils;

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

  public static String get(String key) {
    String sysProp = System.getProperty(key);
    if (sysProp != null) {
      return sysProp;
    }
    return properties.getProperty(key);
  }
}
