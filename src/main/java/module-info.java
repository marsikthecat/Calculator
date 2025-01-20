module com.example.calulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calulator to javafx.fxml;
    exports com.example.calulator;
}