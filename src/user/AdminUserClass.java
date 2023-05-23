package user;

public class AdminUserClass implements AdminUser {

    private final String name;
    private final String password;
    private  int addedShowAmount;

    public AdminUserClass(String name, String password) {
        this.name = name;
        this.password = password;
        addedShowAmount=0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean passwordMatches(String password) {
        return password.equals(this.password);
    }

    @Override
    public int getNumberOfPostedShows() {
        return addedShowAmount;
    }

    @Override
    public  void addedShow(){
        addedShowAmount++;
    }
}
