package com.example.calulator;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class OverlayMenu extends VBox {
  private int roundTo = 8;
  private final ObservableList<String> history = FXCollections.observableArrayList();
  private boolean updatingTemperature = false;

  public OverlayMenu() {
    Label closeLable = new Label("X");
    closeLable.setOnMouseClicked(e -> setVisible(false));
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox closeNav = new HBox();
    closeNav.setPadding(new Insets(5));
    closeNav.getChildren().addAll(spacer, closeLable);


    Label settings = new Label("⚙" + " Settings: ");
    Label roundToDecimalPoint = new Label();
    Spinner<Integer> intSpinner = new Spinner<>(0, 16, 8);
    intSpinner.valueProperty().addListener((obs, oldVal, newVal) -> roundTo = newVal);
    roundToDecimalPoint.textProperty().bind(Bindings.concat("Round numbers to: ", intSpinner.valueProperty()));

    Label history = new Label("⏳" + " History:" );
    ListView<String> historyView = new ListView<>(this.history);

    Label converter = new Label("<->" + " Converter: ");

    Label geometry = new Label("Geometry: ");

    VBox content = new VBox();
    content.getChildren().addAll(settings, roundToDecimalPoint, intSpinner, history, historyView, converter,
            speedBox(), temperatureBox(), geometry, sphereBox());

    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    getChildren().addAll(closeNav, scrollPane);
    getStyleClass().add("overlay");
  }

  public int getRoundTo() {
    return roundTo;
  }

  public void pushHistory(String expr) {
    history.add(expr);
  }

  private HBox speedBox() {
    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 5, 10, 5));
    Label speed = new Label("Km/h to m/s: ");
    TextField input = new TextField();
    TextField result = new TextField();
    input.textProperty().addListener(((observableValue, oldVal, newVal) -> {
      if (!newVal.isEmpty()) {
        try {
          double d = Double.parseDouble(newVal);
          result.setText(String.valueOf(d / 3.6));
        } catch (Exception exception) {
          result.setText("");
        }
      } else {
        result.setText("");
      }
    }));
    hBox.getChildren().addAll(speed, input, result);
    return hBox;
  }

  private VBox temperatureBox() {
    VBox temperatureBox = new VBox();
    temperatureBox.setPadding(new Insets(10, 5, 10, 5));

    Label celsiusLabel = new Label("C°: ");
    TextField inputCelsius = new TextField();
    HBox celsiusBox = new HBox(celsiusLabel, inputCelsius);

    Label fahrenheitLabel = new Label("F°: ");
    TextField inputFahrenheit = new TextField();
    HBox fahrenheitBox = new HBox(fahrenheitLabel, inputFahrenheit);

    Label kelvinLabel = new Label("K°: ");
    TextField inputKelvin = new TextField();
    HBox kelvinBox = new HBox(kelvinLabel, inputKelvin);

    inputCelsius.textProperty().addListener((obs, oldVal, newVal) -> {
      if (updatingTemperature) {
        return;
      }
      try {
        if (!newVal.isEmpty()) {
          double c = Double.parseDouble(newVal);
          updatingTemperature = true;
          inputKelvin.setText(String.valueOf(c + 273.15));
          inputFahrenheit.setText(String.valueOf(c * 9 / 5 + 32));
        } else {
          updatingTemperature = true;
          inputKelvin.clear();
          inputFahrenheit.clear();
        }
      } catch (NumberFormatException e) {
        updatingTemperature = true;
        inputKelvin.clear();
        inputFahrenheit.clear();
      } finally {
        updatingTemperature = false;
      }
    });

    inputKelvin.textProperty().addListener((obs, oldVal, newVal) -> {
      if (updatingTemperature) {
        return;
      }
      try {
        if (!newVal.isEmpty()) {
          double k = Double.parseDouble(newVal);
          updatingTemperature = true;
          inputCelsius.setText(String.valueOf(k - 273.15));
          inputFahrenheit.setText(String.valueOf((k - 273.15) * 9 / 5 + 32));
        } else {
          updatingTemperature = true;
          inputCelsius.clear();
          inputFahrenheit.clear();
        }
      } catch (NumberFormatException e) {
        updatingTemperature = true;
        inputCelsius.clear();
        inputFahrenheit.clear();
      } finally {
        updatingTemperature = false;
      }
    });

    inputFahrenheit.textProperty().addListener((obs, oldVal, newVal) -> {
      if (updatingTemperature) {
        return;
      }
      try {
        if (!newVal.isEmpty()) {
          double f = Double.parseDouble(newVal);
          updatingTemperature = true;
          inputCelsius.setText(String.valueOf((f - 32) * 5 / 9));
          inputKelvin.setText(String.valueOf((f - 32) * 5 / 9 + 273.15));
        } else {
          updatingTemperature = true;
          inputCelsius.clear();
          inputKelvin.clear();
        }
      } catch (NumberFormatException e) {
        updatingTemperature = true;
        inputCelsius.clear();
        inputKelvin.clear();
      } finally {
        updatingTemperature = false;
      }
    });
    temperatureBox.getChildren().addAll(celsiusBox, fahrenheitBox, kelvinBox);
    return temperatureBox;
  }

  private VBox sphereBox() {
    VBox sphereBox = new VBox();
    sphereBox.setPadding(new Insets(10, 5, 10, 5));

    Label radius = new Label("Radius of the Sphere/circle: ");
    TextField radiusInput = new TextField();
    HBox inputBox = new HBox(radius, radiusInput);

    Label circleAreaLabel = new Label("Circle Area: ");
    TextField circleArea = new TextField();
    HBox circleAreaBox = new HBox(circleAreaLabel, circleArea);

    Label circumferenceLabel = new Label("Circumference of Circle: ");
    TextField circumference = new TextField();
    HBox circumfercenceBox = new HBox(circumferenceLabel, circumference);

    Label sphereSurfaceAreaLabel = new Label("SurfaceArea of Sphere: ");
    TextField sphereSurfaceArea = new TextField();
    HBox sphereSurfaceAreaBox = new HBox(sphereSurfaceAreaLabel, sphereSurfaceArea);

    Label sphereVolumeLabel = new Label("Volume of Sphere: ");
    TextField sphereVolume = new TextField();
    HBox sphereVolumeBox = new HBox(sphereVolumeLabel, sphereVolume);

    radiusInput.textProperty().addListener(((observableValue, oldVal, newVal) -> {
      if (!newVal.isEmpty()) {
        try {
          double d = Double.parseDouble(newVal);
          circleArea.setText(String.valueOf(Math.PI*d*d));
          circumference.setText(String.valueOf(2*Math.PI*d));
          sphereSurfaceArea.setText(String.valueOf(4*Math.PI*d*d));
          sphereVolume.setText(String.valueOf((double) 4/3*Math.PI*d*d*d));
        } catch (Exception exception) {
          circleArea.setText("");
          circumference.setText("");
          sphereSurfaceArea.setText("");
          sphereVolume.setText("");
        }
      } else {
        circleArea.setText("");
        circumference.setText("");
        sphereSurfaceArea.setText("");
        sphereVolume.setText("");
      }
    }));
    sphereBox.getChildren().addAll(inputBox, circleAreaBox, circumfercenceBox,
            sphereSurfaceAreaBox, sphereVolumeBox );
    return sphereBox;
  }
}