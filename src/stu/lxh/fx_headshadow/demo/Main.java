package stu.lxh.fx_headshadow.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stu.lxh.fx_headshadow.controller.AddPatientViewController;
import stu.lxh.fx_headshadow.controller.MainViewController;
import stu.lxh.fx_headshadow.dao.ActivationUserMapper;
import stu.lxh.fx_headshadow.dao.PatientInfoMapper;
import stu.lxh.fx_headshadow.entity.ActivationUser;
import stu.lxh.fx_headshadow.entity.ButtonInfo;
import stu.lxh.fx_headshadow.entity.Patient;
import stu.lxh.fx_headshadow.util.ComputerUtil;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by LXH on 2019/1/6.
 */
public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private Stage primaryStage;

    private Parent root;
    private AnchorPane rootLayout;

    private Map<String, Patient> patientMap;

    private String cardNumber;
    private int count;

    /**
     * 表示尚未激活
     */
    private static final int NOT_ACTIVATION = 0;
    /**
     * 表示已经激活
     */
    private static final int ACTIVATION = 1;

    private static boolean save;

    static {
        save = false;
    }

    @Override
    public void start(Stage primaryStage) {
        // TODO 读取日志文件，恢复列表等
        patientMap = new LinkedHashMap<>();
        try {
            readTodayLog();
            // 至此读取完整的日志信息，可以恢复列表
            MainViewController.setPatientMap(patientMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO 读取数据库中的信息，验证本计算机是否激活了
        String databaseSerial = ComputerUtil.getCpuSerial() + "_" + ComputerUtil.getLocalMac() +  "_" + ComputerUtil.getOsName();
        try {
            SqlSession sqlSession = getSqlSession();
            ActivationUserMapper activationUserMapper = sqlSession.getMapper(ActivationUserMapper.class);
            ActivationUser activationUser = activationUserMapper.getUserBySerial(databaseSerial);
            if(activationUser == null || activationUser.getActivation() == NOT_ACTIVATION) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Warning Information");
                alert.setContentText("由于您尚未激活本软件，您的病人信息和图像信息将不会被保存！！！");
                save = false;
                alert.showAndWait();
            } else {
                save = true;
                try {
                    initAllPatient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.primaryStage = primaryStage;
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getClassLoader().getResource("stu/lxh/fx_headshadow/view/MainView.fxml"));
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
            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * 初始化全部病人信息，从数据库中读取相关信息
     */
    private void initAllPatient() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
            PatientInfoMapper patientInfoMapper = sqlSession.getMapper(PatientInfoMapper.class);
            List<Patient> allPatients = patientInfoMapper.getAllPatients();
            sqlSession.commit();

            for(Patient patient : allPatients) {
               sqlSession = getSqlSession();
               patientInfoMapper = sqlSession.getMapper(PatientInfoMapper.class);
               List<ButtonInfo> buttonInfoList = patientInfoMapper.getPatientImageInfo(patient.getPatientCardNumber());
               sqlSession.commit();

               Map<String, ButtonInfo> buttonInfoMap = new LinkedHashMap<>();
               for(ButtonInfo buttonInfo : buttonInfoList) {
                   buttonInfoMap.put(buttonInfo.getButtonId(), buttonInfo);
               }
               patient.setPatientPhotoPathMap(buttonInfoMap);
            }
            Map<String, Patient> allPatientsMap = new LinkedHashMap<>();
            for(Patient patient : allPatients) {
                allPatientsMap.put(patient.getPatientCardNumber(), patient);
            }
            System.out.println("allPatientMap.size() : " + allPatientsMap.size());
            MainViewController.setAllPatientsMap(allPatientsMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 读取日志文件恢复当日的病人列表信息
     * @throws IOException
     */
    private void readTodayLog() throws IOException {
        File logDir = new File("log");
        String filePath = logDir + "\\" + LocalDate.now() + ".log";
        System.out.println("filePath:" + filePath);
        // FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(filePath)));
        System.out.println(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(filePath)));
        String str;
        boolean nextIsPatient = false;
        boolean nextIsPatientImage = false;
        boolean nextIsPatientImagePosition = false;
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
                nextIsPatientImagePosition = true;
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
                cardNumber = str.split(" ")[0];
                count = Integer.parseInt(str.split(" ")[1]);
                System.out.println("cardNumber:" + cardNumber);
                for(int i = 0; i < count; i++) {
                    if((str = bufferedReader.readLine()) != null) {
                        JSONObject jsonObject = JSONObject.fromObject(str.split(" ")[1]);
                        ButtonInfo buttonInfo = (ButtonInfo) JSONObject.toBean(jsonObject, ButtonInfo.class);
                        patientMap.get(cardNumber).getPatientPhotoPathMap().put(str.split(" ")[0], buttonInfo);
                    }
                }
            }

            if(nextIsPatientImagePosition) {
                cardNumber = str.split(" ")[0];
                count = Integer.parseInt(str.split(" ")[1]);
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
    }


    /**
     * 将json字符串转为Map结构
     * 如果json复杂，结果可能是map嵌套map
     * @param jsonStr 入参，json格式字符串
     * @return 返回一个map
     */
    public static Map<String, Object> json2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        if(jsonStr != null && !"".equals(jsonStr)){
            //最外层解析
            JSONObject json = JSONObject.fromObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //如果内层还是数组的话，继续解析
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    Iterator<JSONObject> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = it.next();
                        list.add(json2Map(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        }else{
            return null;
        }
    }

    @Override
    public void stop() throws Exception {
        // TODO 完成当天日志信息的写入，用于再次启动读取信息恢复。将所有的patient写json对象形式
        System.out.println("监测到主界面的关闭动作");

        // TODO 如果当前计算机已经使用序列号进行激活，则进行save，否则则不保存信息。
        if(save) {
            File logDir = new File("log");
            String filePath = logDir + "\\" + LocalDate.now() + ".log";

            FileWriter fileWriter = new FileWriter(new File("resources/" + filePath));
            fileWriter.write("日志日期：" + LocalDate.now() + "\n");
            // 将currentCount写入日志文件，以便下次自动生成病历号时使用
            int curCount = AddPatientViewController.getCount();
            fileWriter.write("病历序号：" + curCount + "\n");
            // 将得到的patientMap的Patient以字符串形式写入到日志文件中
            Map<String, Patient> patientMap = MainViewController.getPatientMap();
            System.out.println("patientMap.size() :" + patientMap.size());
            fileWriter.write("病人信息：\n");
            for(String key : patientMap.keySet()) {
                Patient patient = patientMap.get(key);
                JSONObject jsonObject = JSONObject.fromObject(patient);
                fileWriter.write(jsonObject.toString() + "\n");
            }

            fileWriter.write("病人图像信息：\n");
            for(String key : patientMap.keySet()) {
                Patient patient = patientMap.get(key);
                Map<String, ButtonInfo> patientPhotoPathMap = patient.getPatientPhotoPathMap();
                fileWriter.write(patient.getPatientCardNumber() + " " + patientPhotoPathMap.size() + "\n");
                for(String k : patientPhotoPathMap.keySet()) {
                    ButtonInfo buttonInfo = patientPhotoPathMap.get(k);
                    JSONObject jsonObject = JSONObject.fromObject(buttonInfo);
                    fileWriter.write(k + " " + jsonObject.toString() + "\n");
                }
            }

            fileWriter.write("病人图像点位置信息：\n");
            for(String key : patientMap.keySet()) {
                Patient patient = patientMap.get(key);
                Map<String, Map<String, Point2D>> pointPositionMap = patient.getPointPositionMap();
                fileWriter.write(patient.getPatientCardNumber() + " " + pointPositionMap.size() + "\n");
                for(String k : pointPositionMap.keySet()) {
                    Map<String, Point2D> stringPoint2DMap = pointPositionMap.get(k);
                    fileWriter.write(k + " " + stringPoint2DMap.size() + "\n");
                    for(String ke : stringPoint2DMap.keySet()) {
                        Point2D point2D = stringPoint2DMap.get(ke);
                        fileWriter.write(ke + " " + point2D.getX() + " " + point2D.getY() + "\n");
                    }
                }
            }

            fileWriter.close();
            System.out.println("写入完成");
        }
    }


    public static boolean isSave() {
        return save;
    }

    public static void setSave(boolean save) {
        Main.save = save;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
