package com.example.calulator;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A menu which offeres more options for instance convertion, history and rounding settings.
 */

public class OverlayMenu extends VBox {
  private int roundTo = 8;
  private final ObservableList<String> history = FXCollections.observableArrayList();
  private boolean valChanged = false;

  /**
   * Sets up all the headlines, slider, historyListview and the boxes with convertion themes.
   */

  public OverlayMenu() {
    Label closeLable = new Label("✖");
    closeLable.setOnMouseClicked(e -> setVisible(false));
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox closeNav = new HBox();
    closeNav.setPadding(new Insets(5));
    closeNav.getChildren().addAll(spacer, closeLable);

    Label roundToDecimalPoint = new Label("Round numbers to: ");
    Spinner<Integer> intSpinner = new Spinner<>(0, 16, 8);
    intSpinner.setPrefWidth(60);
    intSpinner.valueProperty().addListener((obs, oldVal, newVal) -> roundTo = newVal);
    HBox roundingSettingsBox = new HBox(10, roundToDecimalPoint, intSpinner);
    roundingSettingsBox.setPadding(new Insets(0, 0, 0, 10));

    Label historyLabel = new Label("⏳" + " History:");
    historyLabel.setPadding(new Insets(0, 0, 0, 10));
    ListView<String> listView = new ListView<>(this.history);
    listView.setFocusTraversable(false);
    VBox historyBox = new VBox(historyLabel, listView);

    Label converter = new Label("<->" + " Converter: ");
    converter.setPadding(new Insets(0, 0, 0, 10));

    VBox content = new VBox(10);
    content.getChildren().addAll(roundingSettingsBox, historyBox, converter,
            speedBox(), temperatureBox(), lenghBox(), weightBox(), sphereBox(), cubeBox());

    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    getChildren().addAll(closeNav, scrollPane);
    getStyleClass().add("overlay");
  }

  public int getRoundTo() {
    return roundTo;
  }

  /**
   * the calculation-input or an expression with the corresponding operation applied,
   * gets added in the historyListView. It is not permantent, like a sessionstorage.
   */
  public void pushHistory(String expr) {
    history.add(expr);
  }

  private VBox speedBox() {
    VBox speedBox = new VBox(10);
    speedBox.setPadding(new Insets(10));

    TextField inputKm = new TextField();
    TextField inputMs = new TextField();
    TextField inputMph = new TextField();
    TextField inputMach = new TextField();
    acceptOnlyNumbers(inputKm, inputMs, inputMph, inputMach);

    speedBox.getChildren().addAll(
            new Label("Speed: "),
            new HBox(5, new Label("Km/h:"), inputKm),
            new HBox(5, new Label("M/s:"), inputMs),
            new HBox(5, new Label("Mph:"), inputMph),
            new HBox(5, new Label("Mach:"), inputMach)
    );

    ChangeListener<String> speedListener = (observableValue, oldVal, newVal) -> {
      TextField source = (TextField) ((StringProperty) observableValue).getBean();
      if (valChanged) {
        return;
      }
      if (newVal.isEmpty()) {
        inputKm.clear();
        inputMs.clear();
        inputMph.clear();
        inputMach.clear();
        return;
      }
      try {
        double val = Double.parseDouble(newVal);
        valChanged = true;
        if (source == inputKm) {
          inputMs.setText(String.valueOf(val / 3.6));
          inputMph.setText(String.valueOf(val * 0.6214268));
          inputMach.setText(String.valueOf(val / 1225.08));
        } else if (source == inputMs) {
          inputKm.setText(String.valueOf(val * 3.6));
          inputMph.setText(String.valueOf(val * 2.23713648));
          inputMach.setText(String.valueOf(val * 0.0029385836));
        } else if (source == inputMph) {
          inputKm.setText(String.valueOf(val * 1.6092));
          inputMs.setText(String.valueOf(val * 0.447));
          inputMach.setText(String.valueOf(val * 0.001314));
        } else if (source == inputMach) {
          inputKm.setText(String.valueOf(val * 1225.08));
          inputMs.setText(String.valueOf(val * 340.3));
          inputMph.setText(String.valueOf(val * 761.2975));
        }
      } catch (NumberFormatException e) {
        inputKm.clear();
        inputMs.clear();
        inputMph.clear();
        inputMach.clear();
      } finally {
        valChanged = false;
      }
    };
    inputKm.textProperty().addListener(speedListener);
    inputMs.textProperty().addListener(speedListener);
    inputMph.textProperty().addListener(speedListener);
    inputMach.textProperty().addListener(speedListener);
    return speedBox;
  }

