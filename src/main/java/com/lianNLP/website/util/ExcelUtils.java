package com.lianNLP.website.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvWriter;
import com.jeeframework.util.validate.Validate;
import jxl.write.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 包: com.lianNLP.website.util
 * 源文件:ExcelUtils.java
 *
 * @author haolen  Copyright 2018 , Inc. All rights reserved.2018年11月20日
 */
public class ExcelUtils {

    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Label createCell(int c, int r, String content) {
        Label l = new Label(c, r, content);

        try {
            l.setCellFormat(getWritableCellFormat());
        } catch (Exception e) {
        }

        return l;
    }

    public static WritableCellFormat getWritableCellFormat() throws Exception {
        WritableCellFormat wcfFC = new WritableCellFormat();
        wcfFC.setAlignment(Alignment.CENTRE);
        wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框的颜色和样式
        WritableFont font1 = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD);
        wcfFC.setFont(font1);
        return wcfFC;
    }


    public static XSSFWorkbook createExcel(XSSFWorkbook workBook, String[] titles, String sheetName) {
        XSSFSheet sheet = workBook.createSheet(sheetName);
        XSSFRow row = sheet.createRow(0);
        int col = 0;
        row.createCell(col++).setCellValue("序号");
        for (String title : titles) {
            row.createCell(col++).setCellValue(title);
        }
        return workBook;
    }

    /**
     * 创建SXSSFWorkbook
     * By byron
     *
     * @param titles
     * @return
     */
    public static SXSSFWorkbook createExcel(List<String> titles) {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet();
        Row row = sh.createRow(0);
        int col = 0;
        for (String title : titles) {
            row.createCell(col++).setCellValue(title);
        }
        return wb;
    }

    public static void appendDataListArray(SXSSFWorkbook wb, List<String[]> dataList) {

        Sheet sheet = wb.getSheetAt(0);//获取第一个sheet
        int lastRowNum = sheet.getLastRowNum();

        for (String[] datas : dataList) {
            lastRowNum++;
            Row row = sheet.createRow(lastRowNum);
            int i = 0;
            for (String data : datas) {
                Cell cell = row.createCell(i);
                cell.setCellValue(data);
                i++;
            }
        }

        if (sheet.getLastRowNum() % (wb.getRandomAccessWindowSize()) == 0) {
            try {
                ((SXSSFSheet) sheet).flushRows();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void appendDataListList(SXSSFWorkbook wb, List<List<String>> dataList) {

        Sheet sheet = wb.getSheetAt(0);//获取第一个sheet
        int lastRowNum = sheet.getLastRowNum();

        for (List<String> datas : dataList) {
            lastRowNum++;
            Row row = sheet.createRow(lastRowNum);
            int i = 0;
            for (String data : datas) {
                Cell cell = row.createCell(i);
                cell.setCellValue(data);
                i++;
            }
        }

        /*if (sheet.getLastRowNum() % (wb.getRandomAccessWindowSize()) == 0) {
            try {
                ((SXSSFSheet) sheet).flushRows();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }

    public static void subFileXlsxToCsv(String fileDir, int fileNum, String fileTempPath) throws RuntimeException {

        try {

            List<List<String>> list = XLSCovertCSVReader.readerExcel(fileDir);

            if (Validate.isEmpty(list)) {
                return;
            }

            List<String> titles = list.remove(0);//表头

            int size = list.size();
            int n = size / fileNum;
            /*if(size % fileNum > 0){
                n+=1;
            }*/
            for (int i = 0; i < fileNum; i++) {

                int toIndex = (i + 1) * n;
                if (i >= fileNum - 1) {
                    toIndex = size;
                }
                List<List<String>> newList = list.subList(i * n, toIndex);

                /*File csvFile = CSVFileUtil.createFileAndColName(fileTempPath, System.currentTimeMillis()+"csv"+i+".csv",titles);
                CSVFileUtil.appendListDate(csvFile,newList);*/

                CsvWriter csvWriter = new CsvWriter(fileTempPath + System.currentTimeMillis() + "csv" + i + ".csv", ',', Charset.forName("gb2312"));

                String[] titleArray = new String[titles.size()];
                for (int j = 0; j < titles.size(); j++) {
                    titleArray[j] = titles.get(j);
                }
                csvWriter.writeRecord(titleArray);

                String[] dataArray = new String[titles.size()];
                for (int j = 0; j < newList.size(); j++) {
                    List<String> stringList = newList.get(j);
                    for (int k = 0; k < stringList.size(); k++) {
                        if (k < dataArray.length) {
                            dataArray[k] = stringList.get(k);
                        }
                    }

                    csvWriter.writeRecord(dataArray);

                    if (i % 1000 == 0) {
                        csvWriter.flush();
                    }

                }

                csvWriter.close();


                /*SXSSFWorkbook sxssfWorkbook = ExcelUtils.createExcel(Arrays.asList(titles));
                ExcelUtils.appendDataListArray(sxssfWorkbook,newList);

                if(!fileTempPath.endsWith("/")){
                    fileTempPath += "/";
                }

                FileOutputStream fos = new FileOutputStream(fileTempPath+System.currentTimeMillis()+"xlsx"+i+".xlsx");
                fos.flush();
                sxssfWorkbook.write(fos);
                fos.close();*/

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {

        }

    }

    public static void subFileXlsToCsv(String fileDir, int fileNum, String fileTempPath) throws RuntimeException {

        try {

            List<List<String>> list = ExcelUtils.readExcel(fileDir);

            if (Validate.isEmpty(list)) {
                return;
            }

            List<String> titles = list.remove(0);//表头

            int size = list.size();
            int n = size / fileNum;
            /*if(size % fileNum > 0){
                fileNum+=1;
            }*/
            for (int i = 0; i < fileNum; i++) {

                int toIndex = (i + 1) * n;
                if (i >= fileNum - 1) {
                    toIndex = size;
                }
                List<List<String>> newList = list.subList(i * n, toIndex);

                /*File csvFile = CSVFileUtil.createFileAndColName(fileTempPath, System.currentTimeMillis()+"csv"+i+".csv",titles);
                CSVFileUtil.appendListDate(csvFile,newList);*/

                CsvWriter csvWriter = new CsvWriter(fileTempPath + System.currentTimeMillis() + "csv" + i + ".csv", ',', Charset.forName("gb2312"));

                String[] titleArray = new String[titles.size()];
                for (int j = 0; j < titles.size(); j++) {
                    titleArray[j] = titles.get(j);
                }
                csvWriter.writeRecord(titleArray);

                String[] dataArray = new String[titles.size()];
                for (int j = 0; j < newList.size(); j++) {
                    List<String> stringList = newList.get(j);
                    for (int k = 0; k < stringList.size(); k++) {
                        if (k < dataArray.length) {
                            dataArray[k] = stringList.get(k);
                        }
                    }

                    csvWriter.writeRecord(dataArray);

                    if (i % 1000 == 0) {
                        csvWriter.flush();
                    }

                }

                csvWriter.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {

        }

    }

    /**
     * 用于将数据生成excel表格的静态方法
     *
     * @param fileName  文件名
     * @param dataArray 拿到的数据
     */
    public static void createExcelFile(Map<String, String> filedType, String fileName, List<String> fieldNameList, JSONArray dataArray) throws Exception {
        try {
            int row = 1;
            SXSSFWorkbook sxssfWorkbook = ExcelUtils.createExcel(fieldNameList);

            Sheet sheet = sxssfWorkbook.getSheetAt(0);
            for (Object o : dataArray) {
                Row workRow = sheet.createRow(row);
                JSONArray jsonArray = JSONArray.parseArray(o.toString());
                for (int i = 0; i < fieldNameList.size(); i++) {
                    Cell cell = workRow.createCell(i);
                    cell.setCellValue(jsonArray.getString(i));
                }
                row++;
            }
            if (sheet.getLastRowNum() % (sxssfWorkbook.getRandomAccessWindowSize()) == 0) {
                ((SXSSFSheet) sheet).flushRows();
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.flush();
            sxssfWorkbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * 计算进度
     *
     * @param total
     * @param currentSum
     * @return
     */
    public static int rateOfProgressMethod(double total, int currentSum) {
        double r = currentSum / total;
        return (int) Math.ceil(r * 100L);
    }

    public static void mergeOriginalDataExcel(String path, String fileName) throws Exception {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            File file = new File(path + "/" + fileName + ".xlsx");
            File fildir = new File(path);
            File[] files = fildir.listFiles();
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            XSSFWorkbook xssfWorkbook2 = null;
            int rowNum = 0;
            int firstFileCellNum = 0;
            for (int i = 0; i < files.length; i++) {
                fis = new FileInputStream(files[i]);//读取文件路径
                xssfWorkbook2 = new XSSFWorkbook(fis);
                Sheet sheet2 = xssfWorkbook2.getSheetAt(0);//获取第一个sheet
                int total2 = sheet2.getLastRowNum() + 1;//获取总行数
                if (i == 0) {
                    firstFileCellNum = sheet2.getRow(0).getLastCellNum();
                    for (int j = 0; j < total2; j++) {//遍历第一个文件的所有行
                        Row row = sheet.createRow(rowNum); //拿到第一行，然后给新的文件当标题
                        Row row2 = sheet2.getRow(j);
                        for (int i1 = 0; i1 < firstFileCellNum; i1++) {
                            Cell cell = row.createCell(i1);
                            cell.setCellValue(row2.getCell(i1).getStringCellValue());
                        }
                        rowNum++;
                    }
                } else {
                    for (int j = 1; j < total2; j++) {
                        Row row = sheet.createRow(rowNum);
                        Row row2 = sheet2.getRow(j);
                        for (int i1 = 0; i1 < firstFileCellNum; i1++) {
                            Cell cell = row.createCell(i1);
                            cell.setCellValue(row2.getCell(i1).getStringCellValue());
                        }
                        rowNum++;
                    }
                }
            }

            if (sheet.getLastRowNum() % (workbook.getRandomAccessWindowSize()) == 0) {
                ((SXSSFSheet) sheet).flushRows();
            }
            fos = new FileOutputStream(file);
            fos.flush();
            workbook.write(fos);
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void statisticalResultExport(String fileName, List<Document> list) throws Exception {
        try {
            SXSSFWorkbook wb = new SXSSFWorkbook();
            Sheet sheet = wb.createSheet();
            int cell = 0;
            int rowNum = 0;
            Row row = sheet.createRow(rowNum);
            Set<String> set = list.get(0).keySet();
            for (String s : set) {
                row.createCell(cell).setCellValue(s);
                cell++;
            }
            for (Document document : list) {
                cell = 0;
                rowNum++;
                row = sheet.createRow(rowNum);
                for (Map.Entry<String, Object> entry : document.entrySet()) {
                    if (entry != null) {
                        row.createCell(cell).setCellValue(entry.getValue().toString());
                        cell++;
                    }
                }
            }

            if (sheet.getLastRowNum() % (wb.getRandomAccessWindowSize()) == 0) {
                ((SXSSFSheet) sheet).flushRows();
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.flush();
            wb.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public static List<List<String>> readExcel(String path) {

        File file = new File(path);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<List<String>> list = new ArrayList<List<String>>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();

                //获得第一行行
                Row firstRow = sheet.getRow(0);
                if (firstRow == null) {
                    break;
                }

                //获得第一行的开始列
                int firstCellNum = firstRow.getFirstCellNum();
                //获得第一行的列数
                int lastCellNum = firstRow.getPhysicalNumberOfCells();

                //循环所有行
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }

                    List<String> listCell = new ArrayList<>();
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        if (null != cell) {
                            String cellValue = getCellValue(cell);
                            /**
                             * 导入文件是去除 数值后的小数
                             * by allen
                             * v1.1.1
                             */
                            if (cellValue.matches("\\d+\\.[0]+")) {
                                cellValue = cellValue.substring(0, cellValue.indexOf("."));
                            }
                            if (cellValue.matches("\\d{4}-\\d{2}-\\d{2}\\s*?\\d{2}:\\d{2}:\\d{2}")) {
                                try {
                                    Date date = format.parse(cellValue);
                                    cellValue = String.valueOf(date.getTime());
                                } catch (ParseException e) {
                                }
                            }
                            listCell.add(cellValue);
                        } else {
                            listCell.add("");
                        }

                    }
                    list.add(listCell);
                }
            }
        }
        return list;

    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
      /*  if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }*/
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    cellValue = format.format(date);
                } else { // 纯数字
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static Workbook getWorkBook(File file) {
        //创建Workbook工作薄对象，表示整个excel
        Workbook wb = null;
        try {
            //获取excel文件的io流
            InputStream in = new FileInputStream(file);
            wb = WorkbookFactory.create(in);

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static void main(String[] args) {
        JSONObject jsonObject1 = JSON.parseObject("{\n" +
                "          \"date\": 14,\n" +
                "          \"hours\": 22,\n" +
                "          \"seconds\": 22,\n" +
                "          \"month\": 11,\n" +
                "          \"timezoneOffset\": -480,\n" +
                "          \"year\": 117,\n" +
                "          \"minutes\": 6,\n" +
                "          \"time\": 1513260382000,\n" +
                "          \"day\": 4\n" +
                "        }");

        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonObject1.toJSONString());
        Date date = (Date) net.sf.json.JSONObject.toBean(jsonObject, Date.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(date);
        System.out.println(times);
    }
}
