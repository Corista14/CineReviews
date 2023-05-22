package artist;

import artist.Exceptions.AlreadyHasBioException;
import show.Show;
import show.ShowComparatorByYear;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ArtistClass implements Artist{

    private String name;
    private String dateOfBirth;
    private String placeOfBirth;
    private boolean hasBio;
    private Set<Show> shows;

    public  ArtistClass(String name){
        this.name=name;
        this.hasBio=false;
        shows=new TreeSet<Show>(new ShowComparatorByYear());
    }

    public  ArtistClass (String name,String dateOfBirth, String placeOfBirth){
        this.name=name;
        this.dateOfBirth=dateOfBirth;
        this.placeOfBirth=placeOfBirth;
        this.hasBio=true;
        shows=new TreeSet<Show>(new ShowComparatorByYear());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addBio(String dateOfBirth, String placeOfBirth) throws  AlreadyHasBioException{
        if(hasBio){
            throw new AlreadyHasBioException();
        }else{
            this.placeOfBirth=placeOfBirth;
            this.dateOfBirth=dateOfBirth;
            this.hasBio=true;
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
}
