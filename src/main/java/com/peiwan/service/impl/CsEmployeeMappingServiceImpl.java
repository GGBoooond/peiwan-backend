package com.peiwan.service.impl;

import com.peiwan.entity.CsEmployeeMapping;
import com.peiwan.mapper.CsEmployeeMappingMapper;
import com.peiwan.service.CsEmployeeMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 客服员工关系服务实现类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Service
public class CsEmployeeMappingServiceImpl implements CsEmployeeMappingService {

    private static final Logger log = LoggerFactory.getLogger(CsEmployeeMappingServiceImpl.class);

    private final CsEmployeeMappingMapper csEmployeeMappingMapper;

    public CsEmployeeMappingServiceImpl(CsEmployeeMappingMapper csEmployeeMappingMapper) {
        this.csEmployeeMappingMapper = csEmployeeMappingMapper;
    }

    @Override
    @Transactional
    public CsEmployeeMapping createMapping(Long csUserId, Long employeeUserId) {
        // 检查关系是否已存在
        CsEmployeeMapping existing = csEmployeeMappingMapper.findByCsAndEmployee(csUserId, employeeUserId);
        if (existing != null) {
            throw new RuntimeException("客服员工关系已存在");
        }

        CsEmployeeMapping mapping = new CsEmployeeMapping();
        mapping.setCsUserId(csUserId);
        mapping.setEmployeeUserId(employeeUserId);
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setUpdatedAt(LocalDateTime.now());
        mapping.setDeleted(0);

        csEmployeeMappingMapper.insert(mapping);
        log.info("客服员工关系创建成功: csUserId={}, employeeUserId={}", csUserId, employeeUserId);
        return mapping;
    }

    @Override
    public CsEmployeeMapping findById(Long id) {
        return csEmployeeMappingMapper.selectById(id);
    }

    @Override
    public List<CsEmployeeMapping> findByCsUserId(Long csUserId) {
        return csEmployeeMappingMapper.findByCsUserId(csUserId);
    }

    @Override
    public List<CsEmployeeMapping> findByEmployeeUserId(Long employeeUserId) {
        return csEmployeeMappingMapper.findByEmployeeUserId(employeeUserId);
    }

    @Override
    public CsEmployeeMapping findByCsAndEmployee(Long csUserId, Long employeeUserId) {
        return csEmployeeMappingMapper.findByCsAndEmployee(csUserId, employeeUserId);
    }

    @Override
    public List<CsEmployeeMapping> findAll() {
        return csEmployeeMappingMapper.findAll();
    }

    @Override
    public List<Long> getEmployeeUserIdsByCsUserId(Long csUserId) {
        return csEmployeeMappingMapper.findEmployeeUserIdsByCsUserId(csUserId);
    }

    @Override
    @Transactional
    public CsEmployeeMapping updateMapping(Long id, Long csUserId, Long employeeUserId) {
        CsEmployeeMapping mapping = findById(id);
        if (mapping == null) {
            throw new RuntimeException("客服员工关系不存在");
        }

        // 检查新关系是否已存在（除了当前记录）
        CsEmployeeMapping existing = csEmployeeMappingMapper.findByCsAndEmployee(csUserId, employeeUserId);
        if (existing != null && !existing.getId().equals(id)) {
            throw new RuntimeException("客服员工关系已存在");
        }

        mapping.setCsUserId(csUserId);
        mapping.setEmployeeUserId(employeeUserId);
        mapping.setUpdatedAt(LocalDateTime.now());

        csEmployeeMappingMapper.updateById(mapping);
        log.info("客服员工关系更新成功: id={}, csUserId={}, employeeUserId={}", id, csUserId, employeeUserId);
        return mapping;
    }

    @Override
    @Transactional
    public void deleteMapping(Long id) {
        CsEmployeeMapping mapping = findById(id);
        if (mapping == null) {
            throw new RuntimeException("客服员工关系不存在");
        }

        csEmployeeMappingMapper.deleteById(id);
        log.info("客服员工关系删除成功: id={}", id);
    }

    @Override
    @Transactional
    public void deleteByCsUserId(Long csUserId) {
        int count = csEmployeeMappingMapper.deleteByCsUserId(csUserId);
        log.info("删除客服的所有员工关系: csUserId={}, count={}", csUserId, count);
    }

    @Override
    @Transactional
    public void deleteByEmployeeUserId(Long employeeUserId) {
        int count = csEmployeeMappingMapper.deleteByEmployeeUserId(employeeUserId);
        log.info("删除员工的所有关系: employeeUserId={}, count={}", employeeUserId, count);
    }

    @Override
    @Transactional
    public List<CsEmployeeMapping> createMappings(Long csUserId, List<Long> employeeUserIds) {
        List<CsEmployeeMapping> mappings = new ArrayList<>();
        
        for (Long employeeUserId : employeeUserIds) {
            try {
                CsEmployeeMapping mapping = createMapping(csUserId, employeeUserId);
                mappings.add(mapping);
            } catch (RuntimeException e) {
                log.warn("跳过已存在的客服员工关系: csUserId={}, employeeUserId={}", csUserId, employeeUserId);
            }
        }
        
        log.info("批量创建客服员工关系: csUserId={}, total={}, created={}", csUserId, employeeUserIds.size(), mappings.size());
        return mappings;
    }

    @Override
    @Transactional
    public void reassignEmployee(Long employeeUserId, Long newCsUserId) {
        // 删除员工的现有关系
        deleteByEmployeeUserId(employeeUserId);
        
        // 创建新的关系
        createMapping(newCsUserId, employeeUserId);
        
        log.info("员工重新分配成功: employeeUserId={}, newCsUserId={}", employeeUserId, newCsUserId);
    }

    @Override
    public boolean isCsManageEmployee(Long csUserId, Long employeeUserId) {
        CsEmployeeMapping mapping = csEmployeeMappingMapper.findByCsAndEmployee(csUserId, employeeUserId);
        return mapping != null;
    }
}


