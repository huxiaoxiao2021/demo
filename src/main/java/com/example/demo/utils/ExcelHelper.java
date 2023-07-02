package com.example.demo.utils;

import com.google.common.collect.Lists;
import lombok.Data;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2022/9/27 4:21 PM
 */
public class ExcelHelper {

    private static final XLSTransformer transformer = new XLSTransformer();

    private static String template_path = "/Users/hujiping/project/tools/demo/src/main/resources/template/";

    private static String template_name = "抽检数据";

    private static String file_Suffix = ".xlsx";
    private static String generate_path = "/Users/hujiping/project/tools/demo/src/main/resources/out/";

    @Data
    public static class SpotCheckDto {
        private String waybillCode;
        private String reviewSiteCode;
        private String excessType;
        private String reviewWeight;
        private String reviewVolume;
        private String contrastWeight;
        private String contrastVolume;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {

        String templateFile = template_path + template_name + file_Suffix;
        String generateFile = generate_path + template_name + DateHelper.formatDateTime(new Date()) + file_Suffix;

//        List<String> fieldNameList = Lists.newArrayList();
//        fieldNameList.add("waybillCode");
//        fieldNameList.add("reviewSiteCode");
//        fieldNameList.add("excessType");
//        fieldNameList.add("reviewWeight");
//        fieldNameList.add("reviewVolume");
//        fieldNameList.add("contrastWeight");
//        fieldNameList.add("contrastVolume");
//        List<SpotCheckDto> spotCheckDtos = readFromExcel("/Users/hujiping/project/tools/JD-hu/src/main/resources/out/抽检数据2022-09-27 19:40:48.xlsx", SpotCheckDto.class, fieldNameList);


//        Map<String, Object> params = new HashMap<>();
//        Vector<Map<String, String>> items = new Vector<>();
//        Map<String, String> item = new HashMap<>();
//        item.put("waybillCode", "waybillCode");
//        item.put("siteCode", "review_site_code");
//        item.put("excessType", "getExcessType");
//        item.put("reviewWeight", "review_weight");
//        item.put("reviewVolume", "review_volume");
//        item.put("contrastWeight", "contrast_weight");
//        item.put("contrastVolume", "contrast_volume");
//        item.put("status", "1");
//        item.put("contrastSource", "11");
//        item.put("businessType", "111");
//        items.add(item);
//        params.put("printList", items);
//
//        generateExcel(templateFile, generateFile, params);


        List<String> title = Lists.newArrayList();
        title.add("异常单号");
        title.add("异常原因");
        List<List<String>> data = Lists.newArrayList();
        List<String> rowList = Lists.newArrayList();
        rowList.add("123");
        rowList.add("321");
        data.add(rowList);
        List<String> rowList2 = Lists.newArrayList();
        rowList2.add("456");
        rowList2.add("654");
        data.add(rowList2);
        String sheetName = "测试";
        String filePath = "/Users/hujiping/Downloads/123.xlsx";
        writeExcel(title, data, sheetName, filePath);
        System.out.println(11);
    }

    /**
     * 在指定位置生成excel文件
     *
     * @param templateFile excel模板位置
     * @param generateFile 生成excel位置
     * @param params       excel内容
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void generateExcel(String templateFile, String generateFile, Map<String, Object> params)
            throws IOException, InvalidFormatException {
        // 利用transformXLS生成excel文件
        transformer.transformXLS(templateFile, params, generateFile);
    }

    /**
     * 将数据写入Excel
     *
     * @param title     表头
     * @param data      数据内容
     * @param sheetName sheet名
     * @param filePath  生成文件路径
     */
    public static boolean writeExcel(List<String> title, List<List<String>> data, String sheetName, String filePath) throws IOException {
        if (filePath == null || !filePath.contains(".")) {
            return false;
        }
        String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
        Workbook workbook = null;
        switch (suffix) {
            case "xls":
                workbook = new HSSFWorkbook();
                break;
            case "xlsx":
                workbook = new XSSFWorkbook();
                break;
            default:
                return false;
        }
        // 在workbook中创建一个sheet对应excel中的sheet
        Sheet sheet = workbook.createSheet(sheetName);
        // 在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        Row row = sheet.createRow(0);
        // 创建单元格，设置表头
        int titleSize = title.size();
        for (int i = 0; i < titleSize; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(title.get(i));
        }
        // 写入数据
        for (int i = 0; i < data.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            List<String> rowData = data.get(i);
            // 创建单元格设值
            for (int i1 = 0; i1 < rowData.size(); i1++) {
                row1.createCell(i1).setCellValue(rowData.get(i1));
            }
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream os = null;
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            workbook.write(os);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            os.close();
        }
        return false;
    }

