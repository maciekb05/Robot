package sample;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class Robot extends Thread {
    @FXML
    private Circle robot;
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
    public Robot(World world, Circle robot) {
        this.world = world;
        this.robot = robot;
        listOfRSSIlists = new LinkedList<>();
    }

    public void run() {
        try{
            goToTriangle();
        }catch (InterruptedException ex){
            System.out.println("Interrupted");
        }
    }

    private void move(Direction direction) throws InterruptedException{
        switch(direction) {
            case NORTH:
                if (world.getHeight() > world.getPositionYOfRobot() + 1) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() + 1);
                    moveCounter++;
                    positionY++;
                }
                robot.setCenterY(robot.getCenterY()+1);
                this.sleep(50);
                break;
            case SOUTH:
                if (world.getPositionYOfRobot() - 1 > 0) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() - 1);
                    moveCounter++;
                    positionY--;
                }
                robot.setCenterY(robot.getCenterY()-1);
                this.sleep(50);
                break;
            case EAST:
                if (world.getWidth() > world.getPositionXOfRobot() + 1) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() + 1);
                    moveCounter++;
                    positionX++;
                }
                robot.setCenterX(robot.getCenterX()+1);
                this.sleep(50);
                break;
            case WEST:
                if (world.getPositionXOfRobot() - 1 > 0) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() - 1);
                    moveCounter++;
                    positionX--;
                }
                robot.setCenterX(robot.getCenterX()-1);
                this.sleep(50);
                break;
        }
    }

    public void goToTriangle() throws InterruptedException {
        positionX = world.getPositionXOfRobot();
        positionY = world.getPositionYOfRobot();
        idOfNearest = findNearestTransmitter();
        goToTransmitter(idOfNearest);
        factorA = world.measureRSSI(idOfNearest);
        findMid(idOfNearest);
        factorN = findFactorN(idOfNearest);

        //Setting new coordinate system
        positionX = 0;
        positionY = 0;
        transmitterPosition = findTransmittersPosition();
        LinkedList<Double> finalPosition = finalPosition();
        goToPosition(finalPosition);
        System.out.println("Robot: "+world.getPositionXOfRobot()+", "+world.getPositionYOfRobot());
        System.out.println("Antena: "+world.getListOfTransmitters().get(0).getPositionX()+", "+world.getListOfTransmitters().get(0).getPositionY());
        System.out.println("Antena: "+world.getListOfTransmitters().get(1).getPositionX()+", "+world.getListOfTransmitters().get(1).getPositionY());
        System.out.println("Antena: "+world.getListOfTransmitters().get(2).getPositionX()+", "+world.getListOfTransmitters().get(2).getPositionY());
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

    private void goToPosition(LinkedList<Double> finalPosition) throws InterruptedException {
        Integer x = finalPosition.get(0).intValue() - positionX;
        Integer y = finalPosition.get(1).intValue() - positionY;
        if(x>0){
            for(int i = 0; i < x; ++i){
                move(Direction.EAST);
                if(checkIfInTriangle()) break;
            }
        }
        if(x<0 && !checkIfInTriangle()){
            for(int i = 0; i < -x; ++i){
                move(Direction.WEST);
                if(checkIfInTriangle()) break;
            }
        }
        if(y>0 && !checkIfInTriangle()){
            for(int i = 0; i < y; ++i){
                move(Direction.NORTH);
                if(checkIfInTriangle()) break;
            }
        }
        if(y<0 && !checkIfInTriangle()){
            for(int i = 0; i < -y; ++i){
                move(Direction.SOUTH);
                if(checkIfInTriangle()) break;
            }
        }
    }

    private void goToTransmitter(final int id) throws InterruptedException {
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

    private Double findFactorN(int id) throws InterruptedException {
        while(factorA.equals(world.measureRSSI(id))) {
            move(Direction.NORTH);
        }
        Double factN =((factorA-world.measureRSSI(id))/Math.log10(2));
        return Double.valueOf(Math.round(factN));
    }

    private LinkedList<LinkedList<Double>> findTransmittersPosition() throws InterruptedException {

        LinkedList<LinkedList<Double>> distances = new LinkedList<>();
        distances.add(new LinkedList<>());
        distances.add(new LinkedList<>());
        distances.add(new LinkedList<>());
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j){
                distances.get(j).add(Math.pow(10,(factorA-world.measureRSSI(j))/factorN));
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
            positionList.add(pos);
        }

        return positionList;
    }

    private LinkedList<Double> finalPosition() {
        Integer xMin = Math.toIntExact(Math.round(transmitterPosition.get(0).get(0))),
                xMax = Math.toIntExact(Math.round(transmitterPosition.get(0).get(0).intValue())),
                yMin = Math.toIntExact(Math.round(transmitterPosition.get(0).get(1).intValue())),
                yMax = Math.toIntExact(Math.round(transmitterPosition.get(0).get(1).intValue()));
        for(LinkedList<Double> transmitter : transmitterPosition) {
            if(transmitter.get(0)>xMax) {
                xMax = Math.toIntExact(Math.round(transmitter.get(0)));
            }
            if(transmitter.get(0)<xMin) {
                xMin = Math.toIntExact(Math.round(transmitter.get(0)));
            }
            if(transmitter.get(1)>yMax) {
                yMax = Math.toIntExact(Math.round(transmitter.get(1)));
            }
            if(transmitter.get(1)<yMin) {
                yMin = Math.toIntExact(Math.round(transmitter.get(1)));
            }
        }
        LinkedList<Double> mid = new LinkedList<>();
        mid.add((xMax+xMin)/2.0);
        mid.add((yMax+yMin)/2.0);
        return mid;
    }

    private void findMid(Integer id) throws InterruptedException {
        move(Direction.NORTH);
        if(factorA.equals(world.measureRSSI(id))) {
            return;
        }
        move(Direction.SOUTH);
        move(Direction.EAST);
        if(factorA.equals(world.measureRSSI(id))) {
            return;
        }
        move(Direction.WEST);
        move(Direction.SOUTH);
        if(factorA.equals(world.measureRSSI(id))) {
            return;
        }
        move(Direction.NORTH);

        move(Direction.WEST);
        if(factorA.equals(world.measureRSSI(id))) {
            return;
        }
        move(Direction.EAST);
    }

    private boolean checkIfInTriangle() {
        LinkedList<LinkedList<Double>> straights = new LinkedList<>();
        LinkedList<Double> factors = new LinkedList<>();
        factors = straightEquation(world.getListOfTransmitters().get(0).getPositionX(),world.getListOfTransmitters().get(0).getPositionY(),
                world.getListOfTransmitters().get(1).getPositionX(),world.getListOfTransmitters().get(1).getPositionY());
        straights.add(factors);
        factors = straightEquation(world.getListOfTransmitters().get(1).getPositionX(),world.getListOfTransmitters().get(1).getPositionY(),
                world.getListOfTransmitters().get(2).getPositionX(),world.getListOfTransmitters().get(2).getPositionY());
        straights.add(factors);
        factors = straightEquation(world.getListOfTransmitters().get(0).getPositionX(),world.getListOfTransmitters().get(0).getPositionY(),
                world.getListOfTransmitters().get(2).getPositionX(),world.getListOfTransmitters().get(2).getPositionY());
        straights.add(factors);

        for(int i=0;i<3;i++){
            Double A=straights.get(i).get(0); Double B=straights.get(i).get(1); Double C=straights.get(i).get(2);
            Integer x= world.getListOfTransmitters().get(i).getPositionX(); Integer y= world.getListOfTransmitters().get(i).getPositionY();
            if((A*x+B*y+C)*(A*positionX+B*positionY+C)<0){
                return false;
            }
        }

        return true;
    }
    private LinkedList<Double> straightEquation(Integer x1, Integer y1, Integer x2, Integer y2){
        LinkedList<Double> factors = new LinkedList<>();
        Double A,B,C;
        if(x1.equals(x2)){
            A=0.0;
            B=1.0;
            C=Double.valueOf(x1);
        }else{
            A=1.0;
            B=-Double.valueOf((y1-y2)/(x1-x2));
            C=-(y1-A*x1);
        }
        factors.add(A); factors.add(B); factors.add(C);
        return factors;
    }
}
