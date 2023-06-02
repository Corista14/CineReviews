package show;

import artist.Artist;

import java.util.Iterator;

/**
 * Class that implements the behavior of a Series
 *
 * @author Filipe Corista / João Rodrigues
 */
public class SeriesClass extends AbstractShow implements Series {

    /**
     * Integer storing the number of seasons
     */
    private final int seasonNumber;

    /**
     * Creates a new series
     *
     * @param title              title of the series
     * @param creator            the director of the series
     * @param seasonNumber       number of seasons in the series
     * @param ageOfCertification age of certification of the series
     * @param yearOfRelease      release year of the series
     * @param genres             list of genres in the series
     * @param cast               list of artists in the series
     */
    public SeriesClass(String title, Artist creator, int seasonNumber, String ageOfCertification, int yearOfRelease, Iterator<String> genres, Iterator<Artist> cast) {
        super(title, creator, ageOfCertification, yearOfRelease, genres, cast);
        this.seasonNumber = seasonNumber;
    }

    @Override
    public int getNumberOfSeasons() {
        return seasonNumber;
    }
}
