package review.comparators;

import review.Review;

import java.util.Comparator;

/**
 * Comparator that compares two reviews
 *
 * @author Filipe Corista / João Rodrigues
 */
public class ReviewComparator implements Comparator<Review> {

    private static final int POSITIVE_NUMBER = 1;
    private static final int NEGATIVE_NUMBER = -1;

    @Override
    public int compare(Review r1, Review r2) {
        if (r1.getReviewer().getClass() == r2.getReviewer().getClass()) {
            if (r1.getScore() == r2.getScore())
                return r1.getUserName().compareTo(r2.getUserName());
            else
                return r2.getScore() - r1.getScore();
        } else {
            if (r1.madeByCritic())
                return NEGATIVE_NUMBER;
            else
                return POSITIVE_NUMBER;
        }
    }
}
