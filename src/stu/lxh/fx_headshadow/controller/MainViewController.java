package stu.lxh.fx_headshadow.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import stu.lxh.fx_headshadow.entity.Patient;
import stu.lxh.fx_headshadow.util.PropertiesUtil;
import stu.lxh.fx_headshadow.util.StageManager;
import stu.lxh.fx_headshadow.util.ThumbnailatorUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @FXML
    private ListView<Label> patientInformationListView;

    private Stage addNewPatientStage;
    private Stage headShadowStage;

    private static Stage primaryStage;
    private static SimpleDateFormat sdf;

    private Map<String, Button> imageButtonMap;
    private List<Label> labelList;

    /**
     * 病人的Map，病历号 ： 病人实例对象
     */
    private static Map<String, Patient> patientMap;

    @FXML
    private Label patientNameLabel;
    @FXML
    private Label patientCardNumberLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label firstConsultationTimeLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label dateOfBirthLabel;
    @FXML
    private Label patientContactPhoneLabel;
    @FXML
    private Label patientContactAddressLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button preDayButton;
    @FXML
    private Button nextDayButton;

    private Patient currentPatient;

    static {
        patientMap = new HashMap<>();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @FXML
    public void initialize() {
        imageButtonMap = new HashMap<>();
        labelList = new ArrayList<>();
        StageManager.addController("MainViewController", this);
        init();
        dealAction();
    }

    /**
     * 根据配置文件生成SqlSessionFactory
     * @return
     * @throws IOException
     */
    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "config/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        return sqlSessionFactory;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public void setCurrentPatient(Patient patient) {
        // TODO 将之前的一个patient的信息存储到数据库中后，在进行patient的设置

        currentPatient = patient;
        // 此时需要清空buttonMap中的内容，否则会将多余的图像加入到其他病患中
        imageButtonMap.clear();
    }

    private void init() {
        datePicker.setValue(LocalDate.now());
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

                    // 将本次的stage移除
                    addNewPatientStage.setOnCloseRequest(event1 -> {
                        StageManager.removeStage("addNewPatientStage");
                    });

                    addNewPatientStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // TODO 处理界面关闭
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
                        // TODO ????
                        ThumbnailatorUtil.generateScale(Double.parseDouble(PropertiesUtil.getValue("buttonImageRate", "config.properties")), file.getPath(), oriImagePath);
                        String targetImagePath =  PropertiesUtil.getValue("targetImagePath", "config.properties") + fileName;
                        ThumbnailatorUtil.generateScale(Double.parseDouble(PropertiesUtil.getValue("targetImagePathRate", "config.properties")), file.getPath(), targetImagePath);
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
                    // 每一次都进行更新
                    currentPatient.setPatientPhotoPath(imageButtonMap);
                    addPatientMap(currentPatient);
                }
            }
        });

        imageInfoPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            ObservableList<Node> children = imageInfoPane.getChildren();
            FXMLLoader fxmlLoader = new FXMLLoader();
            for(int i = 0; i < children.size(); i++) {
                Button button = (Button) children.get(i);
                button.setOnMousePressed(event1 -> {
                    // todo 新生成一个图像处理页面
                    try {
                        File file = new File(PropertiesUtil.getValue("oriImagePath", "config.properties") + button.getId());
                        HeadShadowViewController.setPatientImageFile(file);
                        System.out.println(file.getAbsolutePath());
                        // 如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象
                        fxmlLoader.setLocation(MainViewController.class.getResource("/stu/lxh/fx_headshadow/view/HeadShadow.fxml"));
                        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                        Parent root = fxmlLoader.load();
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

                        headShadowStage.setOnCloseRequest(event2 -> {
                            HeadShadowViewController controller = fxmlLoader.getController();
                            Map<String, Point2D> positionMap = controller.getPositionMap();
                            currentPatient.addPositionMap(button.getId(), positionMap);
                        });
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

        // 选中的某一个label为newValue的Label
        patientInformationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String text = newValue.getText();
                String cardNumber = text.split("  ")[1];
                Patient switchPatient = patientMap.get(cardNumber);
                currentPatient = switchPatient;

                // TODO 切换patient，基本信息的内容
                setLabelText(switchPatient);
                // TODO 设置图像信息，将原来的patient的图像信息清除，设置新的patient的图像信息
                imageInfoPane.getChildren().clear();
                Map<String, Button> buttonMap = switchPatient.getPatientPhotoPathMap();
                if(buttonMap.size() >= 1) {
                    for(String key : buttonMap.keySet()) {
                        Button button = buttonMap.get(key);
                        imageInfoPane.getChildren().add(button);
                    }
                }
            }
        });

        preDayButton.setOnMouseClicked(event -> {
            // 查询当天的病人，并进行渲染，dao层
        });

        nextDayButton.setOnMouseClicked(event -> {
            // 查询当天的病人，并进行渲染，dao层
        });
    }

    private void addPatientMap(Patient patient) {
        Patient oldPatient = patientMap.get(patient.getPatientCardNumber());
        System.out.println(oldPatient);
        if(oldPatient == null) {
            patientMap.put(patient.getPatientCardNumber(), patient);
        } else {
            // 替换病人信息，用于复诊等功能使用。
            patientMap.remove(patient.getPatientCardNumber());
            patientMap.put(patient.getPatientCardNumber(), patient);
        }
    }

    public void addPatient(Patient patient) {
        addPatientMap(patient);
        setLabelText(patient);

        Label label = new Label(patient.getPatientName() + "  " + patient.getPatientCardNumber());
        labelList.add(label);
        ObservableList<Label> labels = FXCollections.observableArrayList(labelList);
        patientInformationListView.setItems(null);
        patientInformationListView.setItems(labels);
        // 新病患肯定开始没有图像信息
        imageInfoPane.getChildren().clear();
    }

    private void setLabelText(Patient patient) {
        // 在基本信息显示处将患者基本信息显示出来，并在左边的病人列表中显示该患者。
        patientNameLabel.setText(patient.getPatientName());
        patientCardNumberLabel.setText(patient.getPatientCardNumber());
        ageLabel.setText(String.valueOf(patient.getAge()));
        firstConsultationTimeLabel.setText(String.valueOf(sdf.format(patient.getFirstConsultationTime())));
        genderLabel.setText(patient.getGender());
        dateOfBirthLabel.setText(sdf.format(patient.getDateOfBirth()));
        patientContactPhoneLabel.setText(patient.getPatientContactPhone());
        patientContactAddressLabel.setText(patient.getPatientCOntactAddress());
    }

    public static Map<String, Patient> getPatientMap() {
        return patientMap;
    }
}
