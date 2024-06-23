package org.example.graphic.scene.main.command;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import lombok.Getter;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Panel {
    public Panel(String label) {
        this.label = label;
    }

    private String label;
    @Getter
    protected Dialog<Void> dialog;
    protected ButtonType applyOrContinueButtonType;
    protected ButtonType cancelButtonType ;
    protected ButtonType backButtonType ;
    private  Localizator localizator = Localizator.getInstance();
    protected CopyOnWriteArrayList<GridPane> pages = new CopyOnWriteArrayList<>();
    private volatile int currentPageInd = 0;
    public void initDialog(){
        // Создаем диалог
        dialog = new Dialog<>();
        dialog.initModality(Modality.NONE);
        dialog.setTitle(label);
    }

    public Button createButton(HashMap<Node, String> nodeAndKeys) {
        Button button = new Button();
        nodeAndKeys.put(button, label+"Label");
        button.setOnAction(e -> {
            showForm(nodeAndKeys);
        });
        return button;
    }
    public  void showForm(HashMap<Node,String> nodeAndKeys) {
        Platform.runLater(() -> {
            var firstForm = createFirstForm(nodeAndKeys);
            var secondForm = createSecondForm(nodeAndKeys);
            var thirdForm = createThirdForm(nodeAndKeys);
            pages.clear();
            pages.add(firstForm);
            pages.add(secondForm);
            pages.add(thirdForm);;
            dialog.getDialogPane().setContent(firstForm);
            currentPageInd = 0;
            Application.getMainSceneObj().updateTextUI();


            //            Button continueButton = (Button) dialog.getDialogPane().lookupButton(applyButtonType);
//            continueButton.setOnAction(e -> createSecondForm(nodeAndKeys));
            dialog.showAndWait();
        });
    }
    protected abstract GridPane createFirstForm(HashMap<Node, String> nodeAndKeys);
    protected abstract GridPane createSecondForm(HashMap<Node, String> nodeAndKeys);

    protected abstract GridPane createThirdForm(HashMap<Node, String> nodeAndKeys);


    private void generateApplyOrContinueButtonType(){
        String buttonName =  currentPageInd==pages.size()-1 ? "Apply" : "Continue";
        var type =  ButtonBar.ButtonData.APPLY;
        applyOrContinueButtonType =  new ButtonType(localizator.getKeyString(buttonName+"Label"), type);

    }

    private void initApplyOrContinueButton() {
        Button button = (Button) dialog.getDialogPane().lookupButton(applyOrContinueButtonType);
        button.addEventFilter(ActionEvent.ACTION, event -> {
            if (currentPageInd+1<pages.size()){
                var gridPane = pages.get(currentPageInd+1);

                currentPageInd+=1;
                System.out.println("Нажато continue");
                changeOnAnotherForm(gridPane);
            }else {
                try {
                    onApply();
                } catch (Exception e) {
                    Popup.showError(e.getMessage());
                }
                return;
            }
            event.consume();  // предотвратить закрытие диалога


        });
        Platform.runLater(button::requestFocus);
    }

    protected abstract void onApply() throws Exception;

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

        System.out.println(currentPageInd);
        if (currentPageInd!=0){
            generateBackButtonType();
            dialog.getDialogPane().getButtonTypes().setAll(applyOrContinueButtonType, cancelButtonType,backButtonType);
            System.out.println(dialog.getDialogPane().getButtonTypes());
            initBackButton();
        }else {
            dialog.getDialogPane().getButtonTypes().setAll(applyOrContinueButtonType, cancelButtonType);
        }


        initApplyOrContinueButton();

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
