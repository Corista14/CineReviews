package review;

import user.CriticUser;
import user.OrdinaryUser;

public class ReviewClass implements Review {

    private final OrdinaryUser reviewer;

    private int score;


    public ReviewClass(OrdinaryUser reviewer,int score) {
        this.reviewer = reviewer;
        this.score=score;
    }

    @Override
    public OrdinaryUser getReviewer() {
        return reviewer;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getUserName() {
        return reviewer.getName();
    }

    @Override
    public boolean madeByCritic() {
        return reviewer instanceof CriticUser;
    }
}
