package user;

/**
 * Class for the audience user
 *
 * @author Filipe Corista / João Rodrigues
 */
public class AudienceUserClass extends AbstractOrdinaryUser implements AudienceUser {

    /**
     * Creates a new Audience User
     *
     * @param name name of the user
     */
    public AudienceUserClass(String name) {
        super(name);
    }

}
