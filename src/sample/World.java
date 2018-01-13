package sample;

import javafx.scene.shape.Circle;

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

    public Double measureRSSI(Integer id) {
        Transmitter transmitter = listOfTransmitters.get(id);
        Double distance = Math.sqrt(Math.pow(positionXOfRobot-transmitter.getPositionX(),2)
                +Math.pow(positionYOfRobot-transmitter.getPositionY(),2));
        Double RSSI;
        if(distance == 0){
            RSSI = factorA.doubleValue();
        }
        else {
            RSSI = factorA - factorN * Math.log10(distance);
        }
        return RSSI;
    }

    public World(Integer height, Integer width, Circle rob) {
        this.height = height;
        this.width = width;
        Random random = new Random();
        //factorA = random.nextInt(height > width ? width : height)/10;
        //factorN = random.nextInt(10);
        factorA = 100;
        factorN = 5;
        positionXOfRobot = random.nextInt(width-10)+5;
        positionYOfRobot = random.nextInt(height-10)+5;
        robot = new Robot(this, rob);
        listOfTransmitters = new LinkedList<>();
        for(int i=0; i<3; ++i){
            Transmitter transmitter = new Transmitter(this, random.nextInt(width-10)+5, random.nextInt(height-10)+5);
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

    public boolean isRobotInTriangle(){
        Integer x1=  listOfTransmitters.get(0).getPositionX(); Integer y1= listOfTransmitters.get(0).getPositionY();
        Integer x2=  listOfTransmitters.get(1).getPositionX(); Integer y2= listOfTransmitters.get(1).getPositionY();
        Integer x3=  listOfTransmitters.get(2).getPositionX(); Integer y3= listOfTransmitters.get(2).getPositionY();
        double A = area (x1, y1, x2, y2, x3, y3);

        double A1 = area (positionXOfRobot, positionYOfRobot, x2, y2, x3, y3);

        double A2 = area (x1, y1, positionXOfRobot, positionYOfRobot, x3, y3);

        double A3 = area (x1, y1, x2, y2, positionXOfRobot, positionYOfRobot);

        return (A<((A1 + A2 + A3)*1.2) && A>(( A1 + A2 + A3)*0.8));
    }

    private double area(Integer x1, Integer y1, Integer x2, Integer y2, Integer x3, Integer y3)
    {
        return Math.abs((x1*(y2-y3) + x2*(y3-y1)+
                x3*(y1-y2))/2.0);
    }

    public Integer getPositionXOfRobot() {
        return positionXOfRobot;
    }

    public Integer getPositionYOfRobot() {
        return positionYOfRobot;
    }
}
