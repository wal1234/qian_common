package com.qian.common.log.service.impl;

import com.qian.common.log.domain.SysLoginLog;
import com.qian.common.log.mapper.SysLoginLogMapper;
import com.qian.common.log.service.ISysLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 */
@Slf4j
@Service
public class SysLoginLogServiceImpl implements ISysLoginLogService {
    @Autowired
    private SysLoginLogMapper loginLogMapper;

    /**
     * 新增系统登录日志
     * 
     * @param loginLog 访问日志对象
     */
    @Override
    public void insertLoginLog(SysLoginLog loginLog) {
        loginLogMapper.insertLoginLog(loginLog);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param loginLog 访问日志对象
     * @return 记录集合
     */
    @Override
    public List<SysLoginLog> selectLoginLogList(SysLoginLog loginLog) {
        return loginLogMapper.selectLoginLogList(loginLog);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLoginLogByIds(Long[] infoIds) {
        return loginLogMapper.deleteLoginLogByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginLog() {
        loginLogMapper.cleanLoginLog();
    }
} 