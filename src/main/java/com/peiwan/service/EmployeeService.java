package com.peiwan.service;

import com.peiwan.entity.EmployeeProfile;
import com.peiwan.entity.GameSkill;

import java.util.List;

/**
 * 员工服务接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public interface EmployeeService {

    /**
     * 根据用户ID查找员工资料
     */
    EmployeeProfile findByUserId(Long userId);

    /**
     * 根据工作状态查找员工
     */
    List<EmployeeProfile> findByWorkStatus(EmployeeProfile.WorkStatus workStatus);

    /**
     * 根据客服ID查找管理的员工
     */
    List<EmployeeProfile> findByCsUserId(Long csUserId);

    /**
     * 更新员工工作状态
     */
    EmployeeProfile updateWorkStatus(Long userId, EmployeeProfile.WorkStatus workStatus);

    /**
     * 更新员工资料（包括性别和工作状态）
     */
    EmployeeProfile updateProfile(Long userId, EmployeeProfile.Gender gender, EmployeeProfile.WorkStatus workStatus);

    /**
     * 更新员工游戏技能
     */
    List<GameSkill> updateGameSkills(Long userId, List<GameSkill> gameSkills);

    /**
     * 根据员工资料ID查找游戏技能
     */
    List<GameSkill> findGameSkillsByProfileId(Long profileId);

    /**
     * 创建员工资料
     */
    EmployeeProfile createProfile(Long userId);
}

