package user;

public class AudienceUserClass extends AbstractOrdinaryUser implements AudienceUser {

    /**
     * Creates a new Ordinary User, being Critic or Audience type
     *
     * @param name name of the user
     */
    public AudienceUserClass(String name) {
        super(name);
    }

}
