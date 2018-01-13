package sample;

import javafx.scene.shape.Circle;

public class Test {
    public static void main(String[] args) {
        Integer counter = 0;
        for (int i=0;i<1000;i++){
            World world = new World(100,200, new Circle());
            boolean flag=false;
            try {
                flag = world.getRobot().findHeaven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(flag==(world.isRobotInTriangle())) counter++;
        }
        System.out.println("W "+ counter +" przypadkach jest git.");
    }
}
