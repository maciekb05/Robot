package sample;

public class Test {
    public static void main(String[] args) {
        World world = new World(100,200);
        world.getRobot().goToTriangle();
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
