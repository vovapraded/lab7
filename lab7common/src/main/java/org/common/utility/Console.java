package org.common.utility;

import org.common.dto.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * a class for reading and writing from the console
 */
public interface     Console {




    public abstract void selectFileScanner(Scanner scanner);


    public abstract Scanner getScanner();


    public abstract String getInput();





    public abstract void selectConsoleScanner();
}