package com.example.calulator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Simple Calculator.
 */

public class Main extends Application {

  private TextField outputField;
  private final String[][] symbols = {{"C", ">", "√", "x²", "π"}, {"9", "8", "7", "/", "x³"},
    {"6", "5", "4", "*", "e"}, {"3", "2", "1", "-", "!"}, {"0", "=", ".", "+"}};

  @Override
  public void start(Stage stage) {
    outputField = new TextField();
    outputField.setPrefWidth(250);
    outputField.setFont(new Font(25));
    outputField.setEditable(false);
    outputField.setCursor(Cursor.DISAPPEAR);
    HBox rsection = new HBox();
    rsection.setAlignment(Pos.CENTER);
    rsection.getChildren().addAll(outputField);
    GridPane table = new GridPane();
    table.setAlignment(Pos.CENTER);

    for (int row = 0; row < symbols.length; row++) {
      for (int col = 0; col < symbols[row].length; col++) {
        String symbol = symbols[row][col];
        CalcButton button = new CalcButton(symbol, row, col, table);
        switch (symbol) {
          case "C" -> button.act(outputField::clear);
          case ">" -> button.act(this::delete);
          case "√" -> button.act(this::calcSqrt);
          case "x²" -> button.act(this::calcPow);
          case "π" -> button.act(() -> insert("3.141529"));
          case "x³" -> button.act(this::calcKube);
          case "e" -> button.act(() -> insert("2.718128"));
          case "!" -> button.act(this::calcfacultaet);
          case "/", "*", "+", "-" -> button.act(() -> handleInputButtons(button));
          case "=" -> button.act(this::calculate);
          default -> button.act(() -> insert(symbol));
        }
      }
    }
    VBox box = new VBox();
    box.getChildren().addAll(rsection, table);
    Scene scene = new Scene(box, 270, 310);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  private void insert(String stuff) {
    outputField.setText(outputField.getText() + stuff);
  }

  private void clearAndInsert(String stuff) {
    outputField.clear();
    insert(stuff);
  }

  void delete() {
    if (!outputField.getText().isEmpty()) {
      outputField.setText(outputField.getText().substring(0, outputField.getText().length() - 1));
    }
  }

  void handleInputButtons(Button btn) {
    String btntext = btn.getText();
    String input = outputField.getText();
    if (hasoperator(btntext) && hasoperator(input) && !bminus(input)) {
      calculate();
    } else {
      if (hasTwoOperators(input) && hasoperator(btntext)) {
        calculate();
      } else {
        outputField.setText(outputField.getText() + btn.getText());
      }
    }
  }

  void calcPow() {
    if (checkn(outputField.getText())) {
      double d = Double.parseDouble(outputField.getText());
      clearAndInsert(String.valueOf(d * d));
    } else {
      error("Please enter a number");
    }
  }

  void calcKube() {
    if (checkn(outputField.getText())) {
      double d = Double.parseDouble(outputField.getText());
      clearAndInsert(String.valueOf(d * d * d));
    } else {
      error("Please enter a number");
    }
  }

  void calcSqrt() {
    if (checkn(outputField.getText())) {
      double d = Double.parseDouble(outputField.getText());
      if (d > 0) {
        clearAndInsert(String.valueOf(Math.sqrt(d)));
      } else {
        clearAndInsert(Math.sqrt(Math.abs(d)) + "i");
      }
    } else {
      error("Please enter a number");
    }
  }

  void calcfacultaet() {
    if (checkn(outputField.getText())) {
      double d = Double.parseDouble(outputField.getText());
      if (d >= 0) {
        clearAndInsert(String.valueOf(fakultaet(d)));
      } else {
        error("Calculation only possible for positive numbers");
      }
    } else {
      error("Please enter a number");
    }
  }

  boolean bminus(String s) {
    return s.charAt(0) == '-';
  }

  double fakultaet(double a) {
    if (a <= 1) {
      return a;
    } else {
      return a * fakultaet(a - 1);
    }
  }

  void calculate() {
    String input = outputField.getText();
    int pos = 0;
    char operator = 0;
    char[] operators = {'+', '-', '*', '/'};
    char[] chars = input.toCharArray();
    for (int i = 0; i < input.length(); i++) {
      for (char c : operators) {
        if (chars[i] == c) {
          pos = i;
          operator = c;
        }
      }
    }
    if (pos != 0) {
      String s1 = input.substring(0, pos);
      String s2 = input.substring(pos + 1);
      if (checkn(s1) && checkn(s2)) {
        double n1 = Double.parseDouble(s1);
        double n2 = Double.parseDouble(s2);
        double r;
        switch (operator) {
          case '+' -> r = n1 + n2;
          case '-' -> r = n1 - n2;
          case '*' -> r = n1 * n2;
          case '/' -> r = n1 / n2;
          default -> r = 0;
        }
        outputField.setText(String.valueOf(cut(r)));
      } else {
        error("Calculation failed: \nWrong input");
      }
    }
  }

  boolean checkn(String s1) {
    try {
      Double.parseDouble(s1);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  double cut(double number) {
    double factor = Math.pow(10, 5);
    return Math.floor(number * factor) / factor;
  }

  boolean hasoperator(String s) {
    return s.contains("+") || s.contains("-") || s.contains("*") || s.contains("/");
  }

  boolean hasTwoOperators(String input) {
    String operators = "+-*/";
    int operatorCount = 0;
    for (int i = 0; i < input.length(); i++) {
      if (operators.indexOf(input.charAt(i)) != -1) {
        operatorCount++;
        if (operatorCount >= 2) {
          return true;
        }
      }
    }
    return false;
  }

  private void error(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fucking Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}