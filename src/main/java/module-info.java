module com.opencode.minikeyvault {

    requires static lombok;

    requires transitive javafx.graphics;

    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires com.h2database;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.opencode.minikeyvault.view to javafx.fxml;
    opens com.opencode.minikeyvault.view.commons to javafx.base;

    exports com.opencode.minikeyvault;

}
