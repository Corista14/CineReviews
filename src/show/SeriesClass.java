package show;

import artist.Artist;
import user.AdminUser;

import java.util.Iterator;

public class SeriesClass extends AbstractShow implements Series {

    private int seasonNumber;

    /**
     * Creates a new Series
     *
     * @param title              name of the show
     * @param creatorName        name of the creator of the show
     * @param ageOfCertification age of certification of the show
     * @param yearOfRelease      the show's year of release
     * @param genres             a collection of genres of the show
     * @param cast               a collection of the cast of the show
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
