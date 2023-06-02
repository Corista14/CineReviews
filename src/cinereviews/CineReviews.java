package cinereviews;

import artist.Artist;
import artist.exceptions.AlreadyHasBioException;
import artist.exceptions.UnknownArtistException;
import cinereviews.exceptions.NoArtistException;
import cinereviews.exceptions.NoCollaborationsException;
import review.Review;
import review.exceptions.UserAlreadyReviewedException;
import show.exceptions.ShowAlreadyExistsException;
import show.Show;
import show.exceptions.UnknownShowException;
import user.User;
import user.exceptions.*;

import java.util.Iterator;
import java.util.Set;

/**
 * An interface for the System CineReviews that manages all the other classes and keeps
 * collections of them allowing the execution of commands
 *
 * @author Filipe Corista / João Rodrigues
 */
public interface CineReviews {

    /**
     * Registers a new User to the application
     *
     * @param type     type of the user (Critic, Audience or Admin)
     * @param name     name of the user
     * @param password password (in case it's an admin) of the user
     * @throws UnknownUserTypeException   exception when the user type is unknown
     * @throws UserAlreadyExistsException exception when the user already exists
     */
    void registerUser(String type, String name, String password) throws UnknownUserTypeException, UserAlreadyExistsException;

    /**
     * Returns an iterator with all the users in the platform including admins
     *
     * @return an iterator with all the users in the platform including admins
     */
    Iterator<User> getAllUsers();

    /**
     * Adds a new movie to the platform, to do this an admin is needed and the respective password
     * The movie is created with a given title,director,duration,age certification,releaseYear
     * list of genres and list of cast members, if any actors that are not already created are
     * present in the movie they too are added to the platform
     *
     * @param adminName        name of the admin that will add the movie
     * @param password         password of the admin, an error message is sent if the password is wrong
     * @param title            title and unique identifier of the movie
     * @param director         name of the director of the movie
     * @param duration         length of the movie
     * @param ageCertification age certification of the movie
     * @param releaseYear      release year of the movie
     * @param genres           genres of the movie
     * @param cast             list of the names of the cast members
     * @return number of added artist
     * @throws NotAnAdminException        exception when the user is not an admin
     * @throws WrongPasswordException     exception when the password of the admin is wrong
     * @throws ShowAlreadyExistsException exception when the show already exists
     */
    int addMovie(String adminName, String password, String title,
                 String director, int duration, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast)
            throws NotAnAdminException, WrongPasswordException, ShowAlreadyExistsException;

    /**
     * Adds a new series to the platform, to do this an admin is needed and the respective password
     * The series is created with a given title,creator,amount of seasons, age certification, year of release
     * list of genres and list of the names of the cast members if any actors that are not already created are
     * present in the movie they too are added to the platform
     *
     * @param AdminName        name of the admin that will add the movie
     * @param password         password of the admin, an error message is sent if the password is wrong
     * @param title            title of the series
     * @param creator          name of the creator of the series
     * @param seasonAmount     amount of seasons
     * @param ageCertification age certification of the series
     * @param releaseYear      year of release of the series
     * @param genres           list of genres of the series
     * @param cast             list of names of the cast members
     * @return number of added artists
     * @throws NotAnAdminException        exception when the user is not an admin
     * @throws WrongPasswordException     exception when the password of the admin is wrong
     * @throws ShowAlreadyExistsException exception when the show already exists
     */
    int addSeries(String AdminName, String password, String title,
                  String creator, int seasonAmount, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast) throws NotAnAdminException, WrongPasswordException, ShowAlreadyExistsException;

    /**
     * Lists all the shows that are stored in the platform by alphabetical order of the title
     *
     * @return an iterator with all the shows order by alphabetical order of the title
     */
    Iterator<Show> listAllShows();