  private VBox lenghBox() {
    VBox lenghBox = new VBox(10);
    lenghBox.setPadding(new Insets(10));

    TextField nanoMeter = new TextField();
    TextField mikrometer = new TextField();
    TextField millimeter = new TextField();
    TextField centimeter = new TextField();
    TextField meter = new TextField();
    TextField kilometer = new TextField();
    TextField lightYear = new TextField();
    acceptOnlyNumbers(nanoMeter, mikrometer, millimeter, centimeter, meter, kilometer, lightYear);

    lenghBox.getChildren().addAll(
            new Label("Length: "),
            new HBox(5, new Label("Nanometer: "), nanoMeter),
            new HBox(5, new Label("Micrometer: "), mikrometer),
            new HBox(5, new Label("Millimeter: "), millimeter),
            new HBox(5, new Label("Centimeter: "), centimeter),
            new HBox(5, new Label("Meter: "), meter),
            new HBox(5, new Label("Kilometer: "), kilometer),
            new HBox(5, new Label("Light year: "), lightYear)
    );

    ChangeListener<String> lengthListener = (observableValue, oldVal, newVal) -> {
      TextField source = (TextField) ((StringProperty) observableValue).getBean();
      if (valChanged) {
        return;
      }
      if (newVal.isEmpty()) {
        nanoMeter.clear();
        mikrometer.clear();
        millimeter.clear();
        centimeter.clear();
        meter.clear();
        kilometer.clear();
        lightYear.clear();
        return;
      }
      try {
        double length = Double.parseDouble(newVal);
        valChanged = true;
        if (source == nanoMeter) {
          mikrometer.setText(String.valueOf(length / 1e3));
          millimeter.setText(String.valueOf(length / 1e6));
          centimeter.setText(String.valueOf(length / 1e7));
          meter.setText(String.valueOf(length / 1e9));
          kilometer.setText(String.valueOf(length / 1e12));
          lightYear.setText(String.valueOf(length /  9.4607e24));
        } else if (source == mikrometer) {
          nanoMeter.setText(String.valueOf(length * 1000));
          millimeter.setText(String.valueOf(length / 1000));
          centimeter.setText(String.valueOf(length / 10000));
          meter.setText(String.valueOf(length / 1000000));
          kilometer.setText(String.valueOf(length / 1000000000));
          lightYear.setText(String.valueOf(length /  9.4607e21));
        } else if (source == millimeter) {
          nanoMeter.setText(String.valueOf(length * 1000000));
          mikrometer.setText(String.valueOf(length * 1000));
          centimeter.setText(String.valueOf(length / 10));
          meter.setText(String.valueOf(length / 1000));
          kilometer.setText(String.valueOf(length / 1000000));
          lightYear.setText(String.valueOf(length /  9.4607e18));
        } else if (source == centimeter) {
          nanoMeter.setText(String.valueOf(length * 10000000));
          mikrometer.setText(String.valueOf(length * 10000));
          millimeter.setText(String.valueOf(length * 10));
          meter.setText(String.valueOf(length / 100));
          kilometer.setText(String.valueOf(length / 100000));
          lightYear.setText(String.valueOf(length /  9.4607e17));
        } else if (source == meter) {
          nanoMeter.setText(String.valueOf(length * 1000000000));
          mikrometer.setText(String.valueOf(length * 1000000));
          millimeter.setText(String.valueOf(length * 1000));
          centimeter.setText(String.valueOf(length * 100));
          kilometer.setText(String.valueOf(length / 1000));
          lightYear.setText(String.valueOf(length / 9.4607e15));
        } else if (source == kilometer) {
          nanoMeter.setText(String.valueOf(length * 1e12));
          mikrometer.setText(String.valueOf(length * 1e9));
          millimeter.setText(String.valueOf(length * 100000));
          centimeter.setText(String.valueOf(length * 10000));
          meter.setText(String.valueOf(length * 1000));
          lightYear.setText(String.valueOf(length / 9.4607e12));
        } else if (source == lightYear) {
          nanoMeter.setText(String.valueOf(length * 9.4607e24));
          mikrometer.setText(String.valueOf(length * 9.4607e21));
          millimeter.setText(String.valueOf(length * 9.4607e18));
          centimeter.setText(String.valueOf(length * 9.4607e17));
          meter.setText(String.valueOf(length * 9.4607e15));
          kilometer.setText(String.valueOf(length * 9.4607e12));
        }
      } catch (NumberFormatException e) {
        nanoMeter.clear();
        mikrometer.clear();
        millimeter.clear();
        centimeter.clear();
        meter.clear();
        kilometer.clear();
        lightYear.clear();
      } finally {
        valChanged = false;
      }
    };
    nanoMeter.textProperty().addListener(lengthListener);
    mikrometer.textProperty().addListener(lengthListener);
    millimeter.textProperty().addListener(lengthListener);
    centimeter.textProperty().addListener(lengthListener);
    meter.textProperty().addListener(lengthListener);
    kilometer.textProperty().addListener(lengthListener);
    lightYear.textProperty().addListener(lengthListener);
    return lenghBox;
  }

