module at.htleonding.muehle {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens at.htleonding.muehle to javafx.fxml;
    exports at.htleonding.muehle;
    exports at.htleonding.muehle.controller;
    opens at.htleonding.muehle.controller to javafx.fxml;
}