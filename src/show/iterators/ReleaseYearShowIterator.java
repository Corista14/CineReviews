package show.iterators;

import show.Show;
import show.ShowComparatorByScore;

import java.util.*;

public class ReleaseYearShowIterator implements Iterator<Show> {

    private final Iterator<Show> it;

    public ReleaseYearShowIterator(Iterator<Show> it, int releaseYear) {
        SortedSet<Show> showList = new TreeSet<>(new ShowComparatorByScore());
        while (it.hasNext()) {
            Show next = it.next();
            if (next.getYearOfRelease() == releaseYear)
                showList.add(next);
        }
        this.it = showList.iterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public Show next() {
        return it.next();
    }
}
