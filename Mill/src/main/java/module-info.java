module at.htlleonding.mill {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.derby.tools;
    requires org.mybatis;

    opens at.htlleonding.mill to javafx.fxml;
    exports at.htlleonding.mill;
    exports at.htlleonding.mill.controller;
    opens at.htlleonding.mill.controller to javafx.fxml;
    exports at.htlleonding.mill.view;
    opens at.htlleonding.mill.view to javafx.fxml;
}