package com.example.calulator;

/**
 * This is a parser which parses arithmetic expressions. Nice, huh?.
 */

public class SimpleParser {
  static String input = null;
  static int pos = 0;

  /**
   * start method for parsing.
   */
  public static double parse(String input) throws Exception {
    SimpleParser.input = input;
    SimpleParser.pos = 0;
    return expression();
  }

  /**
   * Handles + - expressions.
   */
  public static double expression() throws Exception {
    double val = term();
    while (hasNextChar() && (peek() == '+' || peek() == '-')) {
      char op = getNextChar();
      if (op == '+') {
        val += term();
      } else {
        val -= term();
      }
    }
    return val;
  }

  /**
   * Handels * / expressions.
   */
  public static double term() throws Exception {
    double val = power();
    while (hasNextChar() && (peek() == '*' || peek() == '/')) {
      char op = getNextChar();
      if (op == '*') {
        val *= power();
      } else {
        val /= power();
      }
    }
    return val;
  }

  /**
   * Powerranger.
   */
  private static double power() throws Exception {
    double val = signedFactor();
    while (hasNextChar() && peek() == '^') {
      getNextChar();
      val = Math.pow(val, power());
    }
    return val;
  }

  /**
   * Handle signed Factores like (-2)... .
   */
  public static double signedFactor() throws Exception {
    if (peek() == '+') {
      getNextChar();
      return factor();
    } else if (peek() == '-') {
      getNextChar();
      return -factor();
    } else {
      return factor();
    }
  }

  /**
   * Handels factors.
   */
  public static double factor() throws Exception {
    if (!hasNextChar()) {
      throw new Exception("Unexpected end of factor");
    }
    if (peek() == '(') {
      return bracedExpression();
    } else {
      return number();
    }
  }

  /**
    * Handels branced Expressions.
   */
  public static double bracedExpression() throws Exception {
    if (getNextChar() != '(') {
      throw new Exception("'(' expected");
    }
    double val = expression();
    if (getNextChar() != ')') {
      throw new Exception("')' expected");
    }
    return val;
  }

  /**
    * Looks for Numbers and parses them.
  */
  public static double number() throws Exception {
    StringBuilder sb = new StringBuilder();
    while (hasNextChar() && (Character.isDigit(peek()) || peek() == '.')) {
      sb.append(getNextChar());
    }
    if (sb.isEmpty()) {
      throw new Exception("Number expected");
    }
    return Double.parseDouble(sb.toString());
  }

  /**
   * Tells whether the input has a next char.
   */
  public static boolean hasNextChar() {
    return pos < input.length();
  }

  /**
   * Peeks the next char of the input and returns it.
   */
  public static char peek() {
    return input.charAt(pos);
  }

  public static char getNextChar() {
    return input.charAt(pos++);
  }
}