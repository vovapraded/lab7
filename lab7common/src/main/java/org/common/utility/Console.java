package org.common.utility;

import org.common.dto.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * a class for reading and writing from the console
 */
public interface     Console {


    void sendToController(List<Ticket> tickets);

    public abstract void selectFileScanner(Scanner scanner);


    public abstract Scanner getScanner();

    void printHello();

    public abstract String getInput();

    public abstract void  goToMenu();

    public abstract String getInputFromCommand(int minCountOfArgs, int maxCountOfArgs) ;

    abstract void sendToController(String s) ;


    public abstract void selectConsoleScanner();
}