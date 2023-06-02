package show.comparators;

import show.Show;

import java.util.Comparator;

/**
 * Comparator that compares two shows by score
 *
 * @author Filipe Corista / João Rodrigues
 */
public class ShowComparatorByScore implements Comparator<Show> {
    @Override
    public int compare(Show o1, Show o2) {
        if (o1.getScore() == o2.getScore()) {
            if(o1.getYearOfRelease()==o2.getYearOfRelease()){
                return o1.getTitle().compareTo(o2.getTitle());
            }else {
                return o2.getYearOfRelease()- o1.getYearOfRelease();
            }
        }else {
            if(o1.getScore()>o2.getScore()){
                return -1;
            }else{
                return 1;
            }
        }
    }
}
