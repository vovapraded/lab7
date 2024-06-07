package org.example.graphic.scene.main.command;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.example.graphic.scene.Application;

import java.util.HashMap;

public abstract class MyButton {

    private final String label;
    private HashMap<Node, String> nodeAndKeys = Application.getMainSceneObj().getNodeAndPropertyKeys();

    protected MyButton(String label) {
        this.label = label;
    }

    public Button createButton() {
        Button button = new Button();
        nodeAndKeys.put(button, label+"Label");
        button.setOnAction(e -> {
            onClick();
        });
        return button;
    }
    public abstract void onClick();
}
