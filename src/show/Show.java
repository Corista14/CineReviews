package show;

import artist.Artist;
import review.Review;
import user.User;

import java.util.Iterator;

/**
 * Interface for the Show
 *
 * @author Filipe Corista / João Rodrigues
 */
public interface Show {

    /**
     * Gets the collection of reviews of this show
     *
     * @return the collection of reviews of the show
     */
    Iterator<Review> getReviews();

    /**
     * Gets the number of reviews of this show
     *
     * @return the number of reviews of this show
     */
    int getReviewsCount();

    /**
     * Adds a new review to the show
     *
     * @param review review that will be added to the show
     */
    void addReview(Review review);

    /**
     * Checks if a user has already reviewed this show
     *
     * @param user user to check if it has already reviewed the show
     * @return true if a user has already reviewed this show, false otherwise
     */
    boolean userHasReviewed(User user);

    /**
     * Gets the year of release of the Show
     *
     * @return the year of release of the Show
     */
    int getYearOfRelease();

    /**
     * Gets the name of the Show
     *
     * @return the name of the Show
     */
    String getTitle();

    /**
     * Returns the score of the show
     *
     * @return the score of the show
     */
    float getScore();

    /**
     * Gets the name of the director
     *
     * @return the name of the director
     */
    String getDirectorName();

    /**
     * Gets the age of certification
     *
     * @return the age of certification
     */
    String getAgeCertification();

    /**
     * Gets the main genre of the show (the first in the collection)
     *
     * @return the main genre of the show (the first in the collection)
     */
    String getMainGenre();

    /**
     * Gets the cast of the show
     *
     * @return the iterator containing the cast of the show
     */
    Iterator<Artist> getCast();

    /**
     * Gets the cast of the show, with the director/creator there
     *
     * @return the iterator of the cast of the show, with the director/creator there
     */
    Iterator<Artist> getCastWithDirector();

    /**
     * Gets the role of an artist (Actor, Director or Creator)
     *
     * @param artist name of the artist
     * @return the role of an artist (Actor, Director or Creator)
     */
    String getArtistRole(String artist);

    /**
     * Checks if the show has a genre with given name
     *
     * @param genre name of the genre
     * @return true if the show has a genre with given name, false otherwise
     */
    boolean hasGenre(String genre);
}
