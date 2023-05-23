package show;

import artist.Artist;
import user.AdminUser;

import java.util.Iterator;

public class MovieClass extends AbstractShow implements Movie {

    /**
     * Integer storing the duration, in minutes, of the movie
     */
    private final int duration;

    /**
     * Creates a new series
     *
     * @param uploader           admin that uploaded the movie
     * @param title              title of the movie
     * @param creatorName        name of the director of the movie
     * @param duration           duration, in minutes, of the movie
     * @param ageOfCertification age of certification of the movie
     * @param yearOfRelease      release year of the movie
     * @param genres             list of genres in the movie
     * @param cast               list of artists in the movie
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
