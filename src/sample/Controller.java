package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class Controller {
    World world;
    @FXML
    Circle robot;
    @FXML
    Circle transmitter1;
    @FXML
    Circle transmitter2;
    @FXML
    Circle transmitter3;
    @FXML
    Pane mapPane;
    @FXML
    BorderPane main;
    @FXML
    VBox left;

    @FXML
    public void initialize() {
        world = new World(200, 400, robot);

        mapPane.resize(world.getHeight(), world.getWidth());

        robot.setCenterX(world.getPositionXOfRobot());
        robot.setCenterY(world.getPositionYOfRobot());
        transmitter1.setCenterX(world.getListOfTransmitters().get(0).getPositionX());
        transmitter1.setCenterY(world.getListOfTransmitters().get(0).getPositionY());
        transmitter2.setCenterX(world.getListOfTransmitters().get(1).getPositionX());
        transmitter2.setCenterY(world.getListOfTransmitters().get(1).getPositionY());
        transmitter3.setCenterX(world.getListOfTransmitters().get(2).getPositionX());
        transmitter3.setCenterY(world.getListOfTransmitters().get(2).getPositionY());
        System.out.println(transmitter1.getCenterX()+", "+transmitter1.getCenterY());
        System.out.println(transmitter2.getCenterX()+", "+transmitter2.getCenterY());
        System.out.println(transmitter3.getCenterX()+", "+transmitter3.getCenterY());

        robot.setVisible(true);
        transmitter1.setVisible(true);
        transmitter2.setVisible(true);
        transmitter3.setVisible(true);
    }

    @FXML
    public void simulate() {
        world.getRobot().setDaemon(true);
        world.getRobot().start();
    }
}
