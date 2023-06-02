package cinereviews;

import artist.Artist;
import artist.ArtistClass;
import artist.comparators.ArtistComparatorByName;
import artist.exceptions.AlreadyHasBioException;
import artist.exceptions.UnknownArtistException;
import cinereviews.comparators.SetArtistComparator;
import cinereviews.exceptions.*;
import review.Review;
import review.ReviewClass;
import review.exceptions.UserAlreadyReviewedException;
import show.*;
import show.comparators.ShowComparatorByScore;
import show.exceptions.ShowAlreadyExistsException;
import show.exceptions.UnknownShowException;
import user.*;
import user.exceptions.*;

import java.util.*;

/**
 * System class of the Cine Reviews application
 *
 * @author Filipe Corista / João Rodrigues
 */
public class CineReviewsClass implements CineReviews {

    // Constants
    private static final String ADMIN = "admin";
    private static final String CRITIC = "critic";
    private static final String AUDIENCE = "audience";

    /**
     * Collection of users in the application. username -> user
     */
    private final SortedMap<String, User> users;

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
    private final SortedMap<Integer, Set<Show>> showsByYear;

    /**
     * Collection of Artists that have never collaborated with the artist whose name is the key
     */
    private final Map<String, Set<Artist>> avoiders;

    /**
     *  Number of artist that were added the last time a show was created
     */
    private int artistAdded;

    /**
     * Size of the largest avoiders group the last time avoiders was called if it was never called it is 0
     */
    private int lastAvoidersSize;

    /**
     * Creates a new CineReviews app
     */
    public CineReviewsClass() {
        users = new TreeMap<>();
        artists = new HashMap<>();
        shows = new TreeMap<>();
        avoiders = new HashMap<>();
        showsByYear = new TreeMap<>();
        artistAdded = 0;
        lastAvoidersSize = 0;
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
            avoiders.put(name, new TreeSet<>(new ArtistComparatorByName()));
            for (Artist artist : artists.values()) {
                avoiders.get(name).add(artist);
                avoiders.get(artist.getName()).add(artists.get(name));
            }
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
        if (artists.isEmpty()) throw new NoArtistException();

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
        if (temp.isEmpty()) throw new NoCollaborationsException();
        return temp.iterator();
    }

    @Override
    public Iterator<Artist> getFriendsOf(String name) {
        Set<Artist> temp = new TreeSet<>(new ArtistComparatorByName());
        Iterator<Artist> it = artists.get(name).getFriends();
        while (it.hasNext()) {
            Artist artist = it.next();
            if (name.compareTo(artist.getName()) < 0)
                temp.add(artist);
        }
        return temp.iterator();
    }

    @Override
    public int getLastAvoidersSize() {
        return lastAvoidersSize;
    }

    @Override
    public Iterator<Set<Artist>> getAvoiders() throws NoArtistException {
        if (artists.isEmpty()) throw new NoArtistException();

        int maxSize = 0;
        Set<Set<Artist>> max_set = new TreeSet<>(new SetArtistComparator());
        for (Artist artist : artists.values()) {
            Set<TreeSet<Artist>> avoidersTry = getAvoidersHelper(artist, new TreeSet<>(new ArtistComparatorByName()));
            for (Set<Artist> set : avoidersTry) {
                if (set.size() == maxSize) {
                    max_set.add(set);
                }
                if (set.size() > maxSize && set.size() != 1) {
                    maxSize = set.size();
                    max_set.clear();
                    max_set.add(set);
                }
            }
        }
        lastAvoidersSize = maxSize;
        return max_set.iterator();
    }

    /**
     * Creates a new Admin and adds it to the users collection
     *
     * @param name     name of the admin
     * @param password password to the admin account
     * @throws UserAlreadyExistsException is thrown if the user already exists inside the collection
     */
    private void createAdmin(String name, String password) throws UserAlreadyExistsException {
        if (users.containsKey(name)) throw new UserAlreadyExistsException();
        users.put(name, new AdminUserClass(name, password));
    }

