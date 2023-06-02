package cinereviews.comparators;

import artist.Artist;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * Class that compares two sets of artists by size.
 * In case of a draw, it should compare all artists in the size by the name
 *
 * @author Filipe Corista / João Rodrigues
 */
public class SetArtistComparator implements Comparator<Set<Artist>> {
    @Override
    public int compare(Set<Artist> o1, Set<Artist> o2) {
        int comp = o2.size() - o1.size();
        if (comp == 0) {
            Iterator<Artist> it1 = o1.iterator();
            Iterator<Artist> it2 = o2.iterator();
            while (it1.hasNext() && it2.hasNext() && comp == 0)
                comp = it1.next().compareTo(it2.next());
        }
        return comp;
    }
}