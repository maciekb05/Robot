package sample;

import java.util.LinkedList;

public class Robot {
    private enum Direction {NORTH, SOUTH, EAST, WEST}
    private final World world;
    private LinkedList<LinkedList<Integer>> listOfRSSIlists;
    private Double factorA;
    private Double factorN;
    private Integer positionX;
    private Integer positionY;
    private Integer moveCounter = 0;
    private LinkedList<LinkedList<Double>> transmitterPosition;
    private Integer idOfNearest;
    public Robot(World world) {
        this.world = world;
        listOfRSSIlists = new LinkedList<>();

    }

    private void move(Direction direction){
        switch(direction) {
            case NORTH:
                if (world.getHeight() > world.getPositionYOfRobot() + 1) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() + 1);
                    moveCounter++;
                    positionY++;
                }
                break;
            case SOUTH:
                if (world.getPositionYOfRobot() - 1 > 0) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() - 1);
                    moveCounter++;
                    positionY--;

                }
                break;
            case EAST:
                if (world.getWidth() > world.getPositionXOfRobot() + 1) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() + 1);
                    moveCounter++;
                    positionX++;
                }
                break;
            case WEST:
                if (world.getPositionXOfRobot() - 1 > 0) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() - 1);
                    moveCounter++;
                    positionX--;
                }
                break;
        }
