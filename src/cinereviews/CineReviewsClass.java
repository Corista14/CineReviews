package cinereviews;

import artist.Artist;
import artist.ArtistClass;
import artist.ArtistComparatorByName;
import artist.exceptions.AlreadyHasBioException;
import artist.exceptions.UnknownArtistException;
import cinereviews.exceptions.*;
import review.Review;
import review.ReviewClass;
import review.exceptions.UserAlreadyReviewedException;
import show.*;
import show.exceptions.ShowAlreadyExistsException;
import show.exceptions.UnknownShowException;
import user.*;
import user.exceptions.*;
import util.Classification;

import java.util.*;

public class CineReviewsClass implements CineReviews {

    // Constants
    private static final String ADMIN = "admin";
    private static final String CRITIC = "critic";
    private static final String AUDIENCE = "audience";

    /**
     * Collection of users in the application. username -> user
     */
    private final Map<String, User> users;
    /**
     * Collection of artist in the application. name -> artist
     */
    private final Map<String, Artist> artists;

    /**
     * Collection of shows in the application. name -> show
     */
    private final Map<String, Show> shows;

    /**
     * Collection of shows ordered primarily by score. releaseYear -> collection of shows
     */
    private final Map<Integer, SortedSet<Show>> showsByYear;

    private int artistAdded;

