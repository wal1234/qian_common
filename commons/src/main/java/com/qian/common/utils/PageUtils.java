package com.qian.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils {
    /**
     * 当前记录起始索引
     */
    public static final int PAGE_NUM = 1;

    /**
     * 每页显示记录数
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 构建分页对象
     *
     * @param list  列表数据
     * @param total 总记录数
     * @return 分页对象
     */
    public static <T> IPage<T> buildPage(List<T> list, long total) {
        return new Page<T>().setRecords(list).setTotal(total);
    }

    /**
     * 构建分页对象
     *
     * @param list  列表数据
     * @param total 总记录数
     * @param page  当前页码
     * @param size  每页记录数
     * @return 分页对象
     */
    public static <T> IPage<T> buildPage(List<T> list, long total, long page, long size) {
        return new Page<T>().setRecords(list).setTotal(total).setCurrent(page).setSize(size);
    }
} 