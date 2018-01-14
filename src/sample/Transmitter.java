package sample;

class Transmitter {
    private final Integer positionX;
    private final Integer positionY;

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
