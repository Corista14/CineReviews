package cinereviews;

import cinereviews.exceptions.NoUsersException;
import show.Show;
import user.User;
import user.exceptions.UnknownUserTypeException;
import user.exceptions.UserAlreadyExistsException;

import java.util.Iterator;

/**
 * An interface for the System Cinereviews that manages all the other classes and keeps
 * colections of them allowing the execution of commands
 *
 * @author Filipe Corista / João Rodrigue
 */
public interface CineReviews {

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
     * @param AdminName        name of the admin that will add the movie
     * @param password         password of the admin, an error message is sent if the password is wrong
     * @param title            title and unique identifier of the movie
     * @param director         name of the director of the movie
     * @param duration         length of the movie
     * @param ageCertification age certification of the movie
     * @param releaseYear      release year of the movie
     * @param genres           genres of the movie
     * @param cast             list of the names of the cast members
     */
    void addMovie(String AdminName, String password, String title,
                  String director, int duration, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast);

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
     */
    void addSeries(String AdminName, String password, String title,
                   String creator, int seasonAmount, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast);

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
     */
    void addArtistBio(String name, String dateOfBirth, String placeOfBirth);

    /**
     * Returns the artists date of birth if he has a bio
     *
     * @param artistName name of the artist
     * @return date of birth of the artist
     */
    String getArtistDateOfBirth(String artistName);
}
