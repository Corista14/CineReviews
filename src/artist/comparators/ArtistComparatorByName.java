package artist.comparators;

import artist.Artist;

import java.util.Comparator;

/**
 * Class that compares an artist by the name
 *
 * @author Filipe Corista / João Rodrigues
 */
public class ArtistComparatorByName implements Comparator<Artist> {

    @Override
    public int compare(Artist o1, Artist o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
