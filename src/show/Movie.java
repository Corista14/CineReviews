package show;


/**
 * Interface for the Movie class
 *
 * @author Filipe Corista / João Rodrigues
 */
public interface Movie extends Show {

    /**
     * Gets the duration, in minutes, of the movie
     *
     * @return the duration, in minutes, of the movie
     */
    int getDuration();
}
