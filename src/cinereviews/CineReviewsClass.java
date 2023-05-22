package cinereviews;

import artist.Artist;
import cinereviews.exceptions.NoUsersException;
import show.Show;
import user.AdminUserClass;
import user.AudienceUserClass;
import user.CriticUserClass;
import user.User;
import user.exceptions.UnknownUserTypeException;
import user.exceptions.UserAlreadyExistsException;

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

    public CineReviewsClass() {
        users = new TreeMap<>();
        artists = new HashMap<>();
        shows = new TreeMap<>();
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
    public Iterator<User> getAllUsers(){
        return users.values().iterator();
    }

    @Override
    public void addMovie(String AdminName, String password, String title, String director, int duration, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast) {

    }

    @Override
    public void addSeries(String AdminName, String password, String title, String creator, int seasonAmount, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast) {

    }

    @Override
    public Iterator<Show> listAllShows() {
        return null;
    }

    @Override
    public void addArtistBio(String name, String dateOfBirth, String placeOfBirth) {

    }

    @Override
    public String getArtistDateOfBirth(String artistName) {
        return null;
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

}
