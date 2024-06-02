package org.example.utility;

import lombok.NoArgsConstructor;
import org.common.utility.*;
import org.common.utility.Console;
import org.example.Main;
import org.example.commands.ExecuteScript;


import java.io.*;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Scanner;

import static lombok.AccessLevel.PRIVATE;

/**
 * a class for reading and writing from the console
 */
@NoArgsConstructor(access = PRIVATE)
public class CurrentConsole implements Console {
    public static Console getInstance() {

        return
            (Console) Proxy.newProxyInstance(
                    CurrentConsole.class.getClassLoader(),
                    new Class<?>[]{Console.class},
                    (proxy, method, args) -> {
                        if ("print".equals(method.getName())) {
                            Main.sendMessageToController((String) args[0]);
                            return null;
                        }
                        return method.invoke(INSTANCE, args);
                    });

    }

    private static final CurrentConsole INSTANCE= new CurrentConsole();
    private  Scanner fileScanner = null;
    private     Scanner defScanner = new Scanner(System.in);
    private  Scanner scanner;
@Override
    public Scanner getScanner() {
        return scanner;
    }
    @Override
    public void printHello(){
        print("Добро пожаловать!\n" +
                "Введите login или register для авторизации");
    }

@Override
    public String getInput() {
        String input = null;
        checkScanner();
        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
        }


        if (input!=null) return input;
        ArrayList<File> stack = ExecuteScript.getStack();
        ArrayList<Scanner> stackScanners = ExecuteScript.getStackScanners();
        int size = stack.size();
        if (stack.isEmpty()){
            System.exit(0);
        }

        print("Чтение файла "+stack.get(stack.size()-1)+" окончено");
        stack.remove(stack.size()-1);
        ExecuteScript.setStack(stack);
        stackScanners.remove(stackScanners.size()-1);
        ExecuteScript.setStackScanners(stackScanners);
        if (stack.isEmpty()){
            selectConsoleScanner();
        }else{
            fileScanner = stackScanners.get(stackScanners.size()-1);

        }

        return  getInput();
    }
    @Override
    public void  goToMenu(){
        throw new InvalidFormatException("Операция отменена");
    }
    @Override

    public String getInputFromCommand(int minCountOfArgs,int maxCountOfArgs){
        this.print("Для отмены операции введите /");
        String input = this.getInput();
        if (input.equals("/")){
            goToMenu();
        }
        int countOfArgs = input.split(" ",-1).length ;
        if (countOfArgs < minCountOfArgs || countOfArgs>maxCountOfArgs ){
            print("Неверное число аргументов");
            return getInputFromCommand(minCountOfArgs,maxCountOfArgs);
        }
        return input;
    }



    public void checkScanner(){
        if (fileScanner==null){
            scanner=defScanner;
        }else {
            scanner =fileScanner;
        }
    }
    @Override
    public void selectFileScanner(Scanner scanner) {
        this.fileScanner = scanner;
    }
@Override
    public void selectConsoleScanner() {
        this.fileScanner = null;
    }



}