    /**
     * Creates a new Audience user and adds it to the user collection
     *
     * @param name name of the user
     * @throws UserAlreadyExistsException is thrown if the user already exists inside the collection
     */
    private void createAudience(String name) throws UserAlreadyExistsException {
        if (users.containsKey(name)) throw new UserAlreadyExistsException();
        users.put(name, new AudienceUserClass(name));
    }

    /**
     * Creates a new Critic user and adds it to the user collection
     *
     * @param name namme of the user
     * @throws UserAlreadyExistsException is thrown if the user already exists inside the collection
     */
    private void createCritic(String name) throws UserAlreadyExistsException {
        if (users.containsKey(name)) throw new UserAlreadyExistsException();
        users.put(name, new CriticUserClass(name));
    }

    /**
     * Converts all the Strings in an iterator to artists and adds them to the collection
     * if they were not already present
     *
     * @param it iterator with all the names that will be converted
     * @return a list with all the names turned into artists
     */
    private List<Artist> convertToArtist(Iterator<String> it) {
        List<Artist> newArtists = new ArrayList<>();
        while (it.hasNext()) {
            String artist = it.next();
            if (!this.artists.containsKey(artist)) {
                avoiders.put(artist, new TreeSet<>(new ArtistComparatorByName()));
                this.artists.put(artist, new ArtistClass(artist));
                for (Artist newArtist : artists.values()) {
                    avoiders.get(newArtist.getName()).add(artists.get(artist));
                }
                artistAdded++;
            }
            newArtists.add(this.artists.get(artist));
        }

        return newArtists;
    }

    /**
     * Adds the director into the artist collection, for the purposes of the avoiders method
     * he is removed from the avoiders of everyone in his show, and everyone on his show
     * are removed form is avoiders
     *
     * @param director director that will be added to the artist collection
     * @param cast     cast of the movie that the director is on
     */
    private void addDirectorToArtists(String director, List<Artist> cast) {
        if (!avoiders.containsKey(director))
            avoiders.put(director, new TreeSet<>(new ArtistComparatorByName()));

        if (!artists.containsKey(director)) {
            artists.put(director, new ArtistClass(director));
            artistAdded++;
        }
        for (Artist art : cast) {
            avoiders.get(director).remove(art);
            avoiders.get(art.getName()).remove(artists.get(director));
        }
    }

    /**
     * Adds the show to his cast, additionally, removes all the artists on the show from
     * each other avoiders
     *
     * @param cast  list of the cast of the show
     * @param title title of the show
     */
    private void addShowToCast(List<Artist> cast, String title) {
        for (Artist next : cast) {
            next.addShow(shows.get(title));
            for (Artist artist : cast) {
                avoiders.get(next.getName()).remove(artist);
            }
        }
    }

    /**
     * Adds a new series to the show collection
     *
     * @param director         director of the show
     * @param cast             cast of the show
     * @param title            title of the show
     * @param seasonNumber     number of seasons of the show
     * @param user             admin that added the show
     * @param ageCertification age certification of the sho
     * @param releaseYear      release year of the show
     * @param genres           genres of the show
     * @return the amount of artists that were created by creating the show
     */
    private int addSeriesHelper(String director, Iterator<String> cast, String title, int seasonNumber, AdminUser
            user, String ageCertification, int releaseYear, Iterator<String> genres) {

        List<Artist> convertedCast = this.convertToArtist(cast);
        addDirectorToArtists(director, convertedCast);
        shows.put(title, new SeriesClass(title, artists.get(director), seasonNumber, ageCertification, releaseYear, genres, convertedCast.iterator()));

        addShowToCast(convertedCast, title);
        updateShowsByYear(releaseYear, title);

        artists.get(director).addShow(shows.get(title));
        user.incrementPostedShows();
        int lastAdded = artistAdded;
        artistAdded = 0;
        return lastAdded;
    }

