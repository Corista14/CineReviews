package show;

import artist.Artist;
import user.AdminUser;

import java.util.Iterator;

public class SeriesClass extends AbstractShow implements Series {

    /**
     * Integer storing the number of seasons
     */
    private final int seasonNumber;

    /**
     * Creates a new series
     *
     * @param uploader           admin that uploaded the series
     * @param title              title of the series
     * @param creatorName        name of the director of the series
     * @param seasonNumber       number of seasons in the series
     * @param ageOfCertification age of certification of the series
     * @param yearOfRelease      release year of the series
     * @param genres             list of genres in the series
     * @param cast               list of artists in the series
     */
    public SeriesClass(AdminUser uploader, String title, String creatorName, int seasonNumber, String ageOfCertification, int yearOfRelease, Iterator<String> genres, Iterator<Artist> cast) {
        super(uploader, title, creatorName, ageOfCertification, yearOfRelease, genres, cast);
        this.seasonNumber = seasonNumber;
    }

    @Override
    public int getNumberOfSeasons() {
        return seasonNumber;
    }
}