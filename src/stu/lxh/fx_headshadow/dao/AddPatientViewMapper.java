package stu.lxh.fx_headshadow.dao;

import org.apache.ibatis.annotations.Param;
import stu.lxh.fx_headshadow.entity.ButtonInfo;
import stu.lxh.fx_headshadow.entity.Patient;

/**
 * Created by LXH on 2019/1/24.
 */
public interface AddPatientViewMapper {
    /**
     * 将病患基本信息插入数据库
     */
    void insertPatient(Patient patient);

    /**
     * 将患者的图片信息插入到数据库中
     */
    void insertPatientPhotoPath(@Param("patientCardNumber") String patientCardNumber, @Param("buttonInfo") ButtonInfo buttonInfo);
}
