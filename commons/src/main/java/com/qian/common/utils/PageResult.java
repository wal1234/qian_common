package com.qian.common.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页数据结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "当前页码")
    private long current;

    @Schema(description = "每页记录数")
    private long size;

    @Schema(description = "总页数")
    private long pages;

    @Schema(description = "数据列表")
    private List<T> records;

    @Schema(description = "是否有上一页")
    private boolean hasPrevious;

    @Schema(description = "是否有下一页")
    private boolean hasNext;

    /**
     * 自定义构造方法
     */
    public PageResult(long total, long current, long size, List<T> records) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.records = records;
        this.pages = size == 0 ? 0 : (total + size - 1) / size;
        this.hasPrevious = current > 1;
        this.hasNext = current < pages;
    }
} 