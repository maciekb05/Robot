package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class Controller {
    private World world;
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
    Button simButton;
    @FXML
    Button restartButton;

    @FXML
    Line line12;
    @FXML
    Line line23;
    @FXML
    Line line13;

    @FXML
    public void initialize() {
        world = new World(200, 400, robot);

        mapPane.resize(world.getHeight(), world.getWidth());

        robot.setFill(Color.BLACK);
        robot.setCenterX(world.getPositionXOfRobot());
        robot.setCenterY(world.getPositionYOfRobot());
        transmitter1.setCenterX(world.getListOfTransmitters().get(0).getPositionX());
        transmitter1.setCenterY(world.getListOfTransmitters().get(0).getPositionY());
        transmitter2.setCenterX(world.getListOfTransmitters().get(1).getPositionX());
        transmitter2.setCenterY(world.getListOfTransmitters().get(1).getPositionY());
        transmitter3.setCenterX(world.getListOfTransmitters().get(2).getPositionX());
        transmitter3.setCenterY(world.getListOfTransmitters().get(2).getPositionY());

        line12.setStartX(world.getListOfTransmitters().get(0).getPositionX());
        line12.setStartY(world.getListOfTransmitters().get(0).getPositionY());
        line12.setEndX(world.getListOfTransmitters().get(1).getPositionX());
        line12.setEndY(world.getListOfTransmitters().get(1).getPositionY());

        line23.setStartX(world.getListOfTransmitters().get(1).getPositionX());
        line23.setStartY(world.getListOfTransmitters().get(1).getPositionY());
        line23.setEndX(world.getListOfTransmitters().get(2).getPositionX());
        line23.setEndY(world.getListOfTransmitters().get(2).getPositionY());

        line13.setStartX(world.getListOfTransmitters().get(0).getPositionX());
        line13.setStartY(world.getListOfTransmitters().get(0).getPositionY());
        line13.setEndX(world.getListOfTransmitters().get(2).getPositionX());
        line13.setEndY(world.getListOfTransmitters().get(2).getPositionY());

        mapPane.setBorder(new Border(new BorderStroke(Paint.valueOf("black"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        line12.setVisible(true);
        line23.setVisible(true);
        line13.setVisible(true);
        robot.setVisible(true);

        transmitter1.setVisible(true);
        transmitter2.setVisible(true);
        transmitter3.setVisible(true);
    }

    @FXML
    public void restart() {
        initialize();
        System.gc();
        world.getRobot().start();
    }

    @FXML
    public void simulate() {
        world.getRobot().start();
        simButton.setDisable(true);
    }
}
