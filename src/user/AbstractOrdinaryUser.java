package user;

/**
 * Abstract class for the user
 *
 * @author Filipe Corista / João Rodrigues
 */
public abstract class AbstractOrdinaryUser extends UserClass implements OrdinaryUser {

    private static final String AUDIENCE = "audience";
    private static final String CRITIC = "critic";

    /**
     * Stores the number of reviews of the
     */
    private int reviewCount;

    /**
     * Creates a new Ordinary User, being Critic or Audience type
     *
     * @param name name of the user
     */
    public AbstractOrdinaryUser(String name) {
        super(name);
        reviewCount = 0;
    }

    @Override
    public int getReviewsCount() {
        return reviewCount;
    }

    @Override
    public void incrementReviewCount() {
        reviewCount++;
    }

    @Override
    public String getUserType() {
        if (this instanceof CriticUser) return CRITIC;
        else return AUDIENCE;
    }
}