    /**
     * Creates a new CineReviews app
     */
    public CineReviewsClass() {
        users = new TreeMap<>();
        artists = new HashMap<>();
        shows = new TreeMap<>();
        showsByYear = new HashMap<>();
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

    @Override
    public int addMovie(String adminName, String password, String title, String director,
                        int duration, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast) throws NotAnAdminException,
            WrongPasswordException, ShowAlreadyExistsException {

        if (!users.containsKey(adminName) || (!(users.get(adminName) instanceof AdminUser user)))
            throw new NotAnAdminException();
        if (!user.passwordMatches(password)) throw new WrongPasswordException();
        if (shows.containsKey(title)) throw new ShowAlreadyExistsException();

        return addMovieHelper(director, duration, cast, title, user, ageCertification, releaseYear, genres);
    }

    @Override
    public int addSeries(String adminName, String password, String title, String director, int seasonAmount, String ageCertification, int releaseYear, Iterator<String> genres, Iterator<String> cast)
            throws NotAnAdminException, WrongPasswordException, ShowAlreadyExistsException {
        if (!users.containsKey(adminName) || (!(users.get(adminName) instanceof AdminUser user)))
            throw new NotAnAdminException();
        if (!user.passwordMatches(password)) throw new WrongPasswordException();
        if (shows.containsKey(title)) throw new ShowAlreadyExistsException();

        return addSeriesHelper(director, cast, title, seasonAmount, user, ageCertification, releaseYear, genres);
    }

    @Override
    public Iterator<Show> listAllShows() {
        return shows.values().iterator();
    }

    @Override
    public boolean addArtistBio(String name, String dateOfBirth, String placeOfBirth) throws AlreadyHasBioException {
        boolean wasCreated;
        if (!artists.containsKey(name)) {
            artists.put(name, new ArtistClass(name, dateOfBirth, placeOfBirth));
            wasCreated = true;
        } else {
            artists.get(name).addBio(dateOfBirth, placeOfBirth);
            wasCreated = false;
        }
        return wasCreated;
    }

    @Override
    public Iterator<Show> getArtistCredits(String artistName) throws UnknownArtistException {
        if (!artists.containsKey(artistName)) throw new UnknownArtistException();
        return artists.get(artistName).getShows();
    }

    @Override
    public boolean artistHasBio(String artist) {
        return artists.get(artist).hasBio();
    }

    @Override
    public String getDateOfBirthOfArtist(String artist) {
        return artists.get(artist).getDateOfBirth();
    }

    @Override
    public String getPlaceOfBirthOfArtist(String artist) {
        return artists.get(artist).getPlaceOfBirth();
    }

    @Override
    public String getArtistRole(String artistName, String show) {
        return shows.get(show).getArtistRole(artistName);
    }

    @Override
    public int reviewShow(String username, String review, String showName, String score) throws
            UnknownUserException, IsAdminException, UnknownShowException, UserAlreadyReviewedException {

        if (!users.containsKey(username)) throw new UnknownUserException();
        if (users.get(username) instanceof AdminUser) throw new IsAdminException();
        if (!shows.containsKey(showName)) throw new UnknownShowException();
        if (shows.get(showName).userHasReviewed(users.get(username))) throw new UserAlreadyReviewedException();

        Show show = shows.get(showName);
        show.addReview(new ReviewClass((OrdinaryUser) users.get(username), review, score));
        ((OrdinaryUser) users.get(username)).incrementReviewCount();
        return show.getReviewsCount();
    }

    @Override
    public Iterator<Review> getReviewsOfShow(String showName) throws UnknownShowException {
        if (!shows.containsKey(showName)) throw new UnknownShowException();
        return shows.get(showName).getReviews();
    }

    @Override
    public float getScoreOfShow(String showName) {
        return shows.get(showName).getScore();
    }

    @Override
    public Iterator<Show> getShowsByYear(int year) {
        // Create a new set so that we can give the ordered tree set by score
        SortedSet<Show> newSet = new TreeSet<>(new ShowComparatorByScore());
        if (showsByYear.containsKey(year)) {
            newSet.addAll(showsByYear.get(year));
        }
        return newSet.iterator();
    }

    @Override
    public Iterator<Show> getShowsByGenre(Iterator<String> genres) {
        Set<Show> temp = new HashSet<>();
        SortedSet<Show> result = new TreeSet<>(new ShowComparatorByScore());
        for (Show show : shows.values()) {
            temp.add(show);
            result.add(show);
        }
        while (genres.hasNext()) {
            String genre = genres.next();
            for (Show show : temp) {
                if (!show.hasGenre(genre)) {
                    result.remove(show);
                }
            }
        }
        return result.iterator();
    }

    @Override
    public Iterator<Artist> getAllFriends() throws NoArtistException, NoCollaborationsException {
        if (!artists.isEmpty()) {
            Set<Artist> temp = new TreeSet<>(new ArtistComparatorByName());
            int maxFriend = 0;
            for (Artist artist : artists.values()) {
                if (artist.getMostTimesWorked() == maxFriend && artist.getMostTimesWorked() > 0) {
                    temp.add(artist);
                }
                if (artist.getMostTimesWorked() > maxFriend) {
                    temp.clear();
                    temp.add(artist);
                    maxFriend = artist.getMostTimesWorked();
                }
            }
            if (temp.isEmpty()) {
                throw new NoCollaborationsException();
            }
            return temp.iterator();
        } else {
            throw new NoArtistException();
        }
    }

    @Override
    public Iterator<Artist> getFriendsOf(String name) {
        Set<Artist> temp = new TreeSet<>(new ArtistComparatorByName());
        Iterator<Artist> it = artists.get(name).getFriends();
        while (it.hasNext()) {
            Artist artist = it.next();
            if (name.compareTo(artist.getName()) < 0) {
                temp.add(artist);
            }
        }
        return temp.iterator();
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
        shows.put(title, new SeriesClass(user, title, artists.get(director), seasonNumber, ageCertification, releaseYear, genres, convertedCast.iterator()));
        addShowToCast(convertedCast, title);
        if (!showsByYear.containsKey(releaseYear)) {
            showsByYear.put(releaseYear, new TreeSet<>(new ShowComparatorByScore()));
        }
        showsByYear.get(releaseYear).add(shows.get(title));
        artists.get(director).addShow(shows.get(title));
        user.incrementPostedShows();

        int lastAdded = artistAdded;
        artistAdded = 0;
        return lastAdded;
    }

    private int addMovieHelper(String director, int duration, Iterator<String> cast, String title, AdminUser user, String ageCertification, int releaseYear, Iterator<String> genres) {
        addDirectorToArtists(director);
        List<Artist> convertedCast = this.convertToArtist(cast);
        shows.put(title, new MovieClass(user, title, artists.get(director), duration, ageCertification, releaseYear, genres, convertedCast.iterator()));
        addShowToCast(convertedCast, title);
        if (!showsByYear.containsKey(releaseYear)) {
            showsByYear.put(releaseYear, new TreeSet<>(new ShowComparatorByScore()));
        }
        showsByYear.get(releaseYear).add(shows.get(title));
        artists.get(director).addShow(shows.get(title));
        user.incrementPostedShows();
        int lastAdded = artistAdded;
        artistAdded = 0;
        return lastAdded;
    }

}
