package cinereviews;

import artist.Artist;
import artist.ArtistClass;
import artist.exceptions.AlreadyHasBioException;
import show.SeriesClass;
import show.exceptions.ShowAlreadyExistsException;
import show.MovieClass;
import show.Show;
import user.*;
import user.exceptions.*;

import java.util.*;

public class CineReviewsClass implements CineReviews {

    // Constants
    private static final String ADMIN = "admin";
    private static final String CRITIC = "critic";
    private static final String AUDIENCE = "audience";


    /**
     * Collection of users in the application
     */
    private final Map<String, User> users;
    /**
     * Collection of artist in the application
     */
    private final Map<String, Artist> artists;

    /**
     * Collection of shows in the application
     */
    private final Map<String, Show> shows;

    private int artistAdded;

    public CineReviewsClass() {
        users = new TreeMap<>();
        artists = new HashMap<>();
        shows = new TreeMap<>();
        artistAdded = 0;
    }

    @Override
    public void registerUser(String type, String name, String password) throws UnknownUserTypeException, UserAlreadyExistsException {
        switch (type) {
            case ADMIN -> createAdmin(name, password);
            case CRITIC -> createCritic(name);
            case AUDIENCE -> createAudience(name);
            default -> throw new UnknownUserTypeException();
        }
    }

    @Override
    public Iterator<User> getAllUsers() {
        return users.values().iterator();
    }

    // TODO: REFACTOR THIS METHOD BECAUSE IT'S UGLY
    @Override
    public int addMovie(String adminName, String password, String title, String director,
                        int duration, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast)
            throws NotAnAdminException, WrongPasswordException, ShowAlreadyExistsException {

        if (!users.containsKey(adminName) || (!(users.get(adminName) instanceof AdminUser user)))
            throw new NotAnAdminException();
        else {
            if (!user.passwordMatches(password)) throw new WrongPasswordException();
            else if (shows.containsKey(title)) throw new ShowAlreadyExistsException();
            else return addMovieHelper(director, duration, cast, title, user, ageCertification, releaseYear, genres);
        }
    }

    @Override
    public int addSeries(String adminName, String password, String title, String director, int seasonAmount, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast)
            throws NotAnAdminException, WrongPasswordException, ShowAlreadyExistsException {
        if (!users.containsKey(adminName) || (!(users.get(adminName) instanceof AdminUser user)))
            throw new NotAnAdminException();
        else {
            if (!user.passwordMatches(password)) throw new WrongPasswordException();
            else if (shows.containsKey(title)) throw new ShowAlreadyExistsException();
            else return addSeriesHelper(director, cast, title, seasonAmount, user, ageCertification, releaseYear, genres);
        }
    }

    @Override
    public Iterator<Show> listAllShows() {
        return shows.values().iterator();
    }

    @Override
    public void addArtistBio(String name, String dateOfBirth, String placeOfBirth) throws AlreadyHasBioException {
        if (!artists.containsKey(name)) {
            artists.put(name, new ArtistClass(name, dateOfBirth, placeOfBirth));
        } else {
            artists.get(name).addBio(dateOfBirth, placeOfBirth);
        }
    }

    @Override
    public String getArtistDateOfBirth(String artistName) {
        return artists.get(artistName).getDateOfBirth();
    }

    @Override
    public String getArtistPlaceOfBirth(String artistName) {
        return artists.get(artistName).getPlaceOfBirth();
    }

    private void createAdmin(String name, String password) throws UserAlreadyExistsException {
        if (users.containsKey(name))
            throw new UserAlreadyExistsException();
        users.put(name, new AdminUserClass(name, password));
    }

    private void createAudience(String name) throws UserAlreadyExistsException {
        if (users.containsKey(name))
            throw new UserAlreadyExistsException();
        users.put(name, new AudienceUserClass(name));
    }

    private void createCritic(String name) throws UserAlreadyExistsException {
        if (users.containsKey(name))
            throw new UserAlreadyExistsException();
        users.put(name, new CriticUserClass(name));
    }

    private List<Artist> convertToArtist(Iterator<String> it) {
        List<Artist> newArtists = new ArrayList<>();
        while (it.hasNext()) {
            String artist = it.next();
            if (!this.artists.containsKey(artist)) {
                this.artists.put(artist, new ArtistClass(artist));
                artistAdded++;
            }
            newArtists.add(this.artists.get(artist));
        }
        return newArtists;
    }

    private void addDirectorToArtists(String director) {
        if (!artists.containsKey(director)) {
            artists.put(director, new ArtistClass(director));
            artistAdded++;
        }
    }

    private void addShowToCast(List<Artist> cast, String title) {
        for (Artist next : cast) {
            next.addShow(shows.get(title));
        }
    }

    private int addSeriesHelper(String director, Iterator<String> cast, String title, int seasonNumber, AdminUser user, String ageCertification, int releaseYear, Iterator<String> genres) {
        addDirectorToArtists(director);
        List<Artist> convertedCast = this.convertToArtist(cast);
        shows.put(title, new SeriesClass(user, title, director, seasonNumber, ageCertification, releaseYear, genres, convertedCast.iterator()));
        addShowToCast(convertedCast, title);
        artists.get(director).addShow(shows.get(title));
        user.addedShow();

        int lastAdded = artistAdded;
        artistAdded = 0;
        return lastAdded;
    }

    private int addMovieHelper(String director, int duration, Iterator<String> cast, String title, AdminUser user, String ageCertification, int releaseYear, Iterator<String> genres) {
        addDirectorToArtists(director);
        List<Artist> convertedCast = this.convertToArtist(cast);
        shows.put(title, new MovieClass(user, title, director, duration, ageCertification, releaseYear, genres, convertedCast.iterator()));
        addShowToCast(convertedCast, title);
        artists.get(director).addShow(shows.get(title));
        user.addedShow();

        int lastAdded = artistAdded;
        artistAdded = 0;
        return lastAdded;
    }

}
