package sample;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.LinkedList;

public class Robot extends Thread {
    @FXML
    private Circle robot;

    private enum Direction {NORTH, SOUTH, EAST, WEST}
    private final World world;
    private LinkedList<Double> previousRssi;

    Robot(World world, Circle robot) {
        this.world = world;
        this.robot = robot;
        previousRssi = new LinkedList<>();
        previousRssi.add(0.0);
        previousRssi.add(0.0);
        previousRssi.add(0.0);
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
                }
                robot.setCenterY(robot.getCenterY()+1);
                sleep(5);
                break;
            case SOUTH:
                if (world.getPositionYOfRobot() - 1 > 0) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() - 1);
                }
                robot.setCenterY(robot.getCenterY()-1);
                sleep(5);
                break;
            case EAST:
                if (world.getWidth() > world.getPositionXOfRobot() + 1) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() + 1);
                }
                robot.setCenterX(robot.getCenterX()+1);
                sleep(5);
                break;
            case WEST:
                if (world.getPositionXOfRobot() - 1 > 0) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() - 1);
                }
                robot.setCenterX(robot.getCenterX()-1);
                sleep(5);
                break;
        }
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

    private LinkedList<Direction> testMove() throws InterruptedException {
        LinkedList<Direction> whereToGo = new LinkedList<>();
        Integer heavenFlag = 0;
        Integer hellFlag = 0;
        for(Direction d : Direction.values()) {
            setPreviousRSSI();
            move(d);
            Integer goodMove = 0;
            for(int i = 0; i <3; i++){
                if(previousRssi.get(i)<world.measureRSSI(i)){
                    goodMove++;
                }
            }

            if(goodMove.equals(2)){
                whereToGo.add(d);
            }
            else if(goodMove.equals(1)){
                heavenFlag++;
            }
            else if(goodMove.equals(3) || goodMove.equals(0)){
                whereToGo.clear();
                return whereToGo;
            }
            oppositeMove(d);
        }
        if(heavenFlag.equals(4)){
            heaven();
        }
        return whereToGo;
    }

    private void heaven() {
        robot.setFill(Color.YELLOW);
    }

    private void hell() {
        robot.setFill(Color.RED);
    }

    public boolean findHeaven() throws InterruptedException {
        LinkedList<Direction> whereToGo = testMove();
        if(whereToGo.size()==0) {
            hell();
            return false;
        }
        if(whereToGo.size()==1){
            moveUntilBetter(whereToGo.get(0));
            heaven();
            return true;
        }
        moveUntilBetter(whereToGo.get(0));
        moveUntilBetter(whereToGo.get(1));
        heaven();
        return true;
    }

    private void moveUntilBetter(Direction direction) throws InterruptedException {
        Integer goodMove = 2;
        while (goodMove.equals(2)){
            goodMove=0;
            setPreviousRSSI();
            move(direction);
            for(int i = 0; i <3; i++){
                if(previousRssi.get(i)<world.measureRSSI(i)){
                    goodMove++;
                }
                else if(previousRssi.get(i).equals(world.measureRSSI(i))){
                    move(direction);
                }
            }
            if(goodMove==1){
                oppositeMove(direction);
            }
        }
    }

    private void setPreviousRSSI(){
        previousRssi.set(0, world.measureRSSI(0));
        previousRssi.set(1, world.measureRSSI(1));
        previousRssi.set(2, world.measureRSSI(2));
    }
}
