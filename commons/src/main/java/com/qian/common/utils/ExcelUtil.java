package com.qian.common.utils;

import com.qian.common.annotation.Excel;
import com.qian.common.annotation.Excels;
import com.qian.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Excel相关处理
 */
public class ExcelUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 当前行号
     */
    private int rownum;

    /**
     * 标题
     */
    private String title;

    /**
     * 最大高度
     */
    private short maxHeight;

    /**
     * 统计列表
     */
    private Map<Integer, Double> statistics = new HashMap<>();

    /**
     * 数字格式
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /**
     * 实体对象
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void init(List<T> list, String sheetName, String title, Type type) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        this.title = title;
        createExcelField();
        createWorkbook();
        createTitle();
        createHead();
        createBody();
    }

    /**
     * 创建excel的标题
     */
    public void createTitle() {
        if (StringUtils.isNotEmpty(title)) {
            Row titleRow = sheet.createRow(rownum == 0 ? rownum++ : 0);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), this.fields.size() - 1));
        }
    }

    /**
     * 创建excel的表头
     */
    public void createHead() {
        Row headRow = sheet.createRow(rownum++);
        headRow.setHeightInPoints(20);
        for (int i = 0; i < fields.size(); i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            Object[] os = fields.get(i);
            String textVal = os[1].toString();
            cell.setCellValue(textVal);
        }
    }

    /**
     * 创建excel的主体
     */
    public void createBody() {
        int startNo = 0;
        int endNo = list.size();

        for (int i = startNo; i < endNo; i++) {
            Row row = sheet.createRow(i + rownum);
            row.setHeightInPoints(20);
            // 得到导出对象
            T vo = list.get(i);
            for (int j = 0; j < fields.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(styles.get("data"));
                // 获取值
                Object value = getTargetValue(vo, fields.get(j)[0].toString(), (Integer) fields.get(j)[4]);
                String textValue = convertTextValue(value);
                cell.setCellValue(textValue);
            }
        }
    }

    /**
     * 创建工作簿
     */
    public void createWorkbook() {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet();
        wb.setSheetName(0, sheetName);
        this.styles = createStyles(wb);
    }

    /**
     * 获取字段值
     */
    public Object getTargetValue(T vo, String field, Integer type) {
        Object value = null;
        if (StringUtils.isNotEmpty(field)) {
            String[] fields = field.split("[.]");
            try {
                for (int i = 0; i < fields.length; i++) {
                    if (i == 0) {
                        value = getValue(vo, fields[i]);
                    } else {
                        value = getValue(value, fields[i]);
                    }
                }
            } catch (Exception e) {
                log.error("获取对象值失败", e);
            }
        }
        return value;
    }

    /**
     * 获取值
     */
    public Object getValue(Object obj, String field) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).get(field);
        } else {
            try {
                String methodName = "get" + StringUtils.capitalize(field);
                Method method = obj.getClass().getMethod(methodName);
                return method.invoke(obj);
            } catch (Exception e) {
                log.error("获取对象值失败", e);
            }
        }
        return null;
    }

    /**
     * 转换文本值
     */
    public String convertTextValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Date) {
            return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, (Date) value);
        }
        if (value instanceof BigDecimal) {
            return DOUBLE_FORMAT.format(value);
        }
        return value.toString();
    }

    /**
     * 创建表格样式
     */
    public Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 12);
        style.setFont(dataFont);
        styles.put("data", style);

        return styles;
    }

    /**
     * 创建Excel字段
     */
    public void createExcelField() {
        this.fields = new ArrayList<>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields) {
            // 单注解
            if (field.isAnnotationPresent(Excel.class)) {
                putToField(field, field.getAnnotation(Excel.class));
            }

            // 多注解
            if (field.isAnnotationPresent(Excels.class)) {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels) {
                    putToField(field, excel);
                }
            }
        }
    }

    /**
     * 放到字段集合中
     */
    public void putToField(Field field, Excel attr) {
        if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == Excel.Type.EXPORT)) {
            this.fields.add(new Object[] { field, attr.name(), attr.prompt(), attr.combo(), attr.isExport() });
        }
    }

    /**
     * 导出Excel
     */
    public void exportExcel() {
        try {
            String filename = encodingFilename(sheetName);
            FileOutputStream os = new FileOutputStream(filename);
            wb.write(os);
            os.close();
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new ServiceException("导出Excel失败，请联系网站管理员！");
        }
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename) {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 导出类型枚举
     */
    public enum Type {
        EXPORT, IMPORT
    }
} 