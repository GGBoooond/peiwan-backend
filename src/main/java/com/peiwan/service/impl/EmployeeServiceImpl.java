package com.peiwan.service.impl;

import com.peiwan.entity.EmployeeProfile;
import com.peiwan.entity.GameSkill;
import com.peiwan.mapper.EmployeeProfileMapper;
import com.peiwan.mapper.GameSkillMapper;
import com.peiwan.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工服务实现类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeProfileMapper employeeProfileMapper;
    private final GameSkillMapper gameSkillMapper;

    public EmployeeServiceImpl(EmployeeProfileMapper employeeProfileMapper, GameSkillMapper gameSkillMapper) {
        this.employeeProfileMapper = employeeProfileMapper;
        this.gameSkillMapper = gameSkillMapper;
    }

    @Override
    public EmployeeProfile findByUserId(Long userId) {
        return employeeProfileMapper.findByUserId(userId);
    }

    @Override
    public List<EmployeeProfile> findByWorkStatus(EmployeeProfile.WorkStatus workStatus) {
        return employeeProfileMapper.findByWorkStatus(workStatus);
    }

    @Override
    public List<EmployeeProfile> findByCsUserId(Long csUserId) {
        return employeeProfileMapper.findByCsUserId(csUserId);
    }

    @Override
    @Transactional
    public EmployeeProfile updateWorkStatus(Long userId, EmployeeProfile.WorkStatus workStatus) {
        EmployeeProfile profile = findByUserId(userId);
        if (profile == null) {
            profile = createProfile(userId);
        }

        profile.setWorkStatus(workStatus);
        profile.setUpdatedAt(LocalDateTime.now());
        employeeProfileMapper.updateById(profile);

        log.info("员工工作状态更新: userId={}, status={}", userId, workStatus);
        return profile;
    }

    @Override
    @Transactional
    public List<GameSkill> updateGameSkills(Long userId, List<GameSkill> gameSkills) {
        EmployeeProfile profile = findByUserId(userId);
        if (profile == null) {
            profile = createProfile(userId);
        }

        // 删除原有技能
        List<GameSkill> existingSkills = findGameSkillsByProfileId(profile.getId());
        for (GameSkill skill : existingSkills) {
            gameSkillMapper.deleteById(skill.getId());
        }

        // 添加新技能
        for (GameSkill skill : gameSkills) {
            skill.setProfileId(profile.getId());
            skill.setCreatedAt(LocalDateTime.now());
            skill.setUpdatedAt(LocalDateTime.now());
            gameSkillMapper.insert(skill);
        }

        log.info("员工游戏技能更新: userId={}, skills={}", userId, gameSkills.size());
        return gameSkills;
    }

    @Override
    public List<GameSkill> findGameSkillsByProfileId(Long profileId) {
        return gameSkillMapper.findByProfileId(profileId);
    }

    @Override
    @Transactional
    public EmployeeProfile createProfile(Long userId) {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setUserId(userId);
        profile.setWorkStatus(EmployeeProfile.WorkStatus.IDLE);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());

        employeeProfileMapper.insert(profile);
        log.info("员工资料创建成功: userId={}", userId);
        return profile;
    }
}