    /**
     * Adds a new movie to the show collection
     *
     * @param director         director of the show
     * @param duration         duration of the show
     * @param cast             cast of the show
     * @param title            title of the show
     * @param user             admin that added the show
     * @param ageCertification age certification of the show
     * @param releaseYear      release year of the show
     * @param genres           genres of the show
     * @return the amount of artists that were created by creating the show
     */
    private int addMovieHelper(String director, int duration, Iterator<String> cast, String title, AdminUser
            user, String ageCertification, int releaseYear, Iterator<String> genres) {

        List<Artist> convertedCast = this.convertToArtist(cast);
        addDirectorToArtists(director, convertedCast);
        shows.put(title, new MovieClass(title, artists.get(director), duration, ageCertification, releaseYear, genres, convertedCast.iterator()));

        addShowToCast(convertedCast, title);
        updateShowsByYear(releaseYear, title);

        artists.get(director).addShow(shows.get(title));
        user.incrementPostedShows();
        int lastAdded = artistAdded;
        artistAdded = 0;
        return lastAdded;
    }

    /**
     * Updates the shows by year
     *
     * @param releaseYear release year of the show
     * @param title       name of the show
     */
    private void updateShowsByYear(int releaseYear, String title) {
        if (!showsByYear.containsKey(releaseYear))
            showsByYear.put(releaseYear, new HashSet<>());
        showsByYear.get(releaseYear).add(shows.get(title));
    }

    /**
     * Gets the avoiders in the app
     *
     * @param currentArtist current artist in the recursion
     * @param artistSet     sets of artist in the recursion
     * @return the avoiders in the app
     */
    private Set<TreeSet<Artist>> getAvoidersHelper(Artist currentArtist, Set<Artist> artistSet) {
        Set<Artist> commonArtists = new HashSet<>();
        Set<Artist> avoiders = this.avoiders.get(currentArtist.getName());

        updateCommonArtists(avoiders, artistSet, currentArtist, commonArtists);
        artistSet.add(currentArtist);

        if (commonArtists.isEmpty()) return getAvoidersEncapsulator(artistSet);

        Set<TreeSet<Artist>> max_set = new HashSet<>();
        updateMaxSet(commonArtists, artistSet, max_set);

        return max_set;
    }

    /**
     * Updates the common artists
     *
     * @param avoiders      the list of the current avoiders
     * @param artistSet     the current set of the recursion
     * @param currentArtist the current artist of the recursion
     * @param commonArtists list of the current common artists
     */
    private void updateCommonArtists(Set<Artist> avoiders, Set<Artist> artistSet, Artist currentArtist, Set<Artist> commonArtists) {
        for (Artist artist : avoiders) {
            boolean valid = true;
            Iterator<Artist> artistIt = artistSet.iterator();

            while (artistIt.hasNext() && valid) {
                Artist nextArt = artistIt.next();
                if (!this.avoiders.get(nextArt.getName()).contains(artist))
                    valid = false;
            }

            if (valid && currentArtist.getName().compareTo(artist.getName()) > 0) {
                commonArtists.add(artist);
            }
        }
    }

    /**
     * Gets the encapsulator of the avoiders
     *
     * @param artistSet the current set of the recursion
     * @return the encapsulator of the avoiders
     */
    private Set<TreeSet<Artist>> getAvoidersEncapsulator(Set<Artist> artistSet) {
        Set<TreeSet<Artist>> encapsulator = new HashSet<>();
        TreeSet<Artist> tree = new TreeSet<>(new ArtistComparatorByName());
        tree.addAll(artistSet);
        encapsulator.add(tree);
        return encapsulator;
    }

    /**
     * Updates the max set of avoiders
     *
     * @param artistSet     the current set of the recursion
     * @param commonArtists list of the current common artists
     * @param max_set       max set of avoiders
     */
    private void updateMaxSet(Set<Artist> commonArtists, Set<Artist> artistSet, Set<TreeSet<Artist>> max_set) {
        int maxSize = 0;
        for (Artist commonArtist : commonArtists) {
            Set<TreeSet<Artist>> avoidersTry = getAvoidersHelper(commonArtist, new HashSet<>(artistSet));
            for (TreeSet<Artist> set : avoidersTry) {
                if (set.size() == maxSize) {
                    max_set.add(set);
                }
                if (set.size() > maxSize && set.size() != 1) {
                    max_set.clear();
                    maxSize = set.size();
                    max_set.add(set);
                }
            }
        }
    }

}
