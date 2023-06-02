package artist;

import artist.exceptions.AlreadyHasBioException;
import show.Show;

import java.util.Iterator;

/**
 * Interface to store the behavior of an Artist
 *
 * @author Filipe Corista / João Rodrigues
 */
public interface Artist extends Comparable<Artist> {

    /**
     * Gets the artist name
     *
     * @return the artist name
     */
    String getName();

    /**
     * Adds a bio to the artist with a given date of birth and place of birth
     *
     * @param dateOfBirth  date of birth that will be added to the artist
     * @param placeOfBirth place of birth that will be added to the artist
     */
    void addBio(String dateOfBirth, String placeOfBirth) throws AlreadyHasBioException;

    /**
     * Returns an ordered iterator with the shows that the artist as participated in
     *
     * @return an ordered iterator with the shows that the artist as participated in
     */
    Iterator<Show> getShows();

    /**
     * Checks if the artist has a bio
     *
     * @return true if the artist has a bio
     */
    boolean hasBio();

    /**
     * Gets the date of birth of the artist if he has a bio
     *
     * @return the date of birth of the artist if he has a bio
     * @pre hasBio()
     */
    String getDateOfBirth();

    /**
     * Gets the place of birth of the artist if he has a bio
     *
     * @return the place of birth of the artist of he has a bio
     * @pre hasBio()
     */
    String getPlaceOfBirth();

    /**
     * Adds a show to an artist
     *
     * @param show show that will be added to the artist
     */
    void addShow(Show show);

    /**
     * Returns the maximum amount of times that this artist has worked with another
     *
     * @return the maximum amount of times that this artist has worked with another
     */
    int getMostTimesWorked();

    /**
     * Gets the iterator of artist that this artist has worked with
     *
     * @return the iterator of artist that this artist has worked with
     */
    Iterator<Artist> getFriends();
}
