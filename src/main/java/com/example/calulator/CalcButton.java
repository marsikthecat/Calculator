package com.example.calulator;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * Button-Objekt for Calculator Grid.
 */

public class CalcButton extends Button {

  /**
   * Constructor that adds this instance in the gridpane according to the r and c.
   */
  public CalcButton(String sign, int r, int c, GridPane gridPane) {
    setText(sign);
    gridPane.add(this, c, r);
    setMinSize(50, 50);
    setMaxSize(50, 50);
    setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-width: 0");
    setOnMouseEntered(e -> setStyle("-fx-background-color: #b6b6b6; -fx-text-fill: black;"
            + " -fx-border-width: 0"));
    setOnMouseExited(e -> setStyle("-fx-background-color: white; -fx-text-fill: black; "
            + "-fx-border-width: 0"));
    if (sign.length() == 1) {
      setFont(Font.font(24));
    } else if (sign.equals("x²") || sign.equals("x³") || sign.equals("x!") || sign.equals("ln")
            || sign.equals("eˣ") || sign.equals("2ˣ")) {
      setFont(Font.font(22));
    } else if (sign.equals("10ˣ") || sign.equals("sin") || sign.equals("tan")) {
      setFont(Font.font(18));
    } else if (sign.equals("cos") || sign.equals("rad") || sign.equals("³√x")
            || sign.equals("1/X")) {
      setFont(Font.font(17));
    } else if (sign.equals("deg")) {
      setFont(Font.font(16));
    } else if (sign.equals("sin⁻¹")) {
      setFont(Font.font(15));
    } else if (sign.equals("cos⁻¹") || sign.equals("tan⁻¹")) {
      setFont(Font.font(14));
    } else {
      setFont(Font.font(13));
    }
  }

  /**
   * Sets up the action, the spcific Button should perform.
   */
  public void act(Runnable action) {
    this.setOnAction(e -> action.run());
  }
}