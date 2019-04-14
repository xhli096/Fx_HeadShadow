package stu.lxh.fx_headshadow.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.sf.json.JSONObject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stu.lxh.fx_headshadow.dao.AddPatientViewMapper;
import stu.lxh.fx_headshadow.dao.PatientInfoMapper;
import stu.lxh.fx_headshadow.entity.ButtonInfo;
import stu.lxh.fx_headshadow.entity.Patient;
import stu.lxh.fx_headshadow.util.PropertiesUtil;
import stu.lxh.fx_headshadow.util.StageManager;
import stu.lxh.fx_headshadow.util.ThumbnailatorUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by LXH on 2019/1/6.
 */
public class MainViewController {
    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);
    
    @FXML
    private Button addNewPatientButton;
    @FXML
    private Button manualImport;
    @FXML
    private FlowPane imageInfoPane;

    @FXML
    private ListView<Label> patientInformationListView;
    @FXML
    private ListView<Label> allPatientListView;

    private Stage addNewPatientStage;
    private Stage headShadowStage;
    private Stage activationStage;

    private static Stage primaryStage;
    private static SimpleDateFormat sdf;

    private Map<String, Button> imageButtonMap;
    private List<Label> labelList;
    private List<Label> allPatientLabelList;

    /**
     * 病人的Map，病历号 ： 病人实例对象
     */
    private static Map<String, Patient> patientMap;
    private static Map<String, Patient> preDayPatientMap;
    private static Map<String, Patient> nextDayPatientMap;
    private static Map<String, Patient> operationPatientMap;

    private static Map<String, Patient> allPatientsMap;

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

    @FXML
    private Button activationButton;

    private static Patient currentPatient;
    // 记录前一天
    private LocalDate preDay;
    // 记录后一天
    private LocalDate nextDay;

    private static Map<String, Map<String, Button>> patientPhotoPathMap;

    static {
        patientMap = new LinkedHashMap<>();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        patientPhotoPathMap = new LinkedHashMap<>();
        preDayPatientMap = new LinkedHashMap<>();
        nextDayPatientMap = new LinkedHashMap<>();
        operationPatientMap = new LinkedHashMap<>();
        allPatientsMap = new LinkedHashMap<>();
    }

    @FXML
    public void initialize() {
        imageButtonMap = new LinkedHashMap<>();
        labelList = new ArrayList<>();
        allPatientLabelList = new ArrayList<>();
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

    /**
     * 获得sqlSession
     * @return
     * @throws IOException
     */
    private SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        return sqlSession;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public void setCurrentPatient(Patient patient) {
        // 此时需要清空buttonMap中的内容，否则会将多余的图像加入到其他病患中
        imageButtonMap.clear();
        // TODO 将之前的一个patient的信息存储到数据库中后，在进行patient的设置
        currentPatient = patient;
        operationPatientMap = patientMap;
    }

    private void init() {
        datePicker.setValue(LocalDate.now());
        preDay = LocalDate.now().minusDays(1);
        nextDay = LocalDate.now().plusDays(1);
        // 初始化全部患者列表
        initAllPatientsListView();
        // 添加一个根据listview初始化的方法
        initListView();
    }

    /**
     * 初始化所有病人列表
     */
    private void initAllPatientsListView() {
        allPatientLabelList.clear();
        System.out.println("allPatientsMap.size():" + allPatientsMap.size());

        for(String key : allPatientsMap.keySet()) {
            Patient patient = allPatientsMap.get(key);
            Label label = new Label(patient.getPatientName() + "  " + patient.getPatientCardNumber());
            allPatientLabelList.add(label);
        }
        ObservableList<Label> labels = FXCollections.observableArrayList(allPatientLabelList);
        System.out.println(labels.size());
        allPatientListView.setItems(null);
        allPatientListView.setItems(labels);
        allPatientListView.getSelectionModel().selectFirst();
        if(allPatientsMap.size() >= 1) {
            String key = (String) allPatientsMap.keySet().toArray()[0];
            String text = allPatientsMap.get(key).getPatientCardNumber();
            operationPatientMap = allPatientsMap;
            reinitPaitentInfoAndImage(text);
        }
    }

    /**
     * 根据不同的patientMap，初始化病人列表
     * @param patientMap
     * patientMap, prePatientMap, nextDayPatient
     */
    private void initPatientList(Map<String, Patient> patientMap) {
        labelList.clear();
        for(String key : patientMap.keySet()) {
            Patient patient = patientMap.get(key);
            Label label = new Label(patient.getPatientName() + "  " + patient.getPatientCardNumber());
            labelList.add(label);
        }
        ObservableList<Label> labels = FXCollections.observableArrayList(labelList);
        System.out.println(labels.size());
        patientInformationListView.setItems(null);
        patientInformationListView.setItems(labels);
        patientInformationListView.getSelectionModel().selectFirst();
        if(patientMap.size() >= 1) {
            String key = (String) patientMap.keySet().toArray()[0];
            String text = patientMap.get(key).getPatientCardNumber();
            // 设置当前操作的Map为所有的病人map
            operationPatientMap = patientMap;
            reinitPaitentInfoAndImage(text);
        }
    }

    /**
     * 软件启动，初始化listView；
     */
    private void initListView() {
        // 恢复listview
        initPatientList(patientMap);
    }

    /**
     * 读取日志恢复前一天或后一天的列表
     * @param localDate
     * @param string
     */
    private void readLog(LocalDate localDate, String string) {
        Map<String, Patient> patientMap = new LinkedHashMap<>();
        try {
            File logDir = new File("log");
            String filePath = logDir + "\\" + localDate + ".log";
            System.out.println("filePath:" + filePath);
            // FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)));

            String str;
            boolean nextIsPatient = false;
            boolean nextIsPatientImage = false;
            boolean nextIsPatientImagePointInfo = false;
            // 按行读取字符串
            while ((str = bufferedReader.readLine()) != null) {
                // 设置count的值
                if(str.startsWith("病历序号：")){
                    int count = Integer.parseInt(str.substring(str.indexOf("：")+1));
                    AddPatientViewController.setCount(count);
                }
                if(str.startsWith("病人信息：")) {
                    nextIsPatient = true;
                    continue;
                }
                if(str.startsWith("病人图像信息：")) {
                    nextIsPatientImage = true;
                    nextIsPatient = false;
                    continue;
                }
                if(str.startsWith("病人图像点位置信息：")) {
                    nextIsPatientImagePointInfo = true;
                    nextIsPatientImage = false;
                    nextIsPatient = false;
                    continue;
                }

                // 进入处理病人信息
                if(nextIsPatient) {
                    JSONObject jsonObject = JSONObject.fromObject(str);
                    Patient patient = (Patient) JSONObject.toBean(jsonObject, Patient.class);
                    patientMap.put(patient.getPatientCardNumber(), patient);
                    System.out.println(patient);
                }
                if(nextIsPatientImage) {
                    String cardNumber = str.split(" ")[0];
                    int count = Integer.parseInt(str.split(" ")[1]);
                    System.out.println("cardNumber:" + cardNumber);
                    for(int i = 0; i < count; i++) {
                        if((str = bufferedReader.readLine()) != null) {
                            JSONObject jsonObject = JSONObject.fromObject(str.split(" ")[1]);
                            ButtonInfo buttonInfo = (ButtonInfo) JSONObject.toBean(jsonObject, ButtonInfo.class);
                            patientMap.get(cardNumber).getPatientPhotoPathMap().put(str.split(" ")[0], buttonInfo);
                        }
                    }
                }
                if(nextIsPatientImagePointInfo) {
                    System.out.println("str:" + str);
                    // TODO 处理点的问题
                    String cardNumber = str.split(" ")[0];
                    int count = Integer.parseInt(str.split(" ")[1]);
                    int mem;
                    System.out.println("cardNumber:" + cardNumber);
                    Patient patient = patientMap.get(cardNumber);
                    Map<String, Map<String, Point2D>> result = new HashMap<>();
                    for(int i = 0; i < count; i++) {
                        if((str = bufferedReader.readLine()) != null) {
                            String buttonId = str.split(" ")[0];
                            mem = Integer.parseInt(str.split(" ")[1]);
                            Map<String, Point2D> point2DMap = new HashMap<>();
                            for(int j = 0; j < mem; j++) {
                                if((str = bufferedReader.readLine()) != null) {
                                    String[] values = str.split(" ");
                                    String pointName = values[0];
                                    Point2D point2D = new Point2D(Double.parseDouble(values[1]), Double.parseDouble(values[2]));
                                    point2DMap.put(pointName, point2D);
                                }
                            }
                            if(point2DMap.size() > 0) {
                                result.put(buttonId, point2DMap);
                            }
                        }
                    }
                    patient.setPointPositionMap(result);
                }
            }
            System.out.println("=======================================================================");
            for(String cardNumber : patientMap.keySet()) {
                Patient patient = patientMap.get(cardNumber);
                System.out.println(patient.toString());
                Map<String, Map<String, Point2D>> pointPositionMap = patient.getPointPositionMap();

                System.out.println(pointPositionMap);
/*
            for(String key : pointPositionMap.keySet()){
                Map<String, Point2D> stringPoint2DMap = pointPositionMap.get(key);
                System.out.println(stringPoint2DMap);
                for(String k : stringPoint2DMap.keySet()) {
                    Point2D point2D = stringPoint2DMap.get(k);
                    System.out.println(point2D);
                }
            }
*/
            }
            System.out.println("======================================================================");

            // 至此读取完整的日志信息，可以恢复列表
            if(string.equals("pre")) {
                MainViewController.setPreDayPatientMap(patientMap);
            } else if(string.equals("next")) {
                MainViewController.setNextDayPatientMap(patientMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化前一天病人列表信息
     */
    private void initPreDayPatientListView() {
        // 首先来查询，先清除，再查询
        preDayPatientMap.clear();
        readLog(preDay, "pre");

        for(String key : preDayPatientMap.keySet()) {
            System.out.println(preDayPatientMap.get(key));
        }

        operationPatientMap = preDayPatientMap;
        System.out.println("preDayPatientMap:" + preDayPatientMap);
        initPatientList(preDayPatientMap);
    }

    /**
     * 初始化后一天病人列表信息
     */
    private void initNextDayPatientListView() {
        // 首先来查询，先清除，再查询
        nextDayPatientMap.clear();
        readLog(nextDay, "next");

        for(String key : preDayPatientMap.keySet()) {
            System.out.println(preDayPatientMap.get(key));
        }

        operationPatientMap = nextDayPatientMap;
        initPatientList(nextDayPatientMap);
    }

    private void dealAction() {
        /**
         * 添加新的患者
         */
        addNewPatientButton.addEventFilter(MouseEvent.ANY, event -> {
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                try {
                    Parent root = FXMLLoader.load(MainViewController.class.getClassLoader().getResource("stu/lxh/fx_headshadow/view/AddPatientView.fxml"));
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

        /**
         * 导入图像按钮
         */
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
                    String oriImagePath = PropertiesUtil.getValue("oriImagePath", "config.properties") + fileName;
                    try {
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

                        ButtonInfo buttonInfo = new ButtonInfo(button.getId(), PropertiesUtil.getValue("targetImagePath", "config.properties") + fileName, button.getPrefWidth(), button.getPrefHeight());

                        currentPatient.addPatientPhotoPath(fileName, buttonInfo);
                        imageInfoPane.getChildren().add(button);
                        // 对数据库中的图像信息进行更新
                        updatePatientImageInfo(currentPatient, buttonInfo);
                    }
                    // 每一次都进行更新，加入到Map中去
                    addPatientMap(currentPatient);
                }
            }
        });

        /**
         * 点击某一个按钮，生成头影标定的页面
         */
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

                        Map<String, Map<String, Point2D>> pointPositionMap = currentPatient.getPointPositionMap();
                        // 如果当前患者是个新患者，则pointPointPositionMap为空，这需要重置HeadShadowViewController中的Map为空，不能使用以前的map
                        if(pointPositionMap.size() == 0) {
                            HeadShadowViewController.setPositionMap(new HashMap<>());
                        }
                        for(String key : pointPositionMap.keySet()) {
                            if(key.equals(button.getId())) {
                                Map<String, Point2D> stringPoint2DMap = pointPositionMap.get(key);
                                System.out.println(key + " " + stringPoint2DMap.size());
                                HeadShadowViewController.setPositionMap(stringPoint2DMap);
                            }
                        }
                        // 如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象
                        fxmlLoader.setLocation(MainViewController.class.getClassLoader().getResource("stu/lxh/fx_headshadow/view/HeadShadow.fxml"));
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

                            SqlSession sqlSession = null;
                            try {
                                sqlSession = getSqlSession();
                                PatientInfoMapper patientInfoMapper = sqlSession.getMapper(PatientInfoMapper.class);
                                for(String key : positionMap.keySet()) {
                                    patientInfoMapper.insertImagePosition(currentPatient.getPatientCardNumber(), button.getId(), key, positionMap.get(key).getX(), positionMap.get(key).getY());
                                }

                                sqlSession.commit();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                sqlSession.close();
                            }
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

        /**
         * 点击患者列表
         */
        // 选中的某一个label为newValue的Label
        patientInformationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String text = newValue.getText();
                String cardNumber = text.split("  ")[1];
                currentPatient = patientMap.get(cardNumber);
                reinitPaitentInfoAndImage(cardNumber);
            }
        });

        /**
         * 全部患者的列表
         */
        allPatientListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String text = newValue.getText();
                String cardNumber = text.split("  ")[1];
                operationPatientMap = allPatientsMap;
                currentPatient = allPatientsMap.get(cardNumber);
                reinitPaitentInfoAndImage(cardNumber);
            }
        });

        /**
         * 点击前一天按钮
         */
        preDayButton.setOnMouseClicked(event -> {
            // 查询当天的病人，并进行渲染，dao层
            // datePicker设置为前一天
            datePicker.setValue(preDay);
            if(preDay.toString().equals(LocalDate.now().toString())) {
                initListView();
            } else {
                // 从日志文件中读取，比数据库中要快。
                initPreDayPatientListView();
            }

            preDay = preDay.minusDays(1);
            nextDay = nextDay.minusDays(1);
        });

        /**
         * 点击下一天按钮
         */
        nextDayButton.setOnMouseClicked(event -> {
            // 查询当天的病人，并进行渲染，dao层
            // datePicker设置为后一天
            datePicker.setValue(nextDay);
            System.out.println("nextDay.toString().equals(LocalDate.now().toString()):" + nextDay.toString().equals(LocalDate.now().toString()));
            if(nextDay.toString().equals(LocalDate.now().toString())) {
                initListView();
            } else {
                // 从日志文件中读取，比数据库中要快。
                initNextDayPatientListView();
            }

            preDay = preDay.plusDays(1);
            nextDay = nextDay.plusDays(1);
        });

        /**
         * 点击激活按钮
         */
        activationButton.setOnMouseClicked(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader();

            Parent root = null;
            try {
                fxmlLoader.setLocation(MainViewController.class.getClassLoader().getResource("stu/lxh/fx_headshadow/view/ActivationView.fxml"));
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                root = fxmlLoader.load();

                Scene scene = new Scene(root, 600, 400);
                activationStage = new Stage();
                activationStage.initModality(Modality.APPLICATION_MODAL);
                activationStage.setScene(scene);

                // 将子窗口保存在StageManager的map中
                StageManager.addStage("activationStage", activationStage);
                StageManager.addController("MainViewStage", this);
                activationStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 重新初始化病人信息和病人图像信息
     */
    private void reinitPaitentInfoAndImage(String cardNumber) {
        // 设置一个optionMap，进行相关的转换。
        System.out.println("operationPatientMap.size() :" + operationPatientMap.size());
        Patient switchPatient = operationPatientMap.get(cardNumber);
        currentPatient = switchPatient;
        System.out.println("switchPatient == null:" + (switchPatient == null));

        // TODO 切换patient，基本信息的内容
        setLabelText(switchPatient);
        // TODO 设置图像信息，将原来的patient的图像信息清除，设置新的patient的图像信息
        imageInfoPane.getChildren().clear();
        Map<String, ButtonInfo> buttonInfoMap = switchPatient.getPatientPhotoPathMap();
        if(buttonInfoMap != null && buttonInfoMap.size() >= 1) {
            for(String key : buttonInfoMap.keySet()) {
                try {
                    ButtonInfo buttonInfo = buttonInfoMap.get(key);
                    Button button = new Button();
                    button.setPrefSize(buttonInfo.getWidth(), buttonInfo.getHeight());
                    button.setGraphic(new ImageView(new Image(new FileInputStream(new File(buttonInfo.getImagePath())))));
                    button.setId(buttonInfo.getButtonId());
                    imageInfoPane.getChildren().add(button);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void recoveryListView(String string) {
        if(string.equals("pre")) {
            preDayPatientMap = new LinkedHashMap<>();
        } else if(string.equals("next")) {
            nextDayPatientMap = new LinkedHashMap<>();
        }
    }

    private Date parseLocalDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();

        Date result = Date.from(instant);

        return result;
    }

    /**
     * 将某个病人的图像信息更新到数据库中
     * @param patient      正在操作的病人
     * @param buttonInfo    按钮信息
     */
    private void updatePatientImageInfo(Patient patient, ButtonInfo buttonInfo) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
            AddPatientViewMapper addPatientViewMapper = sqlSession.getMapper(AddPatientViewMapper.class);
            addPatientViewMapper.insertPatientPhotoPath(patient.getPatientCardNumber(), buttonInfo);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
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

    /**
     * 增加某个病人
     * @param patient   增加的病人
     */
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
        System.out.println("patient:" + (patient == null));
        patientNameLabel.setText(patient.getPatientName());
        patientCardNumberLabel.setText(patient.getPatientCardNumber());
        ageLabel.setText(String.valueOf(patient.getAge()));
        if(patient.getFirstConsultationTime() != null) {
            firstConsultationTimeLabel.setText(String.valueOf(sdf.format(patient.getFirstConsultationTime())));
        }
        genderLabel.setText(patient.getGender());
        dateOfBirthLabel.setText(sdf.format(patient.getDateOfBirth()));
        patientContactPhoneLabel.setText(patient.getPatientContactPhone());
        patientContactAddressLabel.setText(patient.getPatientContactAddress());
    }

    public static Patient getCurrentPatient() {
        return currentPatient;
    }

    public static Map<String, Patient> getPatientMap() {
        return patientMap;
    }

    public static Map<String, Map<String, Button>> getPatientPhotoPathMap() {
        return patientPhotoPathMap;
    }

    public static void setPatientMap(Map<String, Patient> patientMap) {
        MainViewController.patientMap = patientMap;
    }

    public ListView<Label> getPatientInformationListView() {
        return patientInformationListView;
    }

    public void setPatientInformationListView(ListView<Label> patientInformationListView) {
        this.patientInformationListView = patientInformationListView;
    }

    public static Map<String, Patient> getPreDayPatientMap() {
        return preDayPatientMap;
    }

    public static void setPreDayPatientMap(Map<String, Patient> preDayPatientMap) {
        MainViewController.preDayPatientMap = preDayPatientMap;
    }

    public static Map<String, Patient> getNextDayPatientMap() {
        return nextDayPatientMap;
    }

    public static void setNextDayPatientMap(Map<String, Patient> nextDayPatientMap) {
        MainViewController.nextDayPatientMap = nextDayPatientMap;
    }

    public static Map<String, Patient> getAllPatientsMap() {
        return allPatientsMap;
    }

    public static void setAllPatientsMap(Map<String, Patient> allPatientsMap) {
        MainViewController.allPatientsMap = allPatientsMap;
    }
}
