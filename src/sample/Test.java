package sample;

import javafx.scene.shape.Circle;

class Test {
    public static void main(String[] args) {
        Integer counter = 0;
        for (int i=0;i<100000;i++){
            World world = new World(500,1000, new Circle());
            boolean flag=false;
            try {
                flag = world.getRobot().findHeaven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(flag==(world.isRobotInTriangle())) counter++;
        }
        System.out.println("In "+ counter +" cases is good.");
    }
}
