module org.example.graphic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;

    opens org.example.graphic to javafx.fxml;
    opens org.example.graphic.scene to javafx.fxml;
    exports org.example.graphic.node;
    opens org.example.graphic.node to javafx.fxml;

    // Экспорт пакета для использования в других модулях, включая javafx.graphics
    exports org.example.graphic.scene;
}
