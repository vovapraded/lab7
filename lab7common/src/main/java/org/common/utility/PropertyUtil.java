package org.common.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private static final InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("app.properties");
    private static final   Properties properties = new Properties();
      static {
        try (InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("app.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getAddress(){
        return properties.getProperty("server.address");
    }
    public static int getPort(){
        System.out.println(properties.getProperty("server.port"));
        return Integer.parseInt(properties.getProperty("server.port"));
    }
    public static String getPassword(){
        return properties.getProperty("client.password");
    }
    public static void setPassword(String password){

       createOrUpdateProperty("client.password",password);
    }
    public static String getLogin(){
        return properties.getProperty("client.login");
    }
    public static void setLogin(String login){
        createOrUpdateProperty("client.login",login);
    }
    public static void createOrUpdateProperty(String key, String value) {
        if (value != null) {
            // Обновляем значение существующего свойства или создаем новое
            properties.setProperty(key, value);
        } else {
            // Если значение равно null, удаляем свойство
            properties.remove(key);
        }
    }


}

