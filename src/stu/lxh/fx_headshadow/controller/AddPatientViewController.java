package stu.lxh.fx_headshadow.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import stu.lxh.fx_headshadow.dao.AddPatientViewMapper;
import stu.lxh.fx_headshadow.entity.Patient;
import stu.lxh.fx_headshadow.util.StageManager;
import stu.lxh.fx_headshadow.util.SwingUtil;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LXH on 2019/1/11.
 */
public class AddPatientViewController {
    private ToggleGroup rbtnSexGroup;
    private ToggleGroup firstGroup;

    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton nothingRadioButton;
    @FXML
    private RadioButton firstConsultationRadioButton;
    @FXML
    private RadioButton furtherConsultationRadioButton;
    @FXML
    private Button submitButton;

    @FXML
    private TextField patientCardNumberTextField;
    @FXML
    private TextField patientNameTextField;
    @FXML
    private TextField patientAgeTextField;
    @FXML
    private TextField doctorTextField;
    @FXML
    private ComboBox<String> birthYearComboBox;
    @FXML
    private ComboBox<String> birthMonthComboBox;
    @FXML
    private ComboBox<String> birthDayComboBox;
    @FXML
    private DatePicker consultationTime;
    @FXML
    private TextField contactPhoneTextField;
    @FXML
    private TextField contractAddressTextField;

    private static Date preDate;

    private static SimpleDateFormat cardSdf;
    private static SimpleDateFormat sdf;

    private static int count = 0;

    static {
        cardSdf= new SimpleDateFormat("yyyyMMdd");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @FXML
    public void initialize() {
        init();
        reinit();
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

    /**
     * 再次初始化
     */
    private void reinit() {
        // 初始化月
        SwingUtil.initComboBoxList(birthMonthComboBox, 1, 12);
        // 初始化年
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        if(preDate == null) {
            preDate = currentDate;
        } else {
            String cur = sdf.format(currentDate);
            String pre = sdf.format(preDate);
            if(!cur.equals(pre)) {
                preDate = currentDate;
                count = 0;
            }
        }
        int curYear = calendar.get(Calendar.YEAR);
        int minYear = curYear - 110;
        SwingUtil.initComboBoxList(birthYearComboBox, minYear, curYear);
        reinitBirthDate();

        // 自动生成病历号
        StringBuffer sb = new StringBuffer();
        StringBuffer stringBuffer = new StringBuffer();
        int index = 3 - String.valueOf(++count).length();
        for(int i = 0; i < index; i++) {
            sb.append("0");
        }
        sb.append(count);
        stringBuffer.append(cardSdf.format(preDate) + sb.toString());
        patientCardNumberTextField.setText(stringBuffer.toString());
    }

    /**
     * 再次初始化“日”
     */
    private void reinitBirthDate() {
        int curYear = Integer.parseInt(birthYearComboBox.getValue());
        int curMonth = Integer.parseInt(birthMonthComboBox.getValue());
        SwingUtil.setDateComboBox(birthDayComboBox, curYear, curMonth);
    }

    /**
     * 处理各种点击事件
     */
    private void dealAction() {
        final SqlSession[] sqlSession = {null};
        final int[] patientAge = {0};
        final Date[] firstConsultationTime = {null};

        birthYearComboBox.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> reinitBirthDate());
        birthMonthComboBox.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> reinitBirthDate());


        submitButton.setOnMouseClicked((MouseEvent event) -> {
            try {
                MainViewController mainViewController = (MainViewController) StageManager.getController("MainViewStage");
                String patientCardNumber = patientCardNumberTextField.getText();
                String patientName = patientNameTextField.getText();
                String gender = null;

                if(femaleRadioButton.isSelected()) {
                    gender = "男";
                } else if(femaleRadioButton.isSelected()) {
                    gender = "女";
                } else if(nothingRadioButton.isSelected()) {
                    gender = "未填";
                }

                if(patientAgeTextField.getText() != null &&  patientAgeTextField.getText().length() != 0) {
                    patientAge[0] = Integer.parseInt(patientAgeTextField.getText());
                }

                String birthYear = birthYearComboBox.getValue();
                String birthMonth = birthMonthComboBox.getValue();
                String birthDay = birthDayComboBox.getValue();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(birthYear + "-" + birthMonth + "-" + birthDay);

                if(consultationTime.getValue() != null && consultationTime.getValue().toString().length() != 0) {
                    firstConsultationTime[0] = sdf.parse(consultationTime.getValue().toString());
                }

                Date dateOfBirth = sdf.parse(stringBuffer.toString());
                String first = null;
                if(firstConsultationRadioButton.isSelected()) {
                    first = "初诊";
                } else if(furtherConsultationRadioButton.isSelected()) {
                    first = "复诊";
                }
                String doctor = doctorTextField.getText();

                String contactPhone = contactPhoneTextField.getText();
                String contractAddress = contractAddressTextField.getText();

                Patient patient = new Patient(patientName, patientCardNumber, gender, dateOfBirth, firstConsultationTime[0], patientAge[0], first, doctor, contactPhone, contractAddress);
                sqlSession[0] = getSqlSession();
                AddPatientViewMapper addPatientViewMapper = sqlSession[0].getMapper(AddPatientViewMapper.class);
                addPatientViewMapper.insertPatient(patient);
                sqlSession[0].commit();

                System.out.println(patient.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(sqlSession[0] != null) {
                    sqlSession[0].close();
                }
            }

        });
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化性别单选按钮
        rbtnSexGroup = new ToggleGroup();
        rbtnSexGroup.getToggles().addAll(maleRadioButton, femaleRadioButton, nothingRadioButton);

        // 初始化初诊/复诊单选按钮
        firstGroup = new ToggleGroup();
        firstGroup.getToggles().addAll(firstConsultationRadioButton, furtherConsultationRadioButton);

    }
}
