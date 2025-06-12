package com.qian.common.utils.poi;

import com.qian.common.exception.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtil<T> {
    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int SHEET_SIZE = 65536;

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
    private List<ExcelStyle> styles;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 实体对象
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 导出Excel
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        this.init(list, sheetName, Type.EXPORT);
        this.export(response.getOutputStream());
    }

    /**
     * 导出Excel
     */
    private void export(OutputStream outputStream) throws IOException {
        try {
            wb.write(outputStream);
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                throw new ServiceException("关闭Excel失败");
            }
        }
    }

    /**
     * 初始化数据
     */
    private void init(List<T> list, String sheetName, Type type) {
        if (list == null) {
            list = List.of();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
        createTitle();
        createData();
    }

    /**
     * 创建工作簿
     */
    private void createWorkbook() {
        this.wb = new XSSFWorkbook();
        this.sheet = wb.createSheet();
        wb.setSheetName(0, sheetName);
    }

    /**
     * 创建标题
     */
    private void createTitle() {
        Row row = sheet.createRow(0);
        for (int i = 0; i < fields.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(fields.get(i)[1].toString());
            cell.setCellStyle(styles.get(0).getStyle());
            sheet.setColumnWidth(i, 15 * 256);
        }
    }

    /**
     * 创建数据
     */
    private void createData() {
        int startNo = 0;
        int endNo = Math.min(startNo + SHEET_SIZE, list.size());
        for (int i = startNo; i < endNo; i++) {
            Row row = sheet.createRow(i + 1);
            T vo = list.get(i);
            for (int j = 0; j < fields.size(); j++) {
                Cell cell = row.createCell(j);
                Object value = getTargetValue(vo, (String) fields.get(j)[0]);
                cell.setCellValue(value == null ? "" : value.toString());
                cell.setCellStyle(styles.get(1).getStyle());
            }
        }
    }

    /**
     * 获取字段值
     */
    private Object getTargetValue(T vo, String field) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(vo);
        } catch (Exception e) {
            throw new ServiceException("获取字段值失败");
        }
    }

    /**
     * 创建Excel字段
     */
    private void createExcelField() {
        this.fields = List.of();
        this.styles = List.of();
    }

    /**
     * 导出类型枚举
     */
    public enum Type {
        EXPORT, IMPORT
    }

    /**
     * Excel样式
     */
    private static class ExcelStyle {
        private CellStyle style;

        public CellStyle getStyle() {
            return style;
        }
    }

    /**
     * 导入Excel
     *
     * @param inputStream Excel文件输入流
     * @return 导入数据列表
     */
    public List<T> importExcel(InputStream inputStream) throws IOException {
        try {
            this.wb = WorkbookFactory.create(inputStream);
            this.sheet = wb.getSheetAt(0);
            return parseData();
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                throw new ServiceException("关闭Excel失败");
            }
        }
    }

    /**
     * 解析数据
     */
    private List<T> parseData() {
        List<T> dataList = new ArrayList<>();
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows <= 1) {
            return dataList;
        }

        // 获取标题行
        Row headerRow = sheet.getRow(0);
        int columns = headerRow.getPhysicalNumberOfCells();

        // 从第二行开始读取数据
        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            try {
                T entity = clazz.getDeclaredConstructor().newInstance();
                for (int j = 0; j < columns; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }

                    String fieldName = (String) fields.get(j)[0];
                    Object value = getCellValue(cell);
                    setFieldValue(entity, fieldName, value);
                }
                dataList.add(entity);
            } catch (Exception e) {
                throw new ServiceException("解析Excel数据失败：" + e.getMessage());
            }
        }

        return dataList;
    }

    /**
     * 获取单元格值
     */
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * 设置字段值
     */
    private void setFieldValue(T entity, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        
        if (value != null) {
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                field.set(entity, value.toString());
            } else if (fieldType == Integer.class || fieldType == int.class) {
                field.set(entity, Integer.parseInt(value.toString()));
            } else if (fieldType == Long.class || fieldType == long.class) {
                field.set(entity, Long.parseLong(value.toString()));
            } else if (fieldType == Double.class || fieldType == double.class) {
                field.set(entity, Double.parseDouble(value.toString()));
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                field.set(entity, Boolean.parseBoolean(value.toString()));
            } else if (fieldType == Date.class) {
                field.set(entity, (Date) value);
            } else {
                field.set(entity, value);
            }
        }
    }
} 