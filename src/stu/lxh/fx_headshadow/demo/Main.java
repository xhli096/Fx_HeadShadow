package stu.lxh.fx_headshadow.demo;

import com.alibaba.fastjson.JSONObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import stu.lxh.fx_headshadow.controller.MainViewController;
import stu.lxh.fx_headshadow.entity.Patient;
import stu.lxh.fx_headshadow.util.StageManager;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by LXH on 2019/1/6.
 */
public class Main extends Application {
    private Stage primaryStage;

    private Parent root;
    private AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO 读取日志文件，恢复列表等
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("../view/MainView.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        root = fxmlLoader.load();
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

        this.primaryStage.setOnCloseRequest((WindowEvent event) -> {
            // TODO 完成当天日志信息的写入，用于再次启动读取信息恢复。将所有的patient写json对象形式
            System.out.println("监测到主界面的关闭动作");
            File logDir = new File("resources/log");
            String filePath = logDir + "\\" + LocalDate.now() + ".log";
            if(!logDir.exists()) {
                try {
                    logDir.mkdir();
                    FileOutputStream fos = new FileOutputStream(filePath);
                    fos.write(0);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 将得到的patientMap的Patient以字符串形式写入到日志文件中
            Map<String, Patient> patientMap = MainViewController.getPatientMap();
            // 将json字符串转化为object
            // JSONObject jsonObject = JSONObject.parseObject();
        });

        this.primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
