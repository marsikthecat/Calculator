package com.example.calulator;

public class CalcHelper {

  public CalcHelper() {
  }

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
        return !content.contains("+") && !content.contains("-") &&
                !content.contains("*") && !content.contains("/");
    }
  }

  public boolean hasSecondOperator(char i) {
    return i == '+' || i == '-' || i == '*' || i == '/';
  }

  public boolean checkn(String s1) {
    try {
      Double.parseDouble(s1);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }


}
