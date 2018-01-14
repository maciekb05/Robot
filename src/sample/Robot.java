package sample;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.LinkedList;

/**
 * Represents Robot object in the World object. It allows you to check if robot is in a robots' heaven ({@link #inHeaven()}).
 */
public class Robot extends Thread {
    @FXML
    private final Circle robot;

    private enum Direction {NORTH, SOUTH, EAST, WEST}
    private final World world;
    private final LinkedList<Double> previousRSSI;
    private Integer amIInHeaven = 0; // 1 - heaven, -1 - hell, 0- I have no idea

    /**
     * Construct a new Robot object.
     *
     * @param world the world where robot landed
     * @param robot FXML parameter use to represent Robot in GUI
     */
    Robot(World world, Circle robot) {
        this.world = world;
        this.robot = robot;
        previousRSSI = new LinkedList<>();
        previousRSSI.add(0.0);
        previousRSSI.add(0.0);
        previousRSSI.add(0.0);
    }


    /**
     * starts a thread of simulation
     */
    public void run() {
        try {
            inHeaven();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if robot is in heaven (triangle which has its vertices in transmitters). When he is far from heaven, method returns false.
     * Otherwise he ascertains where he is and goes inside. After that method returns true.
     *
     * @return <code>true</code> when robot is in heaven (after method execution).
     *         <code>false</code> otherwise
     * @throws InterruptedException if sleep method is interrupted
     */
    public boolean inHeaven() throws InterruptedException {
        LinkedList<Direction> whereToGo = testMove();
        if(amIInHeaven.equals(-1)) {
            hell();
            return false;
        }
        if(amIInHeaven.equals(1)){
            heaven();
            return true;
        }
        if(whereToGo.size()==1){
            moveUntilBetter(whereToGo.get(0));
            heaven();
            return true;
        }
        if(whereToGo.size()==2){
            moveUntilBetter(whereToGo.get(0));
            moveUntilBetter(whereToGo.get(1));
            heaven();
            return true;
        }
        return false;
    }

    private void move(Direction direction) throws InterruptedException{
        switch(direction) {
            case NORTH:
                if (world.getHeight() > world.getPositionYOfRobot() + 1) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() + 1);
                }
                robot.setCenterY(robot.getCenterY()+1);
                sleep(20);
                break;
            case SOUTH:
                if (world.getPositionYOfRobot() - 1 > 0) {
                    world.setPositionYOfRobot(world.getPositionYOfRobot() - 1);
                }
                robot.setCenterY(robot.getCenterY()-1);
                sleep(20);
                break;
            case EAST:
                if (world.getWidth() > world.getPositionXOfRobot() + 1) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() + 1);
                }
                robot.setCenterX(robot.getCenterX()+1);
                sleep(20);
                break;
            case WEST:
                if (world.getPositionXOfRobot() - 1 > 0) {
                    world.setPositionXOfRobot(world.getPositionXOfRobot() - 1);
                }
                robot.setCenterX(robot.getCenterX()-1);
                sleep(20);
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
        setPreviousRSSI();
        for(Direction d : Direction.values()) {
            move(d);
            Integer betterRSSI = 0;
            for(int i = 0; i <3; i++){
                if(previousRSSI.get(i)<world.measureRSSI(i)){
                    betterRSSI++;
                }
            }

            if(betterRSSI.equals(2)){
                whereToGo.add(d);
            }
            else if(betterRSSI.equals(1)){
                heavenFlag++;
            }
            else if(betterRSSI.equals(3) || betterRSSI.equals(0)){
                amIInHeaven = -1;
                return whereToGo;
            }
            oppositeMove(d);
        }
        if(heavenFlag.equals(4)){
            amIInHeaven = 1;
        }
        return whereToGo;
    }

    private void heaven() {
        robot.setFill(Color.LIME);
    }

    private void hell() {
        robot.setFill(Color.RED);
    }

    private void moveUntilBetter(Direction direction) throws InterruptedException {
        Integer goodMove;
        do{
            goodMove=0;
            setPreviousRSSI();
            move(direction);
            for(int i = 0; i <3; i++){
                if(previousRSSI.get(i)<world.measureRSSI(i)){
                    goodMove++;
                }
                else if(previousRSSI.get(i).equals(world.measureRSSI(i))){
                    move(direction);
                }
            }
            if(goodMove==1){
                oppositeMove(direction);
            }
        } while (goodMove.equals(2));
    }

    private void setPreviousRSSI(){
        previousRSSI.set(0, world.measureRSSI(0));
        previousRSSI.set(1, world.measureRSSI(1));
        previousRSSI.set(2, world.measureRSSI(2));
    }
}
