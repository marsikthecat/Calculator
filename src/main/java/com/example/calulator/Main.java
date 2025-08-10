package com.example.calulator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Simple Calculator.
 * Main: 251 lines.
 * CalcButton: 38 lines.
 * SimpleParser: 111 lines.
 * OverlayMenu: 405 lines.
 * 805 lines + 18 lines css = 823 lines.
 */

public class Main extends Application {

  private TextField outputField;
  private final String[][] symbols = {
          {"ln", "mod", "x³", "√", "x²", "log10"},
          {"e", "(", "7", "4", "1", "1/X"},
          {"π", ")", "8", "5", "2", "0"},
          {"C", "!", "9", "6", "3", "."},
          {">", "/", "*", "+", "-", "="}};

  private final OverlayMenu overlayMenu = new OverlayMenu();

  @Override
  public void start(Stage stage) {
    Label menuIcon = new Label("☰");
    menuIcon.setOnMouseClicked(e -> overlayMenu.setVisible(true));
    menuIcon.setFont(new Font(20));
    menuIcon.setTextFill(Color.WHITE);
    HBox topNav = new HBox(menuIcon);
    topNav.setPadding(new Insets(5, 0, 15, 10));

    overlayMenu.setLayoutX(0);
    overlayMenu.setLayoutY(0);
    overlayMenu.setVisible(false);

    outputField = new TextField();
    outputField.setStyle("-fx-pref-width: 300px; -fx-font-size: 25; -fx-cursor: not-allowed");
    outputField.setEditable(false);
    outputField.setFocusTraversable(false);
    HBox rsection = new HBox();
    rsection.setAlignment(Pos.CENTER);
    rsection.getChildren().addAll(outputField);

    GridPane table = getGridPaneWithButtons();

    VBox box = new VBox();
    box.setStyle("-fx-background-color: #b9b9b9");
    box.getChildren().addAll(topNav, rsection, table, overlayMenu);

    StackPane root = new StackPane(box, overlayMenu);

    Scene scene = getScene(root);
    stage.setTitle("Calculator");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  private Scene getScene(StackPane stackPane) {
    Scene scene = new Scene(stackPane, 320, 480);
    scene.getStylesheets().add(Objects.requireNonNull(
            getClass().getResource("/styles.css")).toExternalForm());
    scene.setOnKeyPressed(e -> {
      if (e.getCode().isDigitKey() || e.getCode() == KeyCode.PLUS
          || e.getCode() == KeyCode.MINUS || e.getCode() == KeyCode.MULTIPLY
          || e.getCode() == KeyCode.DIVIDE || e.getCode() == KeyCode.BRACELEFT
          || e.getCode() == KeyCode.BRACERIGHT || e.getCode() == KeyCode.PERIOD) {
        insert(e.getText());
      }
      if (e.getCode() == KeyCode.F2) {
        calculate();
      }
      if (e.getCode() == KeyCode.BACK_SPACE) {
        delete();
      }
    });
    return scene;
  }

  private GridPane getGridPaneWithButtons() {
    GridPane table = new GridPane();
    table.setAlignment(Pos.CENTER);
    table.setHgap(10);
    table.setVgap(10);
    table.setPadding(new Insets(10));
    for (int row = 0; row < symbols.length; row++) {
      for (int col = 0; col < symbols[row].length; col++) {
        String symbol = symbols[row][col];
        CalcButton button = new CalcButton(symbol, row, col, table);
        switch (symbol) {
          case "C" -> button.act(() -> outputField.clear());
          case ">" -> button.act(this::delete);
          case "√", "x²", "x³", "!", "ln", "mod", "log10", "1/X" ->
              button.act(() -> calcExp(symbol));
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
  }

  private void clearAndInsert(String stuff) {
    overlayMenu.pushHistory(stuff);
    outputField.clear();
    int roundTo = overlayMenu.getRoundTo();
    if (stuff.length() > roundTo) {
      double d = Double.parseDouble(stuff);
      BigDecimal bd = new BigDecimal(d);
      bd = bd.setScale(roundTo, RoundingMode.HALF_UP);
      insert(bd.toString());
    } else {
      insert(stuff);
    }
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
      overlayMenu.pushHistory(specialOperation + " ->" + outputField.getText());
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
      case "ln": ln(d);
      break;
      case "log10": log10(d);
      break;
      case "1/X": fraction(d);
      break;
      default: error(new UnsupportedOperationException("Unsupported Operation detected"));
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

  private void faculty(double d) {
    if (d > 170) {
      error(new IllegalArgumentException("My friend, the value is too big for me"));
      return;
    }
    try {
      double ans = facultyHelper(d);
      clearAndInsert(String.valueOf(ans));
    } catch (IllegalArgumentException e) {
      error(e);
    }
  }

  private void ln(double d) {
    if (d < 0) {
      error(new IllegalArgumentException("My friend, the ln is not defined below zero"));
    }
    clearAndInsert(String.valueOf(Math.log(d)));
  }

  private void log10(double d) {
    clearAndInsert(String.valueOf(Math.log10(d)));
  }

  private void fraction(double d) {
    clearAndInsert(String.valueOf(1 / d));
  }

  private double facultyHelper(double a) throws IllegalArgumentException {
    if (a < 0) {
      error(new IllegalArgumentException("My friend, faculty is only for positive or zero"));
    }
    return a <= 1 ? 1 : a * facultyHelper(a - 1);
  }

  private void calculate()  {
    try {
      double result = SimpleParser.parse(outputField.getText());
      overlayMenu.pushHistory(outputField.getText());
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

  /**
   * look below OMG!, there is a main method!.
   */
  public static void main(String[] args) {
    launch(args);
  }
}