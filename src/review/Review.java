package review;

import user.OrdinaryUser;

public interface Review {

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

    boolean madeByCritic();

}
