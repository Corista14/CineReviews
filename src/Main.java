import artist.Artist;
import artist.exceptions.AlreadyHasBioException;
import artist.exceptions.UnknownArtistException;
import cinereviews.CineReviews;
import cinereviews.CineReviewsClass;
import cinereviews.exceptions.NoArtistException;
import cinereviews.exceptions.NoCollaborationsException;
import cinereviews.exceptions.NoShowsThisYearException;
import cinereviews.exceptions.NoShowsWithGenreException;
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
import util.Classification;
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
    private static final String NO_ARTISTS = "No artists yet!\n";
    private static final String NO_COLLABORATIONS = "No collaborations yet!\n";
    private static final String FRIENDS_HEADER = "These artists have worked on %d projects together\n";
    private static final String FRIENDS_PRINTER = "%s and %s\n";

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
                case ARTIST -> executeArtist(in, cine);
                case CREDITS -> executeCredits(in, cine);
                case REVIEW -> executeReview(in, cine);
                case REVIEWS -> executeReviews(in, cine);
                case GENRE -> executeGenre(in, cine);
                case RELEASED -> executeReleased(in, cine);
                case FRIENDS -> executeFriends(in, cine);
                default -> System.out.println(UNKNOWN_COMMAND);
            }
        } while (!command.name().equals(Command.EXIT.name()));
    }

    private static void executeFriends(Scanner in, CineReviews cine) {

        try {
            Iterator<Artist> allFriendsIt = cine.getAllFriends();   //TODO:FIX THIS ITS SO SCUFFY
            Artist friend = allFriendsIt.next();
            System.out.printf(FRIENDS_HEADER, friend.getMostTimesWorked());
            allFriendsIt = cine.getAllFriends();
            while (allFriendsIt.hasNext()) {
                friend = allFriendsIt.next();
                Iterator<Artist> friendsOf = cine.getFriendsOf(friend.getName());
                while (friendsOf.hasNext()) {
                    System.out.printf(FRIENDS_PRINTER, friend.getName(), friendsOf.next().getName());
                }
            }
        } catch (NoArtistException e) {
            System.out.printf(NO_ARTISTS);
        } catch (NoCollaborationsException e) {
            System.out.printf(NO_COLLABORATIONS);
        }
    }

    private static void executeReleased(Scanner in, CineReviews cine) {
        int year = in.nextInt();
        in.nextLine();
        Iterator<Show> it = cine.getShowsByYear(year);
        if (!it.hasNext()) System.out.println(NO_SHOWS_IN_YEAR);
        else {
            System.out.printf(RELEASED_HEADER, year);
            while (it.hasNext()) {
                Show show = it.next();
                if (show instanceof Movie) {
                    System.out.printf(RELEASED_MOVIE, show.getTitle(), show.getDirectorName(), show.getYearOfRelease(), show.getScore());
                } else {
                    System.out.printf(RELEASED_SERIES, show.getTitle(), show.getDirectorName(), show.getYearOfRelease(), show.getScore());
                }

            }
        }
    }

    private static void executeGenre(Scanner in, CineReviews cine) {
        List<String> genres = readSequenceOfStrings(in);
        Iterator<Show> it = cine.getShowsByGenre(genres.iterator());
        if (!it.hasNext()) System.out.println("No show was found within the criteria.");//uma excessao fica mais giro
        else {
            System.out.println("Search by genre:");
            while (it.hasNext()) {
                Show next = it.next();
                if (next instanceof Movie)
                    System.out.printf(RELEASED_MOVIE, next.getTitle(), next.getDirectorName(), next.getYearOfRelease(), next.getScore());
                else
                    System.out.printf(RELEASED_SERIES, next.getTitle(), next.getDirectorName(), next.getYearOfRelease(), next.getScore());
            }
        }
    }


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

    // TODO: Refactor this method to look cleaner
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
                            cine.getArtistRole(artistName, movie.getTitle()), "movie");
                else if (next instanceof Series series)
                    System.out.printf(SHOW_CREDIT, series.getTitle(), series.getYearOfRelease(),
                            cine.getArtistRole(artistName, series.getTitle()), "series");
            }
        } catch (UnknownArtistException e) {
            System.out.printf(UNKNOWN_ARTIST, artistName);
        }
    }

    private static void executeArtist(Scanner in, CineReviews cine) {
        String name = in.nextLine().trim();
        String dateOfBirth = in.nextLine();
        String placeOfBirth = in.nextLine();

        try {
            boolean wasCreated = cine.addArtistBio(name, dateOfBirth, placeOfBirth);
            if (wasCreated)
                System.out.printf(BIO_CREATED, name);
            else
                System.out.printf(BIO_UPDATED, name);
        } catch (AlreadyHasBioException e) {
            System.out.printf(ALREADY_HAS_BIO, name);
        }
    }


    // TODO: Refactor this method to look cleaner
    private static void executeShows(CineReviews cine) {
        Iterator<Show> it = cine.listAllShows();
        if (!it.hasNext()) System.out.println(NO_SHOWS);
        else {
            System.out.println(ALL_SHOWS);
            while (it.hasNext()) {
                Show next = it.next();
                StringBuilder cast = new StringBuilder();
                Iterator<Artist> castIt = next.getCast();
                while (castIt.hasNext()) {
                    Artist nextArtist = castIt.next();
                    cast.append(nextArtist.getName());
                    if (castIt.hasNext()) cast.append("; ");
                }
                if (next instanceof Series series)
                    System.out.printf("%s; %s; %d; %s; %d; %s; %s\n", series.getTitle(), series.getDirectorName(),
                            series.getNumberOfSeasons(), series.getAgeCertification(), series.getYearOfRelease(),
                            series.getMainGenre(), cast);
                else if (next instanceof Movie movie)
                    System.out.printf("%s; %s; %d; %s; %d; %s; %s\n", movie.getTitle(), movie.getDirectorName(),
                            movie.getDuration(), movie.getAgeCertification(), movie.getYearOfRelease(),
                            movie.getMainGenre(), cast);
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
