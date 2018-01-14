package sample;

/**
 * Represents a transmitter in the world. It has only its position and getters methods.
 */
public class Transmitter {
    private final Integer positionX;
    private final Integer positionY;

    /**
     * Construct a new Transmitter object with position x and y set.
     *
     * @param positionX the x-coordinate in pixels
     * @param positionY the y-coordinate in pixels
     */
    Transmitter(Integer positionX, Integer positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * @return the x-coordinate in pixels
     */
    public Integer getPositionX() {
        return positionX;
    }

    /**
     * @return the y-coordinate in pixels
     */
    public Integer getPositionY() {
        return positionY;
    }
}
