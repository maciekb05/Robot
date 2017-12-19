package sample;

import java.util.LinkedList;

public class Robot {
    private enum Direction {NORTH, SOUTH, EAST, WEST}
    private final World world;
    private LinkedList<LinkedList<Integer>> listOfRSSIlists;
    private Double factorA;
    private Double factorN;


    public Robot(World world) {
        this.world = world;
        listOfRSSIlists = new LinkedList<>();
    }

    private void move(Direction direction){
        switch(direction) {
            case NORTH:
                if (world.getHeight() > world.getPositionYOfRobot() + 1) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() + 1);
                }
                break;
            case SOUTH:
                if (world.getPositionYOfRobot() - 1 > 0) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() - 1);
                }
                break;
            case EAST:
                if (world.getWidth() > world.getPositionXOfRobot() + 1) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() + 1);
                }
                break;
            case WEST:
                if (world.getPositionXOfRobot() - 1 > 0) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() - 1);
                }
                break;
        }
    }

    public void insideTriangle() {
        //TODO
    }

    public void outsideTriangle() {
        //TODO
    }

    public void goToTriangle() {
        Integer idOfNearest = findNearestTransmitter();
        goToTransmitter(idOfNearest);
        factorA = world.measureRSSI(idOfNearest);
        factorN = findFactorN(idOfNearest);
    }

    private int findNearestTransmitter() {
        LinkedList<Double> RSSIs = world.measureRSSIs();
        double maxRSSI = RSSIs.getFirst();
        int id = 0;
        for(int i = 1; i<3; i++) {
            if(maxRSSI < RSSIs.get(i)){
                maxRSSI = RSSIs.get(i);
                id = i;
            }
        }
        return id;
    }

    private void goToTransmitter(final int id) {
        Boolean nearTransmitter = true;
        while(!nearTransmitter) {
            Boolean move = false;
            Double currentRSSI = world.measureRSSI(id);
            move(Direction.NORTH);
            while(currentRSSI>world.measureRSSI(id)){
                currentRSSI = world.measureRSSI(id);
                move(Direction.NORTH);
                move = true;
            }
            move(Direction.SOUTH);
            move(Direction.EAST);
            while(currentRSSI>world.measureRSSI(id)){
                currentRSSI = world.measureRSSI(id);
                move(Direction.EAST);
                move = true;
            }
            move(Direction.WEST);
            move(Direction.SOUTH);
            while(currentRSSI>world.measureRSSI(id)){
                currentRSSI = world.measureRSSI(id);
                move(Direction.SOUTH);
                move = true;
            }
            move(Direction.NORTH);
            move(Direction.WEST);
            while(currentRSSI>world.measureRSSI(id)){
                currentRSSI = world.measureRSSI(id);
                move(Direction.WEST);
                move = true;
            }
            move(Direction.EAST);
            if(!move){
                nearTransmitter = true;
            }
        }
    }

    private Double findFactorN(int id) {
        while(factorA==world.measureRSSI(id)) {
            move(Direction.NORTH);
        }
        return (factorA-world.measureRSSI(id))/Math.log10(2);
    }

    private void findFactors() {

    }

    public boolean checkIfInTriangle() {
        //TODO
        return false;
    }
}
