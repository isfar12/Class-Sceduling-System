// User.java
public abstract class User {
    protected String name;
    protected String key;

    public User(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public abstract void displayDashboard(Schedule schedule, CommentSection commentSection);
}