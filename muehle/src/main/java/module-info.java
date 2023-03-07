module at.htleonding.muehle.muehle {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens at.htleonding.muehle.muehle to javafx.fxml;
    exports at.htleonding.muehle.muehle;
}