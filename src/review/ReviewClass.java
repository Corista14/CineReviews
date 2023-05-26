package review;

import user.CriticUser;
import user.OrdinaryUser;
import util.Classification;

/**
 * Class to implement the behavior of a Review
 *
 * @author Filipe Corista / João Rodrigues
 */
public class ReviewClass implements Review {

    private final OrdinaryUser reviewer;

    private final String classification;

    private final String description;

    /**
     * Creates a new review
     *
     * @param reviewer       ordinary user that made the review
     * @param description    description of the review
     * @param classification classification of the review (check Classification enum)
     */
    public ReviewClass(OrdinaryUser reviewer, String description, String classification) {
        this.reviewer = reviewer;
        this.description = description;
        this.classification = classification;
    }

    @Override
    public OrdinaryUser getReviewer() {
        return reviewer;
    }

    @Override
    public int getScore() {
        return Classification.valueOf(classification.toUpperCase()).getValue();
    }

    @Override
    public String getUserName() {
        return reviewer.getName();
    }

    @Override
    public boolean madeByCritic() {
        return reviewer instanceof CriticUser;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getClassification() {
        return classification;
    }
}
