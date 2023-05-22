package show;

import java.util.Comparator;

public class ShowComparatorByYear implements Comparator<Show> {

    @Override
    public int compare(Show show1,Show show2){
        if(show1.getYearOfRelease()-show2.getYearOfRelease()==0){
            return show1.getName().compareTo(show2.getName());
        }else {
            return show1.getYearOfRelease()-show2.getYearOfRelease();
        }
    }
}
