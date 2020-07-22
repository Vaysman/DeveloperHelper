package ru.tishtech.developerhelper.service;

public class CorrectorService {

  public static String toCapitalString(String original) {
    return original.substring(0, 1).toUpperCase() + original.substring(1);
  }

  public static String toSmallString(String original) {
    return original.substring(0, 1).toLowerCase() + original.substring(1);
  }
}