  private VBox temperatureBox() {
    VBox temperatureBox = new VBox(10);
    temperatureBox.setPadding(new Insets(10));

    TextField inputCelsius = new TextField();
    TextField inputFahrenheit = new TextField();
    TextField inputKelvin = new TextField();
    acceptOnlyNumbers(inputCelsius, inputFahrenheit, inputKelvin);

    temperatureBox.getChildren().addAll(
            new Label("Temperature: "),
            new HBox(5, new Label("C°:"), inputCelsius),
            new HBox(5, new Label("F°:"), inputFahrenheit),
            new HBox(5, new Label("K°:"), inputKelvin)
    );

    ChangeListener<String> temperaturListener = (observableValue, oldVal, newVal) -> {
      TextField source = (TextField) ((StringProperty) observableValue).getBean();
      if (valChanged) {
        return;
      }
      if (newVal.isEmpty()) {
        inputCelsius.clear();
        inputFahrenheit.clear();
        inputKelvin.clear();
        return;
      }
      try {
        valChanged = true;
        double temperature = Double.parseDouble(newVal);
        if (source == inputCelsius) {
          inputKelvin.setText(String.valueOf(temperature + 273.15));
          inputFahrenheit.setText(String.valueOf(temperature * 9 / 5 + 32));
        } else if (source == inputFahrenheit) {
          inputCelsius.setText(String.valueOf((temperature - 32) * 5 / 9));
          inputKelvin.setText(String.valueOf((temperature - 32) * 5 / 9 + 273.15));
        } else if (source == inputKelvin) {
          inputCelsius.setText(String.valueOf(temperature - 273.15));
          inputFahrenheit.setText(String.valueOf((temperature - 273.15) * 9 / 5 + 32));
        }
      } catch (NumberFormatException e) {
        inputCelsius.clear();
        inputFahrenheit.clear();
        inputKelvin.clear();
      } finally {
        valChanged = false;
      }
    };
    inputCelsius.textProperty().addListener(temperaturListener);
    inputKelvin.textProperty().addListener(temperaturListener);
    inputFahrenheit.textProperty().addListener(temperaturListener);
    return temperatureBox;
  }

  private VBox weightBox() {
    VBox weightBox = new VBox(10);
    weightBox.setPadding(new Insets(10));

    TextField inputGramm = new TextField();
    TextField inputKilogramm = new TextField();
    TextField inputPound = new TextField();
    acceptOnlyNumbers(inputGramm, inputKilogramm, inputPound);

    weightBox.getChildren().addAll(
            new Label("Weight: "),
            new HBox(5, new Label("Gramm g:"), inputGramm),
            new HBox(5, new Label("Kilogramm Kg:"), inputKilogramm),
            new HBox(5, new Label("Pfund Ib:"), inputPound)
    );

    ChangeListener<String> weightListener = (observableValue, oldVal, newVal) -> {
      TextField source = (TextField) ((StringProperty) observableValue).getBean();
      if (valChanged) {
        return;
      }
      if (newVal.isEmpty()) {
        inputGramm.clear();
        inputKilogramm.clear();
        inputPound.clear();
        return;
      }
      try {
        valChanged = true;
        double weight = Double.parseDouble(newVal);
        if (source == inputGramm) {
          inputPound.setText(String.valueOf((weight / 1000) * 2.204623));
          inputKilogramm.setText(String.valueOf(weight / 1000));
        } else if (source == inputKilogramm) {
          inputGramm.setText(String.valueOf(weight * 1000));
          inputPound.setText(String.valueOf(weight * 2.204623));
        } else if (source == inputPound) {
          inputGramm.setText(String.valueOf((weight / 2.204623) * 1000));
          inputKilogramm.setText(String.valueOf(weight / 2.204623));
        }
      } catch (NumberFormatException e) {
        inputGramm.clear();
        inputKilogramm.clear();
        inputPound.clear();
      } finally {
        valChanged = false;
      }
    };
    inputGramm.textProperty().addListener(weightListener);
    inputPound.textProperty().addListener(weightListener);
    inputKilogramm.textProperty().addListener(weightListener);
    return weightBox;
  }

