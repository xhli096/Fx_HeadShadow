package stu.lxh.fx_headshadow.entity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by LXH on 2019/1/14.
 */
public class Patient {
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 病例号
     */
    private String patientCardNumber;
    /**
     * 性别
     */
    private String gender;
    /**
     * 出生年月
     */
    private Date dateOfBirth;
    /**
     * 初诊就诊时间
     */
    private Date firstConsultationTime;
    /**
     * 患者年龄
     */
    private int age;
    /**
     * 是否为初诊
     */
    private String first;
    /**
     * 主治医生
     */
    private String doctor;
    /**
     * 联系电话
     */
    private String patientContactPhone;
    /**
     * 联系地址
     */
    private String patientContactAddress;

    private Map<String, ButtonInfo> patientPhotoPathMap;
//    /**
//     * 存储某一个病例图的标志点信息
//     */
//    private Map<String, Map<String, Point2D>> pointPositionMap;

    public Patient() {
        this.patientPhotoPathMap = new LinkedHashMap<>();
//        this.pointPositionMap = new HashMap<>();
    }

    public Patient(String patientName, String patientCardNumber, String gender, Date dateOfBirth, Date firstConsultationTime, int age, String first, String doctor, String patientContactPhone, String patientCOntactAddress) {
        this.patientName = patientName;
        this.patientCardNumber = patientCardNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.firstConsultationTime = firstConsultationTime;
        this.age = age;
        this.first = first;
        this.doctor = doctor;
        this.patientContactPhone = patientContactPhone;
        this.patientContactAddress = patientCOntactAddress;
        this.patientPhotoPathMap = new LinkedHashMap<>();
//        this.pointPositionMap = new HashMap<>();
    }

    public Map<String, ButtonInfo> getPatientPhotoPathMap() {
        return patientPhotoPathMap;
    }

    public void addPatientPhotoPath(String key, ButtonInfo buttonInfo) {
        if(patientPhotoPathMap.keySet().contains(key)) {
            patientPhotoPathMap.remove(key);
            patientPhotoPathMap.put(key, buttonInfo);
        } else {
            patientPhotoPathMap.put(key, buttonInfo);
       }
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientCardNumber() {
        return patientCardNumber;
    }

    public void setPatientCardNumber(String patientCardNumber) {
        this.patientCardNumber = patientCardNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getFirstConsultationTime() {
        return firstConsultationTime;
    }

    public void setFirstConsultationTime(Date firstConsultationTime) {
        this.firstConsultationTime = firstConsultationTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPatientContactPhone() {
        return patientContactPhone;
    }

    public void setPatientContactPhone(String patientContactPhone) {
        this.patientContactPhone = patientContactPhone;
    }

    public String getPatientContactAddress() {
        return patientContactAddress;
    }

    public void setPatientContactAddress(String patientContactAddress) {
        this.patientContactAddress = patientContactAddress;
    }

//    /**
//     * 添加某一个图片的标记点信息
//     */
//    public void addPositionMap(String imageId, Map<String, Point2D> positionMap) {
//        if(pointPositionMap.get(imageId) == null) {
//            pointPositionMap.put(imageId, positionMap);
//        } else {
//            pointPositionMap.replace(imageId, pointPositionMap.get(imageId), positionMap);
//        }
//    }

    public void setPatientPhotoPathMap(Map<String, ButtonInfo> patientPhotoPathMap) {
        this.patientPhotoPathMap = patientPhotoPathMap;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientName='" + patientName + '\'' +
                ", patientCardNumber='" + patientCardNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", firstConsultationTime=" + firstConsultationTime +
                ", age=" + age +
                ", first='" + first + '\'' +
                ", doctor='" + doctor + '\'' +
                ", patientContactPhone='" + patientContactPhone + '\'' +
                ", patientContactAddress='" + patientContactAddress + '\'' +
                ", patientPhotoPathMap=" + patientPhotoPathMap +
                '}';
    }
}
