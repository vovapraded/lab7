package org.example.utility;

import lombok.NoArgsConstructor;
import org.common.dto.Ticket;
import org.common.utility.*;
import org.common.utility.Console;
import org.example.Main;
import org.example.commands.ExecuteScript;


import java.io.*;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static lombok.AccessLevel.PRIVATE;

/**
 * a class for reading and writing from the console
 */
@NoArgsConstructor(access = PRIVATE)
public class CurrentConsole implements Console {
    private static final Console instance = new CurrentConsole();

    public static Console getInstance() {

        return instance;

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

        sendToController("Чтение файла "+stack.get(stack.size()-1)+" окончено");
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
    public void sendToController(String s) {
        Main.getConsoleEventPublisher().sendMessageToController(s);
    }

    @Override
    public void sendToController(Exception e) {
        Main.getConsoleEventPublisher().sendExceptionToController(e);

    }

    @Override
    public void sendToController(List<Ticket> tickets) {

        Main.getConsoleEventPublisher().sendTicketsToController(tickets);
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