package sample;

import java.util.LinkedList;

public class World {
    private Integer height;
    private Integer width;
    LinkedList<Transmitter> listOfTransmitters;
    Robot robot;
    Integer positionXOfRobot;
    Integer positionYOfRobot;

    public World(Integer height, Integer width) {
        this.height = height;
        this.width = width;
        robot = new Robot();
    }
}
