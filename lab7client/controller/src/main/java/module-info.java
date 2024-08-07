module controller {
    exports org.controller;
    exports org.controller.connect.to.graphic;

    requires lab7common;
    requires lab7client;
    requires static lombok;
    requires com.querydsl.core;
    requires org.apache.commons.lang3;
    requires javafx.graphics;
    requires org.hibernate.orm.core;

}