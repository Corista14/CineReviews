import artist.Artist;
import artist.exceptions.AlreadyHasBioException;
import artist.exceptions.UnknownArtistException;
import cinereviews.CineReviews;
import cinereviews.CineReviewsClass;
import cinereviews.exceptions.NoArtistException;
import cinereviews.exceptions.NoCollaborationsException;
import review.Review;
import review.exceptions.UserAlreadyReviewedException;
import show.Movie;
import show.Series;
import show.Show;
import show.exceptions.ShowAlreadyExistsException;
import show.exceptions.UnknownShowException;
import user.AdminUser;
import user.OrdinaryUser;
import user.User;
import user.exceptions.*;
import util.Command;

import java.util.*;

/**
 * Entrypoint of the CineReviews application
 *
 * @author Filipe Corista / João Rodrigues
 */
public class Main {

    private static final String ADMIN = "admin";
    private static final String MOVIE = "movie";
    private static final String SERIES = "series";


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
    private static final String ALL_SHOWS = "All shows:";
    private static final String NO_SHOWS = "No shows have been uploaded.";
    private static final String ALREADY_HAS_BIO = "Bio of %s is already available!\n";
    private static final String BIO_CREATED = "%s bio was created.\n";
    private static final String BIO_UPDATED = "%s bio was updated.\n";
    private static final String UNKNOWN_ARTIST = "No information about %s!\n";
    private static final String SHOW_CREDIT = "%s; %d; %s [%s]\n";
    private static final String ADMIN_CANT_REVIEW = "Admin %s cannot review shows!\n";
    private static final String ALREADY_REVIEWED = "%s has already reviewed %s!\n";
    private static final String REVIEW_REGISTERED = "Review for %s was registered [%d reviews].\n";
    private static final String UNKNOWN_SHOW = "Show %s does not exist!\n";
    private static final String UNKNOWN_USER = "User %s does not exist!\n";
    private static final String SHOW_HAS_NO_REVIEWS = "Show %s has no reviews.\n";
    private static final String REVIEWS_OF = "Reviews of %s [%.1f]:\n";
    private static final String REVIEW_OF_USER = "Review of %s (%s): %s [%s]\n";
    private static final String NO_SHOWS_IN_YEAR = "No show was found within the criteria.";
    private static final String RELEASED_HEADER = "Shows released on %d:\n";
    private static final String RELEASED_MOVIE = "Movie %s by %s released on %d [%.1f]\n";
    private static final String RELEASED_SERIES = "Series %s by %s released on %d [%.1f]\n";
    private static final String NO_ARTISTS = "No artists yet!";
    private static final String NO_COLLABORATIONS = "No collaborations yet!\n";
    private static final String FRIENDS_HEADER = "These artists have worked on %d projects together:\n";
    private static final String FRIENDS_PRINTER = "%s and %s\n";
    private static final String NO_SHOWS_WITH_CRITERIA = "No show was found within the criteria.";
    private static final String SHOWS_BY_GENRE = "Search by genre:";
    private static final String SMALL_WORLD = "It is a small world!";
    private static final String NEVER_WORKED = "These %d artists never worked together:\n";
    private static final String PRINT_SHOW = "%s; %s; %d; %s; %d; %s; %s\n";

    public static void main(String[] args) {
        executeCommands();
    }

