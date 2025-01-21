package com.example.calulator;

/**
 * Helper class for performing helpful methods.
 */

public class CalcHelper {

  /**
   * empty constructor.
   */

  public CalcHelper() {}

  /**
   * checks, if more operators can be inserted.
   */

  public boolean insertOperator(String content) {
    boolean insertOperator = true;
    if (content.charAt(0) == '-') {
      for (int i = 1; i < content.length(); i++) {
        if (hasSecondOperator(content.charAt(i))) {
          insertOperator = false;
          break;
        }
      }
      return insertOperator;
    } else {
      return !content.contains("+") && !content.contains("-")
              && !content.contains("*") && !content.contains("/");
    }
  }

  public boolean hasSecondOperator(char i) {
    return i == '+' || i == '-' || i == '*' || i == '/';
  }

  /**
   * checks if the input is a number.
   */

  public void isNumber(String s1) throws InvalidInputException {
    try {
      Double.parseDouble(s1);
    } catch (NumberFormatException e) {
      throw new InvalidInputException("Invalid Input");
    }
  }
}