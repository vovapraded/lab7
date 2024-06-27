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
    protected   Localizator localizator = Localizator.getInstance();
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

        if (currentPageInd!=0){
            generateBackButtonType();
            dialog.getDialogPane().getButtonTypes().setAll(applyOrContinueButtonType, cancelButtonType,backButtonType);
            initBackButton();
        }else {
            dialog.getDialogPane().getButtonTypes().setAll(applyOrContinueButtonType, cancelButtonType);
        }


        initApplyOrContinueButton();

//        dialog.show();
    }



    protected void changeOnAnotherForm(GridPane gridPane){
        dialog.getDialogPane().setContent(gridPane);

        updateButtons();


    }
    protected TextField createNumericTextField(boolean canBeNull, boolean mustBePositive) {
        TextField textField = new TextField();
        var minus = mustBePositive ? "" : "-";
        var minusWithQ = mustBePositive ? "" : "-?";
        var plusOrStar = canBeNull ? "*" :"+";
        var regexp1 = minusWithQ+"\\d"+plusOrStar;
        var regexp2 = "[^\\d"+minus+"]";

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regexp1) || newValue.length() > 18) {
                newValue = newValue.replaceAll(regexp2, "");
                textField.setText(newValue.substring(0,Math.min(newValue.length(),18)));
            }
        });
        return textField;
    }

    protected TextField createDoubleTextField() {
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            var str = newValue.replaceAll("[^\\d.-]","");
            while (!checkDouble(str) ) {
                while (str.codePoints().filter(ch -> ch == '.').count()>1){
                    str = str.replaceFirst("\\.","");
                }while (str.codePoints().filter(ch -> ch == '-').count()>1){
                    str=removeLastChar(str, '-');
                }
                if (str.indexOf('-')>0){
                    str = str.substring(str.indexOf('-'));
                }
                if (!checkDouble(str) && str.length()>1){
                    str = str.substring(0,str.length()-1);
                }
            }
            textField.setText(str);

        });
        return textField;
    }
    private boolean checkDouble(String str){
        try {
            if (str.isEmpty()){
                return true;
            }
            double number = Double.parseDouble(str);
            return !Double.isInfinite(number) && !Double.isNaN(number);

        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static String removeLastChar(String str, char targetChar) {
        // Ищем индекс последнего вхождения символа
        int lastIndex = str.lastIndexOf(targetChar);

        if (lastIndex != -1) {
            // Создаем StringBuilder из исходной строки
            StringBuilder sb = new StringBuilder(str);

            // Удаляем символ по найденному индексу
            sb.deleteCharAt(lastIndex);

            // Возвращаем результат в виде строки
            return sb.toString();
        } else {
            // Если символ не найден, возвращаем null или исходную строку, в зависимости от требований
            return null;
        }
    }
}