  private VBox sphereBox() {
    VBox sphereBox = new VBox(10);
    sphereBox.setPadding(new Insets(10));

    TextField inputRadius = new TextField();
    acceptOnlyNumbers(inputRadius);
    TextField circleArea = new TextField();
    TextField circumference = new TextField();
    TextField sphereSurfaceArea = new TextField();
    TextField sphereVolume = new TextField();

    sphereBox.getChildren().addAll(
            new Label("Geometry - Circle and Sphere: "),
            new HBox(5, new Label("Radius: "), inputRadius),
            new HBox(5, new Label("Circle Area: "), circleArea),
            new HBox(5, new Label("Circumference: "), circumference),
            new HBox(5, new Label("Volume: "), sphereVolume),
            new HBox(5, new Label("Surface Area: "), sphereSurfaceArea)
    );

    inputRadius.textProperty().addListener(((observableValue, oldVal, newVal) -> {
      if (!newVal.isEmpty()) {
        try {
          double d = Double.parseDouble(newVal);
          circleArea.setText(String.valueOf(Math.PI * d * d));
          circumference.setText(String.valueOf(2 * Math.PI * d));
          sphereSurfaceArea.setText(String.valueOf(4 * Math.PI * d * d));
          sphereVolume.setText(String.valueOf((double) 4 / 3 * Math.PI * d * d * d));
        } catch (Exception exception) {
          circleArea.clear();
          circumference.clear();
          sphereSurfaceArea.clear();
          sphereVolume.clear();
        }
      } else {
        circleArea.clear();
        circumference.clear();
        sphereSurfaceArea.clear();
        sphereVolume.clear();
      }
    }));
    return sphereBox;
  }

  private VBox cubeBox() {
    VBox cubeBox = new VBox(10);
    cubeBox.setPadding(new Insets(10));

    TextField inputLength = new TextField();
    acceptOnlyNumbers(inputLength);
    TextField squareArea = new TextField();
    TextField circumference = new TextField();
    TextField cubeVolume = new TextField();
    TextField cubeSurface = new TextField();

    cubeBox.getChildren().addAll(
            new Label("Geometry - Square and Cube: "),
            new HBox(5, new Label("Length: "), inputLength),
            new HBox(5, new Label("Area of square: "), squareArea),
            new HBox(5, new Label("Circumfercence: "), circumference),
            new HBox(5, new Label("Volume of cube: "), cubeVolume),
            new HBox(5, new Label("Surface Area: "), cubeSurface)
    );

    inputLength.textProperty().addListener(((observableValue, oldVal, newVal) -> {
      if (!newVal.isEmpty()) {
        try {
          double d = Double.parseDouble(newVal);
          squareArea.setText(String.valueOf(d * d));
          circumference.setText(String.valueOf(4 * d));
          cubeVolume.setText(String.valueOf(d * d * d));
          cubeSurface.setText(String.valueOf(6 * (d * d)));
        } catch (Exception exception) {
          squareArea.clear();
          circumference.clear();
          cubeVolume.clear();
          cubeSurface.clear();
        }
      } else {
        squareArea.clear();
        circumference.clear();
        cubeVolume.clear();
        cubeSurface.clear();
      }
    }));
    return cubeBox;
  }

  private void acceptOnlyNumbers(TextField... textField) {
    for (TextField field : textField) {
      field.addEventFilter(KeyEvent.KEY_TYPED, event -> {
        String character = event.getCharacter();
        if (!character.matches("[0-9.]")) {
          event.consume();
          return;
        }
        if (character.equals(".") && field.getText().contains(".")) {
          event.consume();
        }
      });
    }
  }
}