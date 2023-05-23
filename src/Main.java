import cinereviews.CineReviews;
import cinereviews.CineReviewsClass;
import show.Movie;
import show.Series;
import show.Show;
import show.exceptions.ShowAlreadyExistsException;
import user.AdminUser;
import user.OrdinaryUser;
import user.User;
import user.exceptions.NotAnAdminException;
import user.exceptions.UnknownUserTypeException;
import user.exceptions.UserAlreadyExistsException;
import user.exceptions.WrongPasswordException;
import util.Command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Entrypoint of the CineReviews application
 *
 * @author Filipe Corista / João Rodrigues
 */
public class Main {

    private static final String ADMIN = "admin";

    // Constant Message
    private static final String BYE = "Bye!";
    private static final String LIST_COMMAND = "%s - %s\n";
    private static final String UNKNOWN_COMMAND = "Unknown command. Type help to see available commands.";
    private static final String UNKNOWN_USER_TYPE = "Unknown user type!";
    private static final String USER_ALREADY_EXISTS = "User %s already exists!\n";
    private static final String USER_REGISTERED = "User %s was registered as %s.\n";
    private static final String LIST_ADMIN = "Admin %s has uploaded %d shows\n";
    private static final String LIST_ORDINARY = "User %s has posted %d reviews\n";
    private static final String NO_USERS = "No users registered.";
    private static final String NOT_AN_ADMIN = "Admin %s does not exist!\n";
    private static final String WRONG_PASSWORD = "Invalid authentication!";
    private static final String SHOW_ALREADY_EXISTS = "Show %s already exists!\n";
    private static final String MOVIE_ADDED = "Movie %s (%d) was uploaded [%d new artists were created].\n";
    private static final String SERIES_ADDED = "Series %s (%d) was uploaded [%d new artists were created].\n";
    private static final String ALL_USERS = "All registered users:";
    private static final String NO_SHOWS = "No shows have been uploaded.";

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
                case MOVIE -> executeMovie(in, cine);
                case SERIES -> executeSeries(in, cine);
                case SHOWS -> executeShows(cine);
                default -> System.out.println(UNKNOWN_COMMAND);
            }
        } while (!command.name().equals(Command.EXIT.name()));
    }

    /** TODO: UPDATE THE PRINTED LIST OF SHOWS:
     *  Format: Title, Director, duration or season number (Movie vs Series), Age Certification, releaseYear,
     *  main genre, cast (a printed comma list of the name of the artists)
     */
    private static void executeShows(CineReviews cine) {
        Iterator<Show> it = cine.listAllShows();
        if (!it.hasNext()) System.out.println(NO_SHOWS);
        else {
            System.out.println(ALL_USERS);
            while (it.hasNext()) {
                Show next = it.next();
                if (next instanceof Series series)
                    System.out.printf("%s; %s; %d; %s; %d; %s; %s");
                else if (next instanceof Movie ordinary)
                    System.out.printf("%s; %s; %d; %s; %d; %s; %s");
            }
        }
    }

    private static void executeSeries(Scanner in, CineReviews cine) {
        String adminName = in.next();
        String password = in.next();
        in.nextLine();

        String title = in.nextLine();
        String directorName = in.nextLine();
        int seasonAmount = in.nextInt();
        in.nextLine();
        String ageOfCertification = in.nextLine();
        int releaseYear = in.nextInt();
        in.nextLine();

        List<String> genres = readSequenceOfStrings(in);
        List<String> cast = readSequenceOfStrings(in);

        try {
            int artistsAdded = cine.addSeries(adminName, password, title, directorName, seasonAmount, ageOfCertification,
                    releaseYear, genres.iterator(), cast.iterator());
            System.out.printf(SERIES_ADDED, title, releaseYear, artistsAdded);
        } catch (NotAnAdminException e) {
            System.out.printf(NOT_AN_ADMIN, adminName);
        } catch (ShowAlreadyExistsException e) {
            System.out.printf(SHOW_ALREADY_EXISTS, title);
        } catch (WrongPasswordException e) {
            System.out.println(WRONG_PASSWORD);
        }

    }

    private static void executeMovie(Scanner in, CineReviews cine) {
        String adminName = in.next();
        String password = in.next();
        in.nextLine();

        String title = in.nextLine();
        String directorName = in.nextLine();
        int duration = in.nextInt();
        in.nextLine();
        String ageOfCertification = in.nextLine();
        int releaseYear = in.nextInt();
        in.nextLine();

        List<String> genres = readSequenceOfStrings(in);
        List<String> cast = readSequenceOfStrings(in);

        try {
            int artistsAdded = cine.addMovie(adminName, password, title, directorName, duration,
                    ageOfCertification, releaseYear, genres.iterator(), cast.iterator());
            System.out.printf(MOVIE_ADDED, title, releaseYear, artistsAdded);
        } catch (NotAnAdminException e) {
            System.out.printf(NOT_AN_ADMIN, adminName);
        } catch (WrongPasswordException e) {
            System.out.println(WRONG_PASSWORD);
        } catch (ShowAlreadyExistsException e) {
            System.out.printf(SHOW_ALREADY_EXISTS, title);
        }
    }

    private static void executeUsers(CineReviews cine) {
        Iterator<User> it = cine.getAllUsers();
        if (!it.hasNext()) System.out.println(NO_USERS);
        else {
            System.out.println(ALL_USERS);
            while (it.hasNext()) {
                User next = it.next();
                if (next instanceof AdminUser admin)
                    System.out.printf(LIST_ADMIN, admin.getName(), admin.getNumberOfPostedShows());
                else if (next instanceof OrdinaryUser ordinary)
                    System.out.printf(LIST_ORDINARY, ordinary.getName(), ordinary.getReviewsCount());
            }
        }

    }

    private static void executeRegister(Scanner in, CineReviews cine) {
        String type = in.next();
        String username = in.next();
        String password = "";
        if (type.equals(ADMIN))
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

    private static List<String> readSequenceOfStrings(Scanner in) {
        List<String> list = new ArrayList<>();
        int n = in.nextInt();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String actor = in.nextLine();
            list.add(actor);
        }
        return list;
    }

}
