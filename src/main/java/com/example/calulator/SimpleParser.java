package com.example.calulator;

public class SimpleParser {
  static String input = null;
  static int pos = 0;

  public static double parse(String input) throws Exception {
    SimpleParser.input = input;
    SimpleParser.pos = 0;
    return expression();
  }

  public static double expression() throws Exception {
    double val = term();
    while (hasNextChar() && (peek() == '+' || peek() == '-')) {
      char op = getNextChar();
      if (op == '+') val += term();
      else val -= term();
    }
    return val;
  }

  public static double term() throws Exception {
    double val = factor();
    while (hasNextChar() && (peek() == '*' || peek() == '/')) {
      char op = getNextChar();
      if (op == '*') val *= factor();
      else val /= factor();
    }
    return val;
  }

  public static double factor() throws Exception {
    System.out.println(input);
    if (!hasNextChar()) throw new Exception("Unexpected end of factor");
    if (peek() == '(') {
      return bracedExpression();
    } else {
      return number();
    }
    }

  public static double bracedExpression() throws Exception {
    if (getNextChar() != '(') throw new Exception("'(' expected");
    double val = expression();
    if (getNextChar() != ')') throw new Exception("')' expected");
    return val;
  }

  public static double number() throws Exception {
    StringBuilder sb = new StringBuilder();
    while (hasNextChar() && (Character.isDigit(peek()) || peek() == '.')) {
      sb.append(getNextChar());
    }
    if (sb.isEmpty()) throw new Exception("Number expected");
    return Double.parseDouble(sb.toString());
  }

  public static boolean hasNextChar() {
    return pos < input.length();
  }

  public static char peek() {
    return input.charAt(pos);
  }

  public static char getNextChar() {
    return input.charAt(pos++);
  }
}