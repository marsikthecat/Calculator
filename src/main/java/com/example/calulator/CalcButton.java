package com.example.calulator;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * Button-Objekt for Calculator Grid.
 */

public class CalcButton extends javafx.scene.control.Button {

  /**
   * Constructor.
   */

  public CalcButton(String sign, int r, int c, GridPane gridPane) {
    this.setText(sign);
    gridPane.add(this, r, c);
    this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    this.setFont(new Font(25));
  }

  public void act(Runnable action) {
    this.setOnAction(e -> action.run());
  }
}
