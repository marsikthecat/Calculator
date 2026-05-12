module com.example.calulator {
    requires javafx.controls;
    requires javafx.fxml;
  requires javafx.graphics;


  opens com.example.calulator to javafx.fxml;
    exports com.example.calulator;
}