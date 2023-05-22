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
     *  Gets the year of release of the Show
     * @return the year of release of the Show
     */
    int getYearOfRelease();

    /**
     * Gets the name of the Show
     * @return the name of the Show
     */
    String getName();
}
