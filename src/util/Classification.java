package util;

/**
 * Enum to store the classifications of the reviews
 *
 * @author Filipe Corista / João Rodrigues
 */
public enum Classification {

    EXCELLENT("excellent", 5),
    GOOD("good", 4),
    AVERAGE("average", 3),
    POOR("poor", 2),
    TERRIBLE("terrible", 1);

    /**
     * Integer storing the value of the classification
     */
    private final int value;

    /**
     * String storing the description of the classification
     */
    private final String description;

    Classification(String description, int value) {
        this.description = description;
        this.value = value;
    }

    /**
     * Gets the value of the classification
     *
     * @return the value of the classification
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the description of the classification
     *
     * @return the description of the classification
     */
    public String getDescription() {
        return description;
    }
}
