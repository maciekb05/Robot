package sample;

import javafx.scene.shape.Circle;

public class Test {
    public static void main(String[] args) {
        World world = new World(100,200, new Circle());
        try {
            world.getRobot().goToTriangle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Robot");
        System.out.println(world.getPositionXOfRobot());
        System.out.println(world.getPositionYOfRobot());
        for(Transmitter transmitter : world.getListOfTransmitters()){
            System.out.println("Transmitter "+transmitter.getId());
            System.out.println(transmitter.getPositionX());
            System.out.println(transmitter.getPositionY());
        }
    }
}
