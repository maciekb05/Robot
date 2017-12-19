package sample;

import java.util.LinkedList;
import java.util.Random;

public class World {
    private final Integer height;
    private final Integer width;
    private LinkedList<Transmitter> listOfTransmitters;
    private Robot robot;
    private Integer positionXOfRobot;
    private Integer positionYOfRobot;
    private final Integer factorA;
    private final Integer factorN;

    public LinkedList<Double> measureRSSIs() {
        LinkedList<Double> RSSIs = new LinkedList<>();
        for (Transmitter transmitter : listOfTransmitters) {
            Double distance = Math.sqrt(Math.pow(positionXOfRobot-transmitter.getPositionX(),2)
                    +Math.pow(positionYOfRobot-transmitter.getPositionY(),2));
            Double RSSI = factorA - factorN * Math.log10(distance);
            RSSIs.add(RSSI);
        }
        return RSSIs;
    }

    public Double measureRSSI(Integer id) {
        Transmitter transmitter = listOfTransmitters.get(id);
        Double distance = Math.sqrt(Math.pow(positionXOfRobot-transmitter.getPositionX(),2)
                +Math.pow(positionYOfRobot-transmitter.getPositionY(),2));
        Double RSSI = factorA - factorN * Math.log10(distance);
        return RSSI;
    }

    public World(Integer height, Integer width) {
        this.height = height;
        this.width = width;
        Random random = new Random();
        //factorA = random.nextInt(height > width ? width : height)/10;
        //factorN = random.nextInt(10);
        factorA = 100;
        factorN = 5;
        positionXOfRobot = random.nextInt(height)+1;
        positionYOfRobot = random.nextInt(width)+1;
        robot = new Robot(this);
        listOfTransmitters = new LinkedList<>();
        for(int i=0; i<3; ++i){
            Transmitter transmitter = new Transmitter(this, random.nextInt(height-3)+2, random.nextInt(width-3)+2);
            listOfTransmitters.add(transmitter);
        }
    }

    public void setPositionXOfRobot(Integer positionXOfRobot) {
        this.positionXOfRobot = positionXOfRobot;
    }

    public void setPositionYOfRobot(Integer positionYOfRobot) {
        this.positionYOfRobot = positionYOfRobot;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public LinkedList<Transmitter> getListOfTransmitters() {
        return listOfTransmitters;
    }

    public Robot getRobot() {
        return robot;
    }

    public Integer getPositionXOfRobot() {
        return positionXOfRobot;
    }

    public Integer getPositionYOfRobot() {
        return positionYOfRobot;
    }
}
