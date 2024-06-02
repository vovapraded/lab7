package org.example.graphic.scene;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.Getter;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.node.PlaceholderTextField;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class MyScene {
    protected Scene scene;
    protected final HashMap<Node,String > nodeAndPropertyKeys = new HashMap<Node,String >();
    protected Localizator localizator = Localizator.getInstance();
    static {
        Locale.setDefault(Locale.UK);
    }

    protected void updateTexts() {
        nodeAndPropertyKeys.keySet().forEach(node -> {
            if (node instanceof Button){
                ((Button) node).setText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }else if (node instanceof Label){
                ((Label) node).setText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }
            else if (node instanceof PlaceholderTextField){
                ((PlaceholderTextField) node).setPlaceholderText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }
        });
    }
    protected ComboBox<String> createChangeLocaleBox() {
        ComboBox<String> changeLocale = new ComboBox<>();
        changeLocale.getItems().addAll("en GB", "ru RU", "en IE", "nl NL", "sq AL");
        updateValueChangeLocale(changeLocale);
        changeLocale.setOnAction(e -> {
            var value = changeLocale.getValue();
            localizator.setBundle(ResourceBundle.getBundle("locales.gui",
                    new Locale(value.split(" ")[0], value.split(" ")[1])));
            updateTexts();

        });

        return changeLocale;
    }

    public void updateValueChangeLocale(ComboBox<String> changeLocale) {
        var lang = localizator.getBundle().getLocale().getLanguage().toLowerCase();
        var country = localizator.getBundle().getLocale().getCountry().toUpperCase();
        var locale = lang+" "+country;
        if (locale.isBlank()) locale = "en GB";

        System.out.println(locale);
        changeLocale.setValue(locale);
    }
    public abstract void updateValueChangeLocale();
}
