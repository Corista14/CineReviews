package util;

/**
 * Enum to store the classifications of the reviews
 *
 * @author Filipe Corista / João Rodrigues
 */
public enum Classification {

    EXCELLENT(5),
    GOOD(4),
    AVERAGE(3),
    POOR(2),
    TERRIBLE(1);

    /**
     * Integer storing the value of the classification
     */
    private final int value;


    Classification(int value) {
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

}