//        System.out.println("move to ("+positionX+","+positionY+")");
    }

    public void insideTriangle() {
        //TODO
    }

    public void outsideTriangle() {
        //TODO
    }

    public void goToTriangle() {
        positionX = world.getPositionXOfRobot();
        positionY = world.getPositionYOfRobot();
        idOfNearest = findNearestTransmitter();
        goToTransmitter(idOfNearest);
        factorA = world.measureRSSI(idOfNearest);
        System.out.println(factorA);
        findMid(idOfNearest);
        System.out.println("DONE");
        factorN = findFactorN(idOfNearest);
        System.out.println("DONE");
        System.out.println(factorN);

        //Setting new coordinate system
        positionX = 0;
        positionY = 0;
        transmitterPosition = findTransmittersPosition();
        LinkedList<Double> finalPosition = finalPosition();
        System.out.println("FInal position: "+finalPosition.get(0)+","+finalPosition.get(1));
        goToPosition(finalPosition);
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

    private void goToPosition(LinkedList<Double> finalPosition) {
        Integer x = finalPosition.get(0).intValue() - positionX;
        Integer y = finalPosition.get(1).intValue() - positionY;
        if(x>0){
            for(int i = 0; i < x; ++i){
                move(Direction.EAST);
            }
        }
        if(x<0){
            for(int i = 0; i < -x; ++i){
                move(Direction.WEST);
            }
        }
        if(y>0){
            for(int i = 0; i < y; ++i){
                move(Direction.NORTH);
            }
        }
        if(y<0){
            for(int i = 0; i < -y; ++i){
                move(Direction.SOUTH);
            }
        }
    }

    private void goToTransmitter(final int id) {
        Double currentRSSI = world.measureRSSI(id);

        move(Direction.NORTH);
        while(currentRSSI<world.measureRSSI(id)){
            currentRSSI = world.measureRSSI(id);
            move(Direction.NORTH);
        }
        move(Direction.SOUTH);
        currentRSSI = world.measureRSSI(id);

        move(Direction.EAST);
        while(currentRSSI<world.measureRSSI(id)){
            currentRSSI = world.measureRSSI(id);
            move(Direction.EAST);
        }
        move(Direction.WEST);
        currentRSSI = world.measureRSSI(id);

        move(Direction.SOUTH);
        while(currentRSSI<world.measureRSSI(id)){
            currentRSSI = world.measureRSSI(id);
            move(Direction.SOUTH);
        }
        move(Direction.NORTH);
        currentRSSI = world.measureRSSI(id);

        move(Direction.WEST);
        while(currentRSSI<world.measureRSSI(id)){
            currentRSSI = world.measureRSSI(id);
            move(Direction.WEST);
        }
        move(Direction.EAST);
    }

    private Double findFactorN(int id) {
        while(factorA.equals(world.measureRSSI(id))) {
            move(Direction.NORTH);
        }
        Double factN =((factorA-world.measureRSSI(id))/Math.log10(2));
        return Double.valueOf(Math.round(factN));
    }

    private LinkedList<LinkedList<Double>> findTransmittersPosition() {

        LinkedList<LinkedList<Double>> distances = new LinkedList<>();
        distances.add(new LinkedList<>());
        distances.add(new LinkedList<>());
        distances.add(new LinkedList<>());
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j){
                distances.get(j).add(Math.pow(10,(factorA-world.measureRSSI(j))/factorN));
                System.out.println(distances.get(j).get(i));
            }
            if(i==0) {
                move(Direction.NORTH);
                positionY+=1;
            } else if (i == 1) {
                move(Direction.EAST);
                positionX+=1;
            }
        }

        LinkedList<LinkedList<Double>> positionList = new LinkedList<>();
        for(int i = 0; i < 3; ++i) {
            Double x = (Math.pow(distances.get(i).get(1),2)+1-Math.pow(distances.get(i).get(2),2))/2;
            Double y = (Math.pow(distances.get(i).get(0),2)+1-Math.pow(distances.get(i).get(1),2))/2;
            LinkedList<Double> pos = new LinkedList<>();
            pos.add(x); pos.add(y);
            System.out.println("Powinno byc: "+(world.getListOfTransmitters().get(idOfNearest).getPositionX()+
                    x)+" "+(world.getListOfTransmitters().get(idOfNearest).getPositionY()+
                    y+2));
            System.out.println("pozycje: "+x+" "+y);
            positionList.add(pos);
        }

        return positionList;
    }

    private LinkedList<Double> finalPosition() {
        Integer xMin = transmitterPosition.get(0).get(0).intValue(),
                xMax = transmitterPosition.get(0).get(0).intValue(),
                yMin = transmitterPosition.get(0).get(1).intValue(),
                yMax = transmitterPosition.get(0).get(1).intValue();
        for(LinkedList<Double> transmitter : transmitterPosition) {
            if(transmitter.get(0)>xMax) {
                xMax = transmitter.get(0).intValue();
            }
            if(transmitter.get(0)<xMin) {
                xMin = transmitter.get(0).intValue();
            }
            if(transmitter.get(1)>yMax) {
                yMax = transmitter.get(1).intValue();
            }
            if(transmitter.get(1)<yMin) {
                yMin = transmitter.get(1).intValue();
            }
        }
        LinkedList<Double> mid = new LinkedList<>();
        mid.add((xMax+xMin)/2.0);
        mid.add((yMax+yMin)/2.0);
        return mid;
    }

    private void findMid(Integer id) {
        move(Direction.NORTH);
        if(factorA.equals(world.measureRSSI(id))) {
            System.out.println("X"+positionX);
            System.out.println("y"+positionY);
//            move(Direction.SOUTH);
            return;
        }
        move(Direction.SOUTH);
        move(Direction.EAST);
        if(factorA.equals(world.measureRSSI(id))) {
            System.out.println("X"+positionX);
            System.out.println("y"+positionY);
//            move(Direction.WEST);
            return;
        }
        move(Direction.WEST);
        move(Direction.SOUTH);
        if(factorA.equals(world.measureRSSI(id))) {
            System.out.println("X"+positionX);
            System.out.println("y"+positionY);
//            move(Direction.NORTH);
            return;
        }
        move(Direction.NORTH);

        move(Direction.WEST);
        if(factorA.equals(world.measureRSSI(id))) {
            System.out.println("X"+positionX);
            System.out.println("y"+positionY);
//            move(Direction.EAST);
            return;
        }
        move(Direction.EAST);
    }

    public boolean checkIfInTriangle() {
        //TODO
        return false;
    }
}
