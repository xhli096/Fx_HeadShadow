package stu.lxh.fx_headshadow.dao;

import org.apache.ibatis.annotations.Param;
import stu.lxh.fx_headshadow.entity.ActivationUser;

/**
 * Created by LXH on 2019/3/9.
 */
public interface ActivationUserMapper {
    /**
     * 将licence插入到数据库中
     * @param activationUser        激活用户
     */
    void insertActivationLicense(ActivationUser activationUser);

    /**
     * 根据licence查询对应的serial，比较数据库中的serial是否与当前计算机的相同
     * @param license       生成的license
     * @return
     */
    ActivationUser getSerialByLicense(@Param("license") String license);

    /**
     * 更新状态
     * @param serial        计算机的serial
     * @param activation    状态信息
     *                       0:表示尚未激活       1：表示已激活
     */
    void updateActivationLicense(@Param("activation") int activation, @Param("serial") String serial);
    void insertAndActivationLicense(ActivationUser activationUser);

    ActivationUser getUserBySerial(@Param("serial") String serial);
}