    /**
     * 获取Excel，将数据转换成 List<T> 的形式
     *
     * @param filePath      文件路径
     * @param fileName      文件名称
     * @param sheetName     sheet名称
     * @param fieldNameList 字段名
     * @param cls           要转换成的实体类
     * @param <T>
     * @return List对象数组
     * @throws IOException
     */
    public static <T> List<T> readExcel2List(String filePath, String fileName, String sheetName,
                                             List<String> fieldNameList, Class<T> cls) throws Exception {
        List<T> list = Lists.newArrayList();
        File file = new File(filePath + File.separator + fileName);
        FileInputStream inputStream = new FileInputStream(file);
        // 使用工厂模式 根据文件扩展名 创建对应的Workbook
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = 1; i < rowCount + 1; i++) {
            T t = cls.newInstance();
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                setValue(t, cls, row, j, fieldNameList);
            }
            list.add(t);
        }
        return list;
    }

    /**
     * 读取excel第一个sheet页内容
     *
     * @param filePath      excel文件路径
     * @param cls           生成对象class
     * @param fieldNameList 对象属性名（顺序与excel顺序一致）
     */
    public static <T> List<T> readFromExcel(String filePath, Class<T> cls, List<String> fieldNameList) {
        List<T> list = Lists.newArrayList();
        try {
            /*
             *简单判断后缀名，如需通过文件流判断文件类型，
             * 请调用getFileTypeByStream方法
             * Excel( xls) 文件头：504B03
             * Excel( xlsx) 文件头：D0CF11
             * */
            boolean xls = filePath.endsWith(".xls");
            boolean xlsx = filePath.endsWith(".xlsx");
            Workbook book;
            Sheet sheet = null;
            InputStream inputStream = new FileInputStream(new File(filePath));
            if (xls) {
                // 解析excel
                POIFSFileSystem pSystem = new POIFSFileSystem(inputStream);
                // 获取整个excel
                book = new HSSFWorkbook(pSystem);
                //获取第一个表单sheet
                sheet = book.getSheetAt(0);
            }
            if (xlsx) {
                // 直接通过流获取整个excel
                book = new XSSFWorkbook(inputStream);
                // 获取第一个表单sheet
                sheet = book.getSheetAt(0);
            }
            if (sheet != null) {
                // 获取第一行
                int firstRow = sheet.getFirstRowNum();
                // 获取最后一行
                int lastRow = sheet.getLastRowNum();
                // 循环行数依次获取列数
                for (int i = firstRow + 1; i < lastRow + 1; i++) {
                    // 获取第 i 行
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        T t = cls.newInstance();
                        // 获取此行的第一列
                        int firstCell = 0;
                        /*
                         *获取此行的存在数据的第一列
                         * int firstCell = row.getFirstCellNum();
                         * */
                        // 获取此行的存在数据的最后一列
                        int lastCell = row.getLastCellNum();
                        for (int j = firstCell; j < lastCell; j++) {
                            setValue(t, cls, row, j, fieldNameList);
                        }
                        list.add(t);
                    }
                }
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static <T> void setValue(T t, Class<T> cls, Row row, int j, List<String> fieldNameList) throws Exception {
        String cellContent = null;
        // 拿到单元格类型
        Cell cell = row.getCell(j);
        int cellType = cell.getCellType();
        switch (cellType) {
            // 字符串类型
            case Cell.CELL_TYPE_STRING:
                cellContent = StringUtils.isEmpty(cell.getStringCellValue()) ? "" : cell.getStringCellValue().trim();
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                cellContent = String.valueOf(cell.getBooleanCellValue());
                break;
            // 数值类型
            case Cell.CELL_TYPE_NUMERIC:
                cellContent = new DecimalFormat("#.##").format(cell.getNumericCellValue());
                break;
            // 取空串
            default:
                cellContent = "";
                break;
        }
        String currentFieldName = fieldNameList.get(j);
        // 获取该类的成员变量
        Field f = cls.getDeclaredField(currentFieldName);
        // 取消语言访问检查
        f.setAccessible(true);
        // 给变量赋值
        f.set(t, cellContent);
    }

}
