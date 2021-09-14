package com.simit.fota.util;

import com.simit.fota.annotation.excelRescoure;
import com.simit.fota.exception.GlobalException;
import com.simit.fota.result.CodeMsg;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelRead {
    //日志输出
    private static Logger logger = LoggerFactory.getLogger(ExcelRead.class);
    //定义excel类型
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType    文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     */
    private static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        //用自带的方法新建工作薄
        Workbook workbook = WorkbookFactory.create(inputStream);
        return workbook;
    }


    /**
     * 将单元格内容转换为字符串
     * @param cell
     * @return
     */
    private static String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();
                // 格式化科学计数法，取一位整数，如取小数，值如0.0,取小数点后几位就写几个0
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 处理Excel内容转为List<Map<String,Object>>输出
     * workbook：已连接的工作薄
     * StatrRow：读取的开始行数（默认填0，0开始,传过来是EXcel的行数值默认从1开始，这里已处理减1）
     * EndRow：读取的结束行数（填-1为全部）
     * ExistTop:是否存在头部（如存在则读取数据时会把头部拼接到对应数据，若无则为当前列数）
     */
    private static List<Map<String, Object>> HandleData(Workbook workbook, int StatrRow, int EndRow, boolean ExistTop) {
        //声明返回结果集result
        List<Map<String, Object>> result = new ArrayList<>();
        //声明一个Excel头部函数
        ArrayList<String> top = new ArrayList<>();
        //解析sheet（sheet是Excel脚页）
        /**
         *此处会读取所有脚页的行数据，若只想读取指定页，不要for循环，直接给sheetNum赋值，脚页从0开始（通常情况Excel都只有一页，所以此处未进行进一步处理）
         */
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }
            //如存在头部，处理头部数据
            if (ExistTop) {
                int firstRowNum = sheet.getFirstRowNum();
                Row firstRow = sheet.getRow(firstRowNum);
                if (null == firstRow) {
                    logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
                }
                for (int i = 0; i < firstRow.getLastCellNum(); i++) {
                    top.add(convertCellValueToString(firstRow.getCell(i)));
                }
            }
            //处理Excel数据内容
            int endRowNum;
            //获取结束行数
            if (EndRow == -1) {
                endRowNum = sheet.getPhysicalNumberOfRows();
            } else {
                endRowNum = EndRow <= sheet.getPhysicalNumberOfRows() ? EndRow : sheet.getPhysicalNumberOfRows();
            }
            //遍历行数
            for (int i = StatrRow - 1; i < endRowNum; i++) {
                Row row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                //获取所有列数据
                for (int y = 0; y < row.getLastCellNum(); y++) {
                    if (top.size() > 0) {
                        if (top.size() >= y) {
                            map.put(top.get(y), convertCellValueToString(row.getCell(y)));
                        } else {
                            map.put(String.valueOf(y + 1), convertCellValueToString(row.getCell(y)));
                        }
                    } else {
                        map.put(String.valueOf(y + 1), convertCellValueToString(row.getCell(y)));
                    }
                }
                result.add(map);
            }
        }
        return result;
    }

    public static List<Object> readExcelByPOJO(MultipartFile file,Class T) throws IOException, IntrospectionException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        return ReadExcelByPOJO(file,file.getOriginalFilename(),0,-1,T);
    }

    /**
     * 方法二
     * 根据给定的实体类中赋值的注解值读取Excel
     * fileName:Excel文件路径
     * StatrRow：读取的开始行数（默认填0）
     * EndRow：读取的结束行数（填-1为全部）
     * Class<T>：传过来的实体类类型
     * 返回一个List<T>:T为实体类
     */
    public static List<Object> ReadExcelByPOJO(MultipartFile excelFile,String fileName, int StatrRow, int EndRow, Class t) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        //判断输入的开始值是否少于等于结束值
        if (StatrRow > EndRow && EndRow != -1) {
            logger.warn("输入的开始行值比结束行值大，请重新输入正确的行数");
            List<Object> error = null;
            return error;
        }
        //声明返回的结果集
        List<Object> result = new ArrayList<>();
        //声明一个工作薄
        Workbook workbook = null;
        //声明一个文件输入流
        InputStream inputStream = null;
        try {
            // 获取Excel后缀名，判断文件类型
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 获取Excel文件
            if ((excelFile == null) || excelFile.isEmpty()) {
                logger.warn("指定的Excel文件不存在！");
                throw new GlobalException(CodeMsg.EXCEL_INVALID);
            }
            // 获取Excel工作簿
            inputStream = excelFile.getInputStream();
            workbook = getWorkbook(inputStream, fileType);
            //处理Excel内容
            result = HandleDataPOJO(workbook, StatrRow, EndRow, t);
        } catch (Exception e) {
            logger.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warn("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
        return result;
    }


    /**
     * 处理Excel内容转为List<T>输出
     * workbook：已连接的工作薄
     * StatrRow：读取的开始行数（默认填0，0开始,传过来是EXcel的行数值默认从1开始，这里已处理减1）
     * EndRow：读取的结束行数（填-1为全部）
     * Class<T>：所映射的实体类
     */
    private static <t> List<Object> HandleDataPOJO(Workbook workbook, int StatrRow, int EndRow, Class<?> t) throws IntrospectionException, NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        //声明返回的结果集
        List<Object> result = new ArrayList<Object>();
        //解析sheet（sheet是Excel脚页）
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }
            //获取头部数据
            //声明头部数据数列对象
            ArrayList<String> top = new ArrayList<>();
            //获取Excel第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warn("解析Excel失败，在第一行没有读取到任何数据！");
                return result;
            }
            for (int i = 0; i < firstRow.getLastCellNum(); i++) {
                top.add(convertCellValueToString(firstRow.getCell(i)));
            }
            //获取实体类的成原变量
            Map<String, Object> POJOfields = getPOJOFieldAndValue(t);
            //判断所需要的数据列
            Map<String, Object> exceltoPOJO = new HashMap<>();
            for (int i = 0; i < top.size(); i++) {
                if (POJOfields.get(top.get(i)) != null && !"".equals(POJOfields.get(top.get(i)))) {
                    exceltoPOJO.put(String.valueOf(i), POJOfields.get(top.get(i)));
                }
            }
            /*处理Excel数据内容*/
            int endRowNum;
            //获取结束行数
            if (EndRow == -1) {
                endRowNum = sheet.getPhysicalNumberOfRows();
            } else {
                endRowNum = EndRow <= sheet.getPhysicalNumberOfRows() ? EndRow : sheet.getPhysicalNumberOfRows();
            }
            List<Map<String, Object>> mapList = new ArrayList<>();
            //遍历行数
            for (int i = StatrRow - 1; i < endRowNum; i++) {
                Row row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }
                //获取需要的列数据
                t texcel = (t) t.newInstance();
                for (Map.Entry<String, Object> map : exceltoPOJO.entrySet()) {
                    //获取Exceld对应列的数据
                    String celldata = convertCellValueToString(row.getCell(Integer.parseInt(map.getKey())));
                    //使用发射
                    //获取实体类T中指定成员变量的对象
                    PropertyDescriptor pd = new PropertyDescriptor((String) map.getValue(), texcel.getClass());
                    //获取成员变量的set方法
                    Method method = pd.getWriteMethod();
                    //判断成员变量的类型
                    Field field = texcel.getClass().getDeclaredField((String) map.getValue());
                    String object = field.getGenericType().getTypeName();
                    if (object.endsWith("String")) {
                        //执行set方法
                        method.invoke(texcel, celldata);
                    }
                    if (object.endsWith("Double")) {
                        Double middata = Double.valueOf(celldata);
                        //执行set方法
                        method.invoke(texcel, middata);
                    }
                    if (object.endsWith("Float")) {
                        Float middata = Float.valueOf(celldata);
                        //执行set方法
                        method.invoke(texcel, middata);
                    }
                    if (object.endsWith("Integer")) {
                        Integer middata = Integer.parseInt(celldata);
                        //执行set方法
                        method.invoke(texcel, middata);
                    }
                    if (object.endsWith("Long")) {
                        Long middata = Long.parseLong(celldata);
                        //执行set方法
                        method.invoke(texcel, middata);
                    }
                }
                result.add(texcel);
            }
        }
        return result;
    }

    /**
     * 获取对应的实体类成员
     * */
    private static Map<String, Object> getPOJOFieldAndValue(Class T) {
        //声明返回结果集
        Map<String, Object> result = new HashMap<>();
        Field[] fields = T.getDeclaredFields();//获取属性名
        if (fields != null) {
            for (Field field : fields) {
                excelRescoure Rescoure = field.getAnnotation(excelRescoure.class);
                if (Rescoure != null && Rescoure.value() != null && !"".equals(Rescoure.value())) {
                    result.put(Rescoure.value(), field.getName());
                }
            }
        } else {
            logger.warn("实体类：" + T + "不存在成员变量");
            return null;
        }
        return result;
    }

}

