package user;

/**
 * Class that implements the behavior of an Admin
 *
 * @author Filipe Corista / João Rodrigues
 */
public class AdminUserClass extends UserClass implements AdminUser {

    /**
     * String storing the password of the user
     */
    private final String password;

    /**
     * Integer storing the number of posted shows
     */
    private int addedShowAmount;

    /**
     * Creates a new Admin user
     *
     * @param name     name of the user
     * @param password password of the user
     */
    public AdminUserClass(String name, String password) {
        super(name);
        this.password = password;
        addedShowAmount = 0;
    }

    @Override
    public boolean passwordMatches(String password) {
        return password.equals(this.password);
    }

    @Override
    public int getNumberOfPostedShows() {
        return addedShowAmount;
    }

    @Override
    public void incrementPostedShows() {
        addedShowAmount++;
    }
}
