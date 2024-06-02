module lab7common {
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.querydsl.core;
    requires java.desktop;
    exports org.common;
    exports org.common.utility;
    exports org.common.network;
    exports org.common.serial;
    exports org.common.commands;
    exports org.common.commands.authorization;
    exports org.common.commands.inner.objects;


}