package com.example.calulator;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class OverlayMenu extends VBox {
  private int roundTo = 8;
  private final ObservableList<String> history = FXCollections.observableArrayList();

  public OverlayMenu() {
    Label closeLable = new Label("X");
    closeLable.setOnMouseClicked(e -> setVisible(false));
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox closeNav = new HBox();
    closeNav.setPadding(new Insets(5));
    closeNav.getChildren().addAll(spacer, closeLable);

    Label settings = new Label("⚙" + " Settings:");

    Label roundToDecimalPoint = new Label();
    Spinner<Integer> intSpinner = new Spinner<>(0, 16, 8);
    intSpinner.valueProperty().addListener((obs, oldVal, newVal) -> roundTo = newVal);
    roundToDecimalPoint.textProperty().bind(Bindings.concat("Round numbers to: ", intSpinner.valueProperty()));

    Label history = new Label("⏳" + " History:" );
    ListView<String> historyView = new ListView<>(this.history);

    Label converter = new Label("<->" + " Converter:" );

    getChildren().addAll(closeNav, settings, roundToDecimalPoint, intSpinner, history, historyView, converter);
    getStyleClass().add("overlay");

    // TODO: Converter + make it prettier
  }

  public int getRoundTo() {
    return roundTo;
  }

  public void pushHistory(String expr) {
    history.add(expr);
  }
}