package sample;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Set;

public class Robot extends Thread {
    @FXML
    private Circle robot;

    private enum Direction {NORTH, SOUTH, EAST, WEST}
    private final World world;
    private Integer moveCounter = 0;
    private LinkedList<Double> previousRssi;

    public Robot(World world, Circle robot) {
        this.world = world;
        this.robot = robot;
        previousRssi = new LinkedList<>();
    }

    public void run() {
        try {
            findHeaven();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void move(Direction direction) throws InterruptedException{
        switch(direction) {
            case NORTH:
                if (world.getHeight() > world.getPositionYOfRobot() + 1) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() + 1);
                    moveCounter++;
                }
                robot.setCenterY(robot.getCenterY()+1);
                this.sleep(50);
                break;
            case SOUTH:
                if (world.getPositionYOfRobot() - 1 > 0) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() - 1);
                    moveCounter++;
                }
                robot.setCenterY(robot.getCenterY()-1);
                this.sleep(50);
                break;
            case EAST:
                if (world.getWidth() > world.getPositionXOfRobot() + 1) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() + 1);
                    moveCounter++;
                }
                robot.setCenterX(robot.getCenterX()+1);
                this.sleep(50);
                break;
            case WEST:
                if (world.getPositionXOfRobot() - 1 > 0) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() - 1);
                    moveCounter++;
                }
                robot.setCenterX(robot.getCenterX()-1);
                this.sleep(50);
                break;
        }
    }

    private LinkedList<Direction> testMove() throws InterruptedException {
        LinkedList<Direction> whereToGo = new LinkedList<>();
        previousRssi.add(world.measureRSSI(0));
        previousRssi.add(world.measureRSSI(1));
        previousRssi.add(world.measureRSSI(2));
        Integer heavenFlag = 0;
        for(Direction d : Direction.values()) {
            move(d);
            Integer goodMove = 0;
            for(int i = 0; i <3; i++){
                if(previousRssi.get(i)<world.measureRSSI(i)){
                    goodMove++;    
                }
            }
            if(goodMove.equals(3) || goodMove.equals(0)){
                whereToGo.clear();
                return whereToGo;
            }
            else if(goodMove.equals(2)){
                whereToGo.add(d);
            }
            else{
                heavenFlag++;
            }
            oppositeMove(d);
        }
        if(heavenFlag.equals(4)){
            System.out.println("To niebo?");
            heaven();
        }
        return whereToGo;
    }

    private void oppositeMove(Direction d) throws InterruptedException {
        if(d==Direction.NORTH){
            move(Direction.SOUTH);
        }
        if(d==Direction.SOUTH){
            move(Direction.NORTH);
        }
        if(d==Direction.EAST){
            move(Direction.WEST);
        }
        if(d==Direction.WEST){
            move(Direction.EAST);
        }
    }

    private void heaven() {
        System.out.println("nebo");
    }

    private void hell() {
        System.out.println("pieklo");
    }

    public void findHeaven() throws InterruptedException {
        LinkedList<Direction> whereToGo = new LinkedList<>();
        whereToGo = testMove();
        if(whereToGo.size()==0) {
            hell();
            return;
        }
        if(whereToGo.size()==1){
            System.out.println("To niebo1?");
            heaven();
            return;
        }
        moveUntilBetter(whereToGo.get(0));
        moveUntilBetter(whereToGo.get(1));
        System.out.println("To niebo2");
        heaven();
    }

    private void moveUntilBetter(Direction direction) throws InterruptedException {
        previousRssi.add(world.measureRSSI(0));
        previousRssi.add(world.measureRSSI(1));
        previousRssi.add(world.measureRSSI(2));
        move(direction);
        Integer goodMove = 2;
        while (goodMove.equals(2)){
            goodMove=0;
            for(int i = 0; i <3; i++){
                if(previousRssi.get(i)<world.measureRSSI(i)){
                    goodMove++;
                }
                previousRssi.set(i,world.measureRSSI(i));
            }
            move(direction);
        }
        oppositeMove(direction);
    }
}
