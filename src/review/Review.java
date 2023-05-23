package review;

import user.OrdinaryUser;

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
     * @return the score fo the review
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

}
