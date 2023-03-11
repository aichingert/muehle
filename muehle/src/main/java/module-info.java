module at.htleonding.muehle {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens at.htleonding.muehle to javafx.fxml;
    exports at.htleonding.muehle;
}