package review;

import user.OrdinaryUser;

/**
 * Interface to store the behavior of a Review
 *
 * @author Filipe Corista / João Rodrigues
 */
public interface Review {

    /**
     * Gets the user that made the review
     *
     * @return the user that made the review
     */
    OrdinaryUser getReviewer();

    /**
     * Gives the score of the review
     *
     * @return the score of the review
     */
    int getScore();

    /**
     * Gives the username of the creator of the review
     *
     * @return the username of the creator of the review
     */
    String getUserName();

    /**
     * Checks if the review was made by a critic user
     *
     * @return true if the review was made by a critic user, false otherwise
     */
    boolean madeByCritic();

    /**
     * Gets the description of the review
     *
     * @return the description of the review
     */
    String getDescription();

    /**
     * Gets the classification of the review
     *
     * @return the classification of the review
     */
    String getClassification();
}
