module org.example.photonomics {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.photonomics to javafx.fxml;
    exports org.example.photonomics;
}