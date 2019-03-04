package stu.lxh.fx_headshadow.dao;

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
}
