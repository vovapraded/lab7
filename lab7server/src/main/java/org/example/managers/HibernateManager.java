package org.example.managers;

import org.common.utility.ConnectToDatabaseException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateManager {
    private final  Configuration configuration ;


     {
        configuration = new Configuration();
        configuration.configure();
    }
//    private static final Collection collection = Collection.getInstance();
    public  Configuration getConfiguration() throws ConnectToDatabaseException{
        return configuration;
    }
}

