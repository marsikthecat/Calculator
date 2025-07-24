package com.example.calulator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple Calculator.
 * Main: 173 lines.
 * CalcButton: 26 lines.
 * InvalidInputException: 11 lines.
 * SimpleParser: 68 lines.
 * 278 lines.
 */

public class Main extends Application {

  private TextField outputField;
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
    stage.setTitle("Calculator");
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
          default -> button.act(() -> insert(symbol));
        }
      }
    }
    return table;
  }

  private void insert(String stuff) {
    outputField.setText(outputField.getText() + stuff);
    if (outputField.getText().length() > 16) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Overflow detected");
      alert.setHeaderText("The result of the calculation exceeded the size of the textfield. \n" +
              "here is the full result:");
      alert.setContentText(outputField.getText());
      alert.showAndWait();
    }
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

  private void calcExp(String specialOperation) {
    double d;
    try {
      d = SimpleParser.parse(outputField.getText());
    } catch (Exception e) {
      error(e);
      return;
    }
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
    if (d > 170) {
      error(new InvalidInputException("My friend, the value is too big for me"));
      return;
    }
    try {
      double ans = facultyHelper(d);
      clearAndInsert(String.valueOf(ans));
    } catch (InvalidInputException e) {
      error(e);
    }
  }

  private double facultyHelper(double a) throws InvalidInputException {
    if (a < 0) {
      throw new InvalidInputException("My friend, faculty is only for positive or zero");
    }
    return a <= 1 ? a : a * facultyHelper(a - 1);
  }

  private void calculate()  {
    try {
      double result = SimpleParser.parse(outputField.getText());
      clearAndInsert(String.valueOf(result));
    } catch (Exception e) {
      error(e);
    }
  }

  private void error(Exception exception) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Something went wrong while calculating: ");
    alert.setContentText(exception.getMessage());
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}