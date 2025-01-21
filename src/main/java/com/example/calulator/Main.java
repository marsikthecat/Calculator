package com.example.calulator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple Calculator.
 */

public class Main extends Application {

  private TextField outputField;
  private final CalcHelper helper = new CalcHelper();
  private final String[][] symbols = {{"C", ">", "√", "x²", "π"}, {"9", "8", "7", "/", "x³"},
    {"6", "5", "4", "*", "e"}, {"3", "2", "1", "-", "!"}, {"0", "=", ".", "+", "N"}};

  @Override
  public void start(Stage stage) {
    outputField = new TextField();
    outputField.setStyle("-fx-pref-width: 250px; -fx-font-size: 25; -fx-cursor: not-allowed");
    outputField.setEditable(false);
    HBox rsection = new HBox();
    rsection.setAlignment(Pos.CENTER);
    rsection.getChildren().addAll(outputField);
    GridPane table = getGridPaneWithButtons();
    VBox box = new VBox();
    box.getChildren().addAll(rsection, table);
    Scene scene = new Scene(box, 270, 310);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  private GridPane getGridPaneWithButtons() {
    GridPane table = new GridPane();
    table.setAlignment(Pos.CENTER);
    for (int row = 0; row < symbols.length; row++) {
      for (int col = 0; col < symbols[row].length; col++) {
        String symbol = symbols[row][col];
        CalcButton button = new CalcButton(symbol, row, col, table);
        switch (symbol) {
          case "C" -> button.act(() -> outputField.clear());
          case ">" -> button.act(this::delete);
          case "√", "x²", "x³", "!", "N" ->  button.act(() -> calcExp(symbol));
          case "=" -> button.act(this::calculate);
          case "π" -> button.act(() -> insert("3.141529"));
          case "e" -> button.act(() -> insert("2.718128"));
          case "/", "*", "+", "-" -> button.act(() -> handleOperatorButtons(button));
          default -> button.act(() -> insert(symbol));
        }
      }
    }
    return table;
  }

  private void insert(String stuff) {
    outputField.setText(outputField.getText() + stuff);
  }

  private void clearAndInsert(String stuff) {
    outputField.clear();
    insert(stuff);
  }

  private void delete() {
    if (!outputField.getText().isEmpty()) {
      outputField.setText(outputField.getText().substring(0, outputField.getText().length() - 1));
    }
  }

  private void handleOperatorButtons(Button btn) {
    String input = outputField.getText();
    if (input.isEmpty()) {
      return;
    }
    if (helper.insertOperator(input)) {
      insert(btn.getText());
    } else {
      calculate();
      insert(btn.getText());
    }
  }

  private void calcExp(String specialOperation) {
    String input = outputField.getText();
    try {
      helper.isNumber(input);
      double d = Double.parseDouble(input);
      switch (specialOperation) {
        case "√": sqrt(d);
          break;
        case "x²": pow(d);
          break;
        case "x³": cube(d);
          break;
        case "!": faculty(d);
          break;
        case "N": negative(d);
          break;
        default: throw new UnsupportedOperationException("Unsupported Operation detected");
      }
    } catch (InvalidInputException e) {
      error(e);
    }
  }

  private void pow(double d) {
    clearAndInsert(String.valueOf(d * d));
  }

  private void cube(double d) {
    clearAndInsert(String.valueOf(d * d * d));
  }

  private void sqrt(double d) {
    if (d > 0) {
      clearAndInsert(String.valueOf(Math.sqrt(d)));
    } else {
      clearAndInsert(Math.sqrt(Math.abs(d)) + "i");
    }
  }

  private void negative(double d) {
    clearAndInsert(String.valueOf(d * -1));
  }

  private void faculty(double d) {
    try {
      fakultaet(d);
    } catch (InvalidInputException e) {
      error(e);
    }
  }

  private double fakultaet(double a) throws InvalidInputException {
    if (a < 0) {
      throw new InvalidInputException("Number must be positive or zero");
    }
    return a <= 1 ? a : a * fakultaet(a - 1);
  }

  private void calculate() {
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
      try {
        helper.isNumber(s1);
        helper.isNumber(s2);

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
        clearAndInsert(String.valueOf(r));
      } catch (InvalidInputException e) {
        error(e);
      }
    }
  }

  private void error(Exception exception) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fucking Error");
    alert.setHeaderText("A fucking error occured: ");
    alert.setContentText(exception.getMessage());
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch();
  }
}