package org.example.graphic.scene;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Getter;
import org.controller.MyController;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.node.PlaceholderTextField;
import org.example.graphic.node.TableColumnAdapter;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class MyScene {
    protected Scene scene;
    @Getter
    protected final HashMap<Node,String > nodeAndPropertyKeys = new HashMap<Node,String >();
    protected Localizator localizator = Localizator.getInstance();
    protected Label changeLocaleLabel;
    protected final static MyController controller = MyController.getInstance();

    static {
        Locale.setDefault(Locale.UK);
    }

    public void updateTexts() {
        nodeAndPropertyKeys.keySet().forEach(node -> {
            if (node instanceof Button){
                ((Button) node).setText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }else if (node instanceof Label){
                ((Label) node).setText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }
            else if (node instanceof PlaceholderTextField){
                ((PlaceholderTextField) node).setPlaceholderText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }else if (node instanceof Text){
                ((Text) node).setText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }else if (node instanceof TableColumnAdapter){
                ((TableColumnAdapter) node).getTableColumn().setText(localizator.getKeyString(nodeAndPropertyKeys.get(node)));
            }
        });
    }
    protected void createChangeLocaleBox() {
        var changeLocale = new ComboBox<String>();
        changeLocaleLabel = new Label("",changeLocale);
        changeLocaleLabel.setContentDisplay(ContentDisplay.BOTTOM);
        changeLocaleLabel.setLabelFor(changeLocale);
        nodeAndPropertyKeys.put(changeLocaleLabel,"changeLocaleLabel");
        changeLocale.getItems().addAll("en GB", "ru RU", "en IE", "nl NL", "sq AL");
        updateValueChangeLocale();
        changeLocale.setOnAction(e -> {
            var value = changeLocale.getValue();
            localizator.setBundle(ResourceBundle.getBundle("locales.gui",
                    new Locale(value.split(" ")[0], value.split(" ")[1])));
            updateTexts();
        });


    }

    public void updateValueChangeLocale() {
        var lang = localizator.getBundle().getLocale().getLanguage().toLowerCase();
        var country = localizator.getBundle().getLocale().getCountry().toUpperCase();
        var locale = lang+" "+country;
        if (locale.isBlank()) locale = "en GB";

        System.out.println(locale);
        var changeLocale = (ComboBox<String>) changeLocaleLabel.getLabelFor();
        changeLocale.setValue(locale);
    }

}
