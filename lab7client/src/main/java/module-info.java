module lab7client {
    exports org.example;
    requires lab7common;
    requires static lombok;
    requires com.google.gson;
    requires guava.primitives.r03;
    requires org.apache.commons.lang3;
    exports org.example.connector.to.controller;

}