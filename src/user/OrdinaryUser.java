package user;

import review.Review;

import java.util.Iterator;

public interface OrdinaryUser extends User {

    /**
     * Gets the number of reviews that a user has
     *
     * @return the number of reviews that a user has
     */
    int getReviewsCount();

    /**
     * Increments the review count by one
     */
    void incrementReviewCount();
}
