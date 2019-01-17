package com.lianNLP.website.util;

import com.alibaba.fastjson.JSONArray;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jeeframework.util.validate.Validate;
import org.bson.Document;
import org.eclipse.jetty.util.ArrayQueue;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


public class CSVFileUtil {

    private FileInputStream fis = null;
    private InputStreamReader isw = null;
    private BufferedReader br = null;
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CSVFileUtil(String filename, String encode) throws Exception {
        fis = new FileInputStream(filename);
        isw = new InputStreamReader(fis, encode);
        br = new BufferedReader(isw);
    }

    // ==========以下是公开方法=============================

    /**
     * 从CSV文件流中读取一个CSV行。
     *
     * @throws Exception
     */
    public String readLine() throws Exception {

        StringBuffer readLine = new StringBuffer();
        boolean bReadNext = true;

        while (bReadNext) {
            //
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = br.readLine();

            // readLine is Null
            if (strReadLine == null) {
                return null;
            }
            readLine.append(strReadLine);

            // 如果双引号是奇数的时候继续读取。考虑有换行的是情况。
            if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
                bReadNext = true;
            } else {
                bReadNext = false;
            }
        }
        return readLine.toString();
    }

    /**
     * 把CSV文件的一行转换成字符串数组。指定数组长度，不够长度的部分设置为null。
     */
    public static String[] fromCSVLine(String source, int size) {
        ArrayList tmpArray = fromCSVLinetoArray(source);
        if (size < tmpArray.size()) {
            size = tmpArray.size();
        }
        String[] rtnArray = new String[size];
        tmpArray.toArray(rtnArray);
        return rtnArray;
    }

    /**
     * 把CSV文件的一行转换成字符串数组。不指定数组长度。
     */
    public static ArrayList fromCSVLinetoArray(String source) {
        if (source == null || source.length() == 0) {
            return new ArrayList();
        }
        int currentPosition = 0;
        int maxPosition = source.length();
        int nextComma = 0;
        ArrayList rtnArray = new ArrayList();
        while (currentPosition < maxPosition) {
            nextComma = nextComma(source, currentPosition);
            rtnArray.add(nextToken(source, currentPosition, nextComma));
            currentPosition = nextComma + 1;
            if (currentPosition == maxPosition) {
                rtnArray.add("");
            }
        }
        return rtnArray;
    }


    /**
     * 把字符串类型的数组转换成一个CSV行。（输出CSV文件的时候用）
     */
    public static String toCSVLine(String[] strArray) {
        if (strArray == null) {
            return "";
        }
        StringBuffer cvsLine = new StringBuffer();
        for (int idx = 0; idx < strArray.length; idx++) {
            String item = addQuote(strArray[idx]);
            cvsLine.append(item);
            if (strArray.length - 1 != idx) {
                cvsLine.append(',');
            }
        }
        return cvsLine.toString();
    }

    /**
     * 字符串类型的List转换成一个CSV行。（输出CSV文件的时候用）
     */
    public static String toCSVLine(ArrayList strArrList) {
        if (strArrList == null) {
            return "";
        }
        String[] strArray = new String[strArrList.size()];
        for (int idx = 0; idx < strArrList.size(); idx++) {
            strArray[idx] = (String) strArrList.get(idx);
        }
        return toCSVLine(strArray);
    }

    public static boolean megerCsvFile(String filePath) {
        try {
            File fileDir = new File(filePath);
            File[] files = fileDir.listFiles();
            File csvFile = files[0];
            for (int i = 1; i < files.length; i++) {
                File file = files[i];
                CSVFileUtil fileUtil = new CSVFileUtil(file.getAbsolutePath(), "GBK");
                fileUtil.readLine();//跳过第一行表头
                String source = fileUtil.readLine();
                while (!Validate.isEmpty(source)) {
                    appendDate(csvFile, source);
                    source = fileUtil.readLine();
                }
                fileUtil.close();//关闭资源
                file.delete();//删掉 合并用的文件
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // ==========以下是内部使用的方法=============================

    /**
     * 计算指定文字的个数。
     *
     * @param str   文字列
     * @param c     文字
     * @param start 开始位置
     * @return 个数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }

    /**
     * 查询下一个逗号的位置。
     *
     * @param source 文字列
     * @param st     检索开始位置
     * @return 下一个逗号的位置。
     */
    private static int nextComma(String source, int st) {
        int maxPosition = source.length();
        boolean inquote = false;
        while (st < maxPosition) {
            char ch = source.charAt(st);
            if (!inquote && ch == ',') {
                break;
            } /*else if ('"' == ch) {
                inquote = !inquote;
            }*/
            st++;
        }
        return st;
    }

    /**
     * 取得下一个字符串
     */
    private static String nextToken(String source, int st, int nextComma) {
        StringBuffer strb = new StringBuffer();
        int next = st;
        while (next < nextComma) {
            char ch = source.charAt(next++);
            if (ch == '"') {
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {
                    strb.append(ch);
                    next++;
                }
            } else {
                strb.append(ch);
            }
        }
        return strb.toString();
    }

    /**
     * 在字符串的外侧加双引号。如果该字符串的内部有双引号的话，把"转换成""。
     *
     * @param item 字符串
     * @return 处理过的字符串
     */
    private static String addQuote(String item) {
        if (item == null || item.length() == 0) {
            return "\"\"";
        }
        StringBuffer sb = new StringBuffer();
        sb.append('"');
        for (int idx = 0; idx < item.length(); idx++) {
            char ch = item.charAt(idx);
            if ('"' == ch) {
                sb.append("\"\"");
            } else {
                sb.append(ch);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static File createFileAndColName(String filePath, String fileName, String[] colNames) {
        File csvFile = new File(filePath, fileName);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(csvFile, "GBK");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < colNames.length; i++) {
                if (i < colNames.length - 1)
                    sb.append(colNames[i] + ",");
                else
                    sb.append(colNames[i] + "\r\n");

            }
            pw.print(sb.toString());
            pw.flush();
            pw.close();
            return csvFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File createFileAndColName(String filePath, String fileName, List<String> colNames) {
        File csvFile = new File(filePath, fileName);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(csvFile, "GBK");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < colNames.size(); i++) {
                if (i < colNames.size() - 1)
                    sb.append(colNames.get(i) + ",");
                else
                    sb.append(colNames.get(i) + "\r\n");

            }
            pw.print(sb.toString());
            pw.flush();
            pw.close();
            return csvFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean appendListDate(File csvFile, List<List<String>> data) {
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile, true), "GBK"), 1024);
            for (int i = 0; i < data.size(); i++) {
                List<String> tempData = data.get(i);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < tempData.size(); j++) {
                    String value = tempData.get(j);
                    value = value.replaceAll("\\r", "");
                    value = value.replaceAll("\\n", "");
                    value = value.replaceAll(",", "，");
                    if (j < tempData.size() - 1)
                        sb.append(value + ",");
                    else
                        sb.append(value + "\r\n");
                }
                bw.write(sb.toString());
                if (i % 1000 == 0)
                    bw.flush();
            }
            bw.flush();
            bw.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean appendDate(File csvFile, String source) {
        try {

            if (Validate.isEmpty(source)) {
                return false;
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile, true), "GBK"), 1024);
            source = source.replaceAll("\\r", "");
            source = source.replaceAll("\\n", "");
            bw.write(source + "\r\n");
            bw.flush();
            bw.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean appendDate(File csvFile, List<String> data) {
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile, true), "GBK"), 1024);
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < data.size(); j++) {
                String value = data.get(j);
                value = value.replaceAll("\\r", "");
                value = value.replaceAll("\\n", "");
                value = value.replaceAll(",", "，");
                if (j < data.size() - 1)
                    sb.append(value + ",");
                else
                    sb.append(value + "\r\n");
            }
            bw.write(sb.toString());
            bw.flush();
            bw.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {

        /*String filePath = "D:\\document\\tmp\\project\\temp\\1515150479366csv2.csv";

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(new FileInputStream(filePath),Charset.forName("gb2312"));

            // 读表头
            while (csvReader.readRecord()){
                String [] s= csvReader.getValues();
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        CsvWriter csvWriter = new CsvWriter("D:\\document\\project\\temp\\test2.csv", ',', Charset.forName("gb2312"));
        String[] strings = new String[]{"test2"};
        try {
            csvWriter.writeRecord(strings);
            csvWriter.writeRecord(new String[]{"test3"});
            csvWriter.writeRecord(new String[]{"test45"});

            csvWriter.flush();

            csvWriter.writeRecord(new String[]{"test445675"});

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void close() {
        try {
            fis.close();
            isw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createCsvFile(String path, String fileName, List<String> fieldNameList, JSONArray dataArray) {

        File csvFile = CSVFileUtil.createFileAndColName(path, fileName, fieldNameList);

        for (Object data : dataArray) {
            List<String> resultList = new ArrayList<>();
            JSONArray datajson = JSONArray.parseArray(data.toString());
            for (int i = 0; i < fieldNameList.size(); i++) {
                String value = datajson.getString(i);
                if (value != null) {
                    resultList.add(value);
                } else {
                    resultList.add("");
                }
            }
            CSVFileUtil.appendDate(csvFile, resultList);
        }
    }

    public static void createStatisticalResultCsvFile(String path, String fileName, List<Document> list) {
        //获取标题头
        Document documentTitle = list.get(0);
        List<String> titleList = new ArrayList<>();
        for (String key : documentTitle.keySet()) {
            titleList.add(key);
        }
        File csvFile = CSVFileUtil.createFileAndColName(path, fileName, titleList);
        for (Document document : list) {
            List<String> resultList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : document.entrySet()) {
                Object o = entry.getValue();
                if (o != null) {
                    resultList.add(o.toString());
                } else {
                    resultList.add("");
                }
            }
            CSVFileUtil.appendDate(csvFile, resultList);
        }
    }


    /**
     * 分csv文件
     *
     * @param fileDir      准备被分的文件
     * @param fileNum      分多少个文件
     * @param fileTempPath 分完的文件放入那个文件夹里
     */
    public static void subFile(String fileDir, int fileNum, String fileTempPath) {

        try {

            CsvReader csvReader = new CsvReader(new FileInputStream(fileDir), Charset.forName("gb2312"));
            csvReader.setSafetySwitch(false);

            //读取第一行
            csvReader.readRecord();
            String[] titleArray = csvReader.getValues();

            //用来分文件的队列
            Queue<CsvWriter> mapQueue = new ArrayQueue();
            for (int i = 0; i < fileNum; i++) {
                File file = new File(fileTempPath + System.currentTimeMillis() + "csv" + i + ".csv");
                CsvWriter csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("gb2312"));
                csvWriter.writeRecord(titleArray);
                mapQueue.add(csvWriter);
            }

            CsvWriter csvWriter = mapQueue.remove();
            mapQueue.add(csvWriter);
            long i = 0;
            while (csvReader.readRecord()) {
                csvWriter.writeRecord(csvReader.getValues());
                if (i % 1000 == 0) {
                    csvWriter.flush();

                    csvWriter = mapQueue.remove();
                    mapQueue.add(csvWriter);
                }
                i++;
            }

            Iterator<CsvWriter> iterator = mapQueue.iterator();
            while (iterator.hasNext()) {
                CsvWriter csvWriterTem = iterator.next();
                //关闭流保存文件
                csvWriterTem.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
