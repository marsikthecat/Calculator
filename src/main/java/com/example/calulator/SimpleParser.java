package com.example.calulator;

/**
 * This is a parser which parser arithmetic expressions. Nice, huh?.
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
   * Expression.
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
   * Term.
   */
  public static double term() throws Exception {
    double val = factor();
    while (hasNextChar() && (peek() == '*' || peek() == '/')) {
      char op = getNextChar();
      if (op == '*') {
        val *= factor();
      } else {
        val /= factor();
      }
    }
    return val;
  }

  /**
   * Factor.
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
    * Braces.
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
    * Number.
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