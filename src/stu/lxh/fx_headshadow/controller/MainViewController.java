package stu.lxh.fx_headshadow.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import stu.lxh.fx_headshadow.util.PropertiesUtil;
import stu.lxh.fx_headshadow.util.StageManager;
import stu.lxh.fx_headshadow.util.ThumbnailatorUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LXH on 2019/1/6.
 */
public class MainViewController {
    @FXML
    private Button addNewPatientButton;
    @FXML
    private Button manualImport;
    @FXML
    private FlowPane imageInfoPane;

    private Stage addNewPatientStage;
    private Stage headShadowStage;

    private static Stage primaryStage;

    private Map<String, Button> imageButtonMap;

    @FXML
    public void initialize() {
        imageButtonMap = new HashMap<>();
        init();
        dealAction();
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    private void init() {
    }

    private void dealAction() {
        addNewPatientButton.addEventFilter(MouseEvent.ANY, event -> {
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                try {
                    Parent root = FXMLLoader.load(MainViewController.class.getResource("../view/AddPatientView.fxml"));
                    Scene scene = new Scene(root, 600, 400);
                    addNewPatientStage = new Stage();
                    addNewPatientStage.initModality(Modality.APPLICATION_MODAL);
                    addNewPatientStage.setScene(scene);

                    // 将子窗口保存在StageManager的map中
                    StageManager.addStage("addNewPatientStage", addNewPatientStage);
                    StageManager.addController("MainViewStage", this);
                    addNewPatientStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        manualImport.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("导入图像");
            File file = fileChooser.showOpenDialog(primaryStage);
            if(file != null) {
                // TODO 判断是否为图片
                String fileName = file.getName();
                System.out.println("file.getAbsolutePath()：" + file.getAbsolutePath());
                System.out.println("file.getPath()：" + file.getPath());
                // bmp,jpg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,WMF,webp
                if(!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".bmp")) {
                    // 不是图片格式，提示框
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("There is a information !!!");
                    alert.setContentText("请您选择一个图片格式");

                    alert.showAndWait();
                } else {
                    // 是图片格式
                    Image image = null;
                    try {
                        String oriImagePath = PropertiesUtil.getValue("oriImagePath", "config.properties") + fileName;
                        ThumbnailatorUtil.generateScale(0.5, file.getPath(), oriImagePath);
                        String targetImagePath =  PropertiesUtil.getValue("targetImagePath", "config.properties") + fileName;
                        ThumbnailatorUtil.generateScale(0.1, file.getPath(), targetImagePath);
                        image = new Image(new FileInputStream(new File(targetImagePath)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(image != null) {
                        Button button = new Button();
                        button.setPrefSize(130, 170);
                        button.setGraphic(new ImageView(image));
                        button.setId(fileName);
                        imageButtonMap.put(fileName, button);
                        imageInfoPane.getChildren().add(button);
                    }
                }
            }
        });

        imageInfoPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            ObservableList<Node> children = imageInfoPane.getChildren();
            for(int i = 0; i < children.size(); i++) {
                Button button = (Button) children.get(i);
                button.setOnMousePressed(event1 -> {
                    // todo 新生成一个图像处理页面
                    try {
                        File file = new File(PropertiesUtil.getValue("oriImagePath", "config.properties") + button.getId());
                        HeadShadowViewController.setPatientImageFile(file);
                        System.out.println(file.getAbsolutePath());
                        Parent root = FXMLLoader.load(MainViewController.class.getResource("/stu/lxh/fx_headshadow/view/HeadShadow.fxml"));
                        Scene scene = new Scene(root, 1000, 200);


                        Screen screen = Screen.getPrimary();
                        Rectangle2D bounds = screen.getVisualBounds();

                        headShadowStage = new Stage();
                        headShadowStage.setX(bounds.getMinX());
                        headShadowStage.setY(bounds.getMinY());
                        headShadowStage.setWidth(bounds.getWidth());
                        headShadowStage.setHeight(bounds.getHeight());

                        headShadowStage.initModality(Modality.APPLICATION_MODAL);
                        headShadowStage.setScene(scene);

                        // 将子窗口保存在StageManager的map中
                        StageManager.addStage("headShadowStage", headShadowStage);
                        StageManager.addController("MainViewStage", this);
                        headShadowStage.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }
}
