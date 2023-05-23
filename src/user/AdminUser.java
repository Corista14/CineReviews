package user;

/**
 * Interface for the admin type user
 *
 * @author Filipe Corista / João Rodrigues
 */
public interface AdminUser extends User {

    /**
     * Gets the number of shows that the user posted
     *
     * @return the number of shows that the user posted
     */
    int getNumberOfPostedShows();

    /**
     * Checks if the password matches with the given String
     *
     * @param password string to compare the password with
     * @return true if the password matches with the given String, false otherwise
     */
    boolean passwordMatches(String password);

    /**
     * Increments the number of posted shows by one
     */
    void incrementPostedShows();
}
