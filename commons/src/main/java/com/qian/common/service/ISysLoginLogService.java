package com.qian.common.service;

import com.qian.common.domain.SysLoginLog;

/**
 * 系统访问日志情况信息 服务层
 */
public interface ISysLoginLogService {
    /**
     * 新增系统登录日志
     *
     * @param loginLog 访问日志对象
     */
    public void insertLoginLog(SysLoginLog loginLog);

    /**
     * 查询系统登录日志集合
     *
     * @param loginLog 访问日志对象
     * @return 记录集合
     */
    public java.util.List<SysLoginLog> selectLoginLogList(SysLoginLog loginLog);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int deleteLoginLogByIds(Long[] ids);

    /**
     * 清空系统登录日志
     */
    public void cleanLoginLog();
} 