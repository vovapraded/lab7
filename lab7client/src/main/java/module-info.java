module lab7client {
    exports org.example;
    requires lab7common;
    requires static lombok;
    requires com.google.gson;
    requires guava.primitives.r03;
    requires org.apache.commons.lang3;
    requires java.sql;
    requires java.desktop;
    exports org.example.utility;
    exports org.example.exception;

}