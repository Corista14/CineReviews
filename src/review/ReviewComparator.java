package review;

import java.util.Comparator;

public class ReviewComparator implements Comparator<Review> {

    @Override
    public int compare(Review r1,Review r2) {
        if (r1.getType().equals(r2.getType())) {
            if(r1.getScore()==r2.getScore()){
                return r1.getUserName().compareTo(r2.getUserName());
            }else{
                return r1.getScore()-r2.getScore();
            }

        } else {
            if(r1.getType().equals("critic")){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
