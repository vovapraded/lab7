package org.example.commands;
import org.common.commands.Command;
import org.common.utility.InvalidFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The command to execute a script from a file
 */
public class ExecuteScript extends Command implements Serializable,ClientCommand{
    @Serial
    private static final long serialVersionUID = "ExecuteScript".hashCode();
    private static ArrayList<File> stack = new ArrayList<>();
    private static ArrayList<Scanner> stackScanners = new ArrayList<>();


    public static ArrayList<File> getStack() {
        return stack;
    }

    public static ArrayList<Scanner> getStackScanners() {
        return stackScanners;
    }

    public static void setStackScanners(ArrayList<Scanner> stackScanners) {
        ExecuteScript.stackScanners = stackScanners;
    }

    public static void setStack(ArrayList<File> stack) {
        ExecuteScript.stack = stack;
    }

    @Override
    public void execute() {
            try {
                File file = new File(stringArg);
                if (!stack.contains(file)){
                    stack.add(file);

                    Scanner scanner = new Scanner(file);
                    stackScanners.add(scanner);
                    console.selectFileScanner(scanner);
                }else {
                    console.sendToController("Ошибка бесконечная рекурсия",true);
                }
            } catch (FileNotFoundException e) {
                throw new InvalidFormatException("Нет такого файла");
            }catch (SecurityException e){
                throw new InvalidFormatException("Нет прав доступа");
        }
    }

    @Override
    public void validate(String arg1) {
        stringArg = arg1;
    }
}
