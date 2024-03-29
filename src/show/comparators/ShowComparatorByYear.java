package show.comparators;

import show.Show;

import java.util.Comparator;

/**
 * Comparator that compares two shows by year of release
 *
 * @author Filipe Corista / João Rodrigues
 */
public class ShowComparatorByYear implements Comparator<Show> {

    @Override
    public int compare(Show show1, Show show2) {
        if (show1.getYearOfRelease() - show2.getYearOfRelease() == 0) {
            return show1.getTitle().compareTo(show2.getTitle());
        } else {
            return show2.getYearOfRelease() - show1.getYearOfRelease();
        }
    }
}
