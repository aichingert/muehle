module at.htleonding.muehle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens at.htleonding.muehle to javafx.fxml;
    exports at.htleonding.muehle;
    exports at.htleonding.muehle.controller;
    opens at.htleonding.muehle.controller to javafx.fxml;
    exports at.htleonding.muehle.view;
    opens at.htleonding.muehle.view to javafx.fxml;
}