package user;

public class CriticUserClass extends AbstractOrdinaryUser implements CriticUser {

    /**
     * Creates a new Ordinary User, being Critic or Audience type
     *
     * @param name name of the user
     */
    public CriticUserClass(String name) {
        super(name);
    }
}
