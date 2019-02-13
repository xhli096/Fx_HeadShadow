package stu.lxh.fx_headshadow.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import stu.lxh.fx_headshadow.controller.MainViewController;

/**
 * Created by LXH on 2019/1/6.
 */
public class Main extends Application {
    private Stage primaryStage;

    private Parent root;
    private AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        root = FXMLLoader.load(Main.class.getResource("../view/MainView.fxml"));
        this.primaryStage.setTitle("头影测量");
        // TODO 传递主stage
        MainViewController.setPrimaryStage(this.primaryStage);
        // TODO 初始化patientTreeView

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        this.primaryStage.setX(bounds.getMinX());
        this.primaryStage.setY(bounds.getMinY());
        this.primaryStage.setWidth(bounds.getWidth());
        this.primaryStage.setHeight(bounds.getHeight());

        this.primaryStage.setScene(new Scene(root, 1000, 200));
        this.primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        //  TODO 删除所有临时图片
    }

    public static void main(String[] args) {
        launch(args);
    }
}
