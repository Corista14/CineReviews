package show;

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
     * Checks if a user has already reviewed this show
     *
     * @param user user to check if it has already reviewed the show
     * @return true if a user has already reviewed this show, false otherwise
     */
    boolean userHasReviewed(User user);
}
