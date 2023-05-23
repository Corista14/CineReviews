package show;

import artist.Artist;
import user.AdminUser;

import java.util.Iterator;

public class MovieClass extends AbstractShow implements Movie {

    private int duration;

    /**
     * Creates a new Movie
     *
     * @param title              name of the show
     * @param creatorName        name of the creator of the show
     * @param ageOfCertification age of certification of the show
     * @param yearOfRelease      the show's year of release
     * @param genres             a collection of genres of the show
     * @param cast               a collection of the cast of the show
     */
    public MovieClass(AdminUser uploader, String title, String creatorName, int duration, String ageOfCertification, int yearOfRelease, Iterator<String> genres, Iterator<Artist> cast) {
        super(uploader, title, creatorName, ageOfCertification, yearOfRelease, genres, cast);
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