    /**
     * Adds a bio to an existing artist or creates a new artist with this bio
     *
     * @param name         name of the artist
     * @param dateOfBirth  date of birth of the artist
     * @param placeOfBirth place of birth of the artist
     * @throws AlreadyHasBioException exception when the artist already has a bio
     */
    boolean addArtistBio(String name, String dateOfBirth, String placeOfBirth) throws AlreadyHasBioException;

    /**
     * Lists the bio and credits of an artist
     *
     * @param artistName name of the artist
     * @return the iterator of the bio and credits of an artist
     * @throws UnknownArtistException exception when there is no artist with the given name
     */
    Iterator<Show> getArtistCredits(String artistName) throws UnknownArtistException;

    /**
     * Checks if an artist has a bio
     *
     * @param artist name of the artist
     * @return true if the artist has a bio, false otherwise
     */
    boolean artistHasBio(String artist);

    /**
     * Gets the place of birth of a given artist
     *
     * @param artist name of the artist
     * @return the place of birth of a given artist
     */
    String getPlaceOfBirthOfArtist(String artist);

    /**
     * Gets the date of birth of a given artist
     *
     * @param artist name of the artist
     * @return the date of birth of a given artist
     */
    String getDateOfBirthOfArtist(String artist);

    /**
     * Gets the role of a given artist in a given show
     *
     * @param artistName name of the artist
     * @param show       name of the show that the artist is in
     * @return the role of a given artist in a given show
     */
    String getArtistRole(String artistName, String show);

    /**
     * Adds a review to a show, getting the number of reviews that that show has
     *
     * @param username name of the user reviewing the show
     * @param review   description of the review
     * @param showName name of the show
     * @param score    classification of the show
     * @return the number of reviews that that show has
     * @throws UnknownUserException         exception when there is no user with the given name
     * @throws IsAdminException             exception when the user is an admin
     * @throws UnknownShowException         exception when there is no show with the given name
     * @throws UserAlreadyReviewedException exception when the user already reviewed the given show
     */
    int reviewShow(String username, String review, String showName, String score) throws
            UnknownUserException, IsAdminException, UnknownShowException, UserAlreadyReviewedException;

    /**
     * Lists the reviews of a show
     *
     * @param showName name of the show
     * @return the iterator of reviews of a show
     * @throws UnknownShowException exception when the there is no show with the given name
     */
    Iterator<Review> getReviewsOfShow(String showName) throws UnknownShowException;

    /**
     * Gets the score of a show
     *
     * @param showName name of the show
     * @return the score of a show
     */
    float getScoreOfShow(String showName);

    /**
     * Lists shows released on a given year
     *
     * @param year year of release of those shows
     * @return the iterator of shows released on a given year
     */
    Iterator<Show> getShowsByYear(int year);

    /**
     * Lists shows of given genres
     *
     * @param genres collection of genres to look for
     * @return the iterator of shows of given genres
     */
    Iterator<Show> getShowsByGenre(Iterator<String> genres);

    /**
     * List the artists that have more projects together
     *
     * @return the iterator of artists that have more projects together
     * @throws NoArtistException         exception when there is no artists in the application
     * @throws NoCollaborationsException exception when there is no collaborations in th application
     */
    Iterator<Artist> getAllFriends() throws NoArtistException, NoCollaborationsException;

    /**
     * Gets the list of artists an artist has worked with
     *
     * @param name name of the artist
     * @return the iterator of artists an artist has worked with
     */
    Iterator<Artist> getFriendsOf(String name);

    /**
     * Gets the largest list of artists that have never worked together
     *
     * @return the iterator of the largest list of artists that have never worked together
     */
    Iterator<Set<Artist>> getAvoiders();

    /**
     * Checks if there are any artists in the app
     * @return true if there are any artists in the app
     */
    boolean hasArtists();

    /**
     * gets the size of the groups that were created in the last avoiders command
     * returns 0 if avoiders was never called
     * @return the size of the groups that were created in the last avoiders command or 0 if avoiders was never called
     */
    int getLastAvoidersSize();
}
