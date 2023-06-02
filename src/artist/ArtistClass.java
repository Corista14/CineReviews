package artist;

import artist.exceptions.AlreadyHasBioException;
import show.Show;
import show.comparators.ShowComparatorByYear;

import java.util.*;

/**
 * Class that handles the Artist behavior
 *
 * @author Filipe Corista / João Rodrigues
 */
public class ArtistClass implements Artist {

    private static final int INITIAL_COLLAB_NUM = 1;
    private static final int COLLAB_INCREMENTER = 1;

    /**
     * String storing the name of the artist
     */
    private final String name;

    /**
     * String storing the date of birth of the artist
     */
    private String dateOfBirth;

    /**
     * String storing the place of birth of the artist
     */
    private String placeOfBirth;

    /**
     * boolean storing true if the artist has a bio, false otherwise
     */
    private boolean hasBio;

    /**
     * Collection of the shows that the artist participated in sorted by year
     */
    private final SortedSet<Show> shows;

    /**
     * Collection of times an artist has worked with this artist.
     * Artist -> number of times has worked with this artist
     */
    private final Map<Artist, Integer> cooperatedTimes;

    /**
     * Collection of artists this artist has worked with
     */
    private final Set<Artist> friends;

    /**
     * Integer storing the maximum number of times this artist has collaborated with some other artist
     */
    private int mostTimesCollaborated;

    /**
     * Creates a new artist without any bio
     *
     * @param name name of the artist
     */
    public ArtistClass(String name) {
        this.name = name;
        this.hasBio = false;
        cooperatedTimes = new HashMap<>();
        friends = new HashSet<>();
        shows = new TreeSet<>(new ShowComparatorByYear());
    }

    /**
     * Creates a new artist with bio
     *
     * @param name         name of the artist
     * @param dateOfBirth  date of birth of the artist
     * @param placeOfBirth place of birth of the artist
     */
    public ArtistClass(String name, String dateOfBirth, String placeOfBirth) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.hasBio = true;
        cooperatedTimes = new HashMap<>();
        friends = new HashSet<>();
        shows = new TreeSet<>(new ShowComparatorByYear());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addBio(String dateOfBirth, String placeOfBirth) throws AlreadyHasBioException {
        if (hasBio) throw new AlreadyHasBioException();
        else {
            this.placeOfBirth = placeOfBirth;
            this.dateOfBirth = dateOfBirth;
            this.hasBio = true;
        }
    }

    @Override
    public Iterator<Show> getShows() {
        return shows.iterator();
    }

    @Override
    public boolean hasBio() {
        return hasBio;
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    @Override
    public void addShow(Show show) {
        shows.add(show);
        Iterator<Artist> it = show.getCastWithDirector();
        while (it.hasNext()) {
            Artist artist = it.next();
            if (!artist.equals(this)) {
                if (hasArtistAsFriend(artist)) updateCooperatedTimes(artist);
                else addArtistToFriends(artist);
            }
        }
    }

    @Override
    public int getMostTimesWorked() {
        return mostTimesCollaborated;
    }

    @Override
    public Iterator<Artist> getFriends() {
        return friends.iterator();
    }

    @Override
    public int compareTo(Artist o) {
        return this.getName().compareTo(o.getName());
    }

    private boolean hasArtistAsFriend(Artist artist) {
        return cooperatedTimes.containsKey(artist);
    }

    private void updateCooperatedTimes(Artist artist) {
        int temp = cooperatedTimes.get(artist) + COLLAB_INCREMENTER;
        cooperatedTimes.put(artist, temp);
        if (temp == mostTimesCollaborated) {
            friends.add(artist);
        }
        if (temp > mostTimesCollaborated) {
            friends.clear();
            friends.add(artist);
            mostTimesCollaborated = temp;
        }
    }

    private void addArtistToFriends(Artist artist) {
        cooperatedTimes.put(artist, INITIAL_COLLAB_NUM);
        if (mostTimesCollaborated <= INITIAL_COLLAB_NUM) {
            friends.add(artist);
            mostTimesCollaborated = INITIAL_COLLAB_NUM;
        }
    }
}
