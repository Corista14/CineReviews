package util;

/**
 * Enum to store the commands of the application
 *
 * @author Filipe Corista / João Rodrigues
 */
public enum Command {
    REGISTER("registers a user in the system"),
    USERS("lists all registered users"),
    MOVIE("uploads a new movie"),
    SERIES("uploads a new series"),
    SHOWS("lists all shows"),
    ARTIST("adds bio information about an artist"),
    CREDITS("lists the bio and credits of an artist"),
    REVIEW("adds a review to a show"),
    REVIEWS("lists the reviews of a show"),
    GENRE("lists shows of given genres"),
    RELEASED("lists shows released in a given year"),
    AVOIDERS("lists artists that have no common projects"),
    FRIENDS("lists artists that have more projects together"),
    HELP("shows the available commands"),
    EXIT("terminates the execution of the program"),
    UNKNOWN("unknown command");

    /**
     * Stores the description of a command
     */
    private final String description;

    Command(String description) {
        this.description = description;
    }

    /**
     * Gets the description of a command
     *
     * @return the description of a command
     */
    public String getDescription() {
        return description;
    }
}
