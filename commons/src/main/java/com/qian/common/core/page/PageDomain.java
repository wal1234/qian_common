package com.qian.common.core.page;

import lombok.Data;

/**
 * 分页数据
 */
@Data
public class PageDomain {
    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    private String isAsc = "asc";

    public String getOrderBy() {
        if (orderByColumn == null || orderByColumn.isEmpty()) {
            return "";
        }
        return orderByColumn + " " + isAsc;
    }
} 