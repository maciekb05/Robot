package sample;

public class Transmitter {
    private Integer positionX;
    private Integer positionY;

    Transmitter(Integer positionX, Integer positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }
}
