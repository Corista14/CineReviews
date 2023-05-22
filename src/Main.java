import cinereviews.CineReviews;
import cinereviews.CineReviewsClass;
import cinereviews.exceptions.NoUsersException;
import user.AdminUser;
import user.OrdinaryUser;
import user.User;
import user.exceptions.UnknownUserTypeException;
import user.exceptions.UserAlreadyExistsException;
import util.Command;

import java.util.Iterator;
import java.util.Scanner;

/**
 * Entrypoint of the CineReviews application
 *
 * @author Filipe Corista / João Rodrigues
 */
public class Main {

    // Constant Message
    private static final String BYE = "Bye!";
    private static final String LIST_COMMAND = "%s - %s\n";
    private static final String UNKNOWN_COMMAND = "Unknown command. Type help to see available commands.";
    private static final String UNKNOWN_USER_TYPE = "Unknown user type!";
    private static final String USER_ALREADY_EXISTS = "User %s already exists!\n";
    private static final String USER_REGISTERED = "User %s was registered as %s.\n";
    private static final String LIST_ADMIN = "Admin %s has uploaded %d shows.\n";
    private static final String LIST_ORDINARY = "User %s has posted %d reviews.\n";
    private static final String NO_USERS = "No users registered.\n";


    public static void main(String[] args) {
        executeCommands();
    }

    private static void executeCommands() {
        Scanner in = new Scanner(System.in);
        CineReviews cine = new CineReviewsClass();

        Command command;

        do {
            command = getCommand(in);
            switch (command) {
                case HELP -> executeHelp();
                case EXIT -> System.out.println(BYE);
                case REGISTER -> executeRegister(in, cine);
                case USERS -> executeUsers(cine);
                default -> System.out.println(UNKNOWN_COMMAND);
            }
        } while (!command.name().equals(Command.EXIT.name()));
    }

    private static void executeUsers(CineReviews cine) {
        Iterator<User> it = cine.getAllUsers();
        if (!it.hasNext())
            System.out.println(NO_USERS);
        while (it.hasNext()) {
            User next = it.next();
            if (next instanceof AdminUser admin)
                System.out.printf(LIST_ADMIN, admin.getName(), admin.getNumberOfPostedShows());
            else if (next instanceof OrdinaryUser ordinary)
                System.out.printf(LIST_ORDINARY, ordinary.getName(), ordinary.getReviewsCount());
        }
    }

    private static void executeRegister(Scanner in, CineReviews cine) throws UnknownUserTypeException, UserAlreadyExistsException {
        String type = in.next();
        String username = in.next();
        String password = "";
        if (type.equals("admin"))
            password = in.next();
        in.nextLine();

        try {
            cine.registerUser(type, username, password);
            System.out.printf(USER_REGISTERED, username, type);
        } catch (UnknownUserTypeException e) {
            System.out.println(UNKNOWN_USER_TYPE);
        } catch (UserAlreadyExistsException e) {
            System.out.printf(USER_ALREADY_EXISTS, username);
        }
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
