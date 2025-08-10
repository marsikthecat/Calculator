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
    gridPane.add(this, r, c);
    setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-width: 0");
    if (sign.length() == 1) {
      setFont(new Font(25));
    } else if (sign.length() == 2) {
      setFont(new Font(23));
    } else if (sign.length() == 3) {
      setFont(new Font(18));
    } else if (sign.length() == 4) {
      setFont(new Font(14));
    }
  }

  /**
   * Sets up the action, the spcific Button should perform.
   */
  public void act(Runnable action) {
    this.setOnAction(e -> action.run());
  }
}