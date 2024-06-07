package org.example.graphic.scene.main.command;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import lombok.Getter;
import org.example.graphic.localizator.Localizator;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Panel {
    public Panel(String label,boolean applyOrContinue) {
        this.label = label;
        this.applyOrContinue = applyOrContinue;
    }
    private boolean applyOrContinue;
    private String label;
    @Getter
    protected Dialog<Void> dialog;
    protected ButtonType applyOrContinuebuttonType;
    protected ButtonType cancelButtonType ;
    protected ButtonType backButtonType ;
    private  Localizator localizator = Localizator.getInstance();
    protected ArrayList<GridPane> pages = new ArrayList<>();
    private int currentPageInd = 0;
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
    private void generateApplyOrContinueButtonType(){
        String buttonName = applyOrContinue ? "Apply" : "Continue";
        var type = applyOrContinue ? ButtonBar.ButtonData.APPLY : ButtonBar.ButtonData.OTHER;
        applyOrContinuebuttonType =  new ButtonType(localizator.getKeyString(buttonName+"Label"), type);

    }

    private void initApplyOrContinueButton() {
        Button button = (Button) dialog.getDialogPane().lookupButton(applyOrContinuebuttonType);
        button.addEventFilter(ActionEvent.ACTION, event -> {
            System.out.println(currentPageInd);
            if (currentPageInd+1<pages.size()){
                var gridPane = pages.get(currentPageInd+1);

                System.out.println("раз");
                currentPageInd+=1;
                changeOnAnotherForm(gridPane);
            }else {
                onApply();
                return;
            }
            event.consume();  // предотвратить закрытие диалога


        });
    }

    protected abstract void onApply() ;

    private void generateCancelButtonType(){
        cancelButtonType = new ButtonType(localizator.getKeyString("CancelLabel"), ButtonBar.ButtonData.CANCEL_CLOSE);
    }
    private void generateBackButtonType(){
        backButtonType = new ButtonType(localizator.getKeyString("BackLabel"), ButtonBar.ButtonData.BACK_PREVIOUS);

    }

    private void initBackButton() {
        Button button = (Button) dialog.getDialogPane().lookupButton(backButtonType);
        if (button!=null){
        button.addEventFilter(ActionEvent.ACTION, event -> {
            if (currentPageInd-1>=0){
                var gridPane = pages.get(currentPageInd-1);
                System.out.println("раз");
                currentPageInd-=1;
                changeOnAnotherForm(gridPane);
            }
            event.consume();  // предотвратить закрытие диалога
        });
        }
    }

    public void updateButtons(){
        generateApplyOrContinueButtonType();
        generateCancelButtonType();
        if (currentPageInd!=0){
            generateBackButtonType();
            dialog.getDialogPane().getButtonTypes().setAll(applyOrContinuebuttonType, cancelButtonType,backButtonType);
        }else {
            dialog.getDialogPane().getButtonTypes().setAll(applyOrContinuebuttonType, cancelButtonType);
        }
        initApplyOrContinueButton();
        initBackButton();
//        dialog.show();
    }
    protected void changeOnAnotherForm(GridPane gridPane){
        dialog.getDialogPane().setContent(gridPane);
        System.out.println("ПОМЕНЯЛИ");
        System.out.println("aboba");
        updateButtons();

    }
    protected TextField createNumericTextField() {
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        return textField;
    }
    protected TextField createDoubleTextField() {
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        return textField;
    }

}
