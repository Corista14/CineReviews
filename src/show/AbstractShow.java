package show;

import artist.Artist;
import review.Review;
import review.comparators.ReviewComparator;
import user.User;

import java.util.*;

/**
 * Class that implements the behavior of a Show
 *
 * @author Filipe Corista / João Rodrigues
 */
public abstract class AbstractShow implements Show {

    private static final String DIRECTOR = "director";
    private static final String CREATOR = "creator";
    private static final String ACTOR = "actor";

    /**
     * Map to store the reviews. Username -> Review
     */
    private final Map<String, Review> reviews;
    private final Set<Review> sortedReviews;
    private final String title;
    private final Artist creator;
    private final String ageOfCertification;
    private final int yearOfRelease;
    private final List<String> genres;
    private final List<Artist> cast;


    /**
     * Creates a new show, being Movie or Series
     *
     * @param title              name of the show
     * @param creator            creator of the show
     * @param ageOfCertification age of certification of the show
     * @param yearOfRelease      the show's year of release
     * @param genres             a collection of genres of the show
     * @param cast               a collection of the cast of the show
     */
    public AbstractShow(String title, Artist creator, String ageOfCertification,
                        int yearOfRelease, Iterator<String> genres, Iterator<Artist> cast) {
        this.title = title;
        this.creator = creator;
        this.ageOfCertification = ageOfCertification;
        this.yearOfRelease = yearOfRelease;
        this.genres = new ArrayList<>();
        this.cast = new ArrayList<>();
        this.sortedReviews = new TreeSet<>(new ReviewComparator());
        addCast(cast);
        addGenres(genres);
        reviews = new HashMap<>();
    }

    @Override
    public Iterator<Review> getReviews() {
        return sortedReviews.iterator();
    }

    @Override
    public void addReview(Review review) {
        reviews.put(review.getReviewer().getName(), review);
        sortedReviews.add(review);
    }

    @Override
    public int getReviewsCount() {
        return reviews.size();
    }

    @Override
    public boolean userHasReviewed(User user) {
        return reviews.containsKey(user.getName());
    }

    @Override
    public int getYearOfRelease() {
        return yearOfRelease;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDirectorName() {
        return creator.getName();
    }

    @Override
    public String getAgeCertification() {
        return ageOfCertification;
    }

    @Override
    public String getMainGenre() {
        return genres.get(0);
    }

    @Override
    public float getScore() {
        if (reviews.size() == 0) return 0;

        float totalscore = 0;
        int divider = 0;
        for (Review review : reviews.values()) {
            int multiplier = 1;
            if (review.madeByCritic())
                multiplier = 5;
            totalscore += review.getScore() * multiplier;
            divider += multiplier;
        }
        totalscore = totalscore / divider;
        totalscore = (float) Math.round(totalscore * 10) / 10;
        return totalscore;
    }

    @Override
    public Iterator<Artist> getCast() {
        List<Artist> shortCast = new ArrayList<>();
        Iterator<Artist> cast = this.cast.iterator();
        int i = 0;
        while (cast.hasNext() && i < 3) {
            Artist next = cast.next();
            shortCast.add(next);
            i++;
        }
        return shortCast.iterator();
    }

    @Override
    public Iterator<Artist> getCastWithDirector() {
        List<Artist> directorCast = new ArrayList<>(cast);
        directorCast.add(creator);
        return directorCast.iterator();
    }

    @Override
    public String getArtistRole(String artist) {
        if (!artist.equals(this.creator.getName())) return ACTOR;
        else {
            if (this instanceof Movie) return DIRECTOR;
            else return CREATOR;
        }
    }

    @Override
    public boolean hasGenre(String genre){
        return genres.contains(genre);
    }

    private void addCast(Iterator<Artist> cast) {
        while (cast.hasNext()) {
            Artist next = cast.next();
            this.cast.add(next);
        }
    }

    private void addGenres(Iterator<String> genres) {
        while (genres.hasNext()) {
            String next = genres.next();
            this.genres.add(next);
        }
    }


}
