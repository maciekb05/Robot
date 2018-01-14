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
        final Integer height = 400;
        final Integer width = 800;

        world = new World(height, width, robot);

        drawPane(width,height);
        drawHeaven();
        drawRobot();

        restartButton.setDisable(true);
    }

    private void drawPane(Integer width, Integer height) {
        mapPane.resize(width, height);
        mapPane.setMinHeight(height);
        mapPane.setMinWidth(width);

        //PaneBorder
        mapPane.setBorder(new Border(new BorderStroke(Paint.valueOf("black"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    private void drawRobot(){
        robot.setFill(Color.BLACK);
        robot.setCenterX(world.getPositionXOfRobot());
        robot.setCenterY(world.getPositionYOfRobot());

        robot.setVisible(true);
    }

    private void drawHeaven(){
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

        transmitter1.setCenterX(world.getListOfTransmitters().get(0).getPositionX());
        transmitter1.setCenterY(world.getListOfTransmitters().get(0).getPositionY());
        transmitter2.setCenterX(world.getListOfTransmitters().get(1).getPositionX());
        transmitter2.setCenterY(world.getListOfTransmitters().get(1).getPositionY());
        transmitter3.setCenterX(world.getListOfTransmitters().get(2).getPositionX());
        transmitter3.setCenterY(world.getListOfTransmitters().get(2).getPositionY());

        line12.setVisible(true);
        line23.setVisible(true);
        line13.setVisible(true);

        transmitter1.setVisible(true);
        transmitter2.setVisible(true);
        transmitter3.setVisible(true);
    }

    @FXML
    public void restart() {
        initialize();
        System.gc();
        simButton.setDisable(false);
    }

    @FXML
    public void simulate() {
        world.getRobot().start();
        simButton.setDisable(true);
        restartButton.setDisable(false);
    }
}
