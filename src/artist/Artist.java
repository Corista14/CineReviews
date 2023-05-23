package artist;

import artist.exceptions.AlreadyHasBioException;
import show.Show;

import java.util.Iterator;

public interface Artist {

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
     * @return the date of birth of the artist if he has a bio
     * @pre hasBio()
     * Gets the date of birth of the artist if he has a bio
     */
    String getDateOfBirth();

    /**
     * @return the place of birth of the artist of he has a bio
     * @pre hasBio()
     * Gets the place of birth of the artist if he has a bio
     */
    String getPlaceOfBirth();

    /**
     * Adds a show to an artist
     *
     * @param show show that will be added to the artist
     */
    void addShow(Show show);


}
