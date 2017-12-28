package sample;

public class Transmitter {
    private World world;
    private static Integer count = 0;
    private Integer id;
    private Integer positionX;
    private Integer positionY;

    public Transmitter(World world, Integer positionX, Integer positionY) {
        id = count++;
        this.world = world;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public Integer getId() {
        return id;
    }
}
