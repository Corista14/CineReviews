package user;

/**
 * Class of the user
 *
 * @author Filipe Corista / João Rodrigues
 */
public class UserClass implements User {

    /**
     * Name of the user
     */
    private final String name;

    /**
     * Creates a new user
     *
     * @param name name of the user
     */
    public UserClass(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
