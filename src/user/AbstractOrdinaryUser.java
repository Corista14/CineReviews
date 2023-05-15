package user;

/**
 * Abstract class for the user
 *
 * @author Filipe Corista / João Rodrigues
 */
public abstract class AbstractOrdinaryUser implements OrdinaryUser {

    /**
     * Stores the number of reviews of the
     */
    private int reviewCount;

    /**
     * Stores the name of the user
     */
    private final String name;

    /**
     * Creates a new Ordinary User, being Critic or Audience type
     *
     * @param name name of the user
     */
    public AbstractOrdinaryUser(String name) {
        this.name = name;
        reviewCount = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getReviewsCount() {
        return reviewCount;
    }
}