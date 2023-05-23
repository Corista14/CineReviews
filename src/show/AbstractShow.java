package show;

import artist.Artist;
import review.Review;
import review.ReviewComparator;
import user.AdminUser;
import user.User;

import java.util.*;

public abstract class AbstractShow implements Show {

    /**
     * Map to store the reviews. Username -> Review
     */
    private final Map<String, Review> reviews;
    private final Set<Review> sortedReviews;//added tree Set
    private final String title;
    private final String creatorName;
    private final String ageOfCertification;
    private final int yearOfRelease;
    private final List<String> genres;
    private final List<Artist> cast;
    private final AdminUser uploader;

    /**
     * Creates a new show, being Movie or Series
     *
     * @param title              name of the show
     * @param creatorName        name of the creator of the show
     * @param ageOfCertification age of certification of the show
     * @param yearOfRelease      the show's year of release
     * @param genres             a collection of genres of the show
     * @param cast               a collection of the cast of the show
     */
    public AbstractShow(AdminUser uploader, String title, String creatorName, String ageOfCertification,
                        int yearOfRelease, Iterator<String> genres, Iterator<Artist> cast) {
        this.uploader = uploader;
        this.title = title;
        this.creatorName = creatorName;
        this.ageOfCertification = ageOfCertification;
        this.yearOfRelease = yearOfRelease;
        this.genres = new ArrayList<>();
        this.cast = new ArrayList<>();
        this.sortedReviews= new TreeSet<>(new ReviewComparator());
        addCast(cast);
        addGenres(genres);
        reviews = new HashMap<>();
    }

    @Override
    public Iterator<Review> getReviews() {
        return sortedReviews.iterator();
    }

    @Override
    public void addReview(Review review){
        reviews.put(review.getReviewer().getName(),review);
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

    @Override
    public float getScore(){
        if(reviews.size()==0){
            return 0;
        }
        float totalscore=0;
        int divider=0;
        for (Review review: reviews.values()){
            int multiplier=1;
            if(review.madeByCritic()){
                multiplier=5;
            }
            totalscore+=review.getScore()*multiplier;
            divider+=multiplier;
        }
        totalscore= totalscore / divider;
       totalscore= Math.round(totalscore*10)/10;
       return totalscore;
    }
}
