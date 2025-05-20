package com.qian.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @TableField(exist = false)
    private String searchValue;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 删除标志（0代表存在 2代表删除） */
    @TableLogic
    private String delFlag;

    /** 请求参数 */
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    /**
     * 设置请求参数
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 添加请求参数
     */
    public void addParam(String key, Object value) {
        getParams().put(key, value);
    }

    /**
     * 获取请求参数
     */
    public Object getParam(String key) {
        return getParams().get(key);
    }

    /**
     * 获取请求参数
     */
    public <T> T getParam(String key, Class<T> clazz) {
        Object value = getParam(key);
        return value == null ? null : (T) value;
    }

    /**
     * 获取请求参数
     */
    public String getParamString(String key) {
        return getParam(key, String.class);
    }

    /**
     * 获取请求参数
     */
    public Integer getParamInteger(String key) {
        return getParam(key, Integer.class);
    }

    /**
     * 获取请求参数
     */
    public Long getParamLong(String key) {
        return getParam(key, Long.class);
    }

    /**
     * 获取请求参数
     */
    public Double getParamDouble(String key) {
        return getParam(key, Double.class);
    }

    /**
     * 获取请求参数
     */
    public Boolean getParamBoolean(String key) {
        return getParam(key, Boolean.class);
    }

    /**
     * 获取请求参数
     */
    public Date getParamDate(String key) {
        return getParam(key, Date.class);
    }
} 