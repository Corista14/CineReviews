import util.Command;

import java.util.Scanner;

/**
 * Entrypoint of the CineReviews application
 * @author Filipe Corista / João Rodrigues
 */
public class Main {

    // Constant Message
    private static final String BYE = "Bye!";
    private static final String LIST_COMMAND = "%s - %s\n";
    private static final String UNKNOWN_COMMAND = "Unknown command. Type help to see available commands.";

    public static void main(String[] args) {
        executeCommands();
    }

    private static void executeCommands() {
        Scanner in = new Scanner(System.in);

        Command command;

        do {
            command = getCommand(in);
            switch (command) {
                case HELP -> executeHelp();
                case EXIT -> System.out.println(BYE);
                default -> System.out.println(UNKNOWN_COMMAND);
            }
        } while(!command.name().equals(Command.EXIT.name()));
    }

    private static void executeHelp() {
        for (Command command : Command.values()) {
            if (command != Command.UNKNOWN) {
                String name = command.name().toLowerCase();
                String description = command.getDescription();
                System.out.printf(LIST_COMMAND, name, description);
            }
        }
    }

    private static Command getCommand(Scanner in) {
        try {
            String command = in.next().toUpperCase();
            return Command.valueOf(command);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }
}
