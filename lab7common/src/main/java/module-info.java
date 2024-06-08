module lab7common {
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.querydsl.core;
    requires com.querydsl.jpa;

    requires java.desktop;
    exports org.common;
    exports org.common.utility;
    exports org.common.network;
    exports org.common.serial;
    exports org.common.commands;
    exports org.common.commands.authorization;
    exports org.common.commands.inner.objects;
    exports org.common.dto;
    exports org.common.managers;
    exports org.common.dao.interfaces;

    opens org.common.dto to org.example.graphic;

}