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
  private final CalcHelper helper = new CalcHelper();
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
          case "/", "*", "+", "-" -> button.act(() -> handleOperatorButtons(button));
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

  private void delete() {
    if (!outputField.getText().isEmpty()) {
      outputField.setText(outputField.getText().substring(0, outputField.getText().length() - 1));
    }
  }

  private void handleOperatorButtons(Button btn) {
    String input = outputField.getText();
    if (helper.insertOperator(input)) {
      insert(btn.getText());
    } else {
      calculate();
      insert(btn.getText());
    }
  }

  private void calcPow() {
    if (helper.checkn(outputField.getText())) {
      double d = Double.parseDouble(outputField.getText());
      clearAndInsert(String.valueOf(d * d));
    } else {
      error("Please enter a number");
    }
  }

  private void calcKube() {
    if (helper.checkn(outputField.getText())) {
      double d = Double.parseDouble(outputField.getText());
      clearAndInsert(String.valueOf(d * d * d));
    } else {
      error("Please enter a number");
    }
  }

  private void calcSqrt() {
    if (helper.checkn(outputField.getText())) {
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

  private void calcfacultaet() {
    if (helper.checkn(outputField.getText())) {
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

  private double fakultaet(double a) {
    if (a <= 1) {
      return a;
    } else {
      return a * fakultaet(a - 1);
    }
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
      if (helper.checkn(s1) && helper.checkn(s2)) {
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
      } else {
        error("Calculation failed: \nInvalid input");
      }
    }
  }

  private void error(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fucking Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}