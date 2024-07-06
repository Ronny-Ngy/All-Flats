module ronny.allflats {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens ronny.allflats2 to javafx.fxml;
    exports ronny.allflats2;
}