    /**
     * Executes the commands of the app
     */
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
                case ARTIST -> executeArtist(in, cine);
                case CREDITS -> executeCredits(in, cine);
                case REVIEW -> executeReview(in, cine);
                case REVIEWS -> executeReviews(in, cine);
                case GENRE -> executeGenre(in, cine);
                case RELEASED -> executeReleased(in, cine);
                case AVOIDERS -> executeAvoiders(cine);
                case FRIENDS -> executeFriends(cine);
                default -> System.out.println(UNKNOWN_COMMAND);
            }
        } while (!command.name().equals(Command.EXIT.name()));

        in.close();
    }

    /**
     * List the artists that have no common projects
     *
     * @param cine object of the system class
     */
    private static void executeAvoiders(CineReviews cine) {
        try {
            Iterator<Set<Artist>> it = cine.getAvoiders();
            if (!it.hasNext()) System.out.println(SMALL_WORLD);
            else printAvoiders(cine, it);
        } catch (NoArtistException e) {
            System.out.println(NO_ARTISTS);
        }
    }

    /**
     * Prints the avoiders
     *
     * @param it   iterator of the set of avoiders
     * @param cine object of the system class
     */
    private static void printAvoiders(CineReviews cine, Iterator<Set<Artist>> it) {
        System.out.printf(NEVER_WORKED, cine.getLastAvoidersSize());
        while (it.hasNext()) {
            Set<Artist> nextSet = it.next();
            Iterator<Artist> artistIt = nextSet.iterator();
            StringBuilder cast = getCommaListOfArtists(artistIt, ", ");
            System.out.println(cast);
        }
    }

    /**
     * List the artists that have more projects together
     *
     * @param cine object of the system class
     */
    private static void executeFriends(CineReviews cine) {
        try {
            Iterator<Artist> allFriendsIt = cine.getAllFriends();
            Artist friend = allFriendsIt.next();
            System.out.printf(FRIENDS_HEADER, friend.getMostTimesWorked());
            allFriendsIt = cine.getAllFriends();
            while (allFriendsIt.hasNext()) {
                friend = allFriendsIt.next();
                Iterator<Artist> friendsOf = cine.getFriendsOf(friend.getName());
                while (friendsOf.hasNext()) {
                    Artist nextArtist = friendsOf.next();
                    System.out.printf(FRIENDS_PRINTER, friend.getName(), nextArtist.getName());
                }
            }
        } catch (NoArtistException e) {
            System.out.println(NO_ARTISTS);
        } catch (NoCollaborationsException e) {
            System.out.printf(NO_COLLABORATIONS);
        }
    }

    /**
     * Lists shows released on a given year
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeReleased(Scanner in, CineReviews cine) {
        int year = in.nextInt();
        in.nextLine();
        Iterator<Show> it = cine.getShowsByYear(year);
        if (!it.hasNext()) System.out.println(NO_SHOWS_IN_YEAR);
        else {
            System.out.printf(RELEASED_HEADER, year);
            printReleasedShow(it);
        }
    }

    /**
     * Lists shows of given genres
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeGenre(Scanner in, CineReviews cine) {
        List<String> genres = readSequenceOfStrings(in);
        Iterator<Show> it = cine.getShowsByGenre(genres.iterator());
        if (!it.hasNext()) System.out.println(NO_SHOWS_WITH_CRITERIA);
        else {
            System.out.println(SHOWS_BY_GENRE);
            printReleasedShow(it);
        }
    }

    /**
     * Prints the released shows given the iterator
     *
     * @param it iterator of the shows
     */
    private static void printReleasedShow(Iterator<Show> it) {
        while (it.hasNext()) {
            Show show = it.next();
            if (show instanceof Movie) {
                System.out.printf(RELEASED_MOVIE, show.getTitle(), show.getDirectorName(), show.getYearOfRelease(), show.getScore());
            } else {
                System.out.printf(RELEASED_SERIES, show.getTitle(), show.getDirectorName(), show.getYearOfRelease(), show.getScore());
            }

        }
    }

    /**
     * Lists the reviews of a show
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeReviews(Scanner in, CineReviews cine) {
        String show = in.nextLine().trim();

        try {
            Iterator<Review> it = cine.getReviewsOfShow(show);
            if (!it.hasNext()) System.out.printf(SHOW_HAS_NO_REVIEWS, show);
            else {
                System.out.printf(REVIEWS_OF, show, cine.getScoreOfShow(show));
                while (it.hasNext()) {
                    Review next = it.next();
                    System.out.printf(REVIEW_OF_USER, next.getReviewer().getName(),
                            next.getReviewer().getUserType(), next.getDescription(), next.getClassification());
                }
            }
        } catch (UnknownShowException e) {
            System.out.printf(UNKNOWN_SHOW, show);
        }
    }

    /**
     * Adds a review to a show
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeReview(Scanner in, CineReviews cine) {
        String username = in.next();
        String show = in.nextLine().trim();
        String review = in.nextLine();
        String score = in.nextLine();

        try {
            int reviewNum = cine.reviewShow(username, review, show, score);
            System.out.printf(REVIEW_REGISTERED, show, reviewNum);
        } catch (UnknownUserException e) {
            System.out.printf(UNKNOWN_USER, username);
        } catch (IsAdminException e) {
            System.out.printf(ADMIN_CANT_REVIEW, username);
        } catch (UnknownShowException e) {
            System.out.printf(UNKNOWN_SHOW, show);
        } catch (UserAlreadyReviewedException e) {
            System.out.printf(ALREADY_REVIEWED, username, show);
        }
    }

    /**
     * Lists the bio and credits of an artist
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeCredits(Scanner in, CineReviews cine) {
        String artistName = in.nextLine().trim();

        try {
            Iterator<Show> credits = cine.getArtistCredits(artistName);
            if (cine.artistHasBio(artistName)) {
                System.out.println(cine.getDateOfBirthOfArtist(artistName));
                System.out.println(cine.getPlaceOfBirthOfArtist(artistName));
            }
            while (credits.hasNext()) {
                Show next = credits.next();
                if (next instanceof Movie movie)
                    System.out.printf(SHOW_CREDIT, movie.getTitle(), movie.getYearOfRelease(),
                            cine.getArtistRole(artistName, movie.getTitle()), MOVIE);
                else if (next instanceof Series series)
                    System.out.printf(SHOW_CREDIT, series.getTitle(), series.getYearOfRelease(),
                            cine.getArtistRole(artistName, series.getTitle()), SERIES);
            }
        } catch (UnknownArtistException e) {
            System.out.printf(UNKNOWN_ARTIST, artistName);
        }
    }

    /**
     * Adds bio information about an artist
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeArtist(Scanner in, CineReviews cine) {
        String name = in.nextLine().trim();
        String dateOfBirth = in.nextLine();
        String placeOfBirth = in.nextLine();

        try {
            boolean wasCreated = cine.addArtistBio(name, dateOfBirth, placeOfBirth);
            if (wasCreated) System.out.printf(BIO_CREATED, name);
            else
                System.out.printf(BIO_UPDATED, name);
        } catch (AlreadyHasBioException e) {
            System.out.printf(ALREADY_HAS_BIO, name);
        }
    }

    /**
     * Lists all shows
     *
     * @param cine object of the system class
     */
    private static void executeShows(CineReviews cine) {
        Iterator<Show> it = cine.listAllShows();
        if (!it.hasNext()) System.out.println(NO_SHOWS);
        else {
            System.out.println(ALL_SHOWS);
            while (it.hasNext()) {
                Show next = it.next();
                Iterator<Artist> castIt = next.getCast();
                StringBuilder cast = getCommaListOfArtists(castIt, "; ");
                if (next instanceof Series series)
                    System.out.printf(PRINT_SHOW, series.getTitle(), series.getDirectorName(),
                            series.getNumberOfSeasons(), series.getAgeCertification(), series.getYearOfRelease(),
                            series.getMainGenre(), cast);
                else if (next instanceof Movie movie)
                    System.out.printf(PRINT_SHOW, movie.getTitle(), movie.getDirectorName(),
                            movie.getDuration(), movie.getAgeCertification(), movie.getYearOfRelease(),
                            movie.getMainGenre(), cast);
            }
        }
    }

    /**
     * Uploads a new series
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
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

    /**
     * Uploads a new movie
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
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

    /**
     * Lists all registered users
     *
     * @param cine object of the system class
     */
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

    /**
     * Registers a user in the system
     *
     * @param in   scanner object
     * @param cine object of the system class
     */
    private static void executeRegister(Scanner in, CineReviews cine) {
        String type = in.next();
        String username = in.next();
        String password = "";
        if (type.equals(ADMIN)) password = in.next();
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

    /**
     * Shows the available commands
     */
    private static void executeHelp() {
        for (Command command : Command.values()) {
            if (command != Command.UNKNOWN) {
                String name = command.name().toLowerCase();
                String description = command.getDescription();
                System.out.printf(LIST_COMMAND, name, description);
            }
        }
    }

    /**
     * Reads a command and checks for the Command enum
     *
     * @param in scanner object
     * @return the command name
     */
    private static Command getCommand(Scanner in) {
        try {
            String command = in.next().toUpperCase();
            return Command.valueOf(command);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    /**
     * Reads a sequence of Strings returning them in a list
     *
     * @param in scanner object
     * @return list of the read Strings
     */
    private static List<String> readSequenceOfStrings(Scanner in) {
        List<String> list = new ArrayList<>();
        int n = in.nextInt();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String next = in.nextLine();
            list.add(next);
        }
        return list;
    }

    /**
     * Gets the comma list of Artists of an iteration
     *
     * @param artistIt  iterator of the artists
     * @param separator separator of the list
     * @return the comma list of Artists of an iteration
     */
    private static StringBuilder getCommaListOfArtists(Iterator<Artist> artistIt, String separator) {
        StringBuilder list = new StringBuilder();
        while (artistIt.hasNext()) {
            Artist nextArt = artistIt.next();
            list.append(nextArt.getName());
            if (artistIt.hasNext()) list.append(separator);
        }
        return list;
    }

}
