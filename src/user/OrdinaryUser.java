package user;

/**
 * Interface that stores the behavior of an Ordinary User (Critic or Audience)
 *
 * @author Filipe Corista / João Rodrigues
 */
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

    String getUserType();
}
