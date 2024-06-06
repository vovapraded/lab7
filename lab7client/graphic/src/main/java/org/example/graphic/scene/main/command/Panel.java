package org.example.graphic.scene.main.command;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import lombok.Getter;
import org.example.graphic.localizator.Localizator;

import java.util.HashMap;

public abstract class Panel {
    public Panel(String label) {
        this.label = label;
    }

    private String label;
    @Getter
    protected Dialog<Void> dialog;
    protected ButtonType applyButtonType ;
    protected ButtonType cancelButtonType ;
    private  Localizator localizator = Localizator.getInstance();
    public void initDialog(){
        // Создаем диалог
        dialog = new Dialog<>();
        dialog.initModality(Modality.NONE);
        dialog.setTitle(label);
        updateButtons();
    }

    public Button createButton(HashMap<Node, String> nodeAndKeys) {
        Button button = new Button();
        nodeAndKeys.put(button, label+"Label");
        button.setOnAction(e -> {
            showForm(nodeAndKeys);
        });
        return button;
    }

    protected abstract void showForm(HashMap<Node, String> nodeAndKeys);
    private void generateApplyButtonType(){
        applyButtonType =  new ButtonType(localizator.getKeyString("ApplyLabel"), ButtonBar.ButtonData.OK_DONE);
    }
    private void generateCancelButtonType(){

        cancelButtonType = new ButtonType(localizator.getKeyString("CancelLabel"), ButtonBar.ButtonData.CANCEL_CLOSE);
    }
    public void updateButtons(){
//        dialog.getDialogPane().getButtonTypes().setAll(applyButtonType);
        generateApplyButtonType();
        generateCancelButtonType();
        dialog.getDialogPane().getButtonTypes().setAll(applyButtonType, cancelButtonType);
//        dialog.show();
    }

}
