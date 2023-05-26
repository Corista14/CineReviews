package user;

/**
 * Class for the critic user
 *
 * @author Filipe Corista / João Rodrigues
 */
public class CriticUserClass extends AbstractOrdinaryUser implements CriticUser {

    /**
     * Creates a new Critic user
     *
     * @param name name of the user
     */
    public CriticUserClass(String name) {
        super(name);
    }
}
