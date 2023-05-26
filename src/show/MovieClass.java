package show;

import artist.Artist;
import user.AdminUser;

import java.util.Iterator;

/**
 * Class that implements the behavior of a Movie
 *
 * @author Filipe Corista / João Rodrigues
 */
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
     * @param creator            the director of the movie
     * @param duration           duration, in minutes, of the movie
     * @param ageOfCertification age of certification of the movie
     * @param yearOfRelease      release year of the movie
     * @param genres             list of genres in the movie
     * @param cast               list of artists in the movie
     */
    public MovieClass(AdminUser uploader, String title, Artist creator, int duration, String ageOfCertification, int yearOfRelease, Iterator<String> genres, Iterator<Artist> cast) {
        super(uploader, title, creator, ageOfCertification, yearOfRelease, genres, cast);
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
