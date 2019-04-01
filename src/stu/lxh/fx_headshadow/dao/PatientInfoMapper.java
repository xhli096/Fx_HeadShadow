package stu.lxh.fx_headshadow.dao;

import org.apache.ibatis.annotations.Param;
import stu.lxh.fx_headshadow.entity.ButtonInfo;
import stu.lxh.fx_headshadow.entity.Patient;

import java.util.Date;
import java.util.List;

/**
 * Created by LXH on 2019/2/28.
 */
public interface PatientInfoMapper {
    List<Patient> getPreDayPatients(Date date);

    /**
     * 获取所有的病人
     * @return
     */
    List<Patient> getAllPatients();

    List<ButtonInfo> getPatientImageInfo(String patientCardNumber);

    void insertImagePosition(@Param("patientCardNumber") String patientCardNumber, @Param("buttonId") String buttonId, @Param("pointName") String pointName,
                             @Param("x") double x, @Param("y") double y);
}
