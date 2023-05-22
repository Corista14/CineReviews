package user;

public class AdminUserClass implements AdminUser {

    private final String name;
    private final String password;

    public AdminUserClass(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfPostedShows() {
        return 0;
    }
}
