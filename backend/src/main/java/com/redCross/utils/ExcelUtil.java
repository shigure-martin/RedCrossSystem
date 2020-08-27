package com.redCross.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.redCross.model.ExcelPictureModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class ExcelUtil {

    public static String NO_DEFINE = "no_define";//未定义的字段

    public static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";//默认日期格式

    public static int DEFAULT_COLOUMN_WIDTH = 10;

    /**
     * 导出Excel 97(.xls)格式 ，少量数据
     *
     * @param title       标题行
     * @param headMap     属性-列名
     * @param jsonArray   数据集
     * @param datePattern 日期格式，null则用默认日期格式
     * @param colWidth    列宽 默认 至少17个字节
     * @param out         输出流
     */
    public static void exportExcel(String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern, Integer colWidth,
                                   OutputStream out) {

        //设置日期格式
        if (datePattern == null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }

        if (colWidth == null) {
            colWidth = 17;
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        workbook.createInformationProperties();
        SummaryInformation si = workbook.getSummaryInformation();
        si.setAuthor("mocar");  //填加xls文件作者信息
        si.setApplicationName("导出程序"); //填加xls文件创建程序信息
        si.setLastAuthor("最后保存者信息"); //填加xls文件最后保存者信息
        si.setComments("mocar"); //填加xls文件作者信息
        si.setTitle("POI导出Excel"); //填加xls文件标题信息
        si.setSubject("POI导出Excel");//填加文件主题信息
        si.setCreateDateTime(new Date());
        //表头样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
        // 列头样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont cellFont = workbook.createFont();
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        HSSFSheet sheet = workbook.createSheet();
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("mocar");
        //设置列宽
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext(); ) {
            String fieldName = iter.next();
            properties[ii] = fieldName;
            headers[ii] = fieldName;
            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : jsonArray) {
            //如果数据超过了，则在第二页显示
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0) sheet = workbook.createSheet();
                //表头 rowIndex=0
                HSSFRow titleRow = sheet.createRow(0);
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                //列头 rowIndex =1
                HSSFRow headerRow = sheet.createRow(1);
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
                //数据内容从 rowIndex=2开始
                rowIndex = 2;
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            HSSFRow dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                HSSFCell newCell = dataRow.createCell(i);
                Object o = jo.get(properties[i]);
                String cellValue = "";
                if (o == null) {
                    cellValue = "";
                } else if (o instanceof Date) {
                    cellValue = new SimpleDateFormat(datePattern).format(o);
                } else {
                    cellValue = o.toString();
                }
                newCell.setCellValue(cellValue);
                newCell.setCellStyle(cellStyle);
            }
            rowIndex++;
        }
        // 自动调整宽度
        /*for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }*/
        try {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出Excel OOXML (.xlsx)格式
     *
     * @param title       标题行
     * @param headMap     属性-列头
     * @param jsonArray   数据集
     * @param datePattern 日期格式，传null值则默认 年月日
     * @param colWidth    列宽 默认 至少17个字节
     * @param realPath    文件物理地址
     */
    public static String exportExcelX(String fileName,String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern, int colWidth, String realPath,List<ExcelPictureModel> excelPictureModels) {
        if (datePattern == null) datePattern = DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //表头样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
        // 列头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        // 单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        SXSSFSheet sheet = workbook.createSheet();
        //设置列宽
//        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext(); ) {
            String fieldName = iter.next();
            properties[ii] = fieldName;
            headers[ii] = headMap.get(fieldName);
            int bytes = fieldName.getBytes().length;
//            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : jsonArray) {
            if (obj == null) {
                continue;
            }
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0) {
                    sheet = workbook.createSheet();//如果数据超过了，则在第二页显示
                }
                SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
                rowIndex = 2;//数据内容从 rowIndex=2开始
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = sheet.createRow(rowIndex);
            try {
                for (int i = 0; i < properties.length; i++) {
                    SXSSFCell newCell = dataRow.createCell(i);
                    Object o = jo.get(properties[i]);
                    String cellValue = "";
                    if (o == null) {
                        cellValue = "";
                    } else if (o instanceof Date) {
                        cellValue = new SimpleDateFormat(datePattern).format(o);
                    } else if (o instanceof Float || o instanceof Double) {
                        cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    } else if (o instanceof Boolean) {
                        cellValue = (Boolean) o ? "1" : "0";
                    } else {
                        cellValue = o.toString();
                    }
                    newCell.setCellValue(cellValue);
                    newCell.setCellStyle(cellStyle);
                }
                rowIndex++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 自动调整宽度
        setSizeColumn(sheet,headMap.size());

        if (excelPictureModels != null && excelPictureModels.size() > 0) {
            Drawing patriarch = sheet.createDrawingPatriarch();
            for (ExcelPictureModel excelPictureModel : excelPictureModels) {
                int column = excelPictureModel.getColumn();
                int row = excelPictureModel.getRow();
                ByteArrayOutputStream photo = excelPictureModel.getPhoto();
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 20, 20,(short) column, row, (short) column + 1, row + 1);
                anchor.setAnchorType(3);
                patriarch.createPicture(anchor,workbook.addPicture(photo.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
                sheet.getRow(row).setHeight((short)3000);
                sheet.setColumnWidth(column,4700);
            }
        }

        try {
            String fileNameActual = fileName + ".xlsx";
            File file = new File(realPath + "/" + fileNameActual);
            OutputStream out = new FileOutputStream(file);
            workbook.write(out);
            workbook.close();
            workbook.dispose();
            return fileNameActual;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 自适应宽度(中文支持)
    private static void setSizeColumn(SXSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum <= size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 1; rowNum < sheet.getLastRowNum(); rowNum++) {
                SXSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    SXSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, (columnWidth + 6)* 256);
        }
    }

    /**
     * 输出到浏览器中
     */
    public static void exportToWeb(HttpServletResponse response, File tempExcelFile, String fileName) throws Exception {
        FileInputStream fin = null;
        try {
            response.reset();// 清空输出流
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));//设定输出文件头
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");//定义输出类型
            // 读取文件并且输出
            fin = new FileInputStream(tempExcelFile);
            byte[] tempBytes = new byte[2048];
//            while (fin.read(tempBytes) != -1) {
//                response.getOutputStream().write(tempBytes);UTF-8
//            }
            int len = 0;
            OutputStream out = response.getOutputStream();
            while ((len = fin.read(tempBytes)) > 0) {
                out.write(tempBytes, 0, len);//将缓冲区的数据输出到客户端浏览器
            }
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new Exception();
        } finally {
            if (fin != null) {
                try {
                    fin.close(); // 关闭流
                } catch (IOException e) {
                    throw new Exception();
                }
            }
            tempExcelFile.delete();
        }
    }

    /**
     * excel(.xlsx)导入
     *
     * @param path     excel文件目录
     * @param keyValue 表头跟类属性对应的map
     * @param clz      返回的类
     * @return 成功返回list 失败返回null
     */
    public static ExcelResponse importExcelX(String path, Map<String, String> keyValue, Class clz) {
        Set<String> standardNos = Sets.newHashSet();
        List<Map<String, String>> result = new ArrayList<>();
        //表头
        Map<String, String> key = new HashMap<>();

        StringBuilder sb = new StringBuilder("");
        try {
            /*读取文件*/
            File f = new File(path);
            InputStream in = new FileInputStream(f);

            XSSFWorkbook wb = new XSSFWorkbook(in);


            int count = wb.getActiveSheetIndex();
            System.out.print(count);
            XSSFSheet xssfSheet = wb.getSheetAt(0);
            XSSFRow firstRow = xssfSheet.getRow(0);

            //读取表头
            for (int cellCount = firstRow.getFirstCellNum(); cellCount < firstRow.getLastCellNum(); cellCount++) {
                XSSFCell cell = firstRow.getCell(cellCount);
                String value = cell.toString();
                key.put("" + cellCount, value);
            }

            //读取信息匹配表头
            for (int j = firstRow.getRowNum() + 1; j <= xssfSheet.getLastRowNum(); j++) {
                XSSFRow row = xssfSheet.getRow(j);
                HashMap<String, String> oneRow = new HashMap<>();
                boolean IsEmpty = true;
                for (int cellCount = row.getFirstCellNum(); cellCount < row.getLastCellNum(); cellCount++) {
                    XSSFCell cell = row.getCell(cellCount);
                    if (cell != null) {
                        String value = cell.toString();
                        if (value != null && !value.equals("")) {
                            oneRow.put(key.get(cellCount + ""), value);
                            IsEmpty = false;
                        }

                    }
                }
                if (!IsEmpty) {
                    result.add(oneRow);
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        List<Object> resultInstanceList = new ArrayList<>();
        List<String> messageList = produceClass(result,resultInstanceList,keyValue,clz);
        return new ExcelResponse(resultInstanceList, messageList);
    }

    public static ExcelResponse importExcel(String path, Map<String, String> keyValue, Class clz) {
        Set<String> standardNos = Sets.newHashSet();
        List<Map<String, String>> result = new ArrayList<>();
        //表头
        Map<String, String> key = new HashMap<>();

        try {
            /*读取文件*/
            File f = new File(path);
            InputStream in = new FileInputStream(f);

            HSSFWorkbook wb = new HSSFWorkbook(in);


            int count = wb.getActiveSheetIndex();
            System.out.print(count);
            HSSFSheet xssfSheet = wb.getSheetAt(0);
            HSSFRow firstRow = xssfSheet.getRow(0);

            //读取表头
            for (int cellCount = firstRow.getFirstCellNum(); cellCount < firstRow.getLastCellNum(); cellCount++) {
                HSSFCell cell = firstRow.getCell(cellCount);
                String value = cell.toString();
                key.put("" + cellCount, value);
            }

            //读取信息匹配表头
            for (int j = firstRow.getRowNum() + 1; j <= xssfSheet.getLastRowNum(); j++) {
                HSSFRow row = xssfSheet.getRow(j);
                HashMap<String, String> oneRow = new HashMap<>();
                boolean IsEmpty = true;
                for (int cellCount = row.getFirstCellNum(); cellCount < row.getLastCellNum(); cellCount++) {
                    HSSFCell cell = row.getCell(cellCount);
                    if (cell != null) {
                        String value = cell.toString();
                        if (value != null && !value.equals("")) {
                            IsEmpty = false;
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC: {
                                    //数字  处理自动转double及科学计数法的问题
                                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                                        Date theDate = cell.getDateCellValue();
                                        oneRow.put(key.get(cellCount + ""), String.valueOf(theDate.getTime()));
                                    } else {
                                        Double num = cell.getNumericCellValue();
                                        BigDecimal bd1 = new BigDecimal(Double.toString(num));
                                        oneRow.put(key.get(cellCount + ""), bd1.toPlainString().replaceAll("0+?$", "").replaceAll("[.]$", ""));
                                    }
                                    break;
                                }
                                default: {
                                    oneRow.put(key.get(cellCount + ""), value);
                                }
                            }
                        }
                    }
                }
                if (!IsEmpty) {
                    result.add(oneRow);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Object> resultInstanceList = new ArrayList<>();
        List<String> messageList = produceClass(result,resultInstanceList,keyValue,clz);
        return new ExcelResponse(resultInstanceList, messageList);
    }

    //将excel读入的转成类
    private static List<String> produceClass(List<Map<String, String>> result, List<Object> resultInstanceList,Map<String, String> keyValue,Class clz){
        StringBuilder sb = new StringBuilder("");
        Field[] fields = clz.getDeclaredFields();//获得全部属性
        for (Map<String, String> map : result) {
            Object o = null;
            try {
                o = clz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Excel 导入失败。");
            }
            for (Field field : fields) {
                String filedName = field.getName();
                System.out.print(filedName + "\n");
                field.setAccessible(true);
                try {
                    String type = field.getType().toString();
                    String value = map.get(keyValue.get(filedName));
                    if (value == null)
                        continue;

                    if (type.endsWith("String")){
                        field.set(o, value);
                    } else if (type.endsWith("Integer")){
                        field.set(o, Integer.parseInt(value.substring(0, value.indexOf("."))));
                    } else if (type.endsWith("BigDecimal")){
                        field.set(o, BigDecimal.valueOf(Double.parseDouble(value)));
                    } else if (type.endsWith("Float")){
                        field.set(o, Float.valueOf(value));
                    } else if (type.endsWith("Date")) {
                        if (value.length() == 10)
                            field.set(o, DateUtil.YYYY_MM_DD.parse(value));
                        else
                            field.set(o, DateUtil.YYYY_MM_DD_HH_MM_SS.parse(value));
                    } else if (type.endsWith("Boolean")) {
                        field.set(o, Boolean.valueOf(value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resultInstanceList.add(o);
        }

        String skiped = sb.toString();
        if (!"".equals(skiped)) {
            skiped = "未导入的序号有: " + skiped;
        } else {
            skiped = "导入成功";
        }
        List<String> messageList = new ArrayList<>();
        messageList.add(skiped);
        return messageList;
    }

    public static String readExcel(final String filePath, int row, int column) throws IOException {
        File f = new File(filePath);
        InputStream in = new FileInputStream(f);


        XSSFWorkbook wb = new XSSFWorkbook(in);

        XSSFSheet xssfSheet = wb.getSheetAt(0);
        int rowCount = Lists.newArrayList(xssfSheet.rowIterator()).size();
        if (row > rowCount) {
            throw new RuntimeException("没有这行。");
        }
        XSSFRow firstRow = xssfSheet.getRow(row);
        int colmumCount = Lists.newArrayList(firstRow.cellIterator()).size();
        if (column > colmumCount) {
            throw new RuntimeException("没有这列。");
        }
        Cell cell = firstRow.getCell(column);
        return cell.getStringCellValue();
    }

    public static String getValueFromPos(String path, String excelPosition) {
        File f = new File(path);
        if (!f.exists()) {
            return "";
        }
        List<String> values = new CustomList();
        String[] positions = excelPosition.split(",");
        try (InputStream in = new FileInputStream(f)) {
            try (XSSFWorkbook wb = new XSSFWorkbook(in)) {
                XSSFSheet xssfSheet = wb.getSheetAt(0);

                if (positions.length == 1) {
                    for (String pos : positions) {
                        CellReference cr = new CellReference(pos);

                        XSSFRow rowFormula = xssfSheet.getRow(cr.getRow());
                        if (rowFormula == null) {
                            continue;
                        }
                        Cell cellFormula = rowFormula.getCell(cr.getCol());
                        if (cellFormula.getCellType() == Cell.CELL_TYPE_NUMERIC || cellFormula.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            double value = cellFormula.getNumericCellValue();
                            long longlongVal = Math.round(value);
                            if (Double.parseDouble(longlongVal + ".0") == value)
                                values.add(Long.toString(longlongVal));
                            else {
                                String tmp;
                                if (isENum(Double.toString(value))) {
                                    tmp = ds.format(value);
                                } else {
                                    tmp = Double.toString(value);
                                }
                                values.add(tmp);
                            }

                        } else if (cellFormula.getCellType() == Cell.CELL_TYPE_STRING) {
                            values.add(cellFormula.getStringCellValue());
                        }
                    }
                } else if (positions.length == 2) {
                    CellReference cr = new CellReference(positions[0]);
                    CellReference cr2 = new CellReference(positions[1]);

                    for (int j = cr.getCol(); j <= cr2.getCol(); j++) {
                        int index = 0;
                        StringBuilder titleBuilder = new StringBuilder();
                        StringBuilder rowValue = new StringBuilder();
                        for (int i = cr.getRow(); i <= cr2.getRow(); i++) {
                            XSSFRow rowFormula = xssfSheet.getRow(i);
                            if (rowFormula == null) {
                                continue;
                            }
                            Cell cellFormula = rowFormula.getCell(j);
                            String tmp = "";
                            if (cellFormula == null) {
                                continue;
                            }
                            if (cellFormula.getCellType() == Cell.CELL_TYPE_NUMERIC || cellFormula.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                double value = cellFormula.getNumericCellValue();
                                long longlongVal = Math.round(value);
                                if (Double.parseDouble(longlongVal + ".0") == value) {
                                    tmp = Long.toString(longlongVal);
                                } else {
                                    if (isENum(Double.toString(value))) {
                                        tmp = ds.format(value);
                                    } else {
                                        tmp = Double.toString(value);
                                    }
                                }
                            } else if (cellFormula.getCellType() == Cell.CELL_TYPE_STRING) {
                                tmp = cellFormula.getStringCellValue();
                            }
                            if (!Strings.isNullOrEmpty(tmp)) {
                                if (index == 0) {
                                    titleBuilder.append(tmp);
                                    index++;
                                } else {
                                    String content = rowValue.toString();
                                    if (Strings.isNullOrEmpty(content)) {
                                        rowValue.append(tmp);
                                    } else {
                                        rowValue.append(",").append(tmp);
                                    }
                                }
                            }
                        }
                        String title = titleBuilder.toString();
                        String result = rowValue.toString();
                        if (Strings.isNullOrEmpty(result)) {
                            values.add(title);
                        } else {
                            values.add(title + ":" + result);
                        }

                    }
                }
            }
            String result = StringUtils.join(values, ";");
            return result.replace(":;", ";");
        } catch (Exception e) {
            log.error("获取单元格数据失败。", e);
            return "";
        }
    }

    public static class CustomList extends ArrayList<String> {
        public CustomList() {
            super();
        }

        @Override
        public boolean add(String s) {
            if (Strings.isNullOrEmpty(s)) {
                return false;
            }
            try {
                Long.parseLong(s);
                return super.add(s);
            } catch (Exception e) {
                return super.add(FormatUtil.formatDouble(s));
            }
        }
    }

    static Pattern pattern = Pattern.compile("(-?\\d+\\.?\\d*)[Ee]{1}[\\+-]?[0-9]*");
    static DecimalFormat ds = new DecimalFormat("0.0000");

    static boolean isENum(String input) {//判断输入字符串是否为科学计数法
        return pattern.matcher(input).matches();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ExcelResponse {
        private List<Object> importedList = Lists.newArrayList();
        private List<String> messageList;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(ExcelUtil.getValueFromPos("/Users/shaoxiong.zhan/IdeaProjects/sdzb/JL0363-000-2019配电箱（工程验收）原始记录.xlsx", "E38,G39"));
    }
}
