package seedu.duke.ui;

import seedu.duke.localtime.CurrentDate;

import java.util.Scanner;

public class UI {

    Scanner line = new Scanner(System.in);

    public static final String DIVIDER = "____________________________________________________\n";
    private static final String WELCOME_MESSAGE = DIVIDER
            + "Current session date: " + CurrentDate.getCurrentFormattedDate() + '\n'
            + "Welcome to SITUS!\n"
            + "What would you like to do first?\n"
            + "To see what I can do, use \"help\"\n"
            + DIVIDER;
    private static final String GOODBYE_MESSAGE = DIVIDER + "Okay, see you soon! Goodbye.\n" + DIVIDER;


    public UI() {
        System.out.print(WELCOME_MESSAGE);
    }

    public String getUserCommand() {
        return line.nextLine().trim().toLowerCase();
    }

    public void printGoodbye() {
        System.out.print(GOODBYE_MESSAGE);
    }

    public void printCommandOutput(String commandOutput) {
        System.out.print(DIVIDER + commandOutput + "\n" + DIVIDER);
    }

